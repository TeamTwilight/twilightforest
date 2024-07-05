package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ProtectionBoxModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.entity.ProtectionBox;

public class ProtectionBoxRenderer<T extends ProtectionBox> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("protectionbox.png");
	private final ProtectionBoxModel<T> boxModel;

	public ProtectionBoxRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.5F;
		boxModel = new ProtectionBoxModel<>(manager.bakeLayer(TFModelLayers.PROTECTION_BOX));
	}

	@Override
	public boolean shouldRender(T p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
		return true;
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {

		float alpha = 1.0F;
		if (entity.lifeTime < 20) alpha = entity.lifeTime / 20F;

		VertexConsumer vertexconsumer = buffer.getBuffer(TFRenderTypes.PROTECTION_BOX);
		boxModel.prepareMobModel(entity, 0, 0, 0);
		boxModel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
