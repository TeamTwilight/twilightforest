package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.client.model.tileentity.GenericTrophyModel;

public class HydraTrophyLegacyModel extends GenericTrophyModel {

	public ModelPart head;
	public ModelPart jaw;

	public HydraTrophyLegacyModel(ModelPart root) {
		this.head = root.getChild("head");
		this.jaw = head.getChild("jaw");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partRoot = mesh.getRoot();

		var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(272, 0)
						.addBox(-16F, -14F, -32F, 32, 24, 32)
						.texOffs(272, 56)
						.addBox(-15F, -2F, -56F, 30, 12, 24)
						.texOffs(272, 132)
						.addBox(-15F, 10F, -20F, 30, 8, 16)
						.texOffs(128, 200)
						.addBox(-2F, -30F, -12F, 4, 24, 24)
						.texOffs(272, 156)
						.addBox(-12F, 10, -49F, 2, 5, 2)
						.texOffs(272, 156)
						.addBox(10F, 10, -49F, 2, 5, 2)
						.texOffs(280, 156)
						.addBox(-8F, 9, -49F, 16, 2, 2)
						.texOffs(280, 160)
						.addBox(-10F, 9, -45F, 2, 2, 16)
						.texOffs(280, 160)
						.addBox(8F, 9, -45F, 2, 2, 16),
				PartPose.ZERO);

		head.addOrReplaceChild("jaw", CubeListBuilder.create()
						.texOffs(272, 92)
						.addBox(-15F, 0F, -32F, 30, 8, 32)
						.texOffs(272, 156)
						.addBox(-10F, -5, -29F, 2, 5, 2)
						.texOffs(272, 156)
						.addBox(8F, -5, -29F, 2, 5, 2)
						.texOffs(280, 156)
						.addBox(-8F, -1, -29F, 16, 2, 2)
						.texOffs(280, 160)
						.addBox(-10F, -1, -25F, 2, 2, 16)
						.texOffs(280, 160)
						.addBox(8F, -1, -25F, 2, 2, 16),
				PartPose.offset(0F, 10F, -20F));

		head.addOrReplaceChild("frill", CubeListBuilder.create()
						.texOffs(272, 200)
						.addBox(-24F, -40.0F, 0F, 48, 48, 4),
				PartPose.offsetAndRotation(0F, 0F, -14F, -0.5235988F, 0F, 0F));

		return LayerDefinition.create(mesh, 512, 256);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * ((float) Math.PI / 180F);
		this.head.xRot = x * ((float) Math.PI / 180F);
	}

	@Override
	public void openMouthForTrophy(float mouthOpen) {
		head.yRot = 0;
		head.xRot = 0;

		head.xRot -= (float) (mouthOpen * (Math.PI / 12.0));
		jaw.xRot = (float) (mouthOpen * (Math.PI / 3.0));
	}
	
	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
