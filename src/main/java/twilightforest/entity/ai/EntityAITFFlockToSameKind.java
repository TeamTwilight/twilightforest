package twilightforest.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityAITFFlockToSameKind extends Goal {
	private static final double MAX_DIST = 256.0D;
	private static final double MIN_DIST = 25.0D;
	/**
	 * The child that is following its parent.
	 */
	private MobEntity flockCreature;
	private Vec3d flockPosition;
	double speed;
	private int moveTimer;

	public EntityAITFFlockToSameKind(MobEntity living, double speed) {
		this.flockCreature = living;
		this.speed = speed;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (this.flockCreature.getRNG().nextInt(40) != 0) {
			return false;
		}

		List<LivingEntity> flockList = this.flockCreature.world.getEntitiesWithinAABB(this.flockCreature.getClass(), this.flockCreature.getBoundingBox().grow(16.0D, 4.0D, 16.0D));

		int flocknum = 0;
		double flockX = 0;
		double flockY = 0;
		double flockZ = 0;

		for (LivingEntity flocker : flockList) {
			flocknum++;
			flockX += flocker.getX();
			flockY += flocker.getY();
			flockZ += flocker.getZ();
		}

		flockX /= flocknum;
		flockY /= flocknum;
		flockZ /= flocknum;


		if (flockCreature.getDistanceSq(flockX, flockY, flockZ) < MIN_DIST) {
			return false;
		} else {
			this.flockPosition = new Vec3d(flockX, flockY, flockZ);
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		if (flockPosition == null) {
			return false;
		} else {
			double distance = this.flockCreature.getDistanceSq(flockPosition.x, flockPosition.y, flockPosition.z);
			return distance >= MIN_DIST && distance <= MAX_DIST;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.moveTimer = 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		this.flockPosition = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void tick() {
		if (--this.moveTimer <= 0) {
			this.moveTimer = 10;
			this.flockCreature.getNavigator().tryMoveToXYZ(flockPosition.x, flockPosition.y, flockPosition.z, this.speed);
		}
	}
}
