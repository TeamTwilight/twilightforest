package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class AdherentModel extends HumanoidModel<HumanoidRenderState> {

	public AdherentModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 24.0F, 4.0F),
			PartPose.ZERO);

		var leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		leftArm.addOrReplaceChild("left_sleeve", CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-1.0F, -2.0F, 2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.ZERO);

		var rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		rightArm.addOrReplaceChild("right_sleeve", CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-3.0F, -2.0F, 2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("left_leg",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		// rotate head normally
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		this.rightArm.xRot = 0.0F;
		this.leftArm.xRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		this.rightArm.zRot += Mth.cos((state.ageInTicks + 10.0F) * 0.133F) * 0.3F + 0.3F;
		this.leftArm.zRot -= Mth.cos((state.ageInTicks + 10.0F) * 0.133F) * 0.3F + 0.3F;
		this.rightArm.xRot += Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
	}
}
