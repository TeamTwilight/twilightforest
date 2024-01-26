package twilightforest.client.renderer.entity.newmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.entity.newmodels.NewKoboldModel;
import twilightforest.client.renderer.entity.TFBipedRenderer;
import twilightforest.entity.monster.Kobold;

public class NewKoboldRenderer extends TFBipedRenderer<Kobold, NewKoboldModel> {

	public NewKoboldRenderer(EntityRendererProvider.Context context, NewKoboldModel model, float shadowSize, String textureName) {
		super(context, model, shadowSize, textureName);

		this.layers.removeIf(r -> r instanceof ItemInHandLayer);
		this.addLayer(new HeldItemLayer(this, context.getItemInHandRenderer()));
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.ItemInHandLayer} with additional transforms
	 */
	private static class HeldItemLayer extends RenderLayer<Kobold, NewKoboldModel> {
		private final ItemInHandRenderer handRenderer;

		public HeldItemLayer(RenderLayerParent<Kobold, NewKoboldModel> renderer, ItemInHandRenderer handRenderer) {
			super(renderer);
			this.handRenderer = handRenderer;
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, Kobold living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean flag = living.getMainArm() == HumanoidArm.RIGHT;
			ItemStack itemstack = flag ? living.getOffhandItem() : living.getMainHandItem();
			ItemStack itemstack1 = flag ? living.getMainHandItem() : living.getOffhandItem();
			if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
				ms.pushPose();
				if (this.getParentModel().young) {
					ms.translate(0.0D, 0.75D, 0.0D);
					ms.scale(0.5F, 0.5F, 0.5F);
				}

				this.renderItem(living, itemstack1, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, ms, buffers, light);
				this.renderItem(living, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, ms, buffers, light);
				ms.popPose();
			}
		}

		private void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transform, HumanoidArm handSide, PoseStack ms, MultiBufferSource buffers, int light) {
			if (!stack.isEmpty()) {
				ms.pushPose();
				this.getParentModel().translateToHand(handSide, ms);
				ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
				ms.mulPose(Axis.YP.rotationDegrees(180.0F));
				boolean flag = handSide == HumanoidArm.LEFT;
				ms.translate(0.05D, 0.125D, -0.35D);
				this.handRenderer.renderItem(entity, stack, transform, flag, ms, buffers, light);
				ms.popPose();
			}
		}
	}
}
