package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EntitySelector;
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
		if (entity.heavySpearTimer == 25) {
			entity.landHeavySpearAttack();
		}
	}

	@Override
	public boolean canUse() {
		return entity.heavySpearTimer > 0 && entity.heavySpearTimer < UpperGoblinKnight.HEAVY_SPEAR_TIMER_START && entity.getTarget() != null && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(entity.getTarget());
	}
}
