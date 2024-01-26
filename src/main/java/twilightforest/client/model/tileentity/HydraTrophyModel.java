package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class HydraTrophyModel extends GenericTrophyModel {

	public final ModelPart head;
	public final ModelPart plate;
	public final ModelPart mouth;

	public HydraTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.plate = this.head.getChild("plate");
		this.mouth = this.head.getChild("mouth");
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
						.texOffs(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
						.texOffs(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
				PartPose.ZERO);

		head.addOrReplaceChild("plate",
				CubeListBuilder.create()
						.texOffs(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
						.texOffs(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
				PartPose.rotation(-0.7853981633974483F, 0.0F, 0.0F));

		head.addOrReplaceChild("mouth",
				CubeListBuilder.create()
						.texOffs(240, 162)
						.addBox(-15.0F, -2.0F, -24.0F, 30.0F, 8.0F, 24.0F),
				PartPose.offset(0.0F, 10.0F, -14.0F));

		return LayerDefinition.create(meshdefinition, 512, 256);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * ((float) Math.PI / 180F);
		this.head.xRot = z * ((float) Math.PI / 180F);
	}
	
	public void openMouthForTrophy(float mouthOpen) {
		this.head.yRot = 0;
		this.head.xRot = 0;

		this.head.xRot -= (float) (mouthOpen * (Math.PI / 12.0));
		this.mouth.xRot = (float) (mouthOpen * (Math.PI / 3.0));
	}
	
	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
