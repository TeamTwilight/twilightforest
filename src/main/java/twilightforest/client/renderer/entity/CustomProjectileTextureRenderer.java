package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.entity.projectile.TFThrowable;

/**
 * This renderer serves as a way to render item textures on a projectile without needing an actual item registered for it.
 * Consider using {@link net.minecraft.client.renderer.entity.ThrownItemRenderer} if your projectile is an existing item already.
 */
public class CustomProjectileTextureRenderer extends EntityRenderer<TFThrowable, EntityRenderState> {

	private final Identifier texture;
	private final float scale;
	private final boolean fullBright;
	private final boolean flashing;

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, Identifier texture, float scale, boolean fullBright, boolean flashing) {
		super(ctx);
		this.texture = texture;
		this.scale = scale;
		this.fullBright = fullBright;
		this.flashing = flashing;
	}

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, Identifier texture) {
		this(ctx, texture, 1.0F, false, false);
	}

	@Override
	protected int getBlockLightLevel(TFThrowable entity, BlockPos pos) {
		return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
	}

	@Override
	public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (this.flashing) {
			poseStack.pushPose();
			float f = (Mth.sin(state.ageInTicks) + 1.0F) * 0.5F;
			float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f2 = (1.0F + f * 0.4F) * f1;
			float f3 = (1.0F + f * 0.1F) / f1;
			poseStack.scale(f2, f3, f2);
			this.submit(state, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, camera);
			poseStack.popPose();
		} else {
			this.submit(state, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, camera);
		}
	}

	//[VanillaCopy] of DragonFireballRender.render, we just input our own texture stuff instead
	public void submit(EntityRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, int light, int overlay, CameraRenderState camera) {
		stack.pushPose();
		stack.scale(0.5F * this.scale, 0.5F * this.scale, 0.5F * this.scale);

		stack.mulPose(camera.orientation);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		submitNodeCollector.submitCustomGeometry(stack, RenderTypes.entityCutout(this.texture), (pose1, vertexConsumer) -> {
			vertex(vertexConsumer, pose1, light, 0.0F, 0.0F, 0.0F, 1.0F, overlay);
			vertex(vertexConsumer, pose1, light, 1.0F, 0.0F, 1.0F, 1.0F, overlay);
			vertex(vertexConsumer, pose1, light, 1.0F, 1.0F, 1.0F, 0.0F, overlay);
			vertex(vertexConsumer, pose1, light, 0.0F, 1.0F, 0.0F, 0.0F, overlay);
		});

		stack.popPose();
		super.submit(state, stack, submitNodeCollector, camera);
	}

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float xOffset, float zOffset, float u, float v, int overlay) {
		consumer.addVertex(pose, xOffset - 0.5F, zOffset - 0.25F, 0.0F).setColor(-1).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}
