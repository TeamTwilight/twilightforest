package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class SimplifiedAttackGoal extends Goal {

	private final Mob mob;
	private int attackTick;

	public SimplifiedAttackGoal(Mob mob) {
		this.mob = mob;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = mob.getTarget();
		return target != null && this.mob.isWithinMeleeAttackRange(target);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.attackTick = 0;
	}

	@Override
	public void stop() {
		this.attackTick = 0;
	}

	@Override
	public void tick() {
		if (this.attackTick > 0) {
			this.attackTick--;
		} else {
			LivingEntity livingentity = this.mob.getTarget();
			if (livingentity == null) {
				this.stop();
				return;
			}
			this.checkAndPerformAttack(livingentity);
		}
	}

	protected void checkAndPerformAttack(LivingEntity entity) {
		if (this.attackTick <= 0 && this.mob.isWithinMeleeAttackRange(entity) && this.mob.hasLineOfSight(entity)) {
			this.attackTick = this.adjustedTickDelay(20);
			this.mob.swing(InteractionHand.MAIN_HAND);
			this.mob.doHurtTarget(getServerLevel(this.mob), entity);
		}
	}
}