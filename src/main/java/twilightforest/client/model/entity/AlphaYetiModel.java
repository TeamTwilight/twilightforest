package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.AlphaYeti;

public class AlphaYetiModel extends HumanoidModel<AlphaYeti> {
	public AlphaYetiModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
				PartPose.ZERO);
		definition.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
				PartPose.ZERO);
		var body = definition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(80, 0)
						.addBox(-24.0F, -60.0F, -18.0F, 48.0F, 72.0F, 36.0F),
				PartPose.offset(0.0F, -6.0F, 0.0F));
		body.addOrReplaceChild("mouth",
				CubeListBuilder.create()
						.texOffs(121, 50)
						.addBox(-17.0F, -7.0F, -1.5F, 34.0F, 29.0F, 2.0F),
				PartPose.offset(0.0F, -37.0F, -18.0F));
		body.addOrReplaceChild("right_eye",
				CubeListBuilder.create()
						.texOffs(64, 0)
						.addBox(-6.0F, -6.0F, -1.5F, 12.0F, 12.0F, 2.0F),
				PartPose.offset(-14.0F, -50.0F, -18.0F));
		body.addOrReplaceChild("left_eye",
				CubeListBuilder.create()
						.texOffs(64, 0)
						.addBox(-6.0F, -6.0F, -1.5F, 12.0F, 12.0F, 2.0F),
				PartPose.offset(14.0F, -50.0F, -18.0F));
		definition.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-15.0F, -6.0F, -8.0F, 16.0F, 48.0F, 16.0F),
				PartPose.offset(-25.0F, -26.0F, 0.0F));
		definition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().mirror()
						.texOffs(0, 0)
						.addBox(-1.0F, -6.0F, -8.0F, 16.0F, 48.0F, 16.0F),
				PartPose.offset(25.0F, -26.0F, 0.0F));
		definition.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(0, 66)
						.addBox(-10.0F, 0.0F, -10.0F, 20.0F, 20.0F, 20.0F),
				PartPose.offset(-13.5F, 4.0F, 0.0F));
		definition.addOrReplaceChild("left_leg",
				CubeListBuilder.create().mirror()
						.texOffs(0, 66)
						.addBox(-10.0F, 0.0F, -10.0F, 20.0F, 20.0F, 20.0F),
				PartPose.offset(13.5F, 4.0F, 0.0F));

		addPairHorns(body, -58.0F, 35F, 1);
		addPairHorns(body, -46.0F, 15F, 2);
		addPairHorns(body, -36.0F, -5F, 3);

		return LayerDefinition.create(mesh, 256, 128);
	}

	/**
	 * Add a pair of horns
	 */
	private static void addPairHorns(PartDefinition partdefinition, float height, float zangle, int set) {

		var leftHorn = partdefinition.addOrReplaceChild("left_horn_" + set,
				CubeListBuilder.create()
						.texOffs(0, 108)
						.addBox(-9.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F),
		PartPose.offsetAndRotation(-24.0F, height, -8.0F,
				0.0F, -30F / (180F / (float) Math.PI), zangle / (180F / (float) Math.PI)));

		leftHorn.addOrReplaceChild("left_horn_" + set + "_top",
				CubeListBuilder.create()
						.texOffs(40, 108)
						.addBox(-14.0F, -4.0F, -4.0F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F,
						0.0F, -20F / (180F / (float) Math.PI), zangle / (180F / (float) Math.PI)));

		var rightHorn = partdefinition.addOrReplaceChild("right_horn_" + set,
				CubeListBuilder.create()
						.texOffs(0, 108)
						.addBox(-1.0F, -5.0F, -5.0F, 10, 10, 10),
				PartPose.offsetAndRotation(24.0F, height, 0.0F,
						0.0F, 30F / (180F / (float) Math.PI), -zangle / (180F / (float) Math.PI)));

		rightHorn.addOrReplaceChild("right_horn_" + set + "_top",
				CubeListBuilder.create()
						.texOffs(40, 108)
						.addBox(-2.0F, -4.0F, -4.0F, 18, 8, 8),
		PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F,
				0.0F, 20F / (180F / (float) Math.PI), -zangle / (180F / (float) Math.PI)));
	}


	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}

	@Override
	public void setupAnim(AlphaYeti entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);

		this.body.xRot = headPitch / (180F / (float) Math.PI);

		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		float f6 = Mth.sin(this.attackTime * (float) Math.PI);
		float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - f6 * 0.6F);
		this.leftArm.yRot = 0.1F - f6 * 0.6F;
		this.rightArm.xRot = -((float) Math.PI / 2F);
		this.leftArm.xRot = -((float) Math.PI / 2F);
		this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
		this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

		this.body.y = -6F;
		this.rightLeg.y = 4F;
		this.leftLeg.y = 4F;

		if (entity.isTired()) {
			// arms down
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;

			// legs out
			this.rightArm.xRot -= ((float) Math.PI / 5F);
			this.leftArm.xRot -= ((float) Math.PI / 5F);
			this.rightLeg.xRot = -((float) Math.PI * 2F / 5F);
			this.leftLeg.xRot = -((float) Math.PI * 2F / 5F);
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.leftLeg.yRot = -((float) Math.PI / 10F);

			//body down
			this.body.y = 6F;
			this.rightLeg.y = 12F;
			this.leftLeg.y = 12F;
		}

		if (entity.isRampaging()) {
			// arms up
			this.rightArm.xRot = Mth.cos(limbSwing * 0.66F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.66F) * 2.0F * limbSwingAmount * 0.5F;

            this.rightArm.yRot += Mth.cos(limbSwing * 0.25F) * 0.5F + 0.5F;
			this.leftArm.yRot -= Mth.cos(limbSwing * 0.25F) * 0.5F + 0.5F;

			this.rightArm.xRot += Math.PI * 1.25;
			this.leftArm.xRot += Math.PI * 1.25;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
		}

		if (entity.isVehicle()) {
			// arms up!
			this.rightArm.xRot += Math.PI;
			this.leftArm.xRot += Math.PI;
		}
	}
}
