package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;
import twilightforest.client.model.entity.KoboldModel;
import twilightforest.entity.monster.Kobold;

public class KoboldRenderer extends TFBipedRenderer<Kobold, KoboldModel> {

	public KoboldRenderer(EntityRendererProvider.Context manager, KoboldModel modelBiped, float shadowSize, String textureName) {
		super(manager, modelBiped, shadowSize, textureName);

		this.layers.removeIf(r -> r instanceof net.minecraft.client.renderer.entity.layers.ItemInHandLayer);
		this.addLayer(new HeldItemLayer(this));
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.ItemInHandLayer} with additional transforms
	 */
	private static class HeldItemLayer extends RenderLayer<Kobold, KoboldModel> {
		public HeldItemLayer(RenderLayerParent<Kobold, KoboldModel> renderer) {
			super(renderer);
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

				this.renderItem(living, itemstack1, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, ms, buffers, light);
				this.renderItem(living, itemstack, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, ms, buffers, light);
				ms.popPose();
			}
		}

		private void renderItem(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transform, HumanoidArm handSide, PoseStack ms, MultiBufferSource buffers, int light) {
			if (!stack.isEmpty()) {
				ms.pushPose();
				this.getParentModel().translateToHand(handSide, ms);
				ms.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
				ms.mulPose(Vector3f.YP.rotationDegrees(180.0F));
				boolean flag = handSide == HumanoidArm.LEFT;
				ms.translate(0.05D, 0.125D, -0.35D);
				Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, stack, transform, flag, ms, buffers, light);
				ms.popPose();
			}
		}
	}
}
