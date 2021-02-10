package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.client.model.entity.ModelTFRedcap;
import twilightforest.entity.EntityTFRedcap;

//[VanillaCopy] of HeldItemLayer, but modified so redcaps actually hold items correctly
public class LayerRedcapHeldItem<T extends EntityTFRedcap, M extends ModelTFRedcap<T> & IHasArm> extends LayerRenderer<T, M> {

	public LayerRedcapHeldItem(IEntityRenderer<T, M> p_i50934_1_) {
		super(p_i50934_1_);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
	      boolean flag = entitylivingbaseIn.getPrimaryHand() == HandSide.RIGHT;
	      ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
	      ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
	      if (!itemstack.isEmpty() || !itemstack1.isEmpty()) {
	         matrixStackIn.push();
	         if (this.getEntityModel().isChild) {
	            float f = 0.5F;
	            matrixStackIn.translate(0.0D, 0.5D, 0.0D);
	            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
	         }

	         matrixStackIn.translate(0.0F, 0.3F, 0.0F);
	         this.func_229135_a_(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
	         this.func_229135_a_(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
	         matrixStackIn.pop();
	      }
	   }

	   private void func_229135_a_(LivingEntity p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack p_229135_5_, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
	      if (!p_229135_2_.isEmpty()) {
	         p_229135_5_.push();
	         this.getEntityModel().translateHand(p_229135_4_, p_229135_5_);
	         p_229135_5_.rotate(Vector3f.XP.rotationDegrees(-90.0F));
	         p_229135_5_.rotate(Vector3f.YP.rotationDegrees(180.0F));
	         boolean flag = p_229135_4_ == HandSide.LEFT;
	         p_229135_5_.translate((double)((float)(flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);
	         Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(p_229135_1_, p_229135_2_, p_229135_3_, flag, p_229135_5_, p_229135_6_, p_229135_7_);
	         p_229135_5_.pop();
	      }
	   }

}
