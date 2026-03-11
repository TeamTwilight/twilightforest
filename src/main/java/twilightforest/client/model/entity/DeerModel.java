package twilightforest.client.model.entity;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.entity.passive.Deer;

public class DeerModel extends QuadrupedModel<Deer> {

	public DeerModel(ModelPart root) {
		super(root, true, 15.25F, 4.0F, 2.0F, 2.0F, 24);
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
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

	private static LayerDefinition createJappaModel() {
		MeshDefinition mesh = QuadrupedModel.createBodyMesh(12, CubeDeformation.NONE);
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(24, 2)
				.addBox(-2.0F, -4.0F, -4.0F, 4.0F, 6.0F, 6.0F)
				.texOffs(52, 0)
				.addBox(-1.5F, -1.0F, -7.0F, 3.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, -0.4363323129985824F, 0.0F, 0.0F));

		head.addOrReplaceChild("right_antler", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F),
			PartPose.offsetAndRotation(-1.0F, -4.0F, 0.0F, 0.0F, -0.39269908169872414F, -0.39269908169872414F));

		head.addOrReplaceChild("left_antler", CubeListBuilder.create()
				.texOffs(32, 16)
				.addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F),
			PartPose.offsetAndRotation(1.0F, -4.0F, 0.0F, 0.0F, 0.39269908169872414F, 0.39269908169872414F));

		var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(36, 6)
				.addBox(-3.0F, -14.0F, -2.0F, 6.0F, 18.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, 10.0F, 7.0F, 1.5707963267948966F, 0.0F, 0.0F));

		body.addOrReplaceChild("neck", CubeListBuilder.create()
				.texOffs(22, 14)
				.addBox(-2.5F, -8.0F, -11.0F, 3.0F, 9.0F, 4.0F),
			PartPose.offsetAndRotation(1.0F, -4.0F, 5.0F, 4.974188f, 0.0F, 0.0F));

		definition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create()
				.texOffs(0, 15)
				.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(-2.0F, 12.0F, 9.5F));

		definition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create()
				.texOffs(10, 15)
				.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(2.0F, 12.0F, 9.5F));

		definition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(-2.0F, 12.0F, -4.5F));

		definition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
				.texOffs(10, 0)
				.addBox(-1.0F, 0.0F, -1.5F, 2.0F, 12.0F, 3.0F),
			PartPose.offset(2.0F, 12.0F, -4.5F));

		return LayerDefinition.create(mesh, 64, 48);
	}

	@Override
	public void prepareMobModel(Deer entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.head.getChild("right_antler").visible = !entity.isBaby();
		this.head.getChild("left_antler").visible = !entity.isBaby();
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
	}
}
