package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.state.entity.NagaSegmentRenderState;
import twilightforest.entity.boss.NagaSegment;

public class NagaSegmentRenderer extends TFPartRenderer<NagaSegment, NagaSegmentRenderState, NagaModel<NagaSegmentRenderState>> {
	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("nagasegment.png");

	public NagaSegmentRenderer(EntityRendererProvider.Context context, NagaModel<NagaSegmentRenderState> model) {
		super(context, model);
	}

	@Override
	public void submit(NagaSegmentRenderState state, PoseStack stack, SubmitNodeCollector buffer, CameraRenderState cameraRenderState) {
		if (!state.isInvisible) {
			stack.pushPose();

			float yawDiff = state.yRot - state.yRotO;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = state.yRotO + yawDiff * state.partialTick;

			stack.mulPose(Axis.YP.rotationDegrees(yaw2));
			stack.mulPose(Axis.XP.rotationDegrees(state.xRot));

			stack.scale(2.0F, 2.0F, 2.0F);
			stack.translate(0.0D, -1.25F, 0.0D);

			super.submit(state, stack, buffer, cameraRenderState);

			stack.popPose();
		}
	}

	@Override
	public NagaSegmentRenderState createRenderState() {
		return new NagaSegmentRenderState();
	}

	@Override
	public void extractRenderState(NagaSegment entity, NagaSegmentRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.parentLight = this.entityRenderDispatcher.getPackedLightCoords(entity.getParent(), partialTick);
	}

	@Override
	public Identifier getTextureLocation(NagaSegmentRenderState state) {
		return TEXTURE;
	}
}
