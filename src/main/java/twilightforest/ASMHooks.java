package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.block.CloudBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.client.TFClientSetup;
import twilightforest.entity.TFPart;
import twilightforest.events.ToolEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimensionSettings;
import twilightforest.init.TFItems;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.item.GiantItem;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.WorldOptions#WorldOptions(long, boolean, boolean, Optional)} <br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		TFDimensionSettings.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.MapRenderer.MapInstance#draw(PoseStack, MultiBufferSource, boolean, int)}<br>
	 * [BEFORE ISTORE 5]
	 */
	public static int mapRenderDecorations(int o, MapItemSavedData data, PoseStack stack, MultiBufferSource buffer, int light) {
		if (data instanceof TFMagicMapData mapData) {
			for (TFMagicMapData.TFMapDecoration decoration : mapData.tfDecorations.values()) {
				decoration.render(o, stack, buffer, light);
				o++;
			}
		}
		return o;
	}

	private static boolean isOurMap(ItemStack stack) {
		return stack.is(TFItems.FILLED_MAGIC_MAP.get()) || stack.is(TFItems.FILLED_MAZE_MAP.get()) || stack.is(TFItems.FILLED_ORE_MAP.get());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#renderArmWithItem(AbstractClientPlayer, float, float, InteractionHand, float, ItemStack, float, PoseStack, MultiBufferSource, int)} <br>
	 * [AFTER FIRST GETSTATIC {@link net.minecraft.world.item.Items#FILLED_MAP}]
	 * <p></p>
	 * Injection Point:<br>
	 * {@link ItemFrame#getFramedMapId()} <br>
	 * [BEFORE FIRST IFEQ]
	 */
	public static boolean shouldMapRender(boolean o, ItemStack stack) {
		return o || isOurMap(stack);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#renderMap(PoseStack, MultiBufferSource, int, ItemStack)}<br>
	 * [BEFORE FIRST ASTORE 6]
	 * <p></p>
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.MapItem#appendHoverText(ItemStack, Level, List, TooltipFlag)}<br>
	 * [AFTER INVOKESTATIC {@link net.minecraft.world.item.MapItem#getSavedData(Integer, Level)}]
	 */
	@Nullable
	public static MapItemSavedData renderMapData(@Nullable MapItemSavedData o, ItemStack stack, @Nullable Level level) {
		return isOurMap(stack) && level != null ? MapItem.getSavedData(stack, level) : o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.sounds.MusicManager#tick()}<br>
	 * [AFTER FIRST INVOKEVIRTUAL]
	 */
	@OnlyIn(Dist.CLIENT)
	public static Music music(Music music) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFGenerationSettings.isTwilightWorldOnClient(Minecraft.getInstance().level))
			return Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).value().getBackgroundMusic().orElse(Musics.GAME);
		return music;
	}


	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}<br>
	 * [AFTER GETFIELD]
	 */
	public static Entity updateMultiparts(Entity entity) {
		if (entity.isMultipartEntity())
			PacketDistributor.TRACKING_ENTITY.with(entity).send(new UpdateTFMultipartPacket(entity));
		return entity;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)}<br>
	 * [BEFORE LAST ARETURN]
	 */
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public static EntityRenderer<?> getMultipartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if(entity instanceof TFPart<?>)
			return TFClientSetup.BakedMultiPartRenderers.lookup(((TFPart<?>) entity).renderer());
		return renderer;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#onResourceManagerReload(ResourceManager)}<br>
	 * [AFTER FIRST INVOKESPECIAL]
	 */
	@OnlyIn(Dist.CLIENT)
	public static EntityRendererProvider.Context bakeMultipartRenders(EntityRendererProvider.Context context) {
		TFClientSetup.BakedMultiPartRenderers.bakeMultiPartRenderers(context);
		return context;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.LevelRenderer#renderLevel(PoseStack, float, long, boolean, Camera, GameRenderer, LightTexture, Matrix4f)}<br>
	 * [AFTER {@link net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering}]
	 */
	public static Iterable<Entity> renderMultiparts(Iterable<Entity> iter) {
		List<Entity> list = new ArrayList<>();
		iter.forEach(entity -> {
			list.add(entity);
			if(entity.isMultipartEntity() && entity.getParts() != null) {
				for (PartEntity<?> part : entity.getParts()) {
					if(part instanceof TFPart)
						list.add(part);
				}
			}
		});
		return list;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.BiomeColors#FOLIAGE_COLOR_RESOLVER}<br>
	 * [BEFORE IRETURN]
	 */
	@OnlyIn(Dist.CLIENT)
	public static int foliage(int o, Biome biome, double x, double z) {
		return FoliageColorHandler.get(o, biome, x, z);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.structure.StructureStart#loadStaticStart(StructurePieceSerializationContext, CompoundTag, long)} <br>
	 * [AFTER INVOKESPECIAL {@link net.minecraft.world.level.levelgen.structure.StructureStart#StructureStart(Structure, ChunkPos, int, PiecesContainer)}]
	 */
	public static StructureStart conquered(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.player.LocalPlayer#rideTick()} <br>
	 * [AFTER FIRST INVOKESPECIAL]
	 */
	public static boolean mountFix(boolean o, boolean wantsToStopRiding, boolean isPassenger) {
		if (wantsToStopRiding && isPassenger)
			return false;
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.WrittenBookItem#getName(net.minecraft.world.item.ItemStack)}<br>
	 * [BEFORE ARETURN]
	 */
	public static Component book(Component component, CompoundTag tag) {
		if (tag.contains(TwilightForestMod.ID + ":book")) {
			return Component.translatable(component.getString());
		} else return component;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.item.Item#getPlayerPOVHitResult(Level, Player, ClipContext.Fluid)}<br>
	 * [BEFORE ARETURN]
	 */
	public static BlockHitResult reach(BlockHitResult o, Level level, Player player, ClipContext.Fluid fluidMode) {
		InteractionHand hand = ToolEvents.INTERACTION_HAND;
		if (hand != null) {
			BlockHitResult hitResult = interactionTooFar(level, player, hand, fluidMode);
			if (hitResult != null) {
				return hitResult;
			}
		}
		return o;
	}

	@Nullable
	private static BlockHitResult interactionTooFar(Level level, Player player, InteractionHand hand, ClipContext.Fluid fluidMode) {
		ItemStack heldStack = player.getItemInHand(hand);
		if (ToolEvents.hasGiantItemInOneHand(player) && !(heldStack.getItem() instanceof GiantItem) && hand == InteractionHand.OFF_HAND) {
			UUID uuidForOppositeHand = GiantItem.GIANT_REACH_MODIFIER;
			AttributeInstance reachDistance = player.getAttribute(NeoForgeMod.BLOCK_REACH.value());
			if (reachDistance != null) {
				AttributeModifier giantModifier = reachDistance.getModifier(uuidForOppositeHand);
				if (giantModifier != null) {
					reachDistance.removeModifier(giantModifier.getId());
					double reach = player.getAttributeValue(NeoForgeMod.BLOCK_REACH.value());
					double trueReach = reach == 0 ? 0 : reach + (player.isCreative() ? 0.5 : 0); // Copied from IForgePlayer#getReachDistance().
					BlockHitResult result = getPlayerPOVHitResultForReach(level, player, trueReach, fluidMode);
					reachDistance.addTransientModifier(giantModifier);
					return result;
				}
			}
		}
		return null;
	}

	/*
		[VANILLA COPY]
		Copied from Item#getPlayerPOVHitResult(Level, Player, ClipContext.Fluid).
		Uses a parameter for reach in assigning vec31 instead of using IForgePlayer#getReachDistance().
	 */
	private static BlockHitResult getPlayerPOVHitResultForReach(Level level, Player player, double reach, ClipContext.Fluid fluidClip) {
		float f = player.getXRot();
		float f1 = player.getYRot();
		Vec3 vec3 = player.getEyePosition();
		float f2 = Mth.cos(-f1 * ((float) Math.PI / 180.0F) - (float) Math.PI);
		float f3 = Mth.sin(-f1 * ((float) Math.PI / 180.0F) - (float) Math.PI);
		float f4 = -Mth.cos(-f * ((float) Math.PI / 180.0F));
		float f5 = Mth.sin(-f * ((float) Math.PI / 180.0F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		Vec3 vec31 = vec3.add((double) f6 * reach, (double) f5 * reach, (double) f7 * reach);
		return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, fluidClip, player));
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.block.MushroomBlock#canSurvive(BlockState, LevelReader, BlockPos)}  }<br>
	 * [AFTER INVOKEINTERFACE {@link LevelReader#getRawBrightness(BlockPos, int)}]
	 */
	public static int shroom(int o, LevelReader level, BlockPos pos) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL))
					return 0;
			}
		}
		return o;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.Level#isRainingAt(BlockPos)}<br>
	 * [BEFORE ALOAD]
	 */
	public static boolean cloud(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.COMMON_CONFIG.cloudBlockPrecipitationDistanceCommon.get() > 0) {
			for (int y = pos.getY(); y < pos.getY() + TFConfig.COMMON_CONFIG.cloudBlockPrecipitationDistanceCommon.get(); y++) {
				BlockPos newPos = pos.atY(y);
				BlockState state = level.getBlockState(newPos);
				if (state.getBlock() instanceof CloudBlock cloudBlock && cloudBlock.getCurrentPrecipitation(newPos, level, level.getRainLevel(1.0F)).getLeft() == Biome.Precipitation.RAIN) {
					return true;
				}
				if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(level.getBlockState(newPos))) {
					return false;
				}
			}
		}
		return isRaining;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.entity.decoration.LeashFenceKnotEntity#survives()}<br>
	 * [BEFORE IRETURN]
	 */
	public static boolean lead(boolean o, LeashFenceKnotEntity entity) {
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return o || (fenceState.is(TFBlocks.WROUGHT_IRON_FENCE) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.chunk.ChunkStatus#getStatusList()}<br>
	 * [HEAD]
	 */
	public static void assertChunkBlanketing() {
		// Only need to touch this class to ensure it's classloaded before other classes cache our reconstructed ChunkStatus sequence
		ChunkBlanketProcessors.init();
	}

	/**
	 * structure_terraform.js: attach<br>
	 * Injection point:<br>
	 * {@link net.minecraft.world.level.levelgen.Beardifier#forStructuresInChunk(StructureManager, ChunkPos)}<br>
	 * [BEFORE ARETURN]
	 */
	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.iterator();
	}

	/**
	 * structure_terraform.js: recompute<br>
	 * Injection point:<br>
	 * {@link net.minecraft.world.level.levelgen.Beardifier#compute(DensityFunction.FunctionContext)}<br>
	 * [BEFORE DRETURN]
	 */
	public static double getCustomDensity(double densityBefore, DensityFunction.FunctionContext context, ObjectListIterator<DensityFunction> customDensities) {
		double newDensity = 0;

		while (customDensities.hasNext()) {
			newDensity += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);

		return densityBefore + newDensity;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.chunk.ChunkGenerator#findNearestMapStructure(ServerLevel, HolderSet, BlockPos, int, boolean)}<br>
	 * [BEFORE LAST ARETURN]
	 */
	@Nullable
	public static Pair<BlockPos, Holder<Structure>> findNearestMapLandmark(@Nullable Pair<BlockPos, Holder<Structure>> oldReturnable, ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		Pair<BlockPos, Holder<Structure>> nearestLandmark = WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius, skipKnownStructures);

		return nearestLandmark != null ? nearestLandmark : oldReturnable;
	}
}
