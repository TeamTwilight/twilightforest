package twilightforest;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.entity.TFPartEntity;
import twilightforest.network.PacketUpdateTFMultipart;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.TFDimensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static volatile World world;

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.gen.settings.DimensionGeneratorSettings#DimensionGeneratorSettings(long, boolean, boolean, SimpleRegistry, Optional)}<br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		TFDimensions.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.MapItemRenderer.Instance#func_228089_a_(MatrixStack, IRenderTypeBuffer, boolean, int)}<br>
	 * [BEFORE FIRST ISTORE]
	 */
	public static void mapRenderContext(MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		TFMagicMapData.TFMapDecoration.RenderContext.stack = stack;
		TFMagicMapData.TFMapDecoration.RenderContext.buffer = buffer;
		TFMagicMapData.TFMapDecoration.RenderContext.light = light;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.audio.MusicTicker#tick()}<br>
	 * [AFTER FIRST INVOKEVIRTUAL]
	 */
	public static BackgroundMusicSelector music(BackgroundMusicSelector music) {
		if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && (music == BackgroundMusicTracks.CREATIVE_MODE_MUSIC || music == BackgroundMusicTracks.UNDER_WATER_MUSIC) && Minecraft.getInstance().world.getDimensionKey().getLocation().equals(TFDimensions.twilightForest.getLocation()))
			return Minecraft.getInstance().world.getBiomeManager().getBiomeAtPosition(Minecraft.getInstance().player.getPosition()).getBackgroundMusic().orElse(BackgroundMusicTracks.WORLD_MUSIC);
		return music;
	}

	private static final WeakHashMap<World, List<TFPartEntity<?>>> cache = new WeakHashMap<>();

	public static void registerMultipartEvents(IEventBus bus) {
		bus.addListener((Consumer<EntityJoinWorldEvent>) event -> {
			if(event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfAbsent(event.getWorld(), (w) -> new ArrayList<>());
				cache.get(event.getWorld()).addAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
						filter(TFPartEntity.class::isInstance).map(obj -> (TFPartEntity<?>) obj).
						collect(Collectors.toList()));

			}
		});
		bus.addListener((Consumer<EntityLeaveWorldEvent>) event -> {
			if(event.getEntity().isMultipartEntity())
			synchronized (cache) {
				cache.computeIfPresent(event.getWorld(), (world, list) -> {
					list.removeAll(Arrays.stream(Objects.requireNonNull(event.getEntity().getParts())).
							filter(TFPartEntity.class::isInstance).map(TFPartEntity.class::cast).
							collect(Collectors.toList()));
					return list;
				});
			}
		});
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.World#getEntitiesInAABBexcluding }<br>
	 * [BEFORE ARETURN]
	 */
	public static synchronized List<Entity> multipartHitbox(List<Entity> list, World world, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
		synchronized (cache) {
			List<TFPartEntity<?>> parts = cache.get(world);
			if(parts != null) {
				for (TFPartEntity<?> part : parts) {
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
	 * {@link net.minecraft.world.TrackedEntity#sendMetadata }<br>
	 * [AFTER GETFIELD]
	 */
	public static Entity updateMultiparts(Entity entity) {
		if (entity.isMultipartEntity())
			TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new PacketUpdateTFMultipart(entity));
		return entity;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.entity.EntityRendererManager#getRenderer(Entity)}  }<br>
	 * [AFTER GETFIELD]
	 */
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public static EntityRenderer<?> getMultipartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity, EntityRendererManager manager) {
		if(entity instanceof TFPartEntity<?>)
			return ((TFPartEntity) entity).renderer(manager);
		return renderer;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.renderer.WorldRenderer#updateCameraAndRender(MatrixStack, float, long, boolean, ActiveRenderInfo, GameRenderer, LightTexture, Matrix4f)} )}  }<br>
	 * [AFTER {@link net.minecraft.client.world.ClientWorld#getAllEntities}]
	 */
	public static Iterable<Entity> renderMutiparts(Iterable<Entity> iter) {
		List<Entity> list = new ArrayList<>();
		iter.forEach(entity -> {
			list.add(entity);
			if(entity.isMultipartEntity() && entity.getParts() != null) {
				for (PartEntity<?> part : entity.getParts()) {
					if(part instanceof TFPartEntity)
						list.add(part);
				}
			}
		});
		return list;
	}

}
