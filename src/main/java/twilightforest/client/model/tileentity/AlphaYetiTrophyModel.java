package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class AlphaYetiTrophyModel extends GenericTrophyModel {
	public final ModelPart main;

	public AlphaYetiTrophyModel(ModelPart part) {
		this.main = part.getChild("main");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var main = partdefinition.addOrReplaceChild("main",
			CubeListBuilder.create()
				.texOffs(80, 0).addBox(-24.0F, -24.0F, -18.0F, 48.0F, 54.0F, 36.0F)
				.texOffs(64, 0).addBox(8.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F)
				.texOffs(121, 50).addBox(-17.0F, -8.0F, -19.5F, 34.0F, 29.0F, 2.0F)
				.texOffs(64, 0).addBox(-20.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F),
			PartPose.offset(0.0F, -6.0F, 0.0F));

		main.addOrReplaceChild("left_horn_1",
				CubeListBuilder.create()
					.texOffs(0, 108).addBox(1.6981F, -10.9848F, -5.1743F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(22.0F, 6.0F, -1.0F, 0.0F, 0.5236F, 0.0873F))

			.addOrReplaceChild("left_horn_1_top",
				CubeListBuilder.create()
					.texOffs(40, 108).addBox(0.4505F, -7.9433F, -4.3855F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(11.0F, -2.0F, 0.0F, 0.0F, 0.3491F, 0.0873F));

		main.addOrReplaceChild("left_horn_2",
				CubeListBuilder.create()
					.texOffs(0, 108).addBox(0.8966F, -10.8637F, -4.4824F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(24.0F, -4.0F, -1.0F, 0.0F, 0.5236F, -0.2618F))

			.addOrReplaceChild("left_horn_2_top",
				CubeListBuilder.create()
					.texOffs(40, 108).addBox(2.5764F, -8.5F, -2.8753F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(9.0F, -1.0F, 0.0F, 0.0F, 0.3491F, -0.2618F));

		main.addOrReplaceChild("left_horn_3",
				CubeListBuilder.create()
					.texOffs(0, 108).addBox(1.9869F, -10.2766F, -3.8528F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(24.0F, -16.0F, -1.0F, 0.0F, 0.5236F, -0.6109F))

			.addOrReplaceChild("left_horn_3_top",
				CubeListBuilder.create()
					.texOffs(40, 108).addBox(4.9031F, -5.5443F, -1.7225F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(8.0F, -2.0F, 0.0F, 0.0F, 0.3491F, -0.6109F));

		main.addOrReplaceChild("right_horn_1",
				CubeListBuilder.create().mirror()
					.texOffs(0, 108).addBox(-11.6981F, -10.9848F, -5.1743F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(-22.0F, 6.0F, -1.0F, 0.0F, -0.5236F, -0.0873F))

			.addOrReplaceChild("right_horn_1_top",
				CubeListBuilder.create().mirror()
					.texOffs(40, 108).addBox(-18.4505F, -7.9433F, -4.3855F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(-11.0F, -2.0F, 0.0F, 0.0F, -0.3491F, -0.0873F));

		main.addOrReplaceChild("right_horn_2",
				CubeListBuilder.create().mirror()
					.texOffs(0, 108).addBox(-10.8966F, -10.8637F, -4.4824F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(-24.0F, -4.0F, -1.0F, 0.0F, -0.5236F, 0.2618F))

			.addOrReplaceChild("right_horn_2_top",
				CubeListBuilder.create().mirror()
					.texOffs(40, 108).addBox(-20.5764F, -8.5F, -2.8753F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(-9.0F, -1.0F, 0.0F, 0.0F, -0.3491F, 0.2618F));

		main.addOrReplaceChild("right_horn_3",
				CubeListBuilder.create().mirror()
					.texOffs(0, 108).addBox(-11.9869F, -10.2766F, -3.8528F, 10.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(-24.0F, -16.0F, -1.0F, 0.0F, -0.5236F, 0.6109F))

			.addOrReplaceChild("right_horn_3_top",
				CubeListBuilder.create().mirror()
					.texOffs(40, 108).addBox(-22.9031F, -5.5443F, -1.7225F, 18.0F, 8.0F, 8.0F),
				PartPose.offsetAndRotation(-8.0F, -2.0F, 0.0F, 0.0F, -0.3491F, 0.6109F));

		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	@Override
	public void setRotations(float x, float y, float z) {
		this.main.yRot = y * ((float) Math.PI / 180F);
		this.main.xRot = z * ((float) Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.main.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}