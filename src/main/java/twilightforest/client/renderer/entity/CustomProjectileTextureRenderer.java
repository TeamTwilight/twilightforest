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
import org.joml.Matrix3f;
import org.joml.Matrix4f;
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
		PoseStack.Pose posestack$pose = ms.last();
		Matrix4f matrix4f = posestack$pose.pose();
		Matrix3f matrix3f = posestack$pose.normal();
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, 0, 0, 1);
		vertex(vertexconsumer, matrix4f, matrix3f, light, 1.0F, 0, 1, 1);
		vertex(vertexconsumer, matrix4f, matrix3f, light, 1.0F, 1, 1, 0);
		vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, 1, 0, 0);
		ms.popPose();
		super.render(entity, entityYaw, partialTicks, ms, buffer, light);
	}

	private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
		p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(TFThrowable entity) {
		return TEXTURE;
	}
}
