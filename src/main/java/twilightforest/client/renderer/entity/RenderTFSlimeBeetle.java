package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFSlimeBeetle;
import twilightforest.entity.EntityTFSlimeBeetle;

public class RenderTFSlimeBeetle extends MobRenderer<EntityTFSlimeBeetle, ModelTFSlimeBeetle> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

	public RenderTFSlimeBeetle(EntityRendererManager manager, ModelTFSlimeBeetle model, float shadowSize) {
		super(manager, model, shadowSize);
		addLayer(new LayerInner(this));
	}

	@Override
	public void render(EntityTFSlimeBeetle entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(this.entityModel.isSitting) matrixStackIn.translate(0, -0.5F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFSlimeBeetle entity) {
		return textureLoc;
	}

	static class LayerInner extends LayerRenderer<EntityTFSlimeBeetle, ModelTFSlimeBeetle> {
		private final ModelTFSlimeBeetle innerModel = new ModelTFSlimeBeetle(true);

		public LayerInner(IEntityRenderer<EntityTFSlimeBeetle, ModelTFSlimeBeetle> renderer) {
			super(renderer);
		}

		@Override
		public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, EntityTFSlimeBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				innerModel.copyModelAttributesTo(getEntityModel());
				innerModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
				innerModel.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				IVertexBuilder buffer = buffers.getBuffer(RenderType.getEntityTranslucent(getEntityTexture(entity)));
				innerModel.render(ms, buffer, light, LivingRenderer.getPackedOverlay(entity, 0), 1, 1, 1, 1);
			}
		}
	}
}
