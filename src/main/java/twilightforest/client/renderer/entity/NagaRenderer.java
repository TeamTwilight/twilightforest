package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.entity.boss.Naga;

public class NagaRenderer<M extends NagaModel<Naga>> extends MobRenderer<Naga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation textureLocCharging = TwilightForestMod.getModelTexture("nagahead_charging.png");

	@SuppressWarnings("this-escape")
	public NagaRenderer(EntityRendererProvider.Context manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new NagaEyelidsLayer<>(this));
	}

	@Override
	protected void scale(Naga entity, PoseStack stack, float partialTicks) {
		super.scale(entity, stack, partialTicks);
		//make size adjustment
		stack.scale(2.01F, 2.01F, 2.01F);
		stack.translate(0.0F, entity.isDazed() ? 1.075F : 0.75F, entity.isDazed() ? 0.175F : 0.0F);
	}

	@Override
	protected float getFlipDegrees(Naga naga) { //Prevent the body from keeling over
		return naga.isDeadOrDying() ? 0.0F : super.getFlipDegrees(naga);
	}

	@Override
	public ResourceLocation getTextureLocation(Naga entity) {
		if (entity.isCharging() || entity.isDeadOrDying()) {
			return textureLocCharging;
		} else {
			return textureLoc;
		}
	}

	public static class NagaEyelidsLayer<T extends Naga, M extends NagaModel<T>> extends RenderLayer<T, M> {
		private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");

		public NagaEyelidsLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack stack, MultiBufferSource buffer, int light, T naga, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (naga.isDazed()) {
				VertexConsumer vertex = buffer.getBuffer(RenderType.entityCutoutNoCull(textureLocDazed));
				this.getParentModel().renderToBuffer(stack, vertex, light, OverlayTexture.pack(0.0F, naga.hurtTime > 0), 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
}
