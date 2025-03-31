package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ProtectionBoxModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.client.state.ProtectionBoxRenderState;
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
	public void render(ProtectionBoxRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {

		float alpha = 1.0F;
		if (state.life < 20) alpha = state.life / 20.0F;

		VertexConsumer vertexconsumer = buffer.getBuffer(TFRenderTypes.PROTECTION_BOX);
		this.boxModel.setupAnim(state);
		this.boxModel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
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
