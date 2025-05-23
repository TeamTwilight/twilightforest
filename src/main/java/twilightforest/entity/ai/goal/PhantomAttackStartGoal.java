package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.KnightPhantom;

import java.util.EnumSet;

public class PhantomAttackStartGoal extends Goal {

	private final KnightPhantom boss;

	public PhantomAttackStartGoal(KnightPhantom entity) {
		this.boss = entity;
		setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null && this.boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_START;
	}

	@Override
	public void tick() {
		LivingEntity target = this.boss.getTarget();
		if (target != null) {
			BlockPos targetPos = BlockPos.containing(target.xOld, target.yOld, target.zOld);

			if (this.boss.isMobWithinHomeArea(this.boss)) {
				this.boss.setChargePos(targetPos);
			} else {
				this.boss.setChargePos(this.boss.getRestrictionPoint().pos());
			}
		}
	}
}