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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class SlimeBeetleRenderer extends MobRenderer<SlimeBeetle, LivingEntityRenderState, SlimeBeetleModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("slimebeetle.png");

	public SlimeBeetleRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeBeetleModel(context.bakeLayer(TFModelLayers.SLIME_BEETLE)), 0.6F);
		this.addLayer(new OuterTailLayer(this));
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}

	public static class OuterTailLayer extends RenderLayer<LivingEntityRenderState, SlimeBeetleModel> {
		public OuterTailLayer(RenderLayerParent<LivingEntityRenderState, SlimeBeetleModel> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, LivingEntityRenderState state, float netHeadYaw, float headPitch) {
			if (!state.isInvisible) {
				VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(TEXTURE));
				this.getParentModel().renderTail(ms, consumer, light, LivingEntityRenderer.getOverlayCoords(state, 0));
			}
		}
	}
}
