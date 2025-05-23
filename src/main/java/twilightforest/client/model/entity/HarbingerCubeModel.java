package twilightforest.client.model.entity;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class HarbingerCubeModel extends QuadrupedModel<LivingEntityRenderState> {

	public HarbingerCubeModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(),
			PartPose.ZERO);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F),
			PartPose.offset(0.0F, 0.0F, -2.0F));
		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(-6F, 16F, 9F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(6F, 16F, 9F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(-9F, 16F, -14F));
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(9F, 16F, -14F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}
}
