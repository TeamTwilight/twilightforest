package twilightforest.client.model.entity;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;

public class SkeletonDruidModel extends SkeletonModel<SkeletonRenderState> {

	public SkeletonDruidModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = SkeletonModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(8, 16)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-2.0F, 0.0F, -2.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(3.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-2.0F, 0.0F, -2.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-1.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("dress", CubeListBuilder.create()
				.texOffs(32, 16)
				.addBox(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}
