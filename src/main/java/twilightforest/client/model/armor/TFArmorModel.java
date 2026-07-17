package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class TFArmorModel extends HumanoidModel<HumanoidRenderState> {

	public TFArmorModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(HumanoidRenderState humanoidRenderState) {
		// [VanillaCopy] ArmorStandArmorModel
		// this prevents helmets from always facing south, and the armor "breathing" on the stand
		if (humanoidRenderState instanceof ArmorStandRenderState state) {
			this.head.xRot = Mth.DEG_TO_RAD * state.headPose.x();
			this.head.yRot = Mth.DEG_TO_RAD * state.headPose.y();
			this.head.zRot = Mth.DEG_TO_RAD * state.headPose.z();
			this.body.xRot = Mth.DEG_TO_RAD * state.bodyPose.x();
			this.body.yRot = Mth.DEG_TO_RAD * state.bodyPose.y();
			this.body.zRot = Mth.DEG_TO_RAD * state.bodyPose.z();
			this.leftArm.xRot = Mth.DEG_TO_RAD * state.leftArmPose.x();
			this.leftArm.yRot = Mth.DEG_TO_RAD * state.leftArmPose.y();
			this.leftArm.zRot = Mth.DEG_TO_RAD * state.leftArmPose.z();
			this.rightArm.xRot = Mth.DEG_TO_RAD * state.rightArmPose.x();
			this.rightArm.yRot = Mth.DEG_TO_RAD * state.rightArmPose.y();
			this.rightArm.zRot = Mth.DEG_TO_RAD * state.rightArmPose.z();
			this.leftLeg.xRot = Mth.DEG_TO_RAD * state.leftLegPose.x();
			this.leftLeg.yRot = Mth.DEG_TO_RAD * state.leftLegPose.y();
			this.leftLeg.zRot = Mth.DEG_TO_RAD * state.leftLegPose.z();
			this.rightLeg.xRot = Mth.DEG_TO_RAD * state.rightLegPose.x();
			this.rightLeg.yRot = Mth.DEG_TO_RAD * state.rightLegPose.y();
			this.rightLeg.zRot = Mth.DEG_TO_RAD * state.rightLegPose.z();
			this.hat.loadPose(this.head.storePose());
		} else {
			super.setupAnim(humanoidRenderState);
		} // TF - Defer to super otherwise
	}
}
