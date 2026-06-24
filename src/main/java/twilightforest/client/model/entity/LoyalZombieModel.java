package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.jetbrains.annotations.NotNull;

public class LoyalZombieModel extends HumanoidModel<LoyalZombieModel.@NotNull LoyalZombieRenderState> {
	public LoyalZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public void setupAnim(LoyalZombieRenderState state) {
		super.setupAnim(state);
		boolean flag = state.isAggressive;
		float f = Mth.sin(state.attackTime * Mth.PI);
		float f1 = Mth.sin((1.0F - (1.0F - state.attackTime) * (1.0F - state.attackTime)) * Mth.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - f * 0.6F);
		this.leftArm.yRot = 0.1F - f * 0.6F;
		float f2 = -Mth.PI / (flag ? 1.5F : 2.25F);
		this.rightArm.xRot = f2;
		this.leftArm.xRot = f2;
		this.rightArm.xRot += f * 1.2F - f1 * 0.4F;
		this.leftArm.xRot += f * 1.2F - f1 * 0.4F;
		AnimationUtils.bobArms(this.rightArm, this.leftArm, state.ageScale);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		int greenColor = ARGB.color(ARGB.alpha(color), (int) (ARGB.red(color) * 0.25F), ARGB.green(color), (int) (ARGB.blue(color) * 0.25F));
		super.renderToBuffer(stack, builder, light, overlay, greenColor);
	}

	public static class LoyalZombieRenderState extends HumanoidRenderState {
		public boolean isAggressive;
	}
}
