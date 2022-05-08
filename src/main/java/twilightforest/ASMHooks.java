package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Function4;
import com.mojang.math.Matrix4f;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.entity.TFEntities;
import twilightforest.entity.TFPart;
import twilightforest.item.TFItems;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.TwilightFeatures;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.WorldGenSettings#WorldGenSettings(long, boolean, boolean, net.minecraft.core.Registry, Optional)}<br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		TwilightFeatures.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.storage.LevelStorageSource#readWorldGenSettings(Dynamic, DataFixer, int)}<br>
	 * [BEFORE FIRST ASTORE]
	 */
	public static Dynamic<Tag> seed(Dynamic<Tag> seed) {
		TwilightFeatures.seed = ((CompoundTag) seed.getValue()).getLong("seed");
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
	public static MapItemSavedData renderMapData(@Nullable MapItemSavedData o, ItemStack stack, Level level) {
		return o == null && isOurMap(stack) ? MapItem.getSavedData(stack, level) : o;
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
			return TFEntities.BakedMultiPartRenderers.lookup(((TFPart<?>) entity).renderer());
		return renderer;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#onResourceManagerReload(ResourceManager)}<br>
	 * [AFTER FIRST INVOKESPECIAL]
	 */
	@OnlyIn(Dist.CLIENT)
	public static EntityRendererProvider.Context bakeMultipartRenders(EntityRendererProvider.Context context) {
		TFEntities.BakedMultiPartRenderers.bakeMultiPartRenderers(context);
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
	public static int foliage(int o, Biome biome, double x, double z) {
		return FoliageColorHandler.get(o, biome, x, z);
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.feature.StructureFeature#loadStaticStart(ServerLevel, CompoundTag, long)} <br>
	 * [AFTER {@link net.minecraft.world.level.levelgen.feature.StructureFeature#createStart(ChunkPos, int, long)}]
	 */
	public static StructureStart conquered(StructureStart start, CompoundTag nbt) {
		if (start instanceof TFStructureStart<?> s)
			s.load(nbt);
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

}
