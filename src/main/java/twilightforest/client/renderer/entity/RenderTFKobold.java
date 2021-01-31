package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.client.model.entity.ModelTFKobold;
import twilightforest.entity.EntityTFKobold;

public class RenderTFKobold extends RenderTFBiped<EntityTFKobold, ModelTFKobold> {

	public RenderTFKobold(EntityRendererManager manager, ModelTFKobold modelBiped, float shadowSize, String textureName) {
		super(manager, modelBiped, shadowSize, textureName);

		this.layerRenderers.removeIf(r -> r instanceof net.minecraft.client.renderer.entity.layers.HeldItemLayer);
		this.addLayer(new HeldItemLayer(this));
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.HeldItemLayer} with additional transforms
	 */
	private static class HeldItemLayer extends LayerRenderer<EntityTFKobold, ModelTFKobold> {
		public HeldItemLayer(IEntityRenderer<EntityTFKobold, ModelTFKobold> renderer) {
			super(renderer);
		}

		@Override
		public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, EntityTFKobold living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean flag = living.getPrimaryHand() == HandSide.RIGHT;
			ItemStack itemstack = flag ? living.getHeldItemOffhand() : living.getHeldItemMainhand();
			ItemStack itemstack1 = flag ? living.getHeldItemMainhand() : living.getHeldItemOffhand();
			if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
				ms.push();
				if (this.getEntityModel().isChild) {
					float f = 0.5F;
					ms.translate(0.0D, 0.75D, 0.0D);
					ms.scale(0.5F, 0.5F, 0.5F);
				}

				ms.translate(0, 0, 0.25);
				this.renderItem(living, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, ms, buffers, light);
				this.renderItem(living, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, ms, buffers, light);
				ms.pop();
			}
		}

		private void renderItem(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transform, HandSide handSide, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
			if (!stack.isEmpty()) {
				ms.push();
				((IHasArm)this.getEntityModel()).translateHand(handSide, ms);
				ms.rotate(Vector3f.XP.rotationDegrees(-90.0F));
				ms.rotate(Vector3f.YP.rotationDegrees(180.0F));
				boolean flag = handSide == HandSide.LEFT;
				ms.translate((flag ? -1 : 1) / 16.0F, 0.125D, -0.625D);
				Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, stack, transform, flag, ms, buffers, light);
				ms.pop();
			}
		}
	}
}
