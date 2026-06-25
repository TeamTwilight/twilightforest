package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class TFArmorModel extends HumanoidModel<HumanoidRenderState> {

	public TFArmorModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		// TODO: 26.1.2 - HumanoidRenderState no longer has getEntity(); ArmorStand pose handling needs rework
		/*
		if (state.getEntity() instanceof ArmorStand stand) {
			this.head.xRot = Mth.DEG_TO_RAD * stand.getHeadPose().getX();
			this.head.yRot = Mth.DEG_TO_RAD * stand.getHeadPose().getY();
			this.head.zRot = Mth.DEG_TO_RAD * stand.getHeadPose().getZ();
			this.body.xRot = Mth.DEG_TO_RAD * stand.getBodyPose().getX();
			this.body.yRot = Mth.DEG_TO_RAD * stand.getBodyPose().getY();
			this.body.zRot = Mth.DEG_TO_RAD * stand.getBodyPose().getZ();
			this.leftArm.xRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().getX();
			this.leftArm.yRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().getY();
			this.leftArm.zRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().getZ();
			this.rightArm.xRot = Mth.DEG_TO_RAD * stand.getRightArmPose().getX();
			this.rightArm.yRot = Mth.DEG_TO_RAD * stand.getRightArmPose().getY();
			this.rightArm.zRot = Mth.DEG_TO_RAD * stand.getRightArmPose().getZ();
			this.leftLeg.xRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().getX();
			this.leftLeg.yRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().getY();
			this.leftLeg.zRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().getZ();
			this.rightLeg.xRot = Mth.DEG_TO_RAD * stand.getRightLegPose().getX();
			this.rightLeg.yRot = Mth.DEG_TO_RAD * stand.getRightLegPose().getY();
			this.rightLeg.zRot = Mth.DEG_TO_RAD * stand.getRightLegPose().getZ();
			this.hat.copyFrom(this.head);
		} else {
			super.setupAnim(state);
		}
		*/
		super.setupAnim(state);
	}
}
