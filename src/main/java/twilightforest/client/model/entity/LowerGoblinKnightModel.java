package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.state.entity.LowerGoblinKnightRenderState;

public class LowerGoblinKnightModel extends HumanoidModel<LowerGoblinKnightRenderState> {

	private final ModelPart tunic;

	public LowerGoblinKnightModel(ModelPart root) {
		super(root);
		this.tunic = root.getChild("tunic");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-2.5F, -5.0F, -3.5F, 5.0F, 5.0F, 5.0F),
			PartPose.offset(0.0F, 10.0F, 1.0F));

		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create(),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(16, 48)
				.addBox(-3.5F, 0.0F, -2.0F, 7.0F, 8.0F, 4.0F),
			PartPose.offset(0.0F, 8.0F, 0.0F));

		partdefinition.addOrReplaceChild("tunic", CubeListBuilder.create()
				.texOffs(64, 19)
				.addBox(-6.0F, 0.0F, -3.0F, 12.0F, 9.0F, 6.0F),
			PartPose.offset(0F, 7.5F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(40, 48)
				.addBox(-2.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
			PartPose.offset(-3.5F, 10.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(40, 48)
				.addBox(0.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
			PartPose.offset(3.5F, 10.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-3.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(-2.5F, 16.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 48)
				.addBox(-1.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(2.5F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(LowerGoblinKnightRenderState entity) {
		if (entity.hasUpperGoblin) {
			this.head.yRot = 0;
			this.head.xRot = 0;
		} else {
			this.head.yRot = entity.yRot * Mth.DEG_TO_RAD;
			this.head.xRot = entity.xRot * Mth.DEG_TO_RAD;
		}
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		if (!entity.hasArmor && !entity.hasUpperGoblin) {
			this.rightArm.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 2.0F * entity.walkAnimationSpeed * 0.5F;
			this.leftArm.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 2.0F * entity.walkAnimationSpeed * 0.5F;
		} else {
			this.rightArm.xRot = 0.0F;
			this.leftArm.xRot = 0.0F;
		}
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * entity.walkAnimationSpeed;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (!entity.hasArmor && !entity.hasUpperGoblin) {
			AnimationUtils.bobArms(this.rightArm, this.leftArm, entity.ageInTicks);
		}

		this.tunic.visible = entity.hasArmor;
	}
}
