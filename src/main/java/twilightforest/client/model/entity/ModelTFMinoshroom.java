package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelTFMinoshroom extends ModelBiped {

	ModelRenderer body;
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg4;
	ModelRenderer udders;

	ModelRenderer snout;

	public ModelRenderer righthorn1;
	public ModelRenderer righthorn2;
	public ModelRenderer lefthorn1;
	public ModelRenderer lefthorn2;

	public ModelTFMinoshroom() {
		textureWidth = 128;
		textureHeight = 32;

		bipedHead = new ModelRenderer(this, 96, 16);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, -6F, -9F);

		body = new ModelRenderer(this, 18, 4);
		body.addBox(-6F, -10F, -7F, 12, 18, 10);
		body.setRotationPoint(0F, 5F, 2F);
		setRotation(body, 1.570796F, 0F, 0F);

		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-3F, 0F, -2F, 4, 12, 4);
		leg1.setRotationPoint(-3F, 12F, 7F);

		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-1F, 0F, -2F, 4, 12, 4);
		leg2.setRotationPoint(3F, 12F, 7F);

		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-3F, 0F, -3F, 4, 12, 4);
		leg3.setRotationPoint(-3F, 12F, -5F);

		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-1F, 0F, -3F, 4, 12, 4);
		leg4.setRotationPoint(3F, 12F, -5F);

		udders = new ModelRenderer(this, 52, 0);
		udders.addBox(-2F, -3F, 0F, 4, 6, 2);
		udders.setRotationPoint(0F, 14F, 6F);
		setRotation(udders, 1.570796F, 0F, 0F);

		bipedBody = new ModelRenderer(this, 64, 0);
		bipedBody.addBox(-4F, 0F, -2.5F, 8, 12, 5);
		bipedBody.setRotationPoint(0F, -6F, -9F);

		this.bipedLeftArm = new ModelRenderer(this, 90, 0);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedLeftArm.setRotationPoint(5F, -4F, -9F);
		this.bipedLeftArm.mirror = true;

		this.bipedRightArm = new ModelRenderer(this, 90, 0);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedRightArm.setRotationPoint(-5F, -4F, -9F);

		// horns
		this.righthorn1 = new ModelRenderer(this, 0, 0);
		this.righthorn1.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3);
		this.righthorn1.setRotationPoint(-2.5F, -6.5F, 0.0F);
		this.righthorn1.rotateAngleY = -25F / (180F / (float) Math.PI);
		this.righthorn1.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.righthorn2 = new ModelRenderer(this, 16, 0);
		this.righthorn2.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2);
		this.righthorn2.setRotationPoint(-4.5F, 0.0F, 0.0F);
		this.righthorn2.rotateAngleY = -15F / (180F / (float) Math.PI);
		this.righthorn2.rotateAngleZ = 45F / (180F / (float) Math.PI);

		this.righthorn1.addChild(righthorn2);

		this.lefthorn1 = new ModelRenderer(this, 0, 0);
		this.lefthorn1.mirror = true;
		this.lefthorn1.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3);
		this.lefthorn1.setRotationPoint(2.5F, -6.5F, 0.0F);
		this.lefthorn1.rotateAngleY = 25F / (180F / (float) Math.PI);
		this.lefthorn1.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.lefthorn2 = new ModelRenderer(this, 16, 0);
		this.lefthorn2.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2);
		this.lefthorn2.setRotationPoint(4.5F, 0.0F, 0.0F);
		this.lefthorn2.rotateAngleY = 15F / (180F / (float) Math.PI);
		this.lefthorn2.rotateAngleZ = -45F / (180F / (float) Math.PI);

		this.lefthorn1.addChild(lefthorn2);

		this.bipedHead.addChild(righthorn1);
		this.bipedHead.addChild(lefthorn1);

		snout = new ModelRenderer(this, 105, 28);
		snout.addBox(-2, -1, -1, 4, 3, 1);
		snout.setRotationPoint(0F, -2.0F, -4F);

		this.bipedHead.addChild(snout);

		// kill off headwear box
		this.bipedHeadwear = new ModelRenderer(this, 0, 0);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		//super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
		body.render(f5);
		leg1.render(f5);
		leg2.render(f5);
		leg3.render(f5);
		leg4.render(f5);
		udders.render(f5);

		bipedBody.render(f5);
		bipedLeftArm.render(f5);
		bipedRightArm.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
		// copied from ModelBiped
		this.bipedHead.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;

		this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;

		this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}

		if (this.rightArmPose != ArmPose.EMPTY) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}


		this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		float var7 = 0.0F;
		float var8 = 0.0F;

		if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
		}

		if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		}

		// copied from ModelQuadruped
		this.body.rotateAngleX = ((float) Math.PI / 2F);
		this.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;

	}
}
