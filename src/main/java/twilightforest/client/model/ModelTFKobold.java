// Date: 6/11/2012 3:12:45 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package twilightforest.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelTFKobold extends ModelBiped {
	//fields

	ModelRenderer rightear;
	ModelRenderer leftear;
	ModelRenderer snout;
	ModelRenderer jaw;

	boolean isJumping;

	public ModelTFKobold() {
		isJumping = false;

		textureWidth = 64;
		textureHeight = 32;

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-3.5F, -7F, -3F, 7, 6, 6);
		bipedHead.setRotationPoint(0F, 13F, 0F);

		bipedBody = new ModelRenderer(this, 12, 19);
		bipedBody.addBox(0F, 0F, 0F, 7, 7, 4);
		bipedBody.setRotationPoint(-3.5F, 12F, -2F);

		bipedRightArm = new ModelRenderer(this, 36, 17);
		bipedRightArm.addBox(-3F, -1F, -1.5F, 3, 7, 3);
		bipedRightArm.setRotationPoint(-3.5F, 12F, 0F);

		bipedLeftArm.mirror = true;
		bipedLeftArm = new ModelRenderer(this, 36, 17);
		bipedLeftArm.addBox(0F, -1F, -1.5F, 3, 7, 3);
		bipedLeftArm.setRotationPoint(3.5F, 12F, 0F);

		bipedLeftArm.mirror = false;
		bipedRightLeg = new ModelRenderer(this, 0, 20);
		bipedRightLeg.addBox(-1.5F, 0F, -1.5F, 3, 5, 3);
		bipedRightLeg.setRotationPoint(-2F, 19F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 0, 20);
		bipedLeftLeg.addBox(-1.5F, 0F, -1.5F, 3, 5, 3);
		bipedLeftLeg.setRotationPoint(2F, 19F, 0F);

		rightear = new ModelRenderer(this, 48, 20);
		rightear.addBox(0F, -4F, 0F, 4, 4, 1);
		rightear.setRotationPoint(3.5F, -3F, -1F);
		rightear.rotateAngleY = 0.2617994F;
		rightear.rotateAngleZ = -0.3490659F;

		bipedHead.addChild(rightear);

		leftear = new ModelRenderer(this, 48, 25);
		leftear.addBox(-4F, -4F, 0F, 4, 4, 1);
		leftear.setRotationPoint(-3.5F, -3F, -1F);
		leftear.rotateAngleY = -0.2617994F;
		leftear.rotateAngleZ = 0.3490659F;

		bipedHead.addChild(leftear);


		snout = new ModelRenderer(this, 28, 0);
		snout.addBox(-1.5F, -2F, -2F, 3, 2, 3);
		snout.setRotationPoint(0F, -2F, -3F);

		bipedHead.addChild(snout);

		jaw = new ModelRenderer(this, 28, 5);
		jaw.addBox(-1.5F, 0F, -2F, 3, 1, 3);
		jaw.setRotationPoint(0F, -2F, -3F);
		jaw.rotateAngleX = 0.20944F;

		bipedHead.addChild(jaw);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
		this.bipedHead.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = par5 / (180F / (float) Math.PI);

		this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;

		this.bipedRightArm.rotateAngleX = -((float) Math.PI * .15F);
		this.bipedLeftArm.rotateAngleX = -((float) Math.PI * .15F);


		this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.19F) * 0.15F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.19F) * 0.15F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.267F) * 0.25F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.267F) * 0.25F;

		if (this.isJumping) {
			// open jaw
			this.jaw.rotateAngleX = 1.44F;
		} else {
			this.jaw.rotateAngleX = 0.20944F;
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float partialTick) {
		// check if entity is jumping
		this.isJumping = par1EntityLiving.motionY > 0;
	}


	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bipedHead.render(f5);
		bipedBody.render(f5);
		bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		bipedRightLeg.render(f5);
		bipedLeftLeg.render(f5);
	}
}
