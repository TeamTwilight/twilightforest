package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.EnforcedHomePoint;

import java.util.EnumSet;

public class AttemptToGoHomeGoal<T extends PathfinderMob & EnforcedHomePoint> extends Goal {
	private final T mob;
	private double wantedX;
	private double wantedY;
	private double wantedZ;
	private final double speedModifier;

	public AttemptToGoHomeGoal(T mob, double speedModifier) {
		this.mob = mob;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.mob.getRestrictionPoint() == null || this.mob.isMobWithinHomeArea(this.mob)) {
			return false;
		} else {
			Vec3 vec3 = DefaultRandomPos.getPosTowards(this.mob, 16, 7, Vec3.atBottomCenterOf(this.mob.getRestrictionPoint().pos()), (float) Math.PI / 2F);
			if (vec3 == null) {
				return false;
			} else {
				this.wantedX = vec3.x;
				this.wantedY = vec3.y;
				this.wantedZ = vec3.z;
				return true;
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.mob.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.mob.getNavigation().stop();
		this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
	}
}