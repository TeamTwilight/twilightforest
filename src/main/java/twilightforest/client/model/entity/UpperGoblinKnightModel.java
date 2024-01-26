package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.UpperGoblinKnight;

public class UpperGoblinKnightModel extends HumanoidModel<UpperGoblinKnight> {

	public final ModelPart breastplate;

	public final ModelPart shield;
	public final ModelPart spear;

	public UpperGoblinKnightModel(ModelPart root) {
		super(root);
		this.breastplate = root.getChild("breastplate");
		this.shield = leftArm.getChild("shield");
		this.spear = rightArm.getChild("spear");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		var hat = definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		var helm = hat.addOrReplaceChild("helmet", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5F, -11.0F, -3.5F, 7, 11, 7),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 45F / (180F / Mth.PI), 0.0F));

		var rightHorn = hat.addOrReplaceChild("right_horn_1", CubeListBuilder.create()
						.texOffs(28, 0)
						.addBox(-6F, -1.5F, -1.5F, 7, 3, 3),
				PartPose.offsetAndRotation(-3.5F, -9F, 0.0F, 0.0F, 15F / (180F / Mth.PI), 10F / (180F / Mth.PI)));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create()
						.texOffs(28, 6)
						.addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, 0.0F, 10F / (180F / Mth.PI)));

		var leftHorn = hat.addOrReplaceChild("left_horn_1", CubeListBuilder.create().mirror()
						.texOffs(28, 0)
						.addBox(-1F, -1.5F, -1.5F, 7, 3, 3),
				PartPose.offsetAndRotation(3.5F, -9F, 0.0F, 0.0F, -15F / (180F / Mth.PI), -10F / (180F / Mth.PI)));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create().mirror()
						.texOffs(28, 6)
						.addBox(0.0F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, 0.0F, -10F / (180F / Mth.PI)));

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 18)
						.addBox(-5.5F, 0.0F, -2.0F, 11, 8, 4)
						.texOffs(30, 24)
						.addBox(-6.5F, 0F, -2F, 1, 4, 4)
						.texOffs(30, 24)
						.addBox(5.5F, 0F, -2F, 1, 4, 4),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		definition.addOrReplaceChild("breastplate", CubeListBuilder.create()
						.texOffs(64, 0)
						.addBox(-6.5F, 0.0F, -3.0F, 13, 12, 6),
				PartPose.offset(0F, 11.5F, 0.0F));

		var rightArm = definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(44, 16)
						.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(-6.5F, 14.0F, 0.0F));

		rightArm.addOrReplaceChild("spear", CubeListBuilder.create()
						.texOffs(108, 0)
						.addBox(-1.0F, -19.0F, -1.0F, 2, 40, 2),
				PartPose.offsetAndRotation(-2F, 8.5F, 0.0F, 90F / (180F / Mth.PI), 0.0F, 0.0F));

		var leftArm = definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(44, 16)
						.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4),
				PartPose.offset(6.5F, 14.0F, 0.0F));

		leftArm.addOrReplaceChild("shield", CubeListBuilder.create()
						.texOffs(63, 36)
						.addBox(-6.0F, -6.0F, -2.0F, 12, 20, 2),
				PartPose.offsetAndRotation(0F, 12F, 0.0F, 90F / (180F / Mth.PI), 0.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(30, 16)
						.addBox(-1.5F, 0.0F, -2.0F, 3, 4, 4),
				PartPose.offset(-4F, 20.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(30, 16)
						.addBox(-1.5F, 0.0F, -2.0F, 3, 4, 4),
				PartPose.offset(4F, 20.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		super.renderToBuffer(stack, builder, light, overlay, red, green, blue, scale);

		this.breastplate.render(stack, builder, light, overlay, red, green, blue, scale);
	}

	@Override
	public void setupAnim(UpperGoblinKnight entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean hasShield = entity.hasShield();

		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.head.zRot = 0;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		this.hat.zRot = this.head.zRot;

		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;

		float leftConstraint = hasShield ? 0.2F : limbSwingAmount;

		if (entity.isShieldDisabled()) {
			this.leftArm.zRot = ((float)(Math.cos((double)entity.tickCount * 3.25D) * Math.PI * (double)0.4F) * Mth.DEG_TO_RAD) - 0.4F;
		} else {
			this.leftArm.zRot = 0.0F;
		}

		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * leftConstraint * 0.5F;
		this.rightArm.zRot = 0.0F;

		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (this.riding) {
			this.rightArm.xRot -= ((float) Math.PI / 5F);
			this.leftArm.xRot -= ((float) Math.PI / 5F);
			this.rightLeg.xRot = 0;
			this.leftLeg.xRot = 0;
//            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
//            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
		}

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
		}

		this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);

		rightArm.xRot -= (Math.PI * 0.66);

		// during swing move arm forward
		if (entity.heavySpearTimer > 0) {
			rightArm.xRot -= this.getArmRotationDuringSwing(60 - entity.heavySpearTimer) / (180F / (float) Math.PI);
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

		// shield arm points somewhat inward
		this.leftArm.zRot = -this.leftArm.zRot;

		// fix shield so that it's always perpendicular to the floor
		this.shield.xRot = (float) (Math.PI * 2 - this.leftArm.xRot);

		this.breastplate.visible = entity.hasArmor();
		this.shield.visible = entity.hasShield();
	}

	/**
	 *
	 */
	private float getArmRotationDuringSwing(float attackTime) {
		if (attackTime <= 10) {
			// rock back
			return attackTime;
		}
		if (attackTime > 10 && attackTime <= 30) {
			// hang back
			return 10F;
		}
		if (attackTime > 30 && attackTime <= 33) {
			// slam forward
			return (attackTime - 30) * -8F + 10F;
		}
		if (attackTime > 33 && attackTime <= 50) {
			// stay forward
			return -15F;
		}
		if (attackTime > 50 && attackTime <= 60) {
			// back to normal
			return (10 - (attackTime - 50)) * -1.5F;
		}

		return 0;
	}
}
