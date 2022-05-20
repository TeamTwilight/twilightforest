package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import twilightforest.client.model.entity.legacy.UpperGoblinKnightLegacyModel;
import twilightforest.client.renderer.entity.TFBipedRenderer;
import twilightforest.entity.monster.UpperGoblinKnight;

public class LegacyUpperGoblinKnightRenderer extends TFBipedRenderer<UpperGoblinKnight, UpperGoblinKnightLegacyModel> {
	public LegacyUpperGoblinKnightRenderer(EntityRendererProvider.Context manager, UpperGoblinKnightLegacyModel model, float shadowSize) {
		super(manager, model, shadowSize, "doublegoblin.png");
	}

	@Override
	protected void setupRotations(UpperGoblinKnight upperKnight, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
		super.setupRotations(upperKnight, stack, ageInTicks, rotationYaw, partialTicks);

		if (upperKnight.heavySpearTimer > 0) {
			stack.mulPose(Vector3f.XP.rotationDegrees(getPitchForAttack((60 - upperKnight.heavySpearTimer) + partialTicks)));
		}
	}

	/**
	 * Figure out what pitch the goblin should be at depending on where it's at on the the timer
	 */
	private float getPitchForAttack(float attackTime) {
		if (attackTime <= 10) {
			// rock back
			return attackTime * 3.0F;
		}
		if (attackTime > 10 && attackTime <= 30) {
			// hang back
			return 30F;
		}
		if (attackTime > 30 && attackTime <= 33) {
			// slam forward
			return (attackTime - 30) * -25F + 30F;
		}
		if (attackTime > 33 && attackTime <= 50) {
			// stay forward
			return -45F;
		}
		if (attackTime > 50 && attackTime <= 60) {
			// back to normal
			return (10 - (attackTime - 50)) * -4.5F;
		}

		return 0;
	}
}
