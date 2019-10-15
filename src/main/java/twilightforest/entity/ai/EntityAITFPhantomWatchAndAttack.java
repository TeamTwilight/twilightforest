package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemShield;
import net.minecraft.util.EnumHand;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class EntityAITFPhantomWatchAndAttack extends EntityAIBase {

	private final EntityTFKnightPhantom boss;
	private int attackTime;
	private int guardCoolDownTime;
	private boolean isGuard;

	public EntityAITFPhantomWatchAndAttack(EntityTFKnightPhantom entity) {
		boss = entity;
	}

	@Override
	public boolean shouldExecute() {
		return boss.getAttackTarget() != null;
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = boss.getAttackTarget();
		if (target != null) {
			boss.faceEntity(target, 10.0F, 500.0F);

			if (target.isEntityAlive()) {
				float f1 = target.getDistance(boss);

				if (boss.getEntitySenses().canSee(target)) {
					if (attackTime-- <= 0 && f1 < 2.0F && target.getEntityBoundingBox().maxY > boss.getEntityBoundingBox().minY && boss.getAttackTarget().getEntityBoundingBox().minY < boss.getEntityBoundingBox().maxY) {
						attackTime = 20;
						boss.attackEntityAsMob(target);
					}
				}

				if (this.boss.getHeldItemOffhand().getItem() instanceof ItemShield && boss.getCurrentFormation() != EntityTFKnightPhantom.Formation.ATTACK_PLAYER_ATTACK && this.isGuard) {
					this.boss.setActiveHand(EnumHand.OFF_HAND);
				} else {
					this.boss.resetActiveHand();
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