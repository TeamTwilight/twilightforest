package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.EntityTFSkeletonCreeper;

public class EntityAITFSkeletonCreeperSwell extends EntityAIBase {
	/** The creeper that is swelling. */
	EntityTFSkeletonCreeper swellingCreeper;
	/** The creeper's attack target. This is used for the changing of the creeper's state.*/
	EntityLivingBase creeperAttackTarget;

	public EntityAITFSkeletonCreeperSwell(EntityTFSkeletonCreeper entitycreeperIn) {
		this.swellingCreeper = entitycreeperIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
		return this.swellingCreeper.getCreeperState() > 0 || entitylivingbase != null && this.swellingCreeper.getDistanceSq(entitylivingbase) < 9.0D;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.swellingCreeper.getNavigator().clearPath();
		this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask() {
		this.creeperAttackTarget = null;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		if (this.creeperAttackTarget == null || this.swellingCreeper.getDistanceSq(this.creeperAttackTarget) > 49.0D || !this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
			this.swellingCreeper.setCreeperState(-1);
		} else {
			this.swellingCreeper.setCreeperState(1);
		}
	}
}