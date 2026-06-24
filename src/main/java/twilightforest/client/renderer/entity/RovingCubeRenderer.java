package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CubeOfAnnihilationModel;
import twilightforest.client.state.entity.CubeOfAnnihilationRenderState;
import twilightforest.entity.RovingCube;

public class RovingCubeRenderer extends EntityRenderer<RovingCube, CubeOfAnnihilationRenderState> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final CubeOfAnnihilationModel model;

	public RovingCubeRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CubeOfAnnihilationModel(context.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public void submit(CubeOfAnnihilationRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();


		poseStack.scale(2.0F, 2.0F, 2.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(state.ageInTicks) * 11.0F));
		poseStack.translate(0.0F, 0.75F, 0.0F);
		submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		poseStack.popPose();
	}

	@Override
	public CubeOfAnnihilationRenderState createRenderState() {
		return new CubeOfAnnihilationRenderState();
	}

	@Override
	public void extractRenderState(RovingCube entity, CubeOfAnnihilationRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.rotation = Mth.sin((entity.tickCount + entity.getYRot()));
	}
}
