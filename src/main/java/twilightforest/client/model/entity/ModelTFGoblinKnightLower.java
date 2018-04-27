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
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);

		if (((EntityTFGoblinKnightLower) par1Entity).hasArmor()) {
			this.renderTunic(par7);
		}
	}

	/**
	 * Renders the tunic, if we're wearing armor
	 */
	public void renderTunic(float par1) {
		this.tunic.render(par1);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
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

		if (par7Entity.isBeingRidden()) {
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


		this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
	}

}
