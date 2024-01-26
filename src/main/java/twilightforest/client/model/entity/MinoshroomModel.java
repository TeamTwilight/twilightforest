package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import twilightforest.entity.boss.Minoshroom;

public class MinoshroomModel extends HumanoidModel<Minoshroom> {

	final ModelPart leg1;
	final ModelPart leg2;
	final ModelPart leg3;
	final ModelPart leg4;
	final ModelPart cowbody;
	final ModelPart udders;

	public MinoshroomModel(ModelPart root) {
		super(root);
		this.leg1 = root.getChild("leg_1");
		this.leg2 = root.getChild("leg_2");
		this.leg3 = root.getChild("leg_3");
		this.leg4 = root.getChild("leg_4");
		this.cowbody = root.getChild("cow_body");
		this.udders = root.getChild("udders");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(96, 16)
						.addBox(-4F, -8F, -4F, 8, 8, 8),
				PartPose.offset(0F, -6F, -9F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		head.addOrReplaceChild("snout", CubeListBuilder.create()
						.texOffs(105, 28)
						.addBox(-2, -1, -1, 4, 3, 1),
				PartPose.offset(0F, -2.0F, -4F));

		var rightHorn = head.addOrReplaceChild("right_horn_1", CubeListBuilder.create().mirror()
						.texOffs(0, 0)
						.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(-2.5F, -6.5F, 0.0F, 0.0F, -25F / (180F / Mth.PI), 10F / (180F / Mth.PI)));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create().mirror()
						.texOffs(16, 0)
						.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, -15F / (180F / Mth.PI), 45F / (180F / Mth.PI)));

		var leftHorn = head.addOrReplaceChild("left_horn_1", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(2.5F, -6.5F, 0.0F, 0.0F, 25F / (180F / Mth.PI), -10F / (180F / Mth.PI)));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create()
						.texOffs(16, 0)
						.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F, 0.0F, 15F / (180F / Mth.PI), -45F / (180F / Mth.PI)));

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(64, 0)
						.addBox(-4F, 0F, -2.5F, 8, 12, 5),
				PartPose.offset(0F, -6F, -9F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(90, 0)
						.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(-5F, -4F, -9F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(90, 0)
						.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(5F, -4F, -9F));

		definition.addOrReplaceChild("cow_body", CubeListBuilder.create()
						.texOffs(18, 4)
						.addBox(-6F, -10F, -7F, 12, 18, 10),
				PartPose.offsetAndRotation(0F, 5F, 2F, 1.570796F, 0F, 0F));

		definition.addOrReplaceChild("udders", CubeListBuilder.create()
						.texOffs(52, 0)
						.addBox(-2F, -3F, 0F, 4, 6, 2),
				PartPose.offsetAndRotation(0F, 14F, 6F, 1.570796F, 0F, 0F));

		definition.addOrReplaceChild("leg_1", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-3F, 0F, -2F, 4, 12, 4),
				PartPose.offset(-3F, 12F, 7F));

		definition.addOrReplaceChild("leg_2", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1F, 0F, -2F, 4, 12, 4),
				PartPose.offset(3F, 12F, 7F));

		definition.addOrReplaceChild("leg_3", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-3F, 0F, -2F, 4, 12, 4),
				PartPose.offset(-3F, 12F, -5F));

		definition.addOrReplaceChild("leg_4", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1F, 0F, -2F, 4, 12, 4),
				PartPose.offset(3F, 12F, -5F));

		return LayerDefinition.create(mesh, 128, 32);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.cowbody, this.udders, this.leg1, this.leg2, this.leg3, this.leg4);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, limbSwing and limbSwingAmount are used for animating the movement of arms
	 * and legs, where limbSwing represents the time(so that arms and legs swing back and forth) and limbSwingAmount represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(Minoshroom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// copied from HumanoidModel

		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;

		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
		}

		if (this.rightArmPose != ArmPose.EMPTY) {
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
		}


		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

		float var7 = 0.0F;
		float var8 = 0.0F;

		if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
			this.leftArm.zRot = 0.0F;
			this.leftArm.yRot = 0.1F - var7 * 0.6F + this.head.yRot + 0.4F;
			this.leftArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot -= var7 * 1.2F - var8 * 0.4F;
			this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
		}

		if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
			this.rightArm.zRot = 0.0F;
			this.rightArm.yRot = -(0.1F - var7 * 0.6F) + this.head.yRot;
			this.rightArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
			this.rightArm.xRot -= var7 * 1.2F - var8 * 0.4F;
			this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		}

		// copied from ModelQuadruped
		this.cowbody.xRot = ((float) Math.PI / 2F);
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		float f = ageInTicks - entity.tickCount;
		float f1 = entity.getChargeAnimationScale(f);
		f1 = f1 * f1;
		float f2 = 1.0F - f1;

		this.leg3.y = 12.0F + (-7F * f1);
		this.leg3.z = -4.0F + (-5.8F * f1);
		this.leg4.y = this.leg3.y;
		this.leg4.z = this.leg3.z;
		this.body.y = -6F + -3.0F * f1;
		this.rightArm.y = -4F - f1;
		this.rightArm.z = -9F + f1;
		this.leftArm.y = rightArm.y;
		this.leftArm.z = rightArm.z;

		if (f1 > 0) {
			if (entity.getMainArm() == HumanoidArm.RIGHT) {
				this.rightArm.xRot = f1 * -1.8F;
				this.leftArm.xRot = 0.0F;
				this.rightArm.zRot = -0.2F;
			} else {
				this.rightArm.xRot = 0.0F;
				this.leftArm.xRot = f1 * -1.8F;
				this.leftArm.zRot = 0.2F;
			}
			this.cowbody.xRot = ((float) Math.PI / 2F) - f1 * (float) Math.PI * 0.2F;
			this.leg3.xRot -= f1 * (float) Math.PI * 0.3F;
			this.leg4.xRot -= f1 * (float) Math.PI * 0.3F;
		}
	}
}
