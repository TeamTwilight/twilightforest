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

public class HydraTrophyLegacyModel extends GenericTrophyModel {

	public final ModelPart head;
	public final ModelPart jaw;

	public HydraTrophyLegacyModel(ModelPart root) {
		this.head = root.getChild("head");
		this.jaw = head.getChild("jaw");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(272, 0)
						.addBox(-16.0F, -14.0F, -16.0F, 32, 24, 32)
						.texOffs(272, 56)
						.addBox(-15.0F, -2.0F, -40.0F, 30, 12, 24)
						.texOffs(272, 132)
						.addBox(-15.0F, 10.0F, -4.0F, 30, 8, 16)
						.texOffs(128, 200)
						.addBox(-2.0F, -30.0F, 4.0F, 4, 24, 24)
						.texOffs(272, 156)
						.addBox(-12.0F, 10.0F, -33.0F, 2, 5, 2)
						.texOffs(272, 156)
						.addBox(10.0F, 10.0F, -33.0F, 2, 5, 2)
						.texOffs(280, 156)
						.addBox(-8.0F, 9.0F, -33.0F, 16, 2, 2)
						.texOffs(280, 160)
						.addBox(-10.0F, 9.0F, -29.0F, 2, 2, 16)
						.texOffs(280, 160)
						.addBox(8.0F, 9.0F, -29.0F, 2, 2, 16),
				PartPose.offset(-16.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("jaw", CubeListBuilder.create()
						.texOffs(272, 92)
						.addBox(-15.0F, 0.0F, -16.0F, 30, 8, 32)
						.texOffs(272, 156)
						.addBox(-10.0F, -5.0F, -15.0F, 2, 5, 2)
						.texOffs(272, 156)
						.addBox(8.0F, -5.0F, -15.0F, 2, 5, 2)
						.texOffs(280, 156)
						.addBox(-8.0F, -1.0F, -15.0F, 16, 2, 2)
						.texOffs(280, 160)
						.addBox(-10.0F, -1.0F, -9.0F, 2, 2, 16)
						.texOffs(280, 160)
						.addBox(8.0F, -1.0F, -9.0F, 2, 2, 16),
				PartPose.offset(0.0F, 10.0F, -20.0F));

		head.addOrReplaceChild("frill", CubeListBuilder.create()
						.texOffs(272, 200)
						.addBox(-24.0F, -48.0F, 16.0F, 48, 48, 4),
				PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, -0.5235988F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 256);
	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
	}

	@Override
	public void openMouthForTrophy(float mouthOpen) {
		head.yRot = 0.0F;
		head.xRot = 0.0F;

		head.xRot -= mouthOpen * (Mth.PI / 12.0F);
		jaw.xRot = mouthOpen * (Mth.PI / 3.0F);
	}
	
	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.head.render(stack, consumer, light, overlay, red, green, blue, alpha);
	}
}
