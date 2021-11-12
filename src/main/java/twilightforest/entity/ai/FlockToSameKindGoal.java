package twilightforest.entity.ai;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.data.ItemTagGenerator;
import twilightforest.entity.monster.Kobold;

import java.util.List;

public class FlockToSameKindGoal extends Goal {
	private static final double MAX_DIST = 256.0D;
	private static final double MIN_DIST = 25.0D;
	/**
	 * The child that is following its parent.
	 */
	private Mob flockCreature;
	private Vec3 flockPosition;
	double speed;
	private int moveTimer;

	public FlockToSameKindGoal(Mob living, double speed) {
		this.flockCreature = living;
		this.speed = speed;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse() {

		if(flockCreature instanceof Kobold kobold && kobold.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTagGenerator.KOBOLD_PACIFICATION_BREADS)) {
			return false;
		}

		if (this.flockCreature.getRandom().nextInt(40) != 0) {
			return false;
		}

		List<? extends Mob> flockList = this.flockCreature.level.getEntitiesOfClass(this.flockCreature.getClass(), this.flockCreature.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));

		if(flockList.size() > 5) {
			return false;
		}

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


		if (flockCreature.distanceToSqr(flockX, flockY, flockZ) < MIN_DIST) {
			return false;
		} else {
			this.flockPosition = new Vec3(flockX, flockY, flockZ);
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse() {
		if (flockPosition == null) {
			return false;
		} else {
			double distance = this.flockCreature.distanceToSqr(flockPosition.x, flockPosition.y, flockPosition.z);
			return distance >= MIN_DIST && distance <= MAX_DIST;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.moveTimer = 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void stop() {
		this.flockPosition = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void tick() {
		if (--this.moveTimer <= 0) {
			this.moveTimer = 10;
			this.flockCreature.getNavigation().moveTo(flockPosition.x, flockPosition.y, flockPosition.z, this.speed);
		}
	}
}
