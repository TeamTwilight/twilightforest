package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.asm.transformers.armor.ArmorColorRenderingTransformer;
import twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer;
import twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer;
import twilightforest.asm.transformers.beardifier.BeardifierComputeTransformer;
import twilightforest.asm.transformers.beardifier.InitializeCustomBeardifierFieldsDuringForStructuresInChunkTransformer;
import twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer;
import twilightforest.asm.transformers.chunk.ChunkStatusTaskTransformer;
import twilightforest.asm.transformers.cloud.IsRainingAtTransformer;
import twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer;
import twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer;
import twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer;
import twilightforest.asm.transformers.map.RenderMapDecorationsTransformer;
import twilightforest.asm.transformers.map.ResolveMapDataForRenderTransformer;
import twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer;
import twilightforest.asm.transformers.map.ShouldMapRenderInArmTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer;
import twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer;
import twilightforest.asm.transformers.multipart.SendDirtytEntityDataTransformer;
import twilightforest.asm.transformers.shroom.ModifySoilDecisionForMushroomBlockSurvivabilityTransformer;
import twilightforest.beans.Autowired;
import twilightforest.beans.TFBeanContext;
import twilightforest.util.multiparts.MultipartEntityUtil;
import twilightforest.block.CloudBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

import java.util.Iterator;

