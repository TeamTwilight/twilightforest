package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.EntityTFKobold;

import java.util.List;

public class EntityAITFPanicOnFlockDeath extends EntityAIBase {
	private EntityCreature flockCreature;
	private float speed;
	private double fleeX;
	private double fleeY;
	private double fleeZ;

	private int fleeTimer;

	public EntityAITFPanicOnFlockDeath(EntityCreature creature, float speed) {
		this.flockCreature = creature;
		this.speed = speed;
		this.setMutexBits(1);
		this.fleeTimer = 0;
	}

	@Override
	public boolean shouldExecute() {
		boolean yikes = fleeTimer > 0;

		// check if any of us is dead within 4 squares
		List<EntityCreature> flockList = this.flockCreature.world.getEntitiesWithinAABB(this.flockCreature.getClass(), this.flockCreature.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D));
		for (EntityLiving flocker : flockList) {
			if (flocker.deathTime > 0) {
				yikes = true;
				break;
			}
		}

		if (!yikes) {
			return false;
		} else {
			Vec3d target = RandomPositionGenerator.findRandomTarget(this.flockCreature, 5, 4);

			if (target == null) {
				return false;
			} else {
				this.fleeX = target.x;
				this.fleeY = target.y;
				this.fleeZ = target.z;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.fleeTimer = 40;
		this.flockCreature.getNavigator().tryMoveToXYZ(this.fleeX, this.fleeY, this.fleeZ, this.speed);

		// panic flag for kobold animations
		if (flockCreature instanceof EntityTFKobold) {
			((EntityTFKobold) flockCreature).setPanicked(true);
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return fleeTimer > 0 && !this.flockCreature.getNavigator().noPath();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		fleeTimer--;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		fleeTimer -= 20;

		// panic flag for kobold animations
		if (flockCreature instanceof EntityTFKobold) {
			((EntityTFKobold) flockCreature).setPanicked(false);
		}
	}


}
