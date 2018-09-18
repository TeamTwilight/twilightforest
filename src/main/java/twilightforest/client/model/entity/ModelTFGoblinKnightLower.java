package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFGoblinKnightLower;

public class ModelTFGoblinKnightLower extends ModelBiped {

	public ModelRenderer tunic;

	public ModelTFGoblinKnightLower() {
		this.isSneak = false;
		this.textureWidth = 128;
		this.textureHeight = 64;

//FIXME: AtomicBlom: Replace with something like LayerCape
/*
		this.bipedCloak = new ModelRenderer(this, 0, 0);
        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
*/
//FIXME: AtomicBlom replace with some variant of LayerDeadmau5Head
/*
        this.bipedEars = new ModelRenderer(this, 24, 0);
        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1);
*/

		this.bipedHead = new ModelRenderer(this, 0, 32);
		this.bipedHead.addBox(-2.5F, -5.0F, -3.5F, 5, 5, 5);
		this.bipedHead.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedHeadwear.addBox(0, 0, 0, 0, 0, 0);

		this.bipedBody = new ModelRenderer(this, 16, 48);
		this.bipedBody.addBox(-3.5F, 0.0F, -2.0F, 7, 8, 4);
		this.bipedBody.setRotationPoint(0.0F, 8.0F, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 40, 48);
		this.bipedRightArm.addBox(-2.0F, -2.0F, -1.5F, 2, 8, 3);
		this.bipedRightArm.setRotationPoint(-3.5F, 10.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 48);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(0.0F, -2.0F, -1.5F, 2, 8, 3);
		this.bipedLeftArm.setRotationPoint(3.5F, 10.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 48);
		this.bipedRightLeg.addBox(-3.0F, 0.0F, -2.0F, 4, 8, 4);
		this.bipedRightLeg.setRotationPoint(-2.5F, 16.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 48);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -2.0F, 4, 8, 4);
		this.bipedLeftLeg.setRotationPoint(2.5F, 16.0F, 0.0F);

		this.tunic = new ModelRenderer(this, 64, 19);
		this.tunic.addBox(-6.0F, 0.0F, -3.0F, 12, 9, 6);
		this.tunic.setRotationPoint(0F, 7.5F, 0.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		if (((EntityTFGoblinKnightLower) entity).hasArmor()) {
			this.renderTunic(scale);
		}
	}

	/**
	 * Renders the tunic, if we're wearing armor
	 */
	public void renderTunic(float scale) {
		this.tunic.render(scale);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		if (entity.isBeingRidden()) {
			this.bipedHead.rotateAngleY = 0;
			this.bipedHead.rotateAngleX = 0;
			this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
			this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		}

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}

		if (this.rightArmPose != ArmPose.EMPTY) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;


		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

}
