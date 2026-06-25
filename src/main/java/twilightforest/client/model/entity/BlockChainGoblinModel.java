package twilightforest.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.client.state.entity.BlockChainGoblinRenderState;

public class BlockChainGoblinModel extends HumanoidModel<BlockChainGoblinRenderState> {

	public BlockChainGoblinModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F),
			PartPose.offset(0.0F, 10.0F, 0.0F));

		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

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

	@Override
	public void setupAnim(BlockChainGoblinRenderState state) {
		super.setupAnim(state);

		this.rightArm.xRot += Mth.PI;
		this.leftArm.xRot += Mth.PI;
	}
}
