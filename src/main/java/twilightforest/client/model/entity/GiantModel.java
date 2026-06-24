package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.HumanoidArm;
import twilightforest.client.state.entity.GiantRenderState;

public class GiantModel extends HumanoidModel<GiantRenderState> {
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart leftPants;
	public final ModelPart rightPants;
	public final ModelPart jacket;
	private final boolean slim;

	public GiantModel(ModelPart root, boolean slim) {
		super(root, RenderTypes::entityTranslucent);
		this.slim = slim;
		this.leftSleeve = this.leftArm.getChild("left_sleeve");
		this.rightSleeve = this.rightArm.getChild("right_sleeve");
		this.leftPants = this.leftLeg.getChild("left_pants");
		this.rightPants = this.rightLeg.getChild("right_pants");
		this.jacket = this.body.getChild("jacket");
	}

	@Override
	public void setupAnim(GiantRenderState state) {
		this.hat.visible = state.showHat;
		this.jacket.visible = state.showJacket;
		this.leftPants.visible = state.showLeftPants;
		this.rightPants.visible = state.showRightPants;
		this.leftSleeve.visible = state.showLeftSleeve;
		this.rightSleeve.visible = state.showRightSleeve;
		super.setupAnim(state);
	}

	public void setAllVisible(boolean visible) {
		this.leftSleeve.visible = visible;
		this.rightSleeve.visible = visible;
		this.leftPants.visible = visible;
		this.rightPants.visible = visible;
		this.jacket.visible = visible;
	}

	@Override
	public void translateToHand(GiantRenderState renderState, HumanoidArm side, PoseStack poseStack) {
		this.root().translateAndRotate(poseStack);
		ModelPart modelpart = this.getArm(side);
		if (this.slim) {
			float f = 0.5F * (float) (side == HumanoidArm.RIGHT ? 1 : -1);
			modelpart.x += f;
			modelpart.translateAndRotate(poseStack);
			modelpart.x -= f;
		} else {
			modelpart.translateAndRotate(poseStack);
		}
	}
}
