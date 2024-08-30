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

	private final ResourceLocation texture;

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture) {
		super(ctx);
		this.texture = texture;
	}

	//[VanillaCopy] of DragonFireballRender.render, we just input our own texture stuff instead
	@Override
	public void render(TFThrowable entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		stack.pushPose();
		stack.scale(0.5F, 0.5F, 0.5F);
		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		PoseStack.Pose pose = stack.last();
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.texture));
		vertex(consumer, pose, light, 0.0F, 0.0F, 0.0F, 1.0F);
		vertex(consumer, pose, light, 1.0F, 0.0F, 1.0F, 1.0F);
		vertex(consumer, pose, light, 1.0F, 1.0F, 1.0F, 0.0F);
		vertex(consumer, pose, light, 0.0F, 1.0F, 0.0F, 0.0F);
		stack.popPose();
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
	}

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float xOffset, float zOffset, float u, float v) {
		consumer.addVertex(pose, xOffset - 0.5F, zOffset - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(TFThrowable entity) {
		return this.texture;
	}
}
