package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class TravelArmorModel extends HumanoidModel<LivingEntity> {

	public TravelArmorModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer(CubeDeformation deformation) {
		// Create a basic Humanoid model definition
		MeshDefinition mesh = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition root = mesh.getRoot();

		// HEAD + optional goggles
		PartDefinition head = root.addOrReplaceChild(
			"head",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4, -8, -4, 8, 8, 8, deformation),
			PartPose.ZERO
		);
		head.addOrReplaceChild(
			"goggles",
			CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4, -4, -4.5F, 8, 4, 1, deformation),
			PartPose.ZERO
		);

		// BODY
		PartDefinition body = root.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-4, 0, -2, 8, 12, 4, deformation),
			PartPose.ZERO
		);

		// Remove this entirely if you don’t want a belt
		// (often it’s what shows up “floating” on scaled or non-humanoid entities)
		body.addOrReplaceChild(
			"belt",
			CubeListBuilder.create()
				.texOffs(0, 32)
				// Tweak the Y/Z to align better with your model
				.addBox(-4, 10, -2, 8, 4, 1, deformation),
			PartPose.ZERO
		);

		// WINGS
		PartDefinition wings = body.addOrReplaceChild(
			"wings",
			CubeListBuilder.create(), // no direct cubes here, just sub-parts
			PartPose.offset(0, 0, 2.0F)
		);
		wings.addOrReplaceChild(
			"left_wing",
			CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(0, -4, 0, 10, 16, 1, deformation),
			PartPose.offsetAndRotation(2.0F, 2.0F, 0.0F, 0.0F, 5.0F * Mth.DEG_TO_RAD, 0.0F)
		);
		wings.addOrReplaceChild(
			"right_wing",
			CubeListBuilder.create()
				.texOffs(40, 0).mirror()
				.addBox(-10, -4, 0, 10, 16, 1, deformation),
			PartPose.offsetAndRotation(-2.0F, 2.0F, 0.0F, 0.0F, -5.0F * Mth.DEG_TO_RAD, 0.0F)
		);

		// ARMS
		root.addOrReplaceChild(
			"right_arm",
			CubeListBuilder.create()
				.texOffs(40, 16)
				.addBox(-3, -2, -2, 4, 12, 4, deformation),
			PartPose.offset(-5.0F, 2.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"left_arm",
			CubeListBuilder.create()
				.texOffs(40, 16).mirror()
				.addBox(-1, -2, -2, 4, 12, 4, deformation),
			PartPose.offset(5.0F, 2.0F, 0.0F)
		);

		// LEGS
		PartDefinition rightLeg = root.addOrReplaceChild(
			"right_leg",
			CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-2, 0, -2, 4, 12, 4, deformation),
			PartPose.offset(-1.9F, 12.0F, 0.0F)
		);
		PartDefinition leftLeg = root.addOrReplaceChild(
			"left_leg",
			CubeListBuilder.create()
				.texOffs(0, 16).mirror()
				.addBox(-2, 0, -2, 4, 12, 4, deformation),
			PartPose.offset(1.9F, 12.0F, 0.0F)
		);

		// Remove or adjust these to avoid “floating squares”
		rightLeg.addOrReplaceChild(
			"right_boot_bump",
			CubeListBuilder.create()
				.texOffs(16, 32)
				.addBox(-2, 9, -3, 4, 3, 1, deformation),
			PartPose.ZERO // or PartPose.offset(0, 0, 0.5F) if you need small adjustment
		);
		leftLeg.addOrReplaceChild(
			"left_boot_bump",
			CubeListBuilder.create()
				.texOffs(16, 32).mirror()
				.addBox(-2, 9, -3, 4, 3, 1, deformation),
			PartPose.ZERO
		);

		// Build the final layer definition
		return LayerDefinition.create(mesh, 64, 32);
	}
}
