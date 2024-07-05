package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class SlimeBeetleRenderer extends MobRenderer<SlimeBeetle, SlimeBeetleModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

	@SuppressWarnings("this-escape")
	public SlimeBeetleRenderer(EntityRendererProvider.Context manager, SlimeBeetleModel model, float shadowSize) {
		super(manager, model, shadowSize);
		addLayer(new LayerInner(this, manager));
	}

	@Override
	public void render(SlimeBeetle entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if (this.model.riding) matrixStackIn.translate(0, -0.5F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(SlimeBeetle entity) {
		return textureLoc;
	}

	static class LayerInner extends RenderLayer<SlimeBeetle, SlimeBeetleModel> {
		private final SlimeBeetleModel innerModel;

		public LayerInner(RenderLayerParent<SlimeBeetle, SlimeBeetleModel> renderer, EntityRendererProvider.Context manager) {
			super(renderer);
			innerModel = new SlimeBeetleModel(manager.bakeLayer(TFModelLayers.NEW_SLIME_BEETLE_TAIL));
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, SlimeBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				innerModel.copyPropertiesTo(getParentModel());
				innerModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
				innerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
				innerModel.renderTail(ms, buffer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		}
	}
}
