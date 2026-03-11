package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ProtectionBoxModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.entity.ProtectionBox;

public class ProtectionBoxRenderer<T extends ProtectionBox> extends EntityRenderer<T> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("protectionbox.png");
	private final ProtectionBoxModel<T> boxModel;

	public ProtectionBoxRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0F;
		this.boxModel = new ProtectionBoxModel<>(context.bakeLayer(TFModelLayers.PROTECTION_BOX));
	}

	@Override
	public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {

		float alpha = 1.0F;
		if (entity.lifeTime < 20) alpha = entity.lifeTime / 20.0F;

		VertexConsumer vertexconsumer = buffer.getBuffer(TFRenderTypes.PROTECTION_BOX);
		this.boxModel.setupAnim(entity, 0.0F, 0.0F, 0, 0.0F, 0.0F);
		this.boxModel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}
}
