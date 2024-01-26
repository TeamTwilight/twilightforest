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

public class QuestRamTrophyLegacyModel extends GenericTrophyModel {

	public final ModelPart head;

	public QuestRamTrophyLegacyModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 70).addBox(-6F, -4.5F, -7F, 12, 9, 15)
						.texOffs(0, 94).addBox(5F, -9F, 1F, 4, 4, 6)
						.texOffs(20, 96).addBox(7F, -8F, 6F, 3, 4, 4)
						.texOffs(34, 95).addBox(8F, -6F, 8F, 3, 6, 3)
						.texOffs(46, 98).addBox(9.5F, -2F, 6F, 3, 3, 3)
						.texOffs(58, 95).addBox(11F, 0F, 1F, 3, 3, 6)
						.texOffs(76, 95).addBox(12F, -4F, -1F, 3, 6, 3)
						.texOffs(88, 97).addBox(13F, -6F, 1F, 3, 3, 4)
						.texOffs(0, 94).addBox(-9F, -9F, 1F, 4, 4, 6)
						.texOffs(20, 96).addBox(-10F, -8F, 6F, 3, 4, 4)
						.texOffs(34, 95).addBox(-11F, -6F, 8F, 3, 6, 3)
						.texOffs(46, 98).addBox(-12.5F, -2F, 6F, 3, 3, 3)
						.texOffs(58, 95).addBox(-14F, 0F, 1F, 3, 3, 6)
						.texOffs(76, 95).addBox(-15F, -4F, -1F, 3, 6, 3)
						.texOffs(88, 97).addBox(-16F, -6F, 1F, 3, 3, 4),
				PartPose.offset(0F, -4F, -0F));

		definition.addOrReplaceChild("neck", CubeListBuilder.create()
						.texOffs(66, 37)
						.addBox(-5.5F, -8F, 0F, 11, 14, 12),
				PartPose.offsetAndRotation(0F, -8F, -7F, 0.2617994F, 0F, 0F));

		head.addOrReplaceChild("nose", CubeListBuilder.create()
						.texOffs(54, 73)
						.addBox(-5.5F, -1F, -6F, 11, 9, 12),
				PartPose.offsetAndRotation(0F, -7F, -1F, 0.5235988F, 0F, 0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * ((float) Math.PI / 180F);
		this.head.xRot = z * ((float) Math.PI / 180F);
	}
	
	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
