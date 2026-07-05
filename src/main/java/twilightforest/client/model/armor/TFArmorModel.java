package twilightforest.client.model.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;

public class TFArmorModel extends HumanoidModel<HumanoidRenderState> {

	public TFArmorModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		// [VanillaCopy] ArmorStandArmorModel
		// this prevents helmets from always facing south, and the armor "breathing" on the stand
		if (state.entityType == EntityType.ARMOR_STAND) {
			ArmorStand stand = (ArmorStand) state.entityType.create(Minecraft.getInstance().level, EntitySpawnReason.NATURAL);
			this.head.xRot = Mth.DEG_TO_RAD * stand.getHeadPose().x();
			this.head.yRot = Mth.DEG_TO_RAD * stand.getHeadPose().y();
			this.head.zRot = Mth.DEG_TO_RAD * stand.getHeadPose().z();
			this.body.xRot = Mth.DEG_TO_RAD * stand.getBodyPose().x();
			this.body.yRot = Mth.DEG_TO_RAD * stand.getBodyPose().y();
			this.body.zRot = Mth.DEG_TO_RAD * stand.getBodyPose().z();
			this.leftArm.xRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().x();
			this.leftArm.yRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().y();
			this.leftArm.zRot = Mth.DEG_TO_RAD * stand.getLeftArmPose().z();
			this.rightArm.xRot = Mth.DEG_TO_RAD * stand.getRightArmPose().x();
			this.rightArm.yRot = Mth.DEG_TO_RAD * stand.getRightArmPose().y();
			this.rightArm.zRot = Mth.DEG_TO_RAD * stand.getRightArmPose().z();
			this.leftLeg.xRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().x();
			this.leftLeg.yRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().y();
			this.leftLeg.zRot = Mth.DEG_TO_RAD * stand.getLeftLegPose().z();
			this.rightLeg.xRot = Mth.DEG_TO_RAD * stand.getRightLegPose().x();
			this.rightLeg.yRot = Mth.DEG_TO_RAD * stand.getRightLegPose().y();
			this.rightLeg.zRot = Mth.DEG_TO_RAD * stand.getRightLegPose().z();
			this.hat.loadPose(this.head.getInitialPose());
		} else {
			super.setupAnim(state);
		}
	}
}
