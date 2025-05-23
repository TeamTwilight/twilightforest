package twilightforest.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

import java.util.EnumSet;

// [VanillaCopy]-ish mixture of Ghast.RandomFloatAroundGoal and MoveTowardsRestrictionGoal
public class GhastguardHomedFlightGoal extends Goal {
	private final CarminiteGhastguard ghast;

	public GhastguardHomedFlightGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	// From RandomFloatAroundGoal, but with extra condition from MoveTowardsRestrictionGoal
	@Override
	public boolean canUse() {
		MoveControl control = this.ghast.getMoveControl();

		if (!control.hasWanted()) {
			return !this.ghast.isMobWithinHomeArea(this.ghast);
		} else {
			double d0 = control.getWantedX() - this.ghast.getX();
			double d1 = control.getWantedY() - this.ghast.getY();
			double d2 = control.getWantedZ() - this.ghast.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return (d3 < 1.0D || d3 > 3600.0D)
				&& !this.ghast.isMobWithinHomeArea(this.ghast);
		}
	}

	// From RandomFloatAroundGoal
	@Override
	public boolean canContinueToUse() {
		return false;
	}

	// From MoveTowardsRestrictionGoal but use move helper instead of PathNavigate
	@Override
	public void start() {
		if (!this.ghast.isRestrictionPointValid(this.ghast.level().dimension())) {
			this.stop();
			return;
		}
		RandomSource random = this.ghast.getRandom();
		double d0 = this.ghast.getX() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double d1 = this.ghast.getY() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		double d2 = this.ghast.getZ() + (random.nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
		this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);

		if (this.ghast.distanceToSqr(Vec3.atLowerCornerOf(this.ghast.getRestrictionPoint().pos())) > 256.0D) {
			Vec3 vecToHome = Vec3.atLowerCornerOf(this.ghast.getRestrictionPoint().pos()).subtract(this.ghast.position()).normalize();

			double targetX = this.ghast.getX() + vecToHome.x() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
			double targetY = this.ghast.getY() + vecToHome.y() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();
			double targetZ = this.ghast.getZ() + vecToHome.z() * this.ghast.getWanderFactor() + (this.ghast.getRandom().nextFloat() * 2.0F - 1.0F) * this.ghast.getWanderFactor();

			this.ghast.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
		} else {
			this.ghast.getMoveControl().setWantedPosition(this.ghast.getRestrictionPoint().pos().getX() + 0.5D, this.ghast.getRestrictionPoint().pos().getY(), this.ghast.getRestrictionPoint().pos().getZ() + 0.5D, 1.0D);
		}
	}
}