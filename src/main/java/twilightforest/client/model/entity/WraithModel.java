package twilightforest.client.model.entity;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class WraithModel extends HumanoidModel<HumanoidRenderState> {

	public WraithModel(ModelPart root) {
		super(root, RenderTypes::entityTranslucent);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("dress", CubeListBuilder.create()
				.texOffs(40, 16)
				.addBox(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(HumanoidRenderState entity) {
		super.setupAnim(entity);

		float var8 = Mth.sin(entity.attackTime * Mth.PI);
		float var9 = Mth.sin((1.0F - (1.0F - entity.attackTime) * (1.0F - entity.attackTime)) * Mth.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - var8 * 0.6F);
		this.leftArm.yRot = 0.1F - var8 * 0.6F;
		this.rightArm.xRot = -Mth.HALF_PI;
		this.leftArm.xRot = -Mth.HALF_PI;
		this.rightArm.xRot -= var8 * 1.2F - var9 * 0.4F;
		this.leftArm.xRot -= var8 * 1.2F - var9 * 0.4F;
		AnimationUtils.bobArms(this.rightArm, this.leftArm, entity.ageInTicks);
	}
}
