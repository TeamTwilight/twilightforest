package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import twilightforest.entity.monster.UpperGoblinKnight;

public class UpperGoblinKnightRenderer<T extends UpperGoblinKnight, M extends HumanoidModel<T>> extends TFBipedRenderer<T, M> {
	public UpperGoblinKnightRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize, "doublegoblin.png");
	}

	@Override
	protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
		super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTicks, scale);

		if (entity.heavySpearTimer > 0) {
			stack.mulPose(Axis.XP.rotationDegrees(this.getPitchForAttack((60.0F - entity.heavySpearTimer) + partialTicks)));
		}
	}

	/**
	 * Figure out what pitch the goblin should be at depending on where it's at on the timer
	 */
	private float getPitchForAttack(float attackTime) {
		if (attackTime <= 10.0F) {
			// rock back
			return attackTime * 3.0F;
		}
		if (attackTime > 10.0F && attackTime <= 30.0F) {
			// hang back
			return 30.0F;
		}
		if (attackTime > 30.0F && attackTime <= 33.0F) {
			// slam forward
			return (attackTime - 30.0F) * -25.0F + 30.0F;
		}
		if (attackTime > 33.0F && attackTime <= 50.0F) {
			// stay forward
			return -45.0F;
		}
		if (attackTime > 50.0F && attackTime <= 60.0F) {
			// back to normal
			return (10.0F - (attackTime - 50.0F)) * -4.5F;
		}

		return 0.0F;
	}
}
