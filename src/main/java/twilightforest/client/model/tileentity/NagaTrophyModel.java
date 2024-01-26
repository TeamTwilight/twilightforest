package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

//This model doesnt require a legacy as the tongue will only show up in newer versions
public class NagaTrophyModel extends GenericTrophyModel {
	public final ModelPart head;
	public final ModelPart tongue;

	public NagaTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.tongue = this.head.getChild("tongue");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F),
		PartPose.offset(0.0F, -4.0F, 0.0F));

		head.addOrReplaceChild("tongue",
				CubeListBuilder.create()
						.texOffs(42, 0)
						.addBox(-3.0F, -3.0F, -14.0F, 6.0F, 0.0F, 6.0F),
				PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
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
