package twilightforest.client.renderer.entity.legacy;

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
import twilightforest.client.model.entity.legacy.SlimeBeetleLegacyModel;
import twilightforest.entity.monster.SlimeBeetle;

public class LegacySlimeBeetleRenderer extends MobRenderer<SlimeBeetle, SlimeBeetleLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

	public LegacySlimeBeetleRenderer(EntityRendererProvider.Context manager, SlimeBeetleLegacyModel model, float shadowSize) {
		super(manager, model, shadowSize);
		addLayer(new LayerInner(this, manager));
	}

	@Override
	public void render(SlimeBeetle entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if(this.model.riding) matrixStackIn.translate(0, -0.5F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(SlimeBeetle entity) {
		return textureLoc;
	}

	static class LayerInner extends RenderLayer<SlimeBeetle, SlimeBeetleLegacyModel> {
		private final SlimeBeetleLegacyModel innerModel;

		public LayerInner(RenderLayerParent<SlimeBeetle, SlimeBeetleLegacyModel> renderer, EntityRendererProvider.Context manager) {
			super(renderer);
			innerModel =  new SlimeBeetleLegacyModel(manager.bakeLayer(TFModelLayers.LEGACY_SLIME_BEETLE_TAIL));
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, SlimeBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				innerModel.copyPropertiesTo(getParentModel());
				innerModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
				innerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
				innerModel.renderTail(ms, buffer, light, LivingEntityRenderer.getOverlayCoords(entity, 0), 1, 1, 1, 1);
			}
		}
	}
}
