package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFRedcap;

public class ModelTFRedcap<T extends EntityTFRedcap> extends BipedModel<T> {

	public ModelTFRedcap(float size) {
		super(size);
		this.textureWidth = 64;
		this.textureHeight = 32;

		bipedHead = new ModelRenderer(this);
		bipedHead.setTextureOffset(0, 0).addBox(-3.5F, -5.0F, -4.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);
		bipedHead.setRotationPoint(0.0F, 4.0F, 0.0F);

		bipedHeadwear = new ModelRenderer(this);
		bipedHeadwear.setRotationPoint(0.0F, 7.0F, 0.0F);
		bipedHeadwear.setTextureOffset(32, 0).addBox(-2.0F, -6.0F, -3.0F, 4.0F, 5.0F, 7.0F, 0.0F, false);

		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.setTextureOffset(12, 19).addBox(-4.0F, 6.0F, -2.0F, 8.0F, 9.0F, 4.0F, 0.0F, false);

		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 8.0F, 0.0F);
		bipedRightArm.setTextureOffset(36, 17).addBox(-2.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);

		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 8.0F, 0.0F);
		bipedLeftArm.setTextureOffset(36, 17).addBox(-1.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);

		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(-2.0F, 15.0F, 0.0F);
		bipedRightLeg.setTextureOffset(0, 20).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(3.0F, 15.0F, 0.0F);
		bipedLeftLeg.setTextureOffset(0, 20).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F, 0.0F, false);

		ModelRenderer goblinRightEar = new ModelRenderer(this);
		goblinRightEar.setRotationPoint(0.0F, 7.0F, 0.0F);
		goblinRightEar.setTextureOffset(48, 20).addBox(3.0F, -10.0F, -1.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

		ModelRenderer goblinLeftEar = new ModelRenderer(this);
		goblinLeftEar.setRotationPoint(0.0F, 7.0F, 0.0F);
		goblinLeftEar.setTextureOffset(48, 20).addBox(-5.0F, -10.0F, -1.0F, 2.0F, 3.0F, 1.0F, 0.0F, true);

		bipedHead.addChild(goblinLeftEar);
		bipedHead.addChild(goblinRightEar);
	}

	@Override
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

		this.bipedHead.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		this.bipedHead.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		this.bipedBody.rotateAngleY = 0.0F;

		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedRightArm.rotationPointX = -5.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointX = 5.0F;
		float f = 1.0F;

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount
				* 0.5F / f;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightLeg.rotateAngleZ = 0.0F;
		this.bipedLeftLeg.rotateAngleZ = 0.0F;

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;

		if (this.isSitting) {
			this.bipedRightArm.rotateAngleX += (-(float)Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += (-(float)Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = -1.4137167F;
			this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
			this.bipedRightLeg.rotateAngleZ = 0.07853982F;
			this.bipedLeftLeg.rotateAngleX = -1.4137167F;
			this.bipedLeftLeg.rotateAngleY = (-(float)Math.PI / 10F);
			this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
		}
		
		ModelHelper.func_239101_a_(this.bipedRightArm, this.bipedLeftArm, ageInTicks);
	      if (this.swimAnimation > 0.0F) {
	         float f1 = limbSwing % 26.0F;
	         HandSide handside = this.getMainHand(entity);
	         float f2 = handside == HandSide.RIGHT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
	         float f3 = handside == HandSide.LEFT && this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
	         if (f1 < 14.0F) {
	            this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, 0.0F);
	            this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, 0.0F);
	            this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float)Math.PI);
	            this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float)Math.PI);
	            this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, (float)Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
	            this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, (float)Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
	         } else if (f1 >= 14.0F && f1 < 22.0F) {
	            float f6 = (f1 - 14.0F) / 8.0F;
	            this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) * f6);
	            this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) * f6);
	            this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float)Math.PI);
	            this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float)Math.PI);
	            this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, 5.012389F - 1.8707964F * f6);
	            this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, 1.2707963F + 1.8707964F * f6);
	         } else if (f1 >= 22.0F && f1 < 26.0F) {
	            float f4 = (f1 - 22.0F) / 4.0F;
	            this.bipedLeftArm.rotateAngleX = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
	            this.bipedRightArm.rotateAngleX = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleX, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
	            this.bipedLeftArm.rotateAngleY = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleY, (float)Math.PI);
	            this.bipedRightArm.rotateAngleY = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleY, (float)Math.PI);
	            this.bipedLeftArm.rotateAngleZ = this.rotLerpRad(f3, this.bipedLeftArm.rotateAngleZ, (float)Math.PI);
	            this.bipedRightArm.rotateAngleZ = MathHelper.lerp(f2, this.bipedRightArm.rotateAngleZ, (float)Math.PI);
	         }

	         this.bipedLeftLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedLeftLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float)Math.PI));
	         this.bipedRightLeg.rotateAngleX = MathHelper.lerp(this.swimAnimation, this.bipedRightLeg.rotateAngleX, 0.3F * MathHelper.cos(limbSwing * 0.33333334F));
	      }

		this.func_230486_a_(entity, ageInTicks);

		this.bipedHeadwear.copyModelAngles(this.bipedHead);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		if(this.isSitting) matrixStack.translate(0, 0.25F, 0);
		bipedHead.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedHeadwear.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedBody.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedRightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedLeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedRightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedLeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
	}
	
	private float getArmAngleSq(float limbSwing) {
	      return -65.0F * limbSwing + limbSwing * limbSwing;
	}
}