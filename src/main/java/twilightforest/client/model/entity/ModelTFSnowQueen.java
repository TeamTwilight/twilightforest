package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen.Phase;

public class ModelTFSnowQueen extends ModelBiped {

	public ModelTFSnowQueen() {

		float par1 = 0;
		float par2 = 0;


		// crown
		this.bipedHeadwear = new ModelRenderer(this, 0, 0);

		this.bipedHeadwear.addChild(makeFrontCrown(-1, -4, 10F));
		this.bipedHeadwear.addChild(makeFrontCrown(0, 4, -10F));
		this.bipedHeadwear.addChild(makeSideCrown(-1, -4, 10F));
		this.bipedHeadwear.addChild(makeSideCrown(0, 4, -10F));

		// copy to back


		// dress
		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 23, 4, par1);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);

		// shrink
		this.bipedRightArm = new ModelRenderer(this, 16, 16);
		this.bipedRightArm.addBox(-2.0F, -2.0F, -1.5F, 3, 12, 3, par1);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 16, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.3F, 3, 12, 3, par1);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, par1);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + par2, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, par1);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + par2, 0.0F);

	}

	private ModelRenderer makeSideCrown(float spikeDepth, float crownX, float angle) {
		ModelRenderer crownSide = new ModelRenderer(this, 28, 28);
		crownSide.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1);
		crownSide.setRotationPoint(crownX, -6.0F, 0.0F);
		crownSide.rotateAngleY = 3.14159F / 2.0F;

		ModelRenderer spike4 = new ModelRenderer(this, 48, 27);
		spike4.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1);
		spike4.rotateAngleX = angle * 1.5F / 180F * 3.14159F;

		ModelRenderer spike3l = new ModelRenderer(this, 52, 28);
		spike3l.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3l.setRotationPoint(-2.5F, 0.0F, 0.0F);
		spike3l.rotateAngleX = angle / 180F * 3.14159F;
		spike3l.rotateAngleZ = -10F / 180F * 3.14159F;

		ModelRenderer spike3r = new ModelRenderer(this, 52, 28);
		spike3r.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3r.setRotationPoint(2.5F, 0.0F, 0.0F);
		spike3r.rotateAngleX = angle / 180F * 3.14159F;
		spike3r.rotateAngleZ = 10F / 180F * 3.14159F;


		crownSide.addChild(spike4);
		crownSide.addChild(spike3l);
		crownSide.addChild(spike3r);
		return crownSide;
	}

	private ModelRenderer makeFrontCrown(float spikeDepth, float crownZ, float angle) {
		ModelRenderer crownFront = new ModelRenderer(this, 28, 30);
		crownFront.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1);
		crownFront.setRotationPoint(0.0F, -6.0F, crownZ);

		ModelRenderer spike4 = new ModelRenderer(this, 48, 27);
		spike4.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1);
		spike4.rotateAngleX = angle * 1.5F / 180F * 3.14159F;

		ModelRenderer spike3l = new ModelRenderer(this, 52, 28);
		spike3l.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3l.setRotationPoint(-2.5F, 0.0F, 0.0F);
		spike3l.rotateAngleX = angle / 180F * 3.14159F;
		spike3l.rotateAngleZ = -10F / 180F * 3.14159F;

		ModelRenderer spike3r = new ModelRenderer(this, 52, 28);
		spike3r.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1);
		spike3r.setRotationPoint(2.5F, 0.0F, 0.0F);
		spike3r.rotateAngleX = angle / 180F * 3.14159F;
		spike3r.rotateAngleZ = 10F / 180F * 3.14159F;

		crownFront.addChild(spike4);
		crownFront.addChild(spike3l);
		crownFront.addChild(spike3r);
		return crownFront;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);

		EntityTFSnowQueen queen = (EntityTFSnowQueen) par7Entity;

		// in beam phase, arms forwards
		if (queen.getCurrentPhase() == Phase.BEAM) {
			if (queen.isBreathing()) {
				float f6 = MathHelper.sin(this.swingProgress * (float) Math.PI);
				float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
				this.bipedRightArm.rotateAngleZ = 0.0F;
				this.bipedLeftArm.rotateAngleZ = 0.0F;
				this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
				this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
				this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
				this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
				this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
				this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
				this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
				this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
			} else {
				// arms up
				this.bipedRightArm.rotateAngleX += Math.PI;
				this.bipedLeftArm.rotateAngleX += Math.PI;
			}
		}
	}

}
