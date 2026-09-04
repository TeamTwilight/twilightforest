package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class SlimeBeetleRenderer extends MobRenderer<SlimeBeetle, LivingEntityRenderState, SlimeBeetleModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("slimebeetle.png");

	public SlimeBeetleRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeBeetleModel(context.bakeLayer(TFModelLayers.SLIME_BEETLE), false), 0.6F);
		this.addLayer(new OuterTailLayer(this, context.getModelSet()));
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}

	public static class OuterTailLayer extends RenderLayer<LivingEntityRenderState, SlimeBeetleModel> {

		private final SlimeBeetleModel model;

		public OuterTailLayer(RenderLayerParent<LivingEntityRenderState, SlimeBeetleModel> renderer, EntityModelSet modelSet) {
			super(renderer);
			this.model = new SlimeBeetleModel(modelSet.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL), true);
		}

		@Override
		public void submit(PoseStack stack, SubmitNodeCollector collector, int light, LivingEntityRenderState state, float yRot, float xRot) {
			if (!state.isInvisible) {
				collector.order(1)
					.submitModel(this.model, state, stack, RenderTypes.entityTranslucent(TEXTURE), light, LivingEntityRenderer.getOverlayCoords(state, 0), state.outlineColor, null);
			}
		}
	}
}
