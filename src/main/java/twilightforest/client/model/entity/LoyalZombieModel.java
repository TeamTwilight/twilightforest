package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.LoyalZombie;

/**
 * [VanillaCopy] {@link net.minecraft.client.model.AbstractZombieModel} due to generic restrictions
 */
public class LoyalZombieModel extends HumanoidModel<LoyalZombie> {

	public LoyalZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public void setupAnim(LoyalZombie e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(e, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		boolean flag = e.isAggressive();
		float f = Mth.sin(this.attackTime * (float)Math.PI);
		float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - f * 0.6F);
		this.leftArm.yRot = 0.1F - f * 0.6F;
		float f2 = -(float)Math.PI / (flag ? 1.5F : 2.25F);
		this.rightArm.xRot = f2;
		this.leftArm.xRot = f2;
		this.rightArm.xRot += f * 1.2F - f1 * 0.4F;
		this.leftArm.xRot += f * 1.2F - f1 * 0.4F;
		this.rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		// GREEEEN
		super.renderToBuffer(stack, builder, light, overlay, red * 0.25F, green, blue * 0.25F, scale);
	}
}
