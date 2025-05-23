package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.LowerGoblinKnight;
import twilightforest.entity.monster.UpperGoblinKnight;

import java.util.EnumSet;

public class RiderSpearAttackGoal extends Goal {

	private final LowerGoblinKnight entity;

	public RiderSpearAttackGoal(LowerGoblinKnight lowerKnight) {
		this.entity = lowerKnight;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); // Prevent moving
	}

	@Override
	public boolean canUse() {
		if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof UpperGoblinKnight && this.entity.getTarget() != null && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(this.entity.getTarget())) {
			int timer = ((UpperGoblinKnight) this.entity.getPassengers().get(0)).heavySpearTimer;
			return timer > 0 && timer < UpperGoblinKnight.HEAVY_SPEAR_TIMER_START;
		} else {
			return false;
		}
	}
}
