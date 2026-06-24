package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.client.state.block.MoonwormRenderState;
import twilightforest.client.state.entity.MoonwormShotRenderState;
import twilightforest.entity.projectile.MoonwormShot;

public class MoonwormShotRenderer extends EntityRenderer<MoonwormShot, MoonwormShotRenderState> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("moonworm.png");
	private final MoonwormModel model;
	private final MoonwormRenderState state = new MoonwormRenderState();

	public MoonwormShotRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		this.model = new MoonwormModel(context.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void submit(MoonwormShotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.translate(0.0F, 0.5F, 0.0F);
		poseStack.scale(-1.0F, -1.0F, -1.0F);

		poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 180.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
		submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

		poseStack.popPose();
	}


	@Override
	public MoonwormShotRenderState createRenderState() {
		return new MoonwormShotRenderState();
	}

	@Override
	public void extractRenderState(MoonwormShot entity, MoonwormShotRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.xRot = entity.getXRot(partialTick);
		state.yRot = entity.getYRot(partialTick);
	}
}
