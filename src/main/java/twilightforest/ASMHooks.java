package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Function4;
import com.mojang.math.Matrix4f;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.entity.TFEntities;
import twilightforest.entity.TFPart;
import twilightforest.item.TFItems;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.registration.TFDimensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.levelgen.WorldGenSettings#WorldGenSettings(long, boolean, boolean, MappedRegistry, Optional)}<br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		TFDimensions.seed = seed;
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
	public static Music music(Music music) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && Minecraft.getInstance().level.dimension().location().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()))
			return Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).getBackgroundMusic().orElse(Musics.GAME);
		return music;
	}

	private static final WeakHashMap<Level, List<TFPart<?>>> cache = new WeakHashMap<>();
	private static final Int2ObjectMap<TFPart<?>> multiparts = new Int2ObjectOpenHashMap<>();

	// This only works on the client side in 1.17...
	public static void registerMultipartEvents(IEventBus bus) {
		bus.addListener((Consumer<EntityJoinWorldEvent>) event -> {
			if(event.getWorld().isClientSide() && event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfAbsent(event.getWorld(), (w) -> new ArrayList<>());
				cache.get(event.getWorld()).addAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
						filter(TFPart.class::isInstance).map(obj -> (TFPart<?>) obj).
						collect(Collectors.toList()));

			}
		});
		bus.addListener((Consumer<EntityLeaveWorldEvent>) event -> {
			if(event.getWorld().isClientSide() && event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfPresent(event.getWorld(), (world, list) -> {
					list.removeAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
							filter(TFPart.class::isInstance).map(obj -> (TFPart<?>) obj).
							collect(Collectors.toList()));
					return list;
				});
			}
		});
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel.EntityCallbacks#onTrackingStart(Entity)}<br>
	 * [FIRST INST]
	 */
	public static void trackingStart(Entity entity) {
		if (entity.isMultipartEntity()) {
			List<TFPart<?>> list = Arrays.stream(Objects.requireNonNull(entity.getParts())).
					filter(TFPart.class::isInstance).map(obj -> (TFPart<?>) obj).
					collect(Collectors.toList());
			list.forEach(part -> multiparts.put(part.getId(), part));
			synchronized (cache) {
				cache.computeIfAbsent(entity.level, (w) -> new ArrayList<>());
				cache.get(entity.level).addAll(list);
			}
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel.EntityCallbacks#onTrackingEnd(Entity)}<br>
	 * [FIRST INST]
	 */
	public static void trackingEnd(Entity entity) {
		if (entity.isMultipartEntity()) {
			List<TFPart<?>> list = Arrays.stream(Objects.requireNonNull(entity.getParts())).
					filter(TFPart.class::isInstance).map(obj -> (TFPart<?>) obj).
					collect(Collectors.toList());
			list.forEach(part -> multiparts.remove(part.getId()));
			synchronized (cache) {
				cache.computeIfPresent(entity.level, (world, parts) -> {
					parts.removeAll(list);
					return parts;
				});
			}
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.level.Level#getEntities(Entity, AABB, Predicate)}<br>
	 * [BEFORE ARETURN]
	 */
	public static synchronized List<Entity> multipartHitbox(List<Entity> list, Level world, @Nullable Entity entityIn, AABB boundingBox, @Nullable Predicate<? super Entity> predicate) {
		synchronized (cache) {
			List<TFPart<?>> parts = cache.get(world);
			if(parts != null) {
				for (TFPart<?> part : parts) {
					if (part != entityIn &&

							part.getBoundingBox().intersects(boundingBox) &&

							(predicate == null || predicate.test(part)) &&

							!list.contains(part))
						list.add(part);
				}
			}
			return list;
		}
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.server.level.ServerLevel#getEntityOrPart(int)}<br>
	 * [BEFORE ARETURN]
	 */
	public static Entity multipartFromID(@Nullable Entity o, int id) {
		return o == null ? multiparts.get(id) : o;
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
	 * {@link net.minecraft.client.Minecraft#doLoadLevel(String, RegistryAccess.RegistryHolder, Function, Function4, boolean, Minecraft.ExperimentalDialogType, boolean)}<br>
	 * [AFTER ALL ALOAD 6]
	 */
	public static Minecraft.ExperimentalDialogType dragons(Minecraft.ExperimentalDialogType type) {
		return TFConfig.CLIENT_CONFIG.disableHereBeDragons.get() ? Minecraft.ExperimentalDialogType.NONE : type;
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
	public static StructureStart<?> conquered(StructureStart<?> start, CompoundTag nbt) {
		if (start instanceof TFStructureStart.Start s)
			s.load(nbt);
		return start;
	}

}
