package twilightforest.client.model.entity.newmodels;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.Troll;

public class NewTrollModel extends HumanoidModel<Troll> {

	public NewTrollModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(52, 31)
						.addBox(-5.0F, -8.0F, -8.0F, 10.0F, 10.0F, 10.0F)
						.texOffs(36, 41)
						.addBox(-2.0F, -4.0F, -11.0F, 4.0F, 8.0F, 4.0F),
				PartPose.offset(0.0F, -11.0F, -1.0F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -37.0F, -6.0F, 16.0F, 26.0F, 15.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(0, 41)
						.addBox(-6.0F, -1.0F, -4.0F, 6.0F, 25.0F, 8.0F),
				PartPose.offset(-8.0F, -9.0F, 0.0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(0, 41)
						.addBox(0.0F, -1.0F, -4.0F, 6.0F, 25.0F, 8.0F),
				PartPose.offset(8.0F, -9.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(28, 54)
						.addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 8.0F),
				PartPose.offset(-4.0F, 13.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
						.texOffs(28, 54)
						.addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 8.0F),
				PartPose.offset(4.0F, 13.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(Troll entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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

		if (entity.isVehicle()) {
			// arms up!
			this.rightArm.xRot += Math.PI;
			this.leftArm.xRot += Math.PI;
		}

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.rightArm.xRot += Math.PI;
		}
		if (this.rightArmPose != ArmPose.EMPTY) {
			this.leftArm.xRot += Math.PI;
		}

		if (this.attackTime > 0F) {
			float swing = 1.0F - this.attackTime;

			this.rightArm.xRot -= (Math.PI * swing);
			this.leftArm.xRot -= (Math.PI * swing);
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
	}

	@Override
	public void prepareMobModel(Troll entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		if (entity.getTarget() != null) {
			this.rightArm.xRot += Math.PI;
			this.leftArm.xRot += Math.PI;
		}
	}
}
