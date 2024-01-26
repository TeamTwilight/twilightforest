package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.LowerGoblinKnight;

public class LowerGoblinKnightModel extends HumanoidModel<LowerGoblinKnight> {

	final ModelPart tunic;

	public LowerGoblinKnightModel(ModelPart root) {
		super(root);
		this.tunic = root.getChild("tunic");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 32)
						.addBox(-2.5F, -5.0F, -3.5F, 5, 5, 5),
				PartPose.offset(0.0F, 10.0F, 1.0F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 48)
						.addBox(-3.5F, 0.0F, -2.0F, 7, 8, 4),
				PartPose.offset(0.0F, 8.0F, 0.0F));

		definition.addOrReplaceChild("tunic", CubeListBuilder.create()
						.texOffs(64, 19)
						.addBox(-6.0F, 0.0F, -3.0F, 12, 9, 6),
				PartPose.offset(0F, 7.5F, 0.0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(40, 48)
						.addBox(-2.0F, -2.0F, -1.5F, 2, 8, 3),
				PartPose.offset(-3.5F, 10.0F, 0.0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(40, 48)
						.addBox(0.0F, -2.0F, -1.5F, 2, 8, 3),
				PartPose.offset(3.5F, 10.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 48)
						.addBox(-3.0F, 0.0F, -2.0F, 4, 8, 4),
				PartPose.offset(-2.5F, 16.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
						.texOffs(0, 48)
						.addBox(-1.0F, 0.0F, -2.0F, 4, 8, 4),
				PartPose.offset(2.5F, 16.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		super.renderToBuffer(stack, builder, light, overlay, red, green, blue, scale);
		this.tunic.render(stack, builder, light, overlay, red, green, blue, scale);
	}

	@Override
	public void setupAnim(LowerGoblinKnight entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.isVehicle()) {
			this.head.yRot = 0;
			this.head.xRot = 0;
		} else {
			this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
			this.head.xRot = headPitch / (180F / (float) Math.PI);
		}
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		if (!entity.hasArmor() && !entity.isVehicle()) {
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		} else {
			this.rightArm.xRot = 0.0F;
			this.leftArm.xRot = 0.0F;
		}
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (!entity.hasArmor() && !entity.isVehicle()) {
			this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
			this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
		}

		this.tunic.visible = entity.hasArmor();
	}
}
