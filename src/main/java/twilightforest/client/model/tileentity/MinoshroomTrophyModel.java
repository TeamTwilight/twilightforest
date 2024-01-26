package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class MinoshroomTrophyModel extends GenericTrophyModel {

	public final ModelPart head;

	public MinoshroomTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F)
						.texOffs(0, 16).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 3.0F, 1.0F)
						.texOffs(32, 0).addBox(-8.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F)
						.texOffs(32, 5).addBox(-8.0F, -11.0F, -1.0F, 2.0F, 3.0F, 3.0F)
						.texOffs(46, 0).addBox(4.0F, -8.0F, -1.0F, 4.0F, 2.0F, 3.0F)
						.texOffs(46, 5).addBox(6.0F, -11.0F, -1.0F, 2.0F, 3.0F, 3.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
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
