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
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.client.TFClientProxy;
import twilightforest.entity.TFPartEntity;
import twilightforest.network.PacketUpdateTFMultipart;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.TFDimensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	public static World world;

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
		if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && (music == BackgroundMusicTracks.CREATIVE_MODE_MUSIC || music == BackgroundMusicTracks.UNDER_WATER_MUSIC) && Minecraft.getInstance().world.getDimensionKey().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get()))
			return Minecraft.getInstance().world.getBiomeManager().getBiomeAtPosition(Minecraft.getInstance().player.getPosition()).getBackgroundMusic().orElse(BackgroundMusicTracks.WORLD_MUSIC);
		return music;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.World#getEntitiesInAABBexcluding }<br>
	 * [BEFORE ARETURN]
	 */
	public static List<Entity> multipartHitbox(List<Entity> list, World world, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
		ASMHooks.world = world;
		Iterable<Entity> loaded = DistExecutor.safeRunForDist(() -> TFClientProxy::getEntityListForASM, () -> TFCommonProxy::getEntityListForASM);
		ASMHooks.world = null;
		if (loaded != null)
			loaded.forEach(entity -> {
				if (entity.isMultipartEntity() && entity.getParts() != null)
					for (PartEntity<?> part : entity.getParts()) {
						if (part instanceof TFPartEntity &&

								part != entityIn &&

								part.getBoundingBox().intersects(boundingBox) &&

								(predicate == null || predicate.test(part)) &&

								!list.contains(part))
							list.add(part);
					}
			});
		return list;
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
