package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class TravellersGearModels {

	public static MeshDefinition addGogglePieces(CubeDeformation deform) {
		MeshDefinition mesh = HumanoidModel.createMesh(deform, 0);
		PartDefinition root = mesh.getRoot();

		root.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		return mesh;
	}

	public static MeshDefinition addGlovePieces(CubeDeformation deform, boolean slim) {
		MeshDefinition mesh = HumanoidModel.createMesh(deform, 0);
		PartDefinition root = mesh.getRoot();

		if (slim) {
			root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deform),
				PartPose.offset(-5.0F, 2.5F, 0.0F)
			);
			root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 0).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, deform),
				PartPose.offset(5.0F, 2.5F, 0.0F)
			);
		} else {
			root.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deform),
				PartPose.offset(-5.0F, 2.0F, 0.0F)
			);
			root.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deform),
				PartPose.offset(5.0F, 2.0F, 0.0F)
			);
		}

		return mesh;
	}

	public static MeshDefinition addBootPieces(CubeDeformation deform) {
		MeshDefinition mesh = HumanoidModel.createMesh(deform, 0);
		PartDefinition root = mesh.getRoot();

		PartDefinition rightLeg = root.getChild("right_leg");
		rightLeg.addOrReplaceChild("right_bump",
			CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-0.5F, -1.0F, 0.0F, 4, 3, 1, deform),
			PartPose.offset(-1.5F, 10.0F, -4.0F)
		);

		PartDefinition leftLeg = root.getChild("left_leg");
		leftLeg.addOrReplaceChild("left_bump",
			CubeListBuilder.create()
				.texOffs(24, 0)
				.mirror()
				.addBox(-0.5F, -1.0F, 0.0F, 4, 3, 1, deform),
			PartPose.offset(-1.5F, 10.0F, -4.0F)
		);

		return mesh;
	}
}

