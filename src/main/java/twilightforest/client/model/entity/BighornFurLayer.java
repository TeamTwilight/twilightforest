package twilightforest.client.model.entity;

import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.passive.Bighorn;

public class BighornFurLayer extends SheepFurModel<Bighorn> {
	public BighornFurLayer(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.6F)),
				PartPose.offset(0.0F, 6.0F, -8.0F));
		definition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(28, 8)
						.addBox(-4.0F, -9.0F, -7.0F, 8.0F, 15.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 5F, 2.0F));

		CubeListBuilder legOffset = CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.4F));

		definition.addOrReplaceChild("right_hind_leg", legOffset, PartPose.offset(-3.0F, 12.0F, 7.0F));
		definition.addOrReplaceChild("left_hind_leg", legOffset, PartPose.offset(3.0F, 12.0F, 7.0F));
		definition.addOrReplaceChild("right_front_leg", legOffset, PartPose.offset(-3.0F, 12.0F, -5.0F));
		definition.addOrReplaceChild("left_front_leg", legOffset, PartPose.offset(3.0F, 12.0F, -5.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
