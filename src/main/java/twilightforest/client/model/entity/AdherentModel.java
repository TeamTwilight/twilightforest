package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.Adherent;

public class AdherentModel extends HumanoidModel<Adherent> {
	public AdherentModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 24.0F, 4.0F),
				PartPose.ZERO);

		var leftArm = partRoot.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		leftArm.addOrReplaceChild("left_sleeve",
				CubeListBuilder.create()
						.texOffs(16, 16)
						.addBox(-1.0F, -2.0F, 2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.ZERO);

		var rightArm = partRoot.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		rightArm.addOrReplaceChild("right_sleeve",
				CubeListBuilder.create()
						.texOffs(16, 16)
						.addBox(-3.0F, -2.0F, 2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
				PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(Adherent entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// rotate head normally
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;

		this.rightArm.xRot = 0.0F;
		this.leftArm.xRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		this.rightArm.zRot += Mth.cos((ageInTicks + 10F) * 0.133F) * 0.3F + 0.3F;
		this.leftArm.zRot -= Mth.cos((ageInTicks + 10F) * 0.133F) * 0.3F + 0.3F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
	}
}
