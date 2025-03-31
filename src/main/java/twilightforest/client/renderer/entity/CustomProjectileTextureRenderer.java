package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.entity.projectile.TFThrowable;

/**
 * This renderer serves as a way to render item textures on a projectile without needing an actual item registered for it.
 * Consider using {@link net.minecraft.client.renderer.entity.ThrownItemRenderer} if your projectile is an existing item already.
 */
public class CustomProjectileTextureRenderer extends EntityRenderer<TFThrowable, EntityRenderState> {

	private final ResourceLocation texture;
	private final float scale;
	private final boolean fullBright;
	private final boolean flashing;

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture, float scale, boolean fullBright, boolean flashing) {
		super(ctx);
		this.texture = texture;
		this.scale = scale;
		this.fullBright = fullBright;
		this.flashing = flashing;
	}

	public CustomProjectileTextureRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture) {
		this(ctx, texture, 1.0F, false, false);
	}

	@Override
	protected int getBlockLightLevel(TFThrowable entity, BlockPos pos) {
		return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
	}

	@Override
	public void render(EntityRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		if (this.flashing) {
			stack.pushPose();
			float f = (Mth.sin(state.ageInTicks) + 1.0F) * 0.5F;
			float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
			f = Mth.clamp(f, 0.0F, 1.0F);
			f *= f;
			f *= f;
			float f2 = (1.0F + f * 0.4F) * f1;
			float f3 = (1.0F + f * 0.1F) / f1;
			stack.scale(f2, f3, f2);
			this.render(state, stack, buffer, light, OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(false)));
			stack.popPose();
		} else {
			this.render(state, stack, buffer, light, OverlayTexture.NO_OVERLAY);
		}
	}

	//[VanillaCopy] of DragonFireballRender.render, we just input our own texture stuff instead
	public void render(EntityRenderState state, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.scale(0.5F * this.scale, 0.5F * this.scale, 0.5F * this.scale);

		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		PoseStack.Pose pose = stack.last();
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.texture));

		vertex(consumer, pose, light, 0.0F, 0.0F, 0.0F, 1.0F, overlay);
		vertex(consumer, pose, light, 1.0F, 0.0F, 1.0F, 1.0F, overlay);
		vertex(consumer, pose, light, 1.0F, 1.0F, 1.0F, 0.0F, overlay);
		vertex(consumer, pose, light, 0.0F, 1.0F, 0.0F, 0.0F, overlay);
		stack.popPose();
		super.render(state, stack, buffer, light);
	}

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float xOffset, float zOffset, float u, float v, int overlay) {
		consumer.addVertex(pose, xOffset - 0.5F, zOffset - 0.25F, 0.0F).setColor(-1).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}
