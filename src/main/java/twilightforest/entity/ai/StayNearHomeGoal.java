package twilightforest.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class StayNearHomeGoal extends Goal {
	private final PathfinderMob entity;
	private final float speed;

	public StayNearHomeGoal(PathfinderMob entityTFYetiAlpha, float sp) {
		this.entity = entityTFYetiAlpha;
		this.speed = sp;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return !this.entity.isWithinRestriction();
	}

	@Override
	public boolean canContinueToUse() {
		return !this.entity.getNavigation().isDone();
	}

	@Override
	public void start() {
		if (this.entity.distanceToSqr(Vec3.atLowerCornerOf(this.entity.getRestrictCenter())) > 256.0D) {
			Vec3 vec3 = DefaultRandomPos.getPosTowards(this.entity, 14, 3, new Vec3(this.entity.getRestrictCenter().getX() + 0.5D, this.entity.getRestrictCenter().getY(), this.entity.getRestrictCenter().getZ() + 0.5D), (Mth.PI / 2F));

			if (vec3 != null) {
				this.entity.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speed);
			}
		} else {
			this.entity.getNavigation().moveTo(this.entity.getRestrictCenter().getX() + 0.5D, this.entity.getRestrictCenter().getY(), this.entity.getRestrictCenter().getZ() + 0.5D, speed);
		}
	}
}
