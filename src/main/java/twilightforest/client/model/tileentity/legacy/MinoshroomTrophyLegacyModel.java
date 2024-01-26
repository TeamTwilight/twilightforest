package twilightforest.client.model.tileentity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.client.model.tileentity.GenericTrophyModel;

public class MinoshroomTrophyLegacyModel extends GenericTrophyModel {

	public final ModelPart head;

	public MinoshroomTrophyLegacyModel(ModelPart part) {
		this.head = part.getChild("head");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(96, 16)
						.addBox(-4F, -8F, -4F, 8, 8, 8),
				PartPose.offset(0F, -6F, -9F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		head.addOrReplaceChild("snout", CubeListBuilder.create()
						.texOffs(105, 28)
						.addBox(-2, -1, -1, 4, 3, 1),
				PartPose.offset(0F, -2.0F, -4F));

		var rightHorn = head.addOrReplaceChild("right_horn_1", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(-2.5F, -6.5F, 0.0F, 0.0F, -25F / (180F / Mth.PI), 10F / (180F / Mth.PI) ));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create()
						.texOffs(16, 0)
						.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, -15F / (180F / Mth.PI), 45F / (180F / Mth.PI)));

		var leftHorn = head.addOrReplaceChild("left_horn_1", CubeListBuilder.create().mirror()
						.texOffs(0, 0)
						.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(2.5F, -6.5F, 0.0F, 0.0F, 25F / (180F / Mth.PI), -10F / (180F / Mth.PI) ));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create()
						.texOffs(16, 0)
						.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F, 0.0F, 15F / (180F / Mth.PI), -45F / (180F / Mth.PI)));

		return LayerDefinition.create(mesh, 128, 32);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * ((float) Math.PI / 180F);
		this.head.xRot = z * ((float) Math.PI / 180F);
	}
	
	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		matrix.translate(0.0F, .25F, 0.0F);
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
