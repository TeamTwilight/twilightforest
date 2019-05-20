package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

import java.util.ArrayList;
import java.util.List;

public class EntityAITFFlockTarget extends EntityAITarget {

	private final EntityLivingBase flockCreature;
	private EntityLivingBase flockTarget;

	public EntityAITFFlockTarget(EntityCreature creature, boolean checkSight) {
		super(creature, false);
		this.flockCreature = creature;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		List<EntityLivingBase> flockList = this.flockCreature.world.getEntitiesWithinAABB(this.flockCreature.getClass(), this.flockCreature.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D));
		List<EntityLivingBase> targetList = new ArrayList<EntityLivingBase>();

		for (EntityLivingBase flocker : flockList) {
			if (flocker.getRevengeTarget() != null) {
				targetList.add(flocker.getRevengeTarget());
			}
		}

		if (targetList.isEmpty()) {
			return false;
		} else {
			// hmm, just pick a random target?
			this.flockTarget = targetList.get(this.flockCreature.world.rand.nextInt(targetList.size()));
			return this.isSuitableTarget(this.flockTarget, true);
		}
	}

	@Override
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.flockTarget);
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		this.flockTarget = null;
		super.resetTask();
	}
}
