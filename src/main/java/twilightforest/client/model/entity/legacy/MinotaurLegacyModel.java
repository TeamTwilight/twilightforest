package twilightforest.client.model.entity.legacy;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.Minotaur;

public class MinotaurLegacyModel extends HumanoidModel<Minotaur> {

	public MinotaurLegacyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partRoot = mesh.getRoot();

		var head = partRoot.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partRoot.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		head.addOrReplaceChild("snout", CubeListBuilder.create()
						.texOffs(9, 12)
						.addBox(-2.0F, -1.0F, -1.0F, 4, 3, 1),
				PartPose.offset(0F, -2.0F, -4F));

		var rightHorn = head.addOrReplaceChild("right_horn_1", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(-2.5F, -6.5F, 0.0F, 0.0F, -25F / (180F / Mth.PI), 10F / (180F / Mth.PI)));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create()
						.texOffs(40, 0)
						.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, -15F / (180F / Mth.PI), 45F / (180F / Mth.PI)));

		var leftHorn = head.addOrReplaceChild("left_horn_1", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3),
				PartPose.offsetAndRotation(2.5F, -6.5F, 0.0F, 0.0F, 25F / (180F / Mth.PI), -10F / (180F / Mth.PI)));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create()
						.texOffs(40, 0)
						.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2),
				PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F, 0.0F, 15F / (180F / Mth.PI), -45F / (180F / Mth.PI)));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
