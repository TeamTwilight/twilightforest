package twilightforest.client.model.entity.legacy;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.passive.Boar;

public class BoarLegacyModel<T extends Boar> extends PigModel<T> {

	public BoarLegacyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = PigModel.createBodyMesh(0, CubeDeformation.NONE);
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(-4.0F, -2.0F, -6.0F, 8.0F, 7.0F, 6.0F)
						.texOffs(28, 0)
						.addBox(-3.0F, 1.0F, -9.0F, 6.0F, 4.0F, 3.0F)
						.texOffs(17, 17)
						.addBox(3.0F, 2.0F, -9.0F, 1.0F, 2.0F, 1.0F)
						.texOffs(17, 17)
						.addBox(-4.0F, 2.0F, -9.0F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 12.0F, -6.0F));

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(28, 10)
						.addBox(-5.0F, -8.0F, -7.0F, 10.0F, 14.0F, 8.0F),
				PartPose.offsetAndRotation(0.0F, 11.0F, 2.0F, 1.5707963267948966F, 0.0F, 0.0F));

		partRoot.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
				PartPose.offset(-3.0F, 18.0F, 7.0F));

		partRoot.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
				PartPose.offset(3.0F, 18.0F, 7.0F));

		partRoot.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
				PartPose.offset(-3.0F, 18.0F, -5.0F));

		partRoot.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F),
				PartPose.offset(3.0F, 18.0F, -5.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
