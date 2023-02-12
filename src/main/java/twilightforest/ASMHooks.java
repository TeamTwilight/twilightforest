package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Matrix4f;
import twilightforest.client.TFClientSetup;
import twilightforest.init.TFDimensionSettings;
import twilightforest.entity.TFPart;
import twilightforest.init.TFItems;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.world.components.structures.start.CustomStructureData;
import twilightforest.world.registration.TFGenerationSettings;

import org.jetbrains.annotations.Nullable;
import java.util.*;

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
	 * {@link net.minecraft.world.level.storage.LevelStorageSource#readWorldGenSettings(Dynamic, DataFixer, int)}<br>
	 * [BEFORE FIRST ASTORE]
	 */
	public static Dynamic<Tag> seed(Dynamic<Tag> seed) {
		TFDimensionSettings.seed = ((CompoundTag) seed.getValue()).getLong("seed");
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.MapRenderer.MapInstance#draw(PoseStack, MultiBufferSource, boolean, int)}<br>
	 * [BEFORE FIRST ISTORE]
	 */
	public static void mapRenderContext(PoseStack stack, MultiBufferSource buffer, int light) {
		TFMagicMapData.TFMapDecoration.RenderContext.stack = stack;
		TFMagicMapData.TFMapDecoration.RenderContext.buffer = buffer;
		TFMagicMapData.TFMapDecoration.RenderContext.light = light;
	}

	private static boolean isOurMap(ItemStack stack) {
		return stack.is(TFItems.FILLED_MAGIC_MAP.get()) || stack.is(TFItems.FILLED_MAZE_MAP.get()) || stack.is(TFItems.FILLED_ORE_MAP.get());
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.ItemInHandRenderer#renderArmWithItem(AbstractClientPlayer, float, float, InteractionHand, float, ItemStack, float, PoseStack, MultiBufferSource, int)} <br>
	 * [AFTER INST AFTER FIRST GETSTATIC {@link net.minecraft.world.item.Items#FILLED_MAP}]
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
			TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new UpdateTFMultipartPacket(entity));
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
	public static Iterable<Entity> renderMutiparts(Iterable<Entity> iter) {
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
}
