package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.KnightPhantomEntity;

import net.minecraft.client.model.HumanoidModel.ArmPose;

@Deprecated
public class KnightPhantomOldModel<T extends KnightPhantomEntity> extends HumanoidModel<T> {

	public ModelPart helmet;
	public ModelPart righthorn1;
	public ModelPart righthorn2;
	public ModelPart lefthorn1;
	public ModelPart lefthorn2;

	@Deprecated
	public KnightPhantomOldModel() {
		super(0.0F, 0.0F, 128, 64);
		this.crouching = false;
//		this.textureWidth = 128;
//		this.textureHeight = 64;

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

		this.head = new ModelPart(this, 0, 0);
		this.head.addBox(0, 0, 0, 0, 0, 0);
		this.head.setPos(0.0F, -10.0F, 0.0F);

		this.hat = new ModelPart(this, 0, 0);
		this.hat.addBox(0, 0, 0, 0, 0, 0);
		this.hat.setPos(0.0F, -10.0F, 0.0F);

		this.helmet = new ModelPart(this, 0, 0);
		this.helmet.addBox(-4.0F, -11.0F, -4.0F, 8, 11, 8);
		this.helmet.yRot = 45F * Mth.DEG_TO_RAD;

		this.righthorn1 = new ModelPart(this, 28, 0);
		this.righthorn1.addBox(-6F, -1.5F, -1.5F, 7, 3, 3);
		this.righthorn1.setPos(-3.5F, -9F, 0.0F);
		this.righthorn1.yRot = 15F * Mth.DEG_TO_RAD;
		this.righthorn1.zRot = 10F * Mth.DEG_TO_RAD;

		this.righthorn2 = new ModelPart(this, 28, 6);
		this.righthorn2.addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2);
		this.righthorn2.setPos(-5.5F, 0.0F, 0.0F);
		this.righthorn2.zRot = 10F * Mth.DEG_TO_RAD;

		this.righthorn1.addChild(righthorn2);

		this.lefthorn1 = new ModelPart(this, 28, 0);
		this.lefthorn1.mirror = true;
		this.lefthorn1.addBox(-1F, -1.5F, -1.5F, 7, 3, 3);
		this.lefthorn1.setPos(3.5F, -9F, 0.0F);
		this.lefthorn1.yRot = -15F * Mth.DEG_TO_RAD;
		this.lefthorn1.zRot = -10F * Mth.DEG_TO_RAD;

		this.lefthorn2 = new ModelPart(this, 28, 6);
		this.lefthorn2.addBox(0.0F, -1.0F, -1.0F, 3, 2, 2);
		this.lefthorn2.setPos(5.5F, 0.0F, 0.0F);
		this.lefthorn2.zRot = -10F * Mth.DEG_TO_RAD;

		this.lefthorn1.addChild(lefthorn2);

		this.hat.addChild(helmet);
		this.hat.addChild(righthorn1);
		this.hat.addChild(lefthorn1);

		this.body = new ModelPart(this, 0, 18);
		this.body.setPos(0.0F, 2.0F, 0.0F);
		this.body.addBox(-7.0F, -12.0F, -3.5F, 14, 12, 7);
		this.body.texOffs(30, 24).addBox(-6.0F, 0F, -3F, 12, 8, 6); // torso

		this.rightArm = new ModelPart(this, 44, 16);
		this.rightArm.addBox(-5.0F, -2.0F, -3.0F, 6, 7, 6);
		this.rightArm.setPos(-8.0F, -8.0F, 0.0F);

		this.leftArm = new ModelPart(this, 44, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -3.0F, 6, 7, 6);
		this.leftArm.setPos(8.0F, -8.0F, 0.0F);

		this.rightLeg = new ModelPart(this, 0, 0);
		this.rightLeg.addBox(0, 0, 0, 0, 0, 0);
		this.rightLeg.setPos(0.0F, 12.0F, 0.0F);

		this.leftLeg = new ModelPart(this, 0, 0);
		this.leftLeg.addBox(0, 0, 0, 0, 0, 0);
		this.leftLeg.setPos(0.0F, 12.0F, 0.0F);
	}


	/**
	 * Sets the model's various rotation angles. For bipeds, limbSwing and limbSwingAmount are used for animating the movement of arms
	 * and legs, where limbSwing represents the time(so that arms and legs swing back and forth) and limbSwingAmount represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.head.zRot = 0;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.hat.zRot = this.head.zRot;

		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

//        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
//        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
//        this.bipedRightLeg.rotateAngleY = 0.0F;
//        this.bipedLeftLeg.rotateAngleY = 0.0F;
//
//        if (this.isRiding)
//        {
//            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
//            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
//            this.bipedRightLeg.rotateAngleX = 0;
//            this.bipedLeftLeg.rotateAngleX = 0;
////            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
////            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
//        }

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
		}

		this.rightArmPose = ArmPose.ITEM;

		if (this.rightArmPose != ArmPose.EMPTY) {
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
		}
//
//        bipedRightArm.rotateAngleX -= (Math.PI * 0.66);
//
//        // during swing move arm forward
//        if (upperKnight.heavySpearTimer > 0)
//        {
//        	bipedRightArm.rotateAngleX -= this.getArmRotationDuringSwing(60 - upperKnight.heavySpearTimer + scaleFactor) / (180F / (float)Math.PI);
//        }

//        this.bipedRightArm.rotateAngleY = 0.0F;
//        this.bipedLeftArm.rotateAngleY = 0.0F;

		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
	}
}
