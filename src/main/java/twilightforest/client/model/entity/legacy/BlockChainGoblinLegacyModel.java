package twilightforest.client.model.entity.legacy;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.BlockChainGoblin;

public class BlockChainGoblinLegacyModel<T extends BlockChainGoblin> extends HumanoidModel<T> {

	ModelPart block;

	public BlockChainGoblinLegacyModel(ModelPart root) {
		super(root);
		this.block = root.getChild("block");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.ZERO);

		var hat = partRoot.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		hat.addOrReplaceChild("helmet", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-2.5F, -9.0F, -2.5F, 5.0F, 9.0F, 5.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 45F / (180F / Mth.PI), 0.0F));

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 21)
						.addBox(-3.5F, 0.0F, -2.0F, 7.0F, 7.0F, 4.0F),
				PartPose.offset(0.0F, 11.0F, 0.0F));

		partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(52, 0)
						.addBox(-3.0F, -1.0F, -2.0F, 3.0F, 12.0F, 3.0F),
				PartPose.offset(-3.5F, 12.0F, 0.0F));

		partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(52, 0)
						.addBox(0.0F, -1.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				PartPose.offset(3.5F, 12.0F, 0.0F));

		partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 12)
						.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
				PartPose.offset(-2.0F, 18.0F, 0.0F));

		partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 12)
						.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
				PartPose.offset(2.0F, 18.0F, 0.0F));

		var block = partRoot.addOrReplaceChild("block", CubeListBuilder.create()
						.texOffs(32, 32)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);

		// Rotation constants
		final float QUARTER_PI = 0.25F * Mth.PI;
		final float ANGLE_MINOR = -35F * Mth.DEG_TO_RAD;
		final float ANGLE_MAJOR = -55F * Mth.DEG_TO_RAD;

		block.addOrReplaceChild("spikes_0", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -9, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_1", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -8, 4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_2", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, -8, 4, ANGLE_MAJOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_3", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, -8, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_4", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, -8, -4, ANGLE_MINOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_5", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -8, -4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_6", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, -8, -4, ANGLE_MINOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_7", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, -8, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_8", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, -8, 4, ANGLE_MAJOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_9", CubeListBuilder.create() // this spike is not really there
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_10", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -4, 4, 0, 0, 0));

		block.addOrReplaceChild("spikes_11", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, -4, 5, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_12", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(5, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_13", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, -4, -4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_14", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, -4, -5, 0, 0, 0));

		block.addOrReplaceChild("spikes_15", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, -4, -4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_16", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-5, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_17", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, -4, 4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_18", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, 1, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_19", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0, 0, 4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_20", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, 0, 4, ANGLE_MINOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_21", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, 0, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_22", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, 0, -4, ANGLE_MAJOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_23", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(4, 0, -4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_24", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, 0, -4, ANGLE_MAJOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_25", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, 0, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_26", CubeListBuilder.create()
						.texOffs(56, 36)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(-4, 0, 4, ANGLE_MINOR, -QUARTER_PI, 0));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		head.y = 11.0F;
		hat.y = 11.0F;
		body.y = 11F;

		rightLeg.y = 18F;
		leftLeg.y = 18F;

		rightArm.setPos(-3.5F, 12F, 0F);
		rightArm.xRot += Math.PI;

		leftArm.setPos(3.5F, 12F, 0F);
		leftArm.xRot += Math.PI;

		float angle = ageInTicks / 4F;
		float length = 0;//16F;

		block.x = (float) Math.sin(angle) * length;
		block.z = (float) -Math.cos(angle) * length;

		block.yRot = -angle;
	}
}