// TODO: Think about reorganizing each group into their own class or subclass of ASMHooks
@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	@Autowired
	private static MultipartEntityUtil multipartEntityUtil = TFBeanContext.blank();

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// armor
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link ArmorColorRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel)} <br/>
	 * Targets: {@link DyedItemColor#getOrDefault(ItemStack, int)}
	 */
	public static int armorColorRendering(int color, ArmorItem armorItem, ItemStack armorStack) {
		if (armorItem instanceof ArcticArmorItem) return DyedItemColor.getOrDefault(armorStack, ArcticArmorItem.DEFAULT_COLOR);
		return color;
	}

	/**
	 * {@link ArmorVisibilityRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link LivingEntity#getVisibilityPercent(Entity)}
	 */
	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - getShroudedArmorPercentage(entity);
	}

	private static float getShroudedArmorPercentage(LivingEntity entity) {
		Iterable<ItemStack> iterable = entity.getArmorSlots();
		int shroudedArmor = 0;
		int nonShroudedArmor = 0;

		for (ItemStack stack : iterable) {
			if (!stack.isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
				shroudedArmor++;
			}

			nonShroudedArmor++;
		}

		return nonShroudedArmor > 0 && shroudedArmor > 0 ? (float) shroudedArmor / (float) nonShroudedArmor : 0.0F;
	}

	/**
	 * {@link CancelArmorRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel)}
	 */
	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
			return false;
		}
		return o;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// beardifier
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link InitializeCustomBeardifierFieldsDuringForStructuresInChunkTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link Beardifier#forStructuresInChunk(StructureManager, ChunkPos)}
	 */
	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.iterator();
	}

	/**
	 * {@link BeardifierComputeTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link Beardifier#compute(DensityFunction.FunctionContext)}
	 */
	public static double getCustomDensity(double o, DensityFunction.FunctionContext context, @Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null)
			return o;

		double newDensity = 0;

		while (customDensities.hasNext()) {
			newDensity += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);

		return o + newDensity;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// book
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link ModifyWrittenBookNameTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link WrittenBookItem#getName(ItemStack)}
	 */
	public static Component modifyWrittenBookName(Component component, ItemStack stack) {
		if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
			return Component.translatable(component.getString());
		} else return component;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// chunk
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link ChunkStatusTaskTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link ChunkStatusTasks#generateSurface}
	 */
	public static void chunkBlanketing(ChunkAccess chunkAccess, WorldGenRegion worldGenRegion) {
		ChunkBlanketProcessors.chunkBlanketing(chunkAccess, worldGenRegion);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// cloud
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link IsRainingAtTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link Level#isRainingAt(BlockPos)}
	 */
	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			LevelChunk chunk = level.getChunkAt(pos);
			for (int y = pos.getY(); y < pos.getY() + TFConfig.commonCloudBlockPrecipitationDistance; y++) {
				BlockPos newPos = pos.atY(y);
				BlockState state = chunk.getBlockState(newPos);
				if (state.getBlock() instanceof CloudBlock cloudBlock && cloudBlock.getCurrentPrecipitation(newPos, level, level.getRainLevel(1.0F)).getLeft() == Biome.Precipitation.RAIN) {
					return true;
				}
				if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(state)) {
					return false;
				}
			}
		}
		return isRaining;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// conquered
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link StructureStartLoadStaticTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link StructureStart#loadStaticStart(StructurePieceSerializationContext, CompoundTag, long)}<br/>
	 * Targets: {@link StructureStart#StructureStart(Structure, ChunkPos, int, PiecesContainer)}
	 */
	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// foliage
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link FoliageColorResolverTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link BiomeColors#FOLIAGE_COLOR_RESOLVER}
	 */
	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return FoliageColorHandler.get(o, biome, x, z);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lead
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link LeashFenceKnotSurvivesTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link LeashFenceKnotEntity#survives()}
	 */
	public static boolean leashFenceKnotSurvives(boolean o, LeashFenceKnotEntity entity) {
		if (o)
			return true; // Short-circuit to avoid an unnecessary #getBlockState call
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// map
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link RenderMapDecorationsTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link MapRenderer.MapInstance#draw(PoseStack, MultiBufferSource, boolean, int)}
	 */
	public static int renderMapDecorations(int o, MapItemSavedData data, PoseStack stack, MultiBufferSource buffer, int light) {
		if (data instanceof TFMagicMapData mapData) {
			for (TFMagicMapData.TFMapDecoration decoration : mapData.tfDecorations.values()) {
				decoration.render(o, stack, buffer, light);
				o++;
			}
		}
		return o;
	}

	/**
	 * {@link ResolveMapDataForRenderTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link ItemInHandRenderer#renderMap(PoseStack, MultiBufferSource, int, ItemStack)}
	 */
	@Nullable
	public static MapItemSavedData resolveMapDataForRender(@Nullable MapItemSavedData o, ItemStack stack, @Nullable Level level) {
		return isOurMap(stack) && level != null ? MapItem.getSavedData(stack, level) : o;
	}

	/**
	 * {@link ResolveNearestNonRandomSpreadMapStructureTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link ChunkGenerator#findNearestMapStructure(ServerLevel, HolderSet, BlockPos, int, boolean)}
	 */
	@Nullable
	public static Pair<BlockPos, Holder<Structure>> resolveNearestNonRandomSpreadMapStructure(@Nullable Pair<BlockPos, Holder<Structure>> o, ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		return WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius, skipKnownStructures).orElse(o);
	}

	/**
	 * {@link ShouldMapRenderInArmTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link ItemInHandRenderer#renderArmWithItem(AbstractClientPlayer, float, float, InteractionHand, float, ItemStack, float, PoseStack, MultiBufferSource, int)}<br/>
	 * Targets: {@link Items#FILLED_MAP} and {@link ItemStack#is(Item)}
	 */
	public static boolean shouldMapRenderInArm(boolean o, ItemStack stack) {
		return o || isOurMap(stack);
	}

	private static boolean isOurMap(ItemStack stack) {
		return stack.is(TFItems.FILLED_MAGIC_MAP.get()) || stack.is(TFItems.FILLED_MAZE_MAP.get()) || stack.is(TFItems.FILLED_ORE_MAP.get());
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// multipart
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link ResolveEntitiesForRendereringTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link LevelRenderer#renderLevel(DeltaTracker, boolean, Camera, GameRenderer, LightTexture, Matrix4f, Matrix4f)}<br/>
	 * [Targets: {@link ClientLevel#entitiesForRendering}]
	 */
	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	/**
	 * {@link ResolveEntityRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link EntityRenderDispatcher#getRenderer(Entity)}<br/>
	 * Targets: {@link EntityRenderDispatcher#renderers}
	 */
	@Nullable
	public static EntityRenderer<?> resolveEntityRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	/**
	 * {@link SendDirtytEntityDataTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link ServerEntity#sendDirtyEntityData}
	 */
	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// shroom
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link ModifySoilDecisionForMushroomBlockSurvivabilityTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link MushroomBlock#canSurvive(BlockState, LevelReader, BlockPos)}<br/>
	 * Targets: {@link BlockState#canSustainPlant(BlockGetter, BlockPos, Direction, BlockState)}
	 */
	public static TriState modifySoilDecisionForMushroomBlockSurvivability(TriState o, LevelReader level, BlockPos pos) {
		if (!o.isDefault())
			return o; // Short-circuit - We should not override non-default soil behaviour otherwise this would allow Mushrooms to survive on ALL blocks
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL))
					return TriState.TRUE;
			}
		}
		return o;
	}
}
