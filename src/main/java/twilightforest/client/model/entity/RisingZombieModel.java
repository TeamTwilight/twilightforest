package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import twilightforest.entity.monster.RisingZombie;

public class RisingZombieModel extends ZombieModel<RisingZombie> {

	private float tick;

	public RisingZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		if (this.young) {
			super.renderToBuffer(stack, consumer, light, overlay, red, green, blue, alpha);
		} else {
			if (this.crouching) {
				stack.translate(0.0F, 0.2F, 0.0F);
			}

			stack.translate(0.0F, (80.0F - Math.min(80.0F, this.tick)) / 80.0F, 0.0F);
			stack.translate(0.0F, (40.0F - Math.min(40.0F, Math.max(0.0F, this.tick - 80.0F))) / 40.0F, 0.0F);
			stack.pushPose();
			final float yOff = 1.0F;
			stack.translate(0.0F, yOff, 0.0F);
			stack.mulPose(Axis.XP.rotationDegrees(-120.0F * (80.0F - Math.min(80.0F, this.tick)) / 80.0F));
			stack.mulPose(Axis.XP.rotationDegrees(30.0F * (40.0F - Math.min(40.0F, Math.max(0.0F, this.tick - 80.0F))) / 40.0F));
			stack.translate(0.0F, -yOff, 0.0F);
			this.headParts().forEach((renderer) -> renderer.render(stack, consumer, light, overlay, red, green, blue, alpha));
			this.body.render(stack, consumer, light, overlay, red, green, blue, alpha);
			this.rightArm.render(stack, consumer, light, overlay, red, green, blue, alpha);
			this.leftArm.render(stack, consumer, light, overlay, red, green, blue, alpha);
			this.hat.render(stack, consumer, light, overlay, red, green, blue, alpha);
			stack.popPose();
			this.rightLeg.render(stack, consumer, light, overlay, red, green, blue, alpha);
			this.leftLeg.render(stack, consumer, light, overlay, red, green, blue, alpha);
		}
	}

	@Override
	protected void setupAttackAnimation(RisingZombie zombie, float ageInTicks) {
		super.setupAttackAnimation(zombie, ageInTicks);
		this.tick = ageInTicks + Minecraft.getInstance().getDeltaFrameTime();
	}
}
