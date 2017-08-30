package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class EntityAIPhantomAttackStart extends EntityAIBase {

	private final EntityTFKnightPhantom boss;

	public EntityAIPhantomAttackStart(EntityTFKnightPhantom entity) {
		boss = entity;
		setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		return boss.getAttackTarget() != null && boss.getCurrentFormation() == EntityTFKnightPhantom.Formation.ATTACK_PLAYER_START;
	}

	@Override
	public void updateTask() {
		EntityLivingBase target = boss.getAttackTarget();
		if (target != null) {
			BlockPos targetPos = new BlockPos(target.lastTickPosX, target.lastTickPosY, target.lastTickPosZ);

			if (boss.isWithinHomeDistanceFromPosition(targetPos)) {
				boss.setChargePos(targetPos);
			} else {
				boss.setChargePos(boss.getHomePosition());
			}
		}
	}
}