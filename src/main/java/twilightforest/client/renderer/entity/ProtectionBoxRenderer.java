package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ProtectionBoxModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.client.state.entity.ProtectionBoxRenderState;
import twilightforest.entity.ProtectionBox;

public class ProtectionBoxRenderer extends EntityRenderer<ProtectionBox, ProtectionBoxRenderState> {

	private final ProtectionBoxModel boxModel;

	public ProtectionBoxRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0F;
		this.boxModel = new ProtectionBoxModel(context.bakeLayer(TFModelLayers.PROTECTION_BOX));
	}

	@Override
	public boolean shouldRender(ProtectionBox entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public void submit(ProtectionBoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

		float alpha = 1.0F;
		if (state.life < 20) alpha = state.life / 20.0F;

		this.boxModel.setupAnim(state);
		//TODO Need to fix RenderType
		submitNodeCollector.submitModel(this.boxModel, state, poseStack, TFRenderTypes.PROTECTION_BOX, state.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.white(alpha), null, state.outlineColor, null);
	}

	@Override
	public ProtectionBoxRenderState createRenderState() {
		return new ProtectionBoxRenderState();
	}

	@Override
	public void extractRenderState(ProtectionBox entity, ProtectionBoxRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.life = entity.lifeTime;
		state.sizeX = entity.sizeX;
		state.sizeY = entity.sizeY;
		state.sizeZ = entity.sizeZ;
	}
}
