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

public class SnowQueenTrophyLegacyModel extends GenericTrophyModel {

	public final ModelPart head;

	public SnowQueenTrophyLegacyModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		var crown = head.addOrReplaceChild("crown", CubeListBuilder.create(),
				PartPose.ZERO);

		makeFrontCrown(crown, -1, -4, 10F, 0);
		makeFrontCrown(crown, 0, 4, -10F, 1);
		makeSideCrown(crown, -1, -4, 10F, 0);
		makeSideCrown(crown, 0, 4, -10F, 1);

		return LayerDefinition.create(mesh, 64, 32);
	}

	private static void makeSideCrown(PartDefinition parent, float spikeDepth, float crownX, float angle, int iteration) {

		var crownSide = parent.addOrReplaceChild("crown_side_" + iteration, CubeListBuilder.create()
						.texOffs(28, 28)
						.addBox(-3.5F, -0.5F, -0.5F, 7, 1, 1),
				PartPose.offsetAndRotation(crownX, -6.0F, 0.0F, 0.0F, Mth.PI / 2.0F, 0.0F));

		crownSide.addOrReplaceChild("spike_4", CubeListBuilder.create()
						.texOffs(48, 27)
						.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F / 180F * Mth.PI, 0.0F, 0.0F));

		crownSide.addOrReplaceChild("spike_3l", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, -10F / 180F * Mth.PI));

		crownSide.addOrReplaceChild("spike_3r", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, 10F / 180F * Mth.PI));

	}

	private static void makeFrontCrown(PartDefinition parent, float spikeDepth, float crownZ, float angle, int iteration) {

		var crownFront = parent.addOrReplaceChild("crown_front_" + iteration, CubeListBuilder.create()
						.texOffs(28, 30)
						.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1),
				PartPose.offset(0.0F, -6.0F, crownZ));

		crownFront.addOrReplaceChild("spike_4", CubeListBuilder.create()
						.texOffs(48, 27)
						.addBox(-0.5F, -3.5F, spikeDepth, 1, 4, 1),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F / 180F * Mth.PI, 0.0F, 0.0F));

		crownFront.addOrReplaceChild("spike_3l", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, -10F / 180F * 3.14159F));

		crownFront.addOrReplaceChild("spike_3r", CubeListBuilder.create()
						.texOffs(52, 28)
						.addBox(-0.5F, -2.5F, spikeDepth, 1, 3, 1),
				PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle / 180F * Mth.PI, 0.0F, 10F / 180F * 3.14159F));

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
