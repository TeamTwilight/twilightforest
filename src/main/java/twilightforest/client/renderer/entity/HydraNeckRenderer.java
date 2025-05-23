package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.state.HydraNeckRenderState;
import twilightforest.entity.boss.HydraNeck;

public class HydraNeckRenderer extends TFPartRenderer<HydraNeck, HydraNeckRenderState, HydraNeckModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraNeckRenderer(EntityRendererProvider.Context context, HydraNeckModel model) {
		super(context, model);
	}

	@Override
	public void render(HydraNeckRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		if (state.active) {
			float yawDiff = state.yRot - state.yRotO;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = state.yRotO + yawDiff * state.partialTick;

			stack.mulPose(Axis.YN.rotationDegrees(yaw2 + 180));
			super.render(state, stack, buffer, light);
		}
	}

	@Override
	public HydraNeckRenderState createRenderState() {
		return new HydraNeckRenderState();
	}

	@Override
	public void extractRenderState(HydraNeck entity, HydraNeckRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		var container = HydraHeadRenderer.getHeadObject(entity.head);
		state.active = container == null || container.isActive();
	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(HydraNeckRenderState state) {
		return TEXTURE;
	}
}
