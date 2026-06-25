package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.client.state.entity.UpperGoblinKnightRenderState;

public class UpperGoblinKnightModel extends HumanoidModel<UpperGoblinKnightRenderState> {

	private final ModelPart breastplate;
	private final ModelPart shield;

	public UpperGoblinKnightModel(ModelPart root) {
		super(root);
		this.breastplate = this.body.getChild("breastplate");
		this.shield = this.leftArm.getChild("shield");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(),
			PartPose.offset(0.0F, 12.0F, 0.0F));

		var hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create(),
			PartPose.offset(0.0F, 12.0F, 0.0F));

		hat.addOrReplaceChild("helmet", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-3.5F, -11.0F, -3.5F, 7.0F, 11.0F, 7.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 45.0F * Mth.DEG_TO_RAD, 0.0F));

		var rightHorn = hat.addOrReplaceChild("right_horn_1", CubeListBuilder.create()
				.texOffs(28, 0)
				.addBox(-6.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(-3.5F, -9.0F, 0.0F, 0.0F, 15.0F * Mth.DEG_TO_RAD, 10.0F * Mth.DEG_TO_RAD));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create()
				.texOffs(28, 6)
				.addBox(-3.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, 0.0F, 10.0F * Mth.DEG_TO_RAD));

		var leftHorn = hat.addOrReplaceChild("left_horn_1", CubeListBuilder.create().mirror()
				.texOffs(28, 0)
				.addBox(-1.0F, -1.5F, -1.5F, 7.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(3.5F, -9.0F, 0.0F, 0.0F, -15.0F * Mth.DEG_TO_RAD, -10.0F * Mth.DEG_TO_RAD));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create().mirror()
				.texOffs(28, 6)
				.addBox(0.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, 0.0F, -10.0F * Mth.DEG_TO_RAD));

		var body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 18)
				.addBox(-5.5F, 0.0F, -2.0F, 11.0F, 8.0F, 4.0F)
				.texOffs(30, 24)
				.addBox(-6.5F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F)
				.texOffs(30, 24)
				.addBox(5.5F, 0.0F, -2.0F, 1.0F, 4.0F, 4.0F),
			PartPose.offset(0.0F, 12.0F, 0.0F));

		body.addOrReplaceChild("breastplate", CubeListBuilder.create()
				.texOffs(64, 0)
				.addBox(-6.5F, 0.0F, -3.0F, 13.0F, 12.0F, 6.0F),
			PartPose.offset(0.0F, -0.5F, 0.0F));

		var rightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(44, 16)
				.addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-6.5F, 14.0F, 0.0F));

		rightArm.addOrReplaceChild("spear", CubeListBuilder.create()
				.texOffs(108, 0)
				.addBox(-1.0F, -19.0F, -1.0F, 2.0F, 40.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 8.5F, 0.0F, 90.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F));

		var leftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(44, 16)
				.addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(6.5F, 14.0F, 0.0F));

		leftArm.addOrReplaceChild("shield", CubeListBuilder.create()
				.texOffs(63, 36)
				.addBox(-6.0F, -6.0F, -2.0F, 12.0F, 20.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 90.0F * Mth.DEG_TO_RAD, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(30, 16)
				.addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F),
			PartPose.offset(-4.0F, 20.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(30, 16)
				.addBox(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F),
			PartPose.offset(4.0F, 20.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(UpperGoblinKnightRenderState state) {
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
		this.head.zRot = 0.0F;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.hat.zRot = this.head.zRot;

		this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;

		float leftConstraint = state.hasShield ? 0.2F : state.walkAnimationSpeed;

		if (state.isShieldDisabled) {
			this.leftArm.zRot = ((Mth.cos(state.ageInTicks * 3.25F) * Mth.PI * 0.4F) * Mth.DEG_TO_RAD) - 0.4F;
		} else {
			this.leftArm.zRot = 0.0F;
		}

		this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * leftConstraint * 0.5F;
		this.rightArm.zRot = 0.0F;

		this.rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * state.walkAnimationSpeed;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (state.isPassenger) {
			this.rightArm.xRot -= (Mth.PI / 5.0F);
			this.leftArm.xRot -= (Mth.PI / 5.0F);
			this.rightLeg.xRot = 0;
			this.leftLeg.xRot = 0;
		}

		this.rightArm.xRot = this.rightArm.xRot * 0.5F - (Mth.PI / 10.0F);

		this.rightArm.xRot -= (Mth.PI * 0.66F);

		// during swing move arm forward
		if (state.spearTimer > 0) {
			this.rightArm.xRot -= state.getArmRotationDuringSwing() * Mth.DEG_TO_RAD;
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		AnimationUtils.bobArms(this.rightArm, this.leftArm, state.ageInTicks);

		// shield arm points somewhat inward
		this.leftArm.zRot = -this.leftArm.zRot;

		// fix shield so that it's always perpendicular to the floor
		this.shield.xRot = Mth.TWO_PI - this.leftArm.xRot;

		this.breastplate.visible = state.hasArmor;
		this.shield.visible = state.hasShield;
	}
}
