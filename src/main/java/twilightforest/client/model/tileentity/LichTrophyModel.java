package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class LichTrophyModel extends GenericTrophyModel {

	public final ModelPart head;
	public final ModelPart crown;

	public LichTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.crown = this.head.getChild("crown");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4F, -8F, -4F, 8, 8, 8),
		PartPose.offset(0F, -4F, 0F));

		head.addOrReplaceChild("crown",
				CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-4F, -8F, -4F, 8, 8, 8, new CubeDeformation(0.5F)),
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
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
