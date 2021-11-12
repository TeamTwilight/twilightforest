package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
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
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		stack.pushPose();

		if (this.young) {
			stack.pushPose();{
				stack.scale(0.75F, 0.75F, 0.75F);
				stack.translate(0.0F, 16.0F * scale, 0.0F);
				this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
				stack.popPose();
				stack.pushPose();
				stack.scale(0.5F, 0.5F, 0.5F);
				stack.translate(0.0F, 24.0F * scale, 0.0F);
				this.body.render(stack, builder, light, overlay, red, green, blue, scale);
				this.rightArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.leftArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.hat.render(stack, builder, light, overlay, red, green, blue, scale);
			}
			stack.popPose();
			this.rightLeg.render(stack, builder, light, overlay, red, green, blue, scale);
			this.leftLeg.render(stack, builder, light, overlay, red, green, blue, scale);
		} else {
			if (this.crouching) {
				stack.translate(0.0F, 0.2F, 0.0F);
			}

			// todo 1.15 ageInTicks/the entity only provided to setRotationAngles now, rework this entire render and move this transform there
			stack.translate(0F, (80F - Math.min(80F, tick)) / 80F, 0F);
			stack.translate(0F, (40F - Math.min(40F, Math.max(0F, tick - 80F))) / 40F, 0F);
			stack.pushPose();
			{
				final float yOff = 1F;
				stack.translate(0, yOff, 0);
				// todo 1.15 ageInTicks/the entity only provided to setRotationAngles now, rework this entire render and move this transform there
				stack.mulPose(Vector3f.XP.rotationDegrees(-120F * (80F - Math.min(80F, tick)) / 80F));
				stack.mulPose(Vector3f.XP.rotationDegrees(30F * (40F - Math.min(40F, Math.max(0F, tick - 80F))) / 40F));
				stack.translate(0, -yOff, 0);
				this.headParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
				this.body.render(stack, builder, light, overlay, red, green, blue, scale);
				this.rightArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.leftArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.hat.render(stack, builder, light, overlay, red, green, blue, scale);
			}
			stack.popPose();
			this.rightLeg.render(stack, builder, light, overlay, red, green, blue, scale);
			this.leftLeg.render(stack, builder, light, overlay, red, green, blue, scale);
		}

		stack.popPose();
	}

	@Override
	protected void setupAttackAnimation(RisingZombie p_102858_, float p_102859_) {
		super.setupAttackAnimation(p_102858_, p_102859_);
		tick = p_102859_ + Minecraft.getInstance().getDeltaFrameTime();
	}
}
