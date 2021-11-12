package twilightforest.client.model.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import twilightforest.entity.monster.Redcap;

public class RedcapLegacyModel <T extends Redcap> extends HumanoidModel<T> {

	public RedcapLegacyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partRoot = mesh.getRoot();

		var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5F, -5.0F, -4.0F, 7.0F, 7.0F, 7.0F),
				PartPose.offset(0.0F, 4.0F, 0.0F));

		partRoot.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-2.0F, -6.0F, -3.0F, 4.0F, 5.0F, 7.0F),
				PartPose.offset(0.0F, 7.0F, 0.0F));

		head.addOrReplaceChild("right_ear", CubeListBuilder.create()
						.texOffs(48, 20)
						.addBox(3.0F, -10.0F, -1.0F, 2.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 7.0F, 0.0F));

		head.addOrReplaceChild("left_ear", CubeListBuilder.create().mirror()
						.texOffs(48, 20)
						.addBox(-5.0F, -10.0F, -1.0F, 2.0F, 3.0F, 1.0F),
				PartPose.offset(0.0F, 7.0F, 0.0F));

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(12, 19).addBox(-4.0F, 6.0F, -2.0F, 8.0F, 9.0F, 4.0F),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(36, 17)
						.addBox(-2.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				PartPose.offset(-5.0F, 8.0F, 0.0F));

		partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(36, 17)
						.addBox(-1.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				PartPose.offset(5.0F, 8.0F, 0.0F));

		partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 20)
						.addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F),
				PartPose.offset(-2.0F, 15.0F, 0.0F));

		partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 20)
						.addBox(-2.0F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F),
				PartPose.offset(3.0F, 15.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
						  float headPitch) {

		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.body.yRot = 0.0F;

		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;
		float f = 1.0F;

		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount
				* 0.5F / f;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		if (this.riding) {
			this.rightArm.xRot += (-(float)Math.PI / 5F);
			this.leftArm.xRot += (-(float)Math.PI / 5F);
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}

		AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		if (this.swimAmount > 0.0F) {
			float f1 = limbSwing % 26.0F;
			HumanoidArm handside = this.getAttackArm(entity);
			float f2 = handside == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			float f3 = handside == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			if (f1 < 14.0F) {
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, 0.0F);
				this.rightArm.xRot = Mth.lerp(f2, this.rightArm.xRot, 0.0F);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = Mth.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, (float)Math.PI + 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
				this.rightArm.zRot = Mth.lerp(f2, this.rightArm.zRot, (float)Math.PI - 1.8707964F * this.getArmAngleSq(f1) / this.getArmAngleSq(14.0F));
			} else if (f1 >= 14.0F && f1 < 22.0F) {
				float f6 = (f1 - 14.0F) / 8.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, ((float)Math.PI / 2F) * f6);
				this.rightArm.xRot = Mth.lerp(f2, this.rightArm.xRot, ((float)Math.PI / 2F) * f6);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = Mth.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
				this.rightArm.zRot = Mth.lerp(f2, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
			} else if (f1 >= 22.0F && f1 < 26.0F) {
				float f4 = (f1 - 22.0F) / 4.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
				this.rightArm.xRot = Mth.lerp(f2, this.rightArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f4);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float)Math.PI);
				this.rightArm.yRot = Mth.lerp(f2, this.rightArm.yRot, (float)Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, (float)Math.PI);
				this.rightArm.zRot = Mth.lerp(f2, this.rightArm.zRot, (float)Math.PI);
			}

			this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F + (float)Math.PI));
			this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F));
		}

		this.setupAttackAnimation(entity, ageInTicks);

		this.hat.copyFrom(this.head);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red,
							   float green, float blue, float alpha) {
		if(this.riding) matrixStack.translate(0, 0.25F, 0);
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		hat.render(matrixStack, buffer, packedLight, packedOverlay);
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	private float getArmAngleSq(float limbSwing) {
		return -65.0F * limbSwing + limbSwing * limbSwing;
	}
}