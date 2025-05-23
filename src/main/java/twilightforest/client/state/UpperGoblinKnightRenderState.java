package twilightforest.client.state;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class UpperGoblinKnightRenderState extends HumanoidRenderState {
	public boolean hasArmor;
	public boolean hasShield;
	public boolean isShieldDisabled;
	public float spearTimer;

	public float getArmRotationDuringSwing() {
		float timer = 60.0F - this.spearTimer;
		if (timer <= 10.0F) {
			// rock back
			return timer;
		}
		if (timer > 10.0F && timer <= 30.0F) {
			// hang back
			return 10.0F;
		}
		if (timer > 30.0F && timer <= 33.0F) {
			// slam forward
			return (timer - 30.0F) * -8.0F + 10.0F;
		}
		if (timer > 33.0F && timer <= 50.0F) {
			// stay forward
			return -15.0F;
		}
		if (timer > 50.0F && timer <= 60.0F) {
			// back to normal
			return (10.0F - (timer - 50.0F)) * -1.5F;
		}

		return 0.0F;
	}

	public float getPitchForAttack() {
		float attackTime = 60.0F - this.spearTimer;
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
