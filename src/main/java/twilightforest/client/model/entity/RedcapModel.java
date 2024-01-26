package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.monster.Redcap;

public class RedcapModel<T extends Redcap> extends FixedHumanoidModel<T> {

	public RedcapModel(ModelPart root) {
		super(root, 3.0F);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5F, -8.0F, -3.5F, 7.0F, 7.0F, 7.0F)
						.texOffs(0, 0)
						.addBox(-4.5F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F)
						.texOffs(0, 0).mirror()
						.addBox(-5.5F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F).mirror(false)
						.texOffs(0, 0).mirror()
						.addBox(3.5F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F).mirror(false)
						.texOffs(0, 0)
						.addBox(4.5F, -6.0F, -0.5F, 1.0F, 2.0F, 1.0F),
				PartPose.offset(0.0F, 8.0F, 0.0F));

		definition.addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(32, 0)
						.addBox(-2.0F, -8.5F, -3.0F, 4.0F, 5.0F, 7.0F),
				PartPose.offset(0.0F, 6.0F, 0.0F));

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(12, 19)
						.addBox(-4.0F, 1.0F, -2.0F, 8.0F, 9.0F, 4.0F),
				PartPose.offset(0.0F, 5.0F, 0.0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(36, 17).mirror()
						.addBox(-3.0F, -1.0F, -1.5F, 3.0F, 12.0F, 3.0F).mirror(false),
				PartPose.offset(-4.0F, 7.0F, 0.0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(36, 17)
						.addBox(0.0F, -1.0F, -1.5F, 3.0F, 12.0F, 3.0F),
				PartPose.offset(4.0F, 7.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 20).mirror()
						.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F).mirror(false),
				PartPose.offset(-2.5F, 15.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 20)
						.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 9.0F, 3.0F),
				PartPose.offset(2.5F, 15.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}