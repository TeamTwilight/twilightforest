package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.entity.projectile.TFThrowable;

/**
 * This renderer serves as a way to render item textures on a projectile without needing an actual item registered for it.
 * Consider using {@link net.minecraft.client.renderer.entity.ThrownItemRenderer} if your projectile is an existing item already.
 */
public class CustomProjectileTextureRenderer extends EntityRenderer<TFThrowable> {

	private final ResourceLocation TEXTURE;

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture) {
		super(ctx);
		this.TEXTURE = texture;
	}

	//[VanillaCopy] of DragonFireballRender.render, we just input our own texture stuff instead
	@Override
	public void render(TFThrowable entity, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
		ms.pushPose();
		ms.scale(0.5F, 0.5F, 0.5F);
		ms.mulPose(this.entityRenderDispatcher.cameraOrientation());
		ms.mulPose(Axis.YP.rotationDegrees(180.0F));
		PoseStack.Pose pose = ms.last();
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		vertex(vertexconsumer, pose, light, 0.0F, 0, 0, 1);
		vertex(vertexconsumer, pose, light, 1.0F, 0, 1, 1);
		vertex(vertexconsumer, pose, light, 1.0F, 1, 1, 0);
		vertex(vertexconsumer, pose, light, 0.0F, 1, 0, 0);
		ms.popPose();
		super.render(entity, entityYaw, partialTicks, ms, buffer, light);
	}

	private static void vertex(VertexConsumer p_114090_, PoseStack.Pose pose, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
		p_114090_.addVertex(pose, p_114094_ - 0.5F, (float) p_114095_ - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv((float) p_114096_, (float) p_114097_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(p_114093_).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(TFThrowable entity) {
		return TEXTURE;
	}
}
