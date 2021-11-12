package twilightforest.client.model.entity.legacy;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.entity.boss.SnowQueen.Phase;

public class SnowQueenLegacyModel extends HumanoidModel<SnowQueen> {

	public SnowQueenLegacyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partRoot = mesh.getRoot();

		var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		partRoot.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		var crown = head.addOrReplaceChild("crown", CubeListBuilder.create(),
				PartPose.ZERO);

		makeFrontCrown(crown, -1, -4, 10F, 0);
		makeFrontCrown(crown, 0, 4, -10F, 1);
		makeSideCrown(crown, -1, -4, 10F, 0);
		makeSideCrown(crown, 0, 4, -10F, 1);

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4.0F, 0.0F, -2.0F, 8, 23, 4),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(16, 16)
						.addBox(-2.0F, -2.0F, -1.5F, 3, 12, 3),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(16, 16)
						.addBox(-1.0F, -2.0F, -1.3F, 3, 12, 3),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3),
				PartPose.offset(-1.9F, 12.0F, 0.0F));

		partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3),
				PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static void makeSideCrown(PartDefinition parent, float spikeDepth, float crownX, float angle, int iteration) {

		var crownSide = parent.addOrReplaceChild("crown_side_" + iteration, CubeListBuilder.create()
						.texOffs(28, 28)
						.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1),
				PartPose.offsetAndRotation(crownX, -6.0F, 0.0F, 0.0F, Mth.PI / 2.0F, 0.0F));

		crownSide.addOrReplaceChild("spike_4", CubeListBuilder.create()
						.texOffs(48, 27)
						.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F / 180F * Mth.PI, 0.0F, 0.0F));

		crownSide.addOrReplaceChild("spike_3l", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, -10F / 180F * Mth.PI));

		crownSide.addOrReplaceChild("spike_3r", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, 10F / 180F * Mth.PI));

	}

	private static void makeFrontCrown(PartDefinition parent, float spikeDepth, float crownZ, float angle, int iteration) {

		var crownFront = parent.addOrReplaceChild("crown_front_" + iteration, CubeListBuilder.create()
						.texOffs(28, 30)
						.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1),
				PartPose.offset(0.0F, -6.0F, crownZ));

		crownFront.addOrReplaceChild("spike_4", CubeListBuilder.create()
						.texOffs(48, 27)
						.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F / 180F * Mth.PI, 0.0F, 0.0F));

		crownFront.addOrReplaceChild("spike_3l", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, -10F / 180F * 3.14159F));

		crownFront.addOrReplaceChild("spike_3r", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, 10F / 180F * 3.14159F));

	}

	@Override
	public void setupAnim(SnowQueen entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		// in beam phase, arms forwards
		if (entity.getCurrentPhase() == Phase.BEAM) {
			if (entity.isBreathing()) {
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
			} else {
				// arms up
				this.rightArm.xRot += Math.PI;
				this.leftArm.xRot += Math.PI;
			}
		}
	}
}
