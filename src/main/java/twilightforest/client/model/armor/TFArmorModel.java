package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class TFArmorModel extends HumanoidModel<HumanoidRenderState> {

	@Nullable
	private EquipmentSlot slot;

	public TFArmorModel(ModelPart root) {
		super(root);
	}

	public void setSlot(EquipmentSlot slot) {
		this.slot = slot;
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		if (state instanceof ArmorStandRenderState armorStandState) {
			// [VanillaCopy] ArmorStandArmorModel
			// this prevents helmets from always facing south, and the armor "breathing" on the stand
			this.head.xRot = Mth.DEG_TO_RAD * armorStandState.headPose.x();
			this.head.yRot = Mth.DEG_TO_RAD * armorStandState.headPose.y();
			this.head.zRot = Mth.DEG_TO_RAD * armorStandState.headPose.z();
			this.body.xRot = Mth.DEG_TO_RAD * armorStandState.bodyPose.x();
			this.body.yRot = Mth.DEG_TO_RAD * armorStandState.bodyPose.y();
			this.body.zRot = Mth.DEG_TO_RAD * armorStandState.bodyPose.z();
			this.leftArm.xRot = Mth.DEG_TO_RAD * armorStandState.leftArmPose.x();
			this.leftArm.yRot = Mth.DEG_TO_RAD * armorStandState.leftArmPose.y();
			this.leftArm.zRot = Mth.DEG_TO_RAD * armorStandState.leftArmPose.z();
			this.rightArm.xRot = Mth.DEG_TO_RAD * armorStandState.rightArmPose.x();
			this.rightArm.yRot = Mth.DEG_TO_RAD * armorStandState.rightArmPose.y();
			this.rightArm.zRot = Mth.DEG_TO_RAD * armorStandState.rightArmPose.z();
			this.leftLeg.xRot = Mth.DEG_TO_RAD * armorStandState.leftLegPose.x();
			this.leftLeg.yRot = Mth.DEG_TO_RAD * armorStandState.leftLegPose.y();
			this.leftLeg.zRot = Mth.DEG_TO_RAD * armorStandState.leftLegPose.z();
			this.rightLeg.xRot = Mth.DEG_TO_RAD * armorStandState.rightLegPose.x();
			this.rightLeg.yRot = Mth.DEG_TO_RAD * armorStandState.rightLegPose.y();
			this.rightLeg.zRot = Mth.DEG_TO_RAD * armorStandState.rightLegPose.z();
			this.hat.loadPose(this.head.getInitialPose());
		} else {
			super.setupAnim(state); // TF - Defer to super otherwise
		}

		this.applyPartVisibility();
	}

	private void applyPartVisibility() {
		if (this.slot == null) return;

		this.allParts().forEach(part -> part.visible = false);
		this.root().visible = true;

		switch (this.slot) {
			case HEAD -> this.head.getAllParts().forEach(part -> part.visible = true);
			case CHEST -> {
				this.body.visible = true;
				this.rightArm.visible = true;
				this.leftArm.visible = true;
			}
			case LEGS -> {
				this.body.visible = true;
				this.rightLeg.visible = true;
				this.leftLeg.visible = true;
			}
			case FEET -> {
				this.rightLeg.visible = true;
				this.leftLeg.visible = true;
			}
			default -> {
			}
		}
	}
}
