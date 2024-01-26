package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class QuestRamTrophyModel extends GenericTrophyModel {

	public final ModelPart horns;
	public final ModelPart head;
	
	public QuestRamTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.horns = this.head.getChild("horns");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(74, 70).addBox(-6.0F, -4.0F, -10.0F, 12.0F, 8.0F, 15.0F)
						.texOffs(42, 71).addBox(-6.0F, -7.0F, -6.0F, 12.0F, 3.0F, 11.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		head.addOrReplaceChild("horns",
				CubeListBuilder.create()
						.texOffs(64, 0).addBox(-9.0F, -6.0F, -1.0F, 4.0F, 10.0F, 10.0F)
						.texOffs(48, 0).addBox(-13.0F, -6.0F, 5.0F, 4.0F, 4.0F, 4.0F)
						.texOffs(92, 0).addBox(5.0F, -6.0F, -1.0F, 4.0F, 10.0F, 10.0F)
						.texOffs(110, 0).addBox(9.0F, -6.0F, 5.0F, 4.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.4363323129985824F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
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
