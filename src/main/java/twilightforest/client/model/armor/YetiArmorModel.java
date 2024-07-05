package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class YetiArmorModel extends TFArmorModel {

	public YetiArmorModel(ModelPart part) {
		super(part);
	}

	public static MeshDefinition addPieces(CubeDeformation deformation) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		//bigger head
		var head = partdefinition.addOrReplaceChild("head",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.5F, -8.0F, -4.0F, 9, 8, 8, deformation),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		// add horns
		addPairHorns(head, 1, -8.0F, 35F);
		addPairHorns(head, 2, -6.0F, 15F);
		addPairHorns(head, 3, -4.0F, -5F);

		partdefinition.getChild("right_leg").addOrReplaceChild("right_ruff",
			CubeListBuilder.create()
				.texOffs(44, 0)
				.addBox(-2.5F, 0.0F, -2.5F, 5, 2, 5, deformation),
			PartPose.offset(0.0F, 6.0F, 0.0F));


		partdefinition.getChild("left_leg").addOrReplaceChild("left_ruff",
			CubeListBuilder.create()
				.texOffs(44, 0)
				.addBox(-2.5F, 0.0F, -2.5F, 5, 2, 5, deformation),
			PartPose.offset(0.0F, 6.0F, 0.0F));


		partdefinition.getChild("right_leg").addOrReplaceChild("right_toe",
			CubeListBuilder.create()
				.texOffs(26, 0)
				.addBox(-2.0F, 0.0F, -1.0F, 4, 2, 1, deformation.extend(-0.01F)),
			PartPose.offset(0.0F, 10.0F, -2.0F));


		partdefinition.getChild("left_leg").addOrReplaceChild("left_toe",
			CubeListBuilder.create()
				.texOffs(26, 0)
				.addBox(-2.0F, 0.0F, -1.0F, 4, 2, 1, deformation.extend(-0.01F)),
			PartPose.offset(0.0F, 10.0F, -2.0F));

		partdefinition.addOrReplaceChild("right_arm",
			CubeListBuilder.create()
				.texOffs(40, 16)
				.addBox(-3.0F, -2.0F, -2.0F, 4, 10, 4, deformation),
			PartPose.offset(-5.0F, 2.0F + 0.0f, 0.0F));

		partdefinition.addOrReplaceChild("left_arm",
			CubeListBuilder.create()
				.texOffs(40, 16).mirror()
				.addBox(-1.0F, -2.0F, -2.0F, 4, 10, 4, deformation),
			PartPose.offset(5.0F, 2.0F + 0.0f, 0.0F));

		return meshdefinition;
	}

	private static void addPairHorns(PartDefinition partdefinition, int iter, float height, float zangle) {

		var leftBottom = partdefinition.addOrReplaceChild("horn_" + iter + "_left_bottom",
			CubeListBuilder.create()
				.texOffs(52, 8)
				.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3),
			PartPose.offsetAndRotation(-4.5F, height, -1.0F,
				0.0F, -30F / (180F / (float) Math.PI), zangle / (180F / (float) Math.PI)));

		leftBottom.addOrReplaceChild("horn_" + iter + "_left_top",
			CubeListBuilder.create()
				.texOffs(34, 10)
				.addBox(-4.0F, -1.0F, -1.0F, 5, 2, 2),
			PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F,
				0.0F, -20F / (180F / (float) Math.PI), zangle / (180F / (float) Math.PI)));

		var rightBottom = partdefinition.addOrReplaceChild("horn_" + iter + "_right_bottom",
			CubeListBuilder.create()
				.texOffs(52, 8)
				.addBox(0.0F, -1.5F, -1.5F, 3, 3, 3),
			PartPose.offsetAndRotation(4.5F, height, -1.0F,
				0.0F, 30F / (180F / (float) Math.PI), -zangle / (180F / (float) Math.PI)));

		rightBottom.addOrReplaceChild("horn_" + iter + "_right_top",
			CubeListBuilder.create()
				.texOffs(34, 10)
				.addBox(-1.0F, -1.0F, -1.0F, 5, 2, 2),
			PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F,
				0.0F, 20F / (180F / (float) Math.PI), -zangle / (180F / (float) Math.PI)));
	}
}
