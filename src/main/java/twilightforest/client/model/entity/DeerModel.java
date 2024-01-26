package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.passive.Deer;

public class DeerModel extends QuadrupedModel<Deer> {

	public DeerModel(ModelPart root) {
		super(root, false, 4.0F, 4.0F, 2.0F, 2.0F, 10);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 5)
						.addBox(-2.0F, -4.0F, -4.0F, 4.0F, 6.0F, 6.0F)
						.texOffs(52, 0)
						.addBox(-1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 3.0F),
				PartPose.offset(0.0F, 0.0F, -9.0F));

		head.addOrReplaceChild("left_antler", CubeListBuilder.create()
						.texOffs(20, 0)
						.addBox(-3.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
						.addBox(-3.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
						.addBox(-4.0F, -6.0F, 0.0F, 1.0F, 1.0F, 3.0F)
						.addBox(-5.0F, -7.0F, 2.0F, 1.0F, 1.0F, 5.0F)
						.addBox(-5.0F, -10.0F, 3.0F, 1.0F, 4.0F, 1.0F)
						.addBox(-6.0F, -13.0F, 4.0F, 1.0F, 4.0F, 1.0F)
						.addBox(-6.0F, -9.0F, 1.0F, 1.0F, 1.0F, 3.0F)
						.addBox(-6.0F, -10.0F, -2.0F, 1.0F, 1.0F, 4.0F)
						.addBox(-7.0F, -11.0F, -5.0F, 1.0F, 1.0F, 4.0F)
						.addBox(-6.0F, -12.0F, -8.0F, 1.0F, 1.0F, 4.0F)
						.addBox(-7.0F, -14.0F, 0.0F, 1.0F, 5.0F, 1.0F)
						.addBox(-6.0F, -15.0F, -5.0F, 1.0F, 5.0F, 1.0F),
				PartPose.ZERO);

		head.addOrReplaceChild("right_antler", CubeListBuilder.create()
						.texOffs(20, 0)
						.addBox(1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
						.addBox(3.0F, -6.0F, 0.0F, 1.0F, 1.0F, 3.0F)
						.addBox(4.0F, -7.0F, 2.0F, 1.0F, 1.0F, 5.0F)
						.addBox(4.0F, -10.0F, 3.0F, 1.0F, 4.0F, 1.0F)
						.addBox(5.0F, -13.0F, 4.0F, 1.0F, 4.0F, 1.0F)
						.addBox(5.0F, -9.0F, 1.0F, 1.0F, 1.0F, 3.0F)
						.addBox(5.0F, -10.0F, -2.0F, 1.0F, 1.0F, 4.0F)
						.addBox(6.0F, -11.0F, -5.0F, 1.0F, 1.0F, 4.0F)
						.addBox(5.0F, -12.0F, -8.0F, 1.0F, 1.0F, 4.0F)
						.addBox(6.0F, -14.0F, 0.0F, 1.0F, 5.0F, 1.0F)
						.addBox(5.0F, -15.0F, -5.0F, 1.0F, 5.0F, 1.0F),
				PartPose.ZERO);

		var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(36, 6)
						.addBox(-4F, -10F, -7F, 6, 18, 8),
				PartPose.offsetAndRotation(1F, 5F, 2F, 1.570796F, 0.0F, 0.0F));

		body.addOrReplaceChild("neck", CubeListBuilder.create()
						.texOffs(10, 19)
						.addBox(-2.5F, -8, -11F, 3, 9, 4),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 4.974188f, 0.0F, 0.0F));

		definition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
						.texOffs(0, 17)
						.addBox(-3F, 0F, -2F, 2, 12, 3),
				PartPose.offset(0F, 12F, 9F));

		definition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
						.texOffs(0, 17)
						.addBox(-1F, 0F, -2F, 2, 12, 3),
				PartPose.offset(2F, 12F, 9F));

		definition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
						.texOffs(0, 17)
						.addBox(-3F, 0F, -2F, 2, 12, 3),
				PartPose.offset(0F, 12F, -5F));

		definition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
						.texOffs(0, 17)
						.addBox(-1F, 0F, -2F, 2, 12, 3),
				PartPose.offset(2F, 12F, -5F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		if (young) {
			stack.pushPose();
			stack.scale(0.75F, 0.75F, 0.75F);
			stack.translate(0F, 0.95F, 0.15F);
			this.headParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
			stack.popPose();

			stack.pushPose();
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.translate(0F, 1.5F, 0.0F);
			this.bodyParts().forEach((modelRenderer) -> modelRenderer.render(stack, builder, light, overlay, red, green, blue, scale));
			stack.popPose();
		} else {
			this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
			this.bodyParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
		}
	}

	@Override
	public void setupAnim(Deer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.getChild("right_antler").visible = !entity.isBaby();
		this.head.getChild("left_antler").visible = !entity.isBaby();
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}
}
