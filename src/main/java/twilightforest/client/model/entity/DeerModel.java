package twilightforest.client.model.entity;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.Set;

public class DeerModel extends QuadrupedModel<LivingEntityRenderState> {
	public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 8.0F, 4.0F, Set.of("head"));

	public DeerModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = QuadrupedModel.createBodyMesh(0, true, false, CubeDeformation.NONE);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 5)
				.addBox(-2.0F, -4.0F, -4.0F, 4.0F, 6.0F, 6.0F)
				.texOffs(52, 0)
				.addBox(-1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 3.0F),
			PartPose.offset(0.0F, 0.0F, -9.0F));

		head.addOrReplaceChild("left_antler", CubeListBuilder.create()
				.texOffs(20, 0)
				.addBox(-3.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
				.addBox(-3.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
				.addBox(-4.0F, -6.0F, 0.0F, 1.0F, 1.0F, 3.0F)
				.addBox(-5.0F, -7.0F, 2.0F, 1.0F, 1.0F, 5.0F)
				.addBox(-5.0F, -10.0F, 3.0F, 1.0F, 4.0F, 1.0F)
				.addBox(-6.0F, -13.0F, 4.0F, 1.0F, 4.0F, 1.0F)
				.addBox(-6.0F, -9.0F, 1.0F, 1.0F, 1.0F, 3.0F)
				.addBox(-6.0F, -10.0F, -2.0F, 1.0F, 1.0F, 4.0F)
				.addBox(-7.0F, -11.0F, -5.0F, 1.0F, 1.0F, 4.0F)
				.addBox(-6.0F, -12.0F, -8.0F, 1.0F, 1.0F, 4.0F)
				.addBox(-7.0F, -14.0F, 0.0F, 1.0F, 5.0F, 1.0F)
				.addBox(-6.0F, -15.0F, -5.0F, 1.0F, 5.0F, 1.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("right_antler", CubeListBuilder.create()
				.texOffs(20, 0)
				.addBox(1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F)
				.addBox(3.0F, -6.0F, 0.0F, 1.0F, 1.0F, 3.0F)
				.addBox(4.0F, -7.0F, 2.0F, 1.0F, 1.0F, 5.0F)
				.addBox(4.0F, -10.0F, 3.0F, 1.0F, 4.0F, 1.0F)
				.addBox(5.0F, -13.0F, 4.0F, 1.0F, 4.0F, 1.0F)
				.addBox(5.0F, -9.0F, 1.0F, 1.0F, 1.0F, 3.0F)
				.addBox(5.0F, -10.0F, -2.0F, 1.0F, 1.0F, 4.0F)
				.addBox(6.0F, -11.0F, -5.0F, 1.0F, 1.0F, 4.0F)
				.addBox(5.0F, -12.0F, -8.0F, 1.0F, 1.0F, 4.0F)
				.addBox(6.0F, -14.0F, 0.0F, 1.0F, 5.0F, 1.0F)
				.addBox(5.0F, -15.0F, -5.0F, 1.0F, 5.0F, 1.0F),
			PartPose.ZERO);

		var body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(36, 6)
				.addBox(-4.0F, -10.0F, -7.0F, 6.0F, 18.0F, 8.0F),
			PartPose.offsetAndRotation(1.0F, 5.0F, 2.0F, 1.570796F, 0.0F, 0.0F));

		body.addOrReplaceChild("neck", CubeListBuilder.create()
				.texOffs(10, 19)
				.addBox(-2.5F, -8.0F, -11.0F, 3.0F, 9.0F, 4.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 4.974188f, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
				.texOffs(0, 17)
				.addBox(-3.0F, 0.0F, -2.0F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(0.0F, 12.0F, 9.0F));

		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
				.texOffs(0, 17)
				.addBox(-1.0F, 0.0F, -2.0F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(2.0F, 12.0F, 9.0F));

		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
				.texOffs(0, 17)
				.addBox(-3.0F, 0.0F, -2.0F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(0.0F, 12.0F, -5.0F));

		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
				.texOffs(0, 17)
				.addBox(-1.0F, 0.0F, -2.0F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(2.0F, 12.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		this.head.getChild("right_antler").visible = !state.isBaby;
		this.head.getChild("left_antler").visible = !state.isBaby;
		super.setupAnim(state);
	}
}
