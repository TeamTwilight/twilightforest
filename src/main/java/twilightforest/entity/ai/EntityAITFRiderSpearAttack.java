package twilightforest.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.EntityPredicates;
import twilightforest.entity.EntityTFGoblinKnightLower;
import twilightforest.entity.EntityTFGoblinKnightUpper;

import java.util.EnumSet;

public class EntityAITFRiderSpearAttack extends Goal {

	private EntityTFGoblinKnightLower entity;

	public EntityAITFRiderSpearAttack(EntityTFGoblinKnightLower lowerKnight) {
		this.entity = lowerKnight;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); // Prevent moving
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof EntityTFGoblinKnightUpper && EntityPredicates.CAN_HOSTILE_AI_TARGET.test(entity.getAttackTarget())) {
			int timer = ((EntityTFGoblinKnightUpper) this.entity.getPassengers().get(0)).heavySpearTimer;
			return timer > 0 && timer < EntityTFGoblinKnightUpper.HEAVY_SPEAR_TIMER_START;
		} else {
			return false;
		}
	}
}
