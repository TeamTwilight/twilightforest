package twilightforest.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.boss.Naga;

public class NagaMoveControl extends MoveControl {

	public NagaMoveControl(Naga naga) {
		super(naga);
	}

	@Override
	public void tick() {
		// TF - slither!
		NagaMovementPattern.MovementState currentState = ((Naga) this.mob).getMovementPattern().getState();
		if (currentState == NagaMovementPattern.MovementState.DAZE) {
			this.mob.xxa = 0F;
		} else if (currentState != NagaMovementPattern.MovementState.CHARGE && currentState != NagaMovementPattern.MovementState.INTIMIDATE) {
			this.mob.xxa = Mth.cos(this.mob.tickCount * 0.3F) * 0.6F;
		} else {
			this.mob.xxa *= 0.8F;
		}

		super.tick();
	}
}