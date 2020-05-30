package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import twilightforest.entity.EntityTFRisingZombie;

public class ModelTFRisingZombie<T extends EntityTFRisingZombie> extends ZombieModel<T> {

	public ModelTFRisingZombie() {
		super(0.0F, true);
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		stack.push();

		if (this.isChild) {
			stack.push();{
				stack.scale(0.75F, 0.75F, 0.75F);
				stack.translate(0.0F, 16.0F * scale, 0.0F);
				this.getHeadParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
				stack.pop();
				stack.push();
				stack.scale(0.5F, 0.5F, 0.5F);
				stack.translate(0.0F, 24.0F * scale, 0.0F);
				this.bipedBody.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedRightArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedLeftArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedHeadwear.render(stack, builder, light, overlay, red, green, blue, scale);
			}
			stack.pop();
			this.bipedRightLeg.render(stack, builder, light, overlay, red, green, blue, scale);
			this.bipedLeftLeg.render(stack, builder, light, overlay, red, green, blue, scale);
		} else {
			if (this.isSneaking) {
				stack.translate(0.0F, 0.2F, 0.0F);
			}

			/* todo 1.15 ageInTicks/the entity only provided to setAngles now, rework this entire render and move this transform there
			stack.translate(0F, (80F - Math.min(80F, ageInTicks)) / 80F, 0F);
			stack.translate(0F, (40F - Math.min(40F, Math.max(0F, ageInTicks - 80F))) / 40F, 0F);
				 */
			stack.push();
			{
				final float yOff = 1F;
				stack.translate(0, yOff, 0);
				/* todo 1.15 ageInTicks/the entity only provided to setAngles now, rework this entire render and move this transform there
				RenderSystem.rotatef(-120F * (80F - Math.min(80F, ageInTicks)) / 80F, 1F, 0F, 0F);
				RenderSystem.rotatef(30F * (40F - Math.min(40F, Math.max(0F, ageInTicks - 80F))) / 40F, 1F, 0F, 0F);
				 */
				stack.translate(0, -yOff, 0);
				this.getHeadParts().forEach((renderer) -> renderer.render(stack, builder, light, overlay, red, green, blue, scale));
				this.bipedBody.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedRightArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedLeftArm.render(stack, builder, light, overlay, red, green, blue, scale);
				this.bipedHeadwear.render(stack, builder, light, overlay, red, green, blue, scale);
			}
			stack.pop();
			this.bipedRightLeg.render(stack, builder, light, overlay, red, green, blue, scale);
			this.bipedLeftLeg.render(stack, builder, light, overlay, red, green, blue, scale);
		}

		stack.pop();
	}
}
