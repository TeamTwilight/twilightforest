package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class KnightPhantomTrophyModel extends GenericTrophyModel {

	public final ModelPart head;
	public final ModelPart helmet;
	
	public KnightPhantomTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.helmet = part.getChild("helmet");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		CubeDeformation deformation = new CubeDeformation(0.25F);

		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
		PartPose.offset(0.0F, -4.0F, 0.0F));

		var helm = partdefinition.addOrReplaceChild("helmet",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		var rightHorn = helm.addOrReplaceChild("right_horn_1",
				CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-5.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, deformation),
				PartPose.offsetAndRotation(-4.0F, -6.5F, 0.0F,
						0.0F, -25F / (180F / (float) Math.PI), 45F / (180F / (float) Math.PI)));

		rightHorn.addOrReplaceChild("right_horn_2",
				CubeListBuilder.create()
						.texOffs(54, 16)
						.addBox(-3.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, deformation),
				PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F,
						0.0F, -15F / (180F / (float) Math.PI), 45F / (180F / (float) Math.PI)));

		var leftHorn = helm.addOrReplaceChild("left_horn_1",
				CubeListBuilder.create().mirror()
						.texOffs(24, 0)
						.addBox(0.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, deformation),
				PartPose.offsetAndRotation(4.0F, -6.5F, 0.0F,
						0.0F, 25F / (180F / (float) Math.PI), -45F / (180F / (float) Math.PI)));

		leftHorn.addOrReplaceChild("left_horn_2",
				CubeListBuilder.create()
						.texOffs(54, 16)
						.addBox(0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, deformation),
				PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F,
						0.0F, 15F / (180F / (float) Math.PI), -45F / (180F / (float) Math.PI)));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * ((float) Math.PI / 180F);
		this.head.xRot = z * ((float) Math.PI / 180F);
		this.helmet.xRot = this.head.xRot;
		this.helmet.yRot = this.head.yRot;
	}
	
	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderHelmToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.helmet.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
