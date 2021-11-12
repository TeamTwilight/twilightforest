package twilightforest.client.model.entity;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.entity.monster.HarbingerCube;

public class HarbingerCubeModel<T extends HarbingerCube> extends QuadrupedModel<T> {

	public HarbingerCubeModel(ModelPart part) {
		super(part, false, 0.0F, 0.0F, 0.0F, 0.0F, 4); //All this is from AgeableModel. Do we scale?
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.ZERO);
		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F),
				PartPose.offset(0.0F, 0.0F, -2.0F));
		partRoot.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(-6F, 16F, 9F));
		partRoot.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(6F, 16F, 9F));
		partRoot.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(-9F, 16F, -14F));
		partRoot.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.offset(9F, 16F, -14F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.body.xRot = 0F;
	}
}
