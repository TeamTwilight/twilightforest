package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
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
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class SlimeBeetleRenderer<T extends SlimeBeetle, M extends EntityModel<LivingEntityRenderState>> extends MobRenderer<T, LivingEntityRenderState, M> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("slimebeetle.png");

	public SlimeBeetleRenderer(EntityRendererProvider.Context context, M model, ModelPart innerRoot, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new OuterTailLayer<>(this, innerRoot));
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState entity) {
		return TEXTURE;
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	public static class OuterTailLayer<T extends LivingEntityRenderState, M extends EntityModel<T>> extends RenderLayer<T, M> {
		private final SlimeBeetleModel<T> tailModel;

		public OuterTailLayer(RenderLayerParent<T, M> renderer, ModelPart innerRoot) {
			super(renderer);
			this.tailModel = new SlimeBeetleModel<>(innerRoot);
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, T entity, float v, float v1) {
			if (!entity.isInvisible) {
				this.tailModel.setupAnim(entity);
				submitNodeCollector.submitModel(this.tailModel, entity, poseStack, RenderTypes.entityTranslucent(TEXTURE), light, LivingEntityRenderer.getOverlayCoords(entity, 0), entity.outlineColor, null);
			}
		}

	}
}
