package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.entity.monster.BlockChainGoblin;

public class BlockChainGoblinModel<T extends BlockChainGoblin> extends FixedHumanoidModel<T> {

	public BlockChainGoblinModel(ModelPart root) {
		super(root, 3.0F);
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F),
			PartPose.offset(0.0F, 10.0F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		head.addOrReplaceChild("helmet", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-2.5F, -8.0F, -2.5F, 5.0F, 9.0F, 5.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(-3.5F, 11.0F, -2.0F, 7.0F, 7.0F, 4.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(20, 0)
				.addBox(-3.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(-3.5F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(20, 0)
				.addBox(0.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(3.5F, 12.0F, 1.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(20, 15)
				.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
			PartPose.offset(-2.0F, 18.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(20, 15)
				.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
			PartPose.offset(2.0F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(52, 2)
				.addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offsetAndRotation(-5.0F, 12.0F, 0.0F, 0.0F, 0.0F, 3.0543261909900767F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(52, 17)
				.addBox(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offsetAndRotation(5.0F, 12.0F, 0.0F, 0.0F, 0.0F, -3.0543261909900767F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 33)
				.addBox(-1.4F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
			PartPose.offset(-2.0F, 18.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(12, 33)
				.addBox(-1.6F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F),
			PartPose.offset(2.0F, 18.0F, 0.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(28, 6)
				.addBox(-3.5F, 1.0F, -2.0F, 7.0F, 6.0F, 4.0F),
			PartPose.offset(0.0F, 11.0F, 0.0F));

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 18)
				.addBox(-7.5F, -9.0F, -2.03F, 15.0F, 10.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -0.7853981633974483F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		head.addOrReplaceChild("helm", CubeListBuilder.create()
				.texOffs(0, 5)
				.addBox(-2.5F, -7.0F, -2.5F, 5.0F, 8.0F, 5.0F),
			PartPose.rotation(0.0F, 0.7853981633974483F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 48);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.rightArm.xRot += Mth.PI;
		this.leftArm.xRot += Mth.PI;
	}
}
