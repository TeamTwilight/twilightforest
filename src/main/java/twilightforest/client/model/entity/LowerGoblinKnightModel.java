package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.entity.monster.LowerGoblinKnight;

public class LowerGoblinKnightModel extends HumanoidModel<LowerGoblinKnight> {

	private final ModelPart tunic;

	public LowerGoblinKnightModel(ModelPart root) {
		super(root);
		this.tunic = root.getChild("tunic");
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-2.5F, -5.0F, -3.5F, 5.0F, 5.0F, 5.0F),
			PartPose.offset(0.0F, 10.0F, 1.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(),
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

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 30)
				.addBox(-2.5F, -5.0F, -3.5F, 5.0F, 5.0F, 5.0F),
			PartPose.offset(0.0F, 8.0F, 1.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(16, 48)
				.addBox(-3.5F, 0.0F, -2.0F, 7.0F, 8.0F, 4.0F),
			PartPose.offset(0.0F, 8.0F, 0.0F));

		partdefinition.addOrReplaceChild("tunic", CubeListBuilder.create()
				.texOffs(64, 19)
				.addBox(-6.0F, 0.0F, -3.0F, 12.0F, 9.0F, 6.0F),
			PartPose.offset(0.0F, 7.5F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(48, 48)
				.addBox(-2.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
			PartPose.offsetAndRotation(-3.5F, 10.0F, 0.0F, 0.0F, 0.0F, 0.10000000116728046F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(38, 48)
				.addBox(0.0F, -2.0F, -1.5F, 2.0F, 8.0F, 3.0F),
			PartPose.offsetAndRotation(3.5F, 10.0F, 0.0F, 0.0F, 0.0F, -0.10000736647217022F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 40)
				.addBox(-3.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(-2.5F, 16.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 52)
				.addBox(-1.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
			PartPose.offset(2.5F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		super.renderToBuffer(stack, builder, light, overlay, color);
		this.tunic.render(stack, builder, light, overlay, color);
	}

	@Override
	public void setupAnim(LowerGoblinKnight entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.isVehicle()) {
			this.head.yRot = 0;
			this.head.xRot = 0;
		} else {
			this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
			this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		}
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
		if (!entity.hasArmor() && !entity.isVehicle()) {
			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		} else {
			this.rightArm.xRot = 0.0F;
			this.leftArm.xRot = 0.0F;
		}
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		if (!entity.hasArmor() && !entity.isVehicle()) {
			AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
		}

		this.tunic.visible = entity.hasArmor();
	}
}
