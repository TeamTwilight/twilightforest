package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.UpperGoblinKnight;

import java.util.EnumSet;

public class HeavySpearAttackGoal extends Goal {

	private final UpperGoblinKnight entity;

	public HeavySpearAttackGoal(UpperGoblinKnight upperKnight) {
		this.entity = upperKnight;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public void tick() {
		if (this.entity.heavySpearTimer == 25) {
			this.entity.landHeavySpearAttack((ServerLevel) this.entity.level());
		}
	}

	@Override
	public boolean canUse() {
		return this.entity.heavySpearTimer > 0 && this.entity.heavySpearTimer < UpperGoblinKnight.HEAVY_SPEAR_TIMER_START && this.entity.getTarget() != null && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(this.entity.getTarget());
	}
}
