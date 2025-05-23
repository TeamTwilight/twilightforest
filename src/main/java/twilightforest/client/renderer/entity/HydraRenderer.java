package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraModel;
import twilightforest.client.state.HydraRenderState;
import twilightforest.entity.boss.Hydra;

public class HydraRenderer extends MobRenderer<Hydra, HydraRenderState, HydraModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraRenderer(EntityRendererProvider.Context context) {
		super(context, new HydraModel(context.bakeLayer(TFModelLayers.HYDRA)), 4.0F);
	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	protected void setupRotations(HydraRenderState state, PoseStack stack, float yaw, float scale) {
		if (this.isShaking(state)) {
			yaw += Mth.cos(Mth.floor(state.ageInTicks) * 3.25F) * Mth.PI * 0.4F;
		}

		if (!state.hasPose(Pose.SLEEPING)) {
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		}

		if (state.deathTime > 0) {
			float f = (state.deathTime - 1.0F) / 20.0F * 1.6F;
			f = Mth.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			stack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees()));
		} else if (state.isUpsideDown) {
			stack.translate(0.0F, 6.0F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	@Override
	public HydraRenderState createRenderState() {
		return new HydraRenderState();
	}

	@Override
	public void extractRenderState(Hydra entity, HydraRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.renderFakeHeads = entity.renderFakeHeads;
	}

	@Override
	public ResourceLocation getTextureLocation(HydraRenderState state) {
		return TEXTURE;
	}
}
