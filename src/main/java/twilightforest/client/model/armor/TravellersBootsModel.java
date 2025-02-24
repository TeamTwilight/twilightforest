package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TravellersBootsModel {
	public static MeshDefinition addPieces(CubeDeformation deform) {
		MeshDefinition mesh = HumanoidModel.createMesh(deform, 0);
		PartDefinition root = mesh.getRoot();

		PartDefinition rightLeg = root.getChild("right_leg");
		rightLeg.addOrReplaceChild("right_bump",
			CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-0.5F, -1.0F, 0.0F, 4, 3, 1, deform),
			PartPose.offset(-1.5F, 10.0F, -3.75F)
		);

		PartDefinition leftLeg = root.getChild("left_leg");
		leftLeg.addOrReplaceChild("left_bump",
			CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-0.5F, -1.0F, 0.0F, 4, 3, 1, deform),
			PartPose.offset(-1.5F, 10.0F, -3.75F)
		);

		return mesh;
	}
}

