package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;
import twilightforest.entity.boss.KnightPhantom;

public class PhantomWatchAndAttackGoal extends Goal {

	private final KnightPhantom boss;
	private int attackTime;
	private int guardCoolDownTime;
	private boolean isGuard;

	public PhantomWatchAndAttackGoal(KnightPhantom entity) {
		this.boss = entity;
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null;
	}

	@Override
	public void tick() {
		LivingEntity target = this.boss.getTarget();
		if (target != null) {
			this.boss.lookAt(target, 10.0F, 500.0F);

			if (target.isAlive()) {
				float f1 = target.distanceTo(this.boss);

				if (this.boss.getSensing().hasLineOfSight(target)) {
					if (attackTime-- <= 0 && f1 < 2.0F && target.getBoundingBox().maxY > this.boss.getBoundingBox().minY && this.boss.getTarget().getBoundingBox().minY < this.boss.getBoundingBox().maxY) {
						attackTime = 20;
						this.boss.doHurtTarget(getServerLevel(this.boss), target);
					}
				}

				if (this.boss.getOffhandItem().getItem() instanceof ShieldItem && this.boss.getCurrentFormation() != KnightPhantom.Formation.ATTACK_PLAYER_ATTACK && this.isGuard) {
					this.boss.startUsingItem(InteractionHand.OFF_HAND);
				} else {
					this.boss.stopUsingItem();
				}

				if (this.isGuard) {
					if (this.guardCoolDownTime <= 180) {
						++this.guardCoolDownTime;
					} else {
						this.isGuard = false;
					}
				} else {
					if (this.guardCoolDownTime > 0) {
						--this.guardCoolDownTime;
					} else {
						this.isGuard = true;
					}
				}
			}
		}
	}
}