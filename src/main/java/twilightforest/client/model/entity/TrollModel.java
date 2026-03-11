package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.entity.monster.Troll;

public class TrollModel extends HumanoidModel<Troll> {

	public TrollModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-5.0F, -8.0F, -3.0F, 10.0F, 10.0F, 10.0F),
			PartPose.offset(0.0F, -9.0F, -6.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		head.addOrReplaceChild("nose", CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(0.0F, -2.0F, -4.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-8.0F, 0.0F, -5.0F, 16.0F, 26.0F, 10.0F),
			PartPose.offset(0.0F, -14.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(32, 36)
				.addBox(-5.0F, -2.0F, -3.0F, 6.0F, 22.0F, 6.0F),
			PartPose.offset(-9.0F, -9.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(32, 36)
				.addBox(-1.0F, -2.0F, -3.0F, 6.0F, 22.0F, 6.0F),
			PartPose.offset(9.0F, -9.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 44)
				.addBox(-3.0F, 0.0F, -4.0F, 6.0F, 12.0F, 8.0F),
			PartPose.offset(-5.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 44)
				.addBox(-3.0F, 0.0F, -4.0F, 6.0F, 12.0F, 8.0F),
			PartPose.offset(5.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(52, 31)
				.addBox(-5.0F, -8.0F, -8.0F, 10.0F, 10.0F, 10.0F)
				.texOffs(36, 41)
				.addBox(-2.0F, -4.0F, -11.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(0.0F, -11.0F, -1.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-8.0F, -37.0F, -6.0F, 16.0F, 26.0F, 15.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 41)
				.addBox(-6.0F, -1.0F, -4.0F, 6.0F, 25.0F, 8.0F),
			PartPose.offset(-8.0F, -9.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(0, 41)
				.addBox(0.0F, -1.0F, -4.0F, 6.0F, 25.0F, 8.0F),
			PartPose.offset(8.0F, -9.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(28, 54)
				.addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 8.0F),
			PartPose.offset(-4.0F, 13.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(28, 54)
				.addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 8.0F),
			PartPose.offset(4.0F, 13.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Troll entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (entity.isVehicle()) {
			// arms up!
			this.rightArm.xRot = Mth.PI;
			this.leftArm.xRot = Mth.PI;
		} else {
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		}
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.rightArm.xRot += Mth.PI;
		}
		if (this.rightArmPose != ArmPose.EMPTY) {
			this.leftArm.xRot += Mth.PI;
		}

		if (this.attackTime > 0F) {
			float swing = 1.0F - this.attackTime;

			this.rightArm.xRot -= (Mth.PI * swing);
			this.leftArm.xRot -= (Mth.PI * swing);
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		if (!entity.isVehicle()) {
			AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}
	}

	@Override
	public void prepareMobModel(Troll entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		if (entity.getTarget() != null) {
			this.rightArm.xRot += Mth.PI;
			this.leftArm.xRot += Mth.PI;
		}
	}
}