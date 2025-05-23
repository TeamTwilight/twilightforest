package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.state.TrollRenderState;

public class TrollModel extends HumanoidModel<TrollRenderState> {

	public TrollModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-5.0F, -8.0F, -3.0F, 10.0F, 10.0F, 10.0F),
			PartPose.offset(0.0F, -9.0F, -6.0F));

		head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

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

	@Override
	public void setupAnim(TrollRenderState state) {
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * state.walkAnimationSpeed;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (state.isHoldingRock) {
			// arms up!
			this.rightArm.xRot = Mth.PI;
			this.leftArm.xRot = Mth.PI;
		} else {
			this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * state.walkAnimationSpeed * 0.5F;
		}
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;

		if (state.attackTime > 0F) {
			float swing = 1.0F - state.attackTime;

			this.rightArm.xRot -= (Mth.PI * swing);
			this.leftArm.xRot -= (Mth.PI * swing);
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		if (!state.isHoldingRock) {
			AnimationUtils.bobArms(this.rightArm, this.leftArm, state.ageInTicks);
		}

//		if (entity.getTarget() != null) {
//			this.rightArm.xRot += Mth.PI;
//			this.leftArm.xRot += Mth.PI;
//		}
	}
}