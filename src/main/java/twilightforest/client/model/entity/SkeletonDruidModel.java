package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.monster.SkeletonDruid;

public class SkeletonDruidModel extends SkeletonModel<SkeletonDruid> {
	private final ModelPart dress;

	public SkeletonDruidModel(ModelPart root) {
		super(root);

		this.dress = root.getChild("dress");
	}

	public static LayerDefinition create(CubeDeformation deformation) {
		MeshDefinition mesh = SkeletonModel.createMesh(deformation, 0);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(8, 16)
						.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
				PartPose.ZERO);

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, deformation),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, deformation),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 2.0F, 12.0F, 2.0F, deformation),
				PartPose.offset(3.0F, 12.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 2.0F, 12.0F, 2.0F, deformation),
				PartPose.offset(-1.0F, 12.0F, 0.0F));

		definition.addOrReplaceChild("dress", CubeListBuilder.create()
						.texOffs(32, 16)
						.addBox(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}


	public static LayerDefinition create() {
		return create(CubeDeformation.NONE);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.dress));
	}
}
