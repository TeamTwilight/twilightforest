package twilightforest.client.renderer.entity.newmodels;

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
import twilightforest.client.model.entity.newmodels.NewSlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class NewSlimeBeetleRenderer extends MobRenderer<SlimeBeetle, NewSlimeBeetleModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

	public NewSlimeBeetleRenderer(EntityRendererProvider.Context manager, NewSlimeBeetleModel model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerInner(this, manager));
	}

	@Override
	public ResourceLocation getTextureLocation(SlimeBeetle entity) {
		return textureLoc;
	}

	static class LayerInner extends RenderLayer<SlimeBeetle, NewSlimeBeetleModel> {
		private final NewSlimeBeetleModel innerModel;

		public LayerInner(RenderLayerParent<SlimeBeetle, NewSlimeBeetleModel> renderer, EntityRendererProvider.Context manager) {
			super(renderer);
			innerModel =  new NewSlimeBeetleModel(manager.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL));
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
