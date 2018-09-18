package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import twilightforest.entity.EntityTFGoblinKnightUpper;

public class RenderTFGoblinKnightUpper extends RenderTFBiped<EntityTFGoblinKnightUpper> {
	public RenderTFGoblinKnightUpper(RenderManager manager, ModelBiped model, float shadowSize) {
		super(manager, model, shadowSize, "doublegoblin.png");
	}

	@Override
	protected void applyRotations(EntityTFGoblinKnightUpper upperKnight, float ageInTicks, float rotationYaw, float partialTicks) {
		super.applyRotations(upperKnight, ageInTicks, rotationYaw, partialTicks);

		if (upperKnight.heavySpearTimer > 0) {
			GlStateManager.rotate(getPitchForAttack((60 - upperKnight.heavySpearTimer) + partialTicks), 1.0F, 0.0F, 0.0F);
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
