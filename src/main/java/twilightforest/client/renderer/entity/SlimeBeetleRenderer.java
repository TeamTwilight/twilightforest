package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.SlimeBeetleModel;
import twilightforest.entity.monster.SlimeBeetle;

public class SlimeBeetleRenderer<T extends SlimeBeetle, M extends HierarchicalModel<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("slimebeetle.png");

	public SlimeBeetleRenderer(EntityRendererProvider.Context context, M model, ModelPart innerRoot, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new OuterTailLayer<>(this, innerRoot));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return TEXTURE;
	}

	public static class OuterTailLayer<T extends SlimeBeetle, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {
		private final SlimeBeetleModel<T> tailModel;

		public OuterTailLayer(RenderLayerParent<T, M> renderer, ModelPart innerRoot) {
			super(renderer);
			this.tailModel = new SlimeBeetleModel<>(innerRoot);
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isInvisible()) {
				this.tailModel.copyPropertiesTo(this.getParentModel());
				this.tailModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
				this.tailModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
				this.tailModel.renderTail(ms, consumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		}
	}
}
