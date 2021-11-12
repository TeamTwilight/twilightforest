package twilightforest.client.model.entity;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.Lich;

import java.util.Arrays;

public class LichModel extends HumanoidModel<Lich> {

	private boolean shadowClone;
	private final ModelPart collar;
	private final ModelPart cloak;

	public LichModel(ModelPart root) {
		super(root);
		this.collar = root.getChild("collar");
		this.cloak = root.getChild("cloak");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		partRoot.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		partRoot.addOrReplaceChild("collar", CubeListBuilder.create()
						.texOffs(32, 16)
						.addBox(-6.0F, -2.0F, -4.0F, 12.0F, 12.0F, 1.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 2.164208F, 0F, 0F));

		partRoot.addOrReplaceChild("cloak", CubeListBuilder.create()
						.texOffs(0, 44)
						.addBox(-6.0F, 2.0F, 0.0F, 12.0F, 19.0F, 1.0F),
				PartPose.offset(0.0F, -4.0F, 2.5F));

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(8, 16)
						.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 24.0F, 4.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		partRoot.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(-5.0F, -2.0F, 0.0F));

		partRoot.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-1.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		partRoot.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(-2.0F, 12.0F, 0.0F));

		partRoot.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
		if (!shadowClone) {
			super.renderToBuffer(stack, builder, light, overlay, red, green, blue, alpha);
		} else {
			float shadow = 0.33f;
			super.renderToBuffer(stack, builder, light, overlay, red * shadow, green * shadow, blue * shadow, 0.8F);
		}
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		if (shadowClone) {
			return super.bodyParts();
		} else {
			return Iterables.concat(Arrays.asList(cloak, collar), super.bodyParts());
		}
	}

	@Override
	public void setupAnim(Lich entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.shadowClone = entity.isShadowClone();
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		float ogSin = Mth.sin(attackTime * 3.141593F);
		float otherSin = Mth.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.141593F);
		rightArm.zRot = 0.0F;
		leftArm.zRot = 0.5F;
		rightArm.yRot = -(0.1F - ogSin * 0.6F);
		leftArm.yRot = 0.1F - ogSin * 0.6F;
		rightArm.xRot = -1.570796F;
		leftArm.xRot = -3.141593F;
		rightArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
		leftArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
		rightArm.zRot += Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
		leftArm.zRot -= Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
		rightArm.xRot += Mth.sin(ageInTicks * 0.167F) * 0.15F;
		leftArm.xRot -= Mth.sin(ageInTicks * 0.167F) * 0.15F;

		head.y = -4.0F;
		hat.y = -4.0F;
	}
}
