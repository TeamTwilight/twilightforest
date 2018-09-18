package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFYetiAlpha;

public class ModelTFYetiAlpha extends ModelBiped {

	public ModelRenderer mouth;
	public ModelRenderer leftEye;
	public ModelRenderer rightEye;


	public ModelTFYetiAlpha() {
		super();

		this.textureWidth = 256;
		this.textureHeight = 128;

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 0, 0, 0);

		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 0, 0, 0);

		this.bipedBody = new ModelRenderer(this, 80, 0);
		this.bipedBody.addBox(-24.0F, -60.0F, -18.0F, 48, 72, 36);
		this.bipedBody.setRotationPoint(0.0F, -6.0F, 0.0F);

		this.mouth = new ModelRenderer(this, 121, 50);
		this.mouth.addBox(-17.0F, -7.0F, -1.5F, 34, 29, 2);
		this.mouth.setRotationPoint(0.0F, -37.0F, -18.0F);
		this.bipedBody.addChild(mouth);

		this.rightEye = new ModelRenderer(this, 64, 0);
		this.rightEye.addBox(-6.0F, -6.0F, -1.5F, 12, 12, 2);
		this.rightEye.setRotationPoint(-14.0F, -50.0F, -18.0F);
		this.bipedBody.addChild(rightEye);

		this.leftEye = new ModelRenderer(this, 64, 0);
		this.leftEye.addBox(-6.0F, -6.0F, -1.5F, 12, 12, 2);
		this.leftEye.setRotationPoint(14.0F, -50.0F, -18.0F);
		this.bipedBody.addChild(leftEye);

		this.bipedRightArm = new ModelRenderer(this, 0, 0);
		this.bipedRightArm.addBox(-15.0F, -6.0F, -8.0F, 16, 48, 16);
		this.bipedRightArm.setRotationPoint(-25.0F, -26.0F, 0.0F);

		this.bipedBody.addChild(this.bipedRightArm);

		this.bipedLeftArm = new ModelRenderer(this, 0, 0);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -6.0F, -8.0F, 16, 48, 16);
		this.bipedLeftArm.setRotationPoint(25.0F, -26.0F, 0.0F);

		this.bipedBody.addChild(this.bipedLeftArm);


		this.bipedRightLeg = new ModelRenderer(this, 0, 66);
		this.bipedRightLeg.addBox(-10.0F, 0.0F, -10.0F, 20, 20, 20);
		this.bipedRightLeg.setRotationPoint(-13.5F, 4.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 66);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-10.0F, 0.0F, -10.0F, 20, 20, 20);
		this.bipedLeftLeg.setRotationPoint(13.5F, 4.0F, 0.0F);

		addPairHorns(-58.0F, 35F);
		addPairHorns(-46.0F, 15F);
		addPairHorns(-36.0F, -5F);


	}


	/**
	 * Add a pair of horns
	 */
	private void addPairHorns(float height, float zangle) {
		ModelRenderer horn1a;
		ModelRenderer horn1b;

		horn1a = new ModelRenderer(this, 0, 108);
		horn1a.addBox(-9.0F, -5.0F, -5.0F, 10, 10, 10);
		horn1a.setRotationPoint(-24.0F, height, -8.0F);
		horn1a.rotateAngleY = -30F / (180F / (float) Math.PI);
		horn1a.rotateAngleZ = zangle / (180F / (float) Math.PI);
		this.bipedBody.addChild(horn1a);

		horn1b = new ModelRenderer(this, 40, 108);
		horn1b.addBox(-14.0F, -4.0F, -4.0F, 18, 8, 8);
		horn1b.setRotationPoint(-8.0F, 0.0F, 0.0F);
		horn1b.rotateAngleY = -20F / (180F / (float) Math.PI);
		horn1b.rotateAngleZ = zangle / (180F / (float) Math.PI);
		horn1a.addChild(horn1b);

		ModelRenderer horn2a;
		ModelRenderer horn2b;

		horn2a = new ModelRenderer(this, 0, 108);
		horn2a.addBox(-1.0F, -5.0F, -5.0F, 10, 10, 10);
		horn2a.setRotationPoint(24.0F, height, 0.0F);
		horn2a.rotateAngleY = 30F / (180F / (float) Math.PI);
		horn2a.rotateAngleZ = -zangle / (180F / (float) Math.PI);
		this.bipedBody.addChild(horn2a);

		horn2b = new ModelRenderer(this, 40, 108);
		horn2b.addBox(-2.0F, -4.0F, -4.0F, 18, 8, 8);
		horn2b.setRotationPoint(8.0F, 0.0F, 0.0F);
		horn2b.rotateAngleY = 20F / (180F / (float) Math.PI);
		horn2b.rotateAngleZ = -zangle / (180F / (float) Math.PI);
		horn2a.addChild(horn2b);
	}


	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		this.bipedBody.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, limbSwing and limbSwingAmount are used for animating the movement of arms
	 * and legs, where limbSwing represents the time(so that arms and legs swing back and forth) and limbSwingAmount represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		EntityTFYetiAlpha yeti = (EntityTFYetiAlpha) entity;


		this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);

		this.bipedBody.rotateAngleX = headPitch / (180F / (float) Math.PI);


		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

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
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		this.bipedBody.rotationPointY = -6F;
		this.bipedRightLeg.rotationPointY = 4F;
		this.bipedLeftLeg.rotationPointY = 4F;


		if (yeti.isTired()) {
			// arms down
			this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;

			// legs out
			this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
			this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);

			//body down
			this.bipedBody.rotationPointY = 6F;
			this.bipedRightLeg.rotationPointY = 12F;
			this.bipedLeftLeg.rotationPointY = 12F;
		}

		if (yeti.isRampaging()) {
			// arms up
			this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.66F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.66F) * 2.0F * limbSwingAmount * 0.5F;

//            this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//            this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

			this.bipedRightArm.rotateAngleY += MathHelper.cos(limbSwing * 0.25F) * 0.5F + 0.5F;
			this.bipedLeftArm.rotateAngleY -= MathHelper.cos(limbSwing * 0.25F) * 0.5F + 0.5F;

			this.bipedRightArm.rotateAngleX += Math.PI * 1.25;
			this.bipedLeftArm.rotateAngleX += Math.PI * 1.25;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
		}

		if (entity.isBeingRidden()) {
			// arms up!
			this.bipedRightArm.rotateAngleX += Math.PI;
			this.bipedLeftArm.rotateAngleX += Math.PI;
		}
	}
}
