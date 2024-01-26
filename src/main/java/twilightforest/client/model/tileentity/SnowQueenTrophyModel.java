package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SnowQueenTrophyModel extends GenericTrophyModel {

	public final ModelPart head;
	public final ModelPart crownFront;
	public final ModelPart crownBack;
	public final ModelPart crownRight;
	public final ModelPart crownLeft;

	public SnowQueenTrophyModel(ModelPart part) {
		this.head = part.getChild("head");
		this.crownRight = this.head.getChild("crown_right");
		this.crownBack = this.head.getChild("crown_back");
		this.crownLeft = this.head.getChild("crown_left");
		this.crownFront = this.head.getChild("crown_front");

	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);
		head.addOrReplaceChild("crown_right",
				CubeListBuilder.create()
						.texOffs(24, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
				PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.39269908169872414F, 1.5707963267948966F, 0.0F));
		head.addOrReplaceChild("crown_back",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, -6.0F, 4.0F, -0.39269908169872414F, 0.0F, 0.0F)
				);
		head.addOrReplaceChild("crown_left",
				CubeListBuilder.create()
						.texOffs(44, 4).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
				PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, -0.39269908169872414F, 1.5707963267948966F, 0.0F));
		head.addOrReplaceChild("crown_front",
				CubeListBuilder.create()
						.texOffs(24, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, -6.0F, -4.0F, 0.39269908169872414F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
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
