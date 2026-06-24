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
import twilightforest.entity.projectile.CubeOfAnnihilation;

public class CubeOfAnnihilationRenderer extends EntityRenderer<CubeOfAnnihilation, CubeOfAnnihilationRenderState> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("cubeofannihilation.png");
	private final CubeOfAnnihilationModel model;

	public CubeOfAnnihilationRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CubeOfAnnihilationModel(context.bakeLayer(TFModelLayers.CUBE_OF_ANNIHILATION));
	}

	@Override
	public CubeOfAnnihilationRenderState createRenderState() {
		return new CubeOfAnnihilationRenderState();
	}

	@Override
	public void submit(CubeOfAnnihilationRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		super.submit(state, poseStack, submitNodeCollector, camera);
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(state.ageInTicks * 11.0F)));
		poseStack.translate(0.0F, -0.5F, 0.0F);
		submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
		poseStack.popPose();
	}

	@Override
	public void extractRenderState(CubeOfAnnihilation entity, CubeOfAnnihilationRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.rotation = Mth.sin((state.ageInTicks + entity.getYRot(partialTicks)));
	}
}
