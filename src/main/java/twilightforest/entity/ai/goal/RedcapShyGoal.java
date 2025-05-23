package twilightforest.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.Redcap;

import java.util.EnumSet;

public class RedcapShyGoal extends RedcapBaseGoal {

	private LivingEntity entityTarget;
	private final float speed;
	private final boolean lefty = Math.random() < 0.5;
	private double targetX;
	private double targetY;
	private double targetZ;

	private static final double minDistance = 3.0;
	private static final double maxDistance = 6.0;

	public RedcapShyGoal(Redcap entityTFRedcap, float moveSpeed) {
		super(entityTFRedcap);
		this.speed = moveSpeed;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity attackTarget = this.redcap.getTarget();

		if (attackTarget == null
			|| !this.redcap.isShy()
			|| attackTarget.distanceTo(this.redcap) > maxDistance
			|| attackTarget.distanceTo(this.redcap) < minDistance
			|| !isTargetLookingAtMe(attackTarget)) {
			return false;
		} else {
			this.entityTarget = attackTarget;
			Vec3 avoidPos = findCirclePoint(this.redcap, this.entityTarget, 5, this.lefty ? 1 : -1);

			this.targetX = avoidPos.x();
			this.targetY = avoidPos.y();
			this.targetZ = avoidPos.z();
			return true;
		}

	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.redcap.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity attackTarget = this.redcap.getTarget();

		if (attackTarget == null) {
			return false;
		} else if (!this.entityTarget.isAlive()) {
			return false;
		} else if (this.redcap.getNavigation().isDone()) {
			return false;
		}

		return this.redcap.isShy() && attackTarget.distanceTo(this.redcap) < maxDistance && attackTarget.distanceTo(this.redcap) > minDistance && isTargetLookingAtMe(attackTarget);
	}

	@Override
	public void tick() {
		this.redcap.getLookControl().setLookAt(this.entityTarget, 30.0F, 30.0F);
	}

	@Override
	public void stop() {
		this.entityTarget = null;
		this.redcap.getNavigation().stop();
	}

	private Vec3 findCirclePoint(Entity circler, Entity toCircle, double radius, double rotation) {
		// compute angle
		double vecx = circler.getX() - toCircle.getX();
		double vecz = circler.getZ() - toCircle.getZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles
		rangle += (float) rotation;

		// figure out where we're headed from the target angle
		double dx = Mth.cos(rangle) * radius;
		double dz = Mth.sin(rangle) * radius;

		// add that to the target entity's position, and we have our destination
		return new Vec3(toCircle.getX() + dx, circler.getBoundingBox().minY, toCircle.getZ() + dz);
	}
}
