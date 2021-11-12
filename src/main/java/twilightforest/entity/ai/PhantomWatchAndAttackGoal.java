package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.InteractionHand;
import twilightforest.entity.boss.KnightPhantom;

public class PhantomWatchAndAttackGoal extends Goal {

	private final KnightPhantom boss;
	private int attackTime;
	private int guardCoolDownTime;
	private boolean isGuard;

	public PhantomWatchAndAttackGoal(KnightPhantom entity) {
		boss = entity;
	}

	@Override
	public boolean canUse() {
		return boss.getTarget() != null;
	}

	@Override
	public void tick() {
		LivingEntity target = boss.getTarget();
		if (target != null) {
			boss.lookAt(target, 10.0F, 500.0F);

			if (target.isAlive()) {
				float f1 = target.distanceTo(boss);

				if (boss.getSensing().hasLineOfSight(target)) {
					if (attackTime-- <= 0 && f1 < 2.0F && target.getBoundingBox().maxY > boss.getBoundingBox().minY && boss.getTarget().getBoundingBox().minY < boss.getBoundingBox().maxY) {
						attackTime = 20;
						boss.doHurtTarget(target);
					}
				}

				if (this.boss.getOffhandItem().getItem() instanceof ShieldItem && boss.getCurrentFormation() != KnightPhantom.Formation.ATTACK_PLAYER_ATTACK && this.isGuard) {
					this.boss.startUsingItem(InteractionHand.OFF_HAND);
				} else {
					this.boss.stopUsingItem();
				}

				if(this.isGuard){
					if(this.guardCoolDownTime <= 180) {
						++this.guardCoolDownTime;
					}else {
						this.isGuard = false;
					}
				}else {
					if (guardCoolDownTime > 0) {
						--this.guardCoolDownTime;
					}else {
						this.isGuard = true;
					}
				}
			}
		}
	}
}