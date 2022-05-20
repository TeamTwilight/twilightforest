package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ArcticArmorModel {

	public static MeshDefinition addPieces(CubeDeformation deformation) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("right_hood",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.0F, -2.0F, -1.0F, 1, 4, 1, new CubeDeformation(0.5F)),
				PartPose.offset(-2.5F, -3.0F, -5.0F));

		head.addOrReplaceChild("left_hood",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.0F, -2.0F, -1.0F, 1, 4, 1, new CubeDeformation(0.5F)),
				PartPose.offset(2.5F, -3.0F, -5.0F));

		head.addOrReplaceChild("top_hood",
				CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-2.0F, -1.0F, -1.0F, 4, 1, 1, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, -5.5F, -5.0F));

		head.addOrReplaceChild("bottom_hood",
				CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-2.0F, -1.0F, -1.0F, 4, 1, 1, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.5F, -5.0F));

		return meshdefinition;
	}
}
