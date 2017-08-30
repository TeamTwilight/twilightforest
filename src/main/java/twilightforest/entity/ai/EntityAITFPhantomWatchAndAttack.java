package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class EntityAITFPhantomWatchAndAttack extends EntityAIBase {

	private final EntityTFKnightPhantom boss;
	private int attackTime;

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
				float f1 = target.getDistanceToEntity(boss);

				if (boss.getEntitySenses().canSee(target)) {
					if (attackTime-- <= 0 && f1 < 2.0F && target.getEntityBoundingBox().maxY > boss.getEntityBoundingBox().minY && boss.getAttackTarget().getEntityBoundingBox().minY < boss.getEntityBoundingBox().maxY) {
						attackTime = 20;
						boss.attackEntityAsMob(target);
					}
				}
			}
		}
	}
}