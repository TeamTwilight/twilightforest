// Date: 3/3/2012 11:56:45 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.passive.Penguin;

public class PenguinModel extends HumanoidModel<Penguin> {

	public PenguinModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 9.0F, 8.0F),
				PartPose.offset(0.0F, 14.0F, 0.0F));

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5F, -4.0F, -3.5F, 7.0F, 5.0F, 7.0F),
				PartPose.offset(0.0F, 13.0F, 0.0F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		head.addOrReplaceChild("beak", CubeListBuilder.create()
						.texOffs(0, 13)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -1.0F, -4.0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(34, 18)
						.addBox(-1.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F),
				PartPose.offset(-4.0F, 15.0F, 0.0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(24, 18)
						.addBox(0.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F),
				PartPose.offset(4.0F, 15.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 8.0F),
				PartPose.offset(-2.0F, 23.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -5.0F, 4.0F, 1.0F, 8.0F),
				PartPose.offset(2.0F, 23.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		if (this.young) {
			float f = 2.0F;
			stack.pushPose();
			stack.scale(1.0F / f, 1.0F / f, 1.0F / f);
			stack.translate(0.0F, 1.5F * scale, 0.0F);
			this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
			stack.popPose();

			stack.pushPose();
			stack.scale(1.0F / f, 1.0F / f, 1.0F / f);
			stack.translate(0.0F, 1.5F * scale, 0.0F);
			this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
			stack.popPose();
		} else {
			this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
			this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
		}
	}

	@Override
	public void setupAnim(Penguin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);

		this.rightLeg.xRot = Mth.cos(limbSwing) * 0.7F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing + (float) Math.PI) * 0.7F * limbSwingAmount;

		this.rightArm.zRot = ageInTicks;
		this.leftArm.zRot = -ageInTicks;
	}
}
