package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

//VanillaCopy of AvoidEntityGoal, but hacked together with EntityAIAvoidEntity from 1.12
//This is so that any Entity will be avoided, including non-living Entities
public class AvoidAnyEntityGoal<T extends Entity> extends Goal {

	private final Predicate<Entity> builtTargetSelector;
	protected final PathfinderMob entity;
	private final double farSpeed;
	private final double nearSpeed;
	protected T avoidTarget;
	protected final float avoidDistance;
	protected Path path;
	protected final PathNavigation navigation;
	/**
	 * Class of entity this behavior seeks to avoid
	 */
	protected final Class<T> classToAvoid;
	protected final Predicate<Entity> avoidTargetSelector;

	public AvoidAnyEntityGoal(PathfinderMob entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		this(entityIn, classToAvoidIn, (entity) -> true, avoidDistanceIn, farSpeedIn, nearSpeedIn);
	}

	public AvoidAnyEntityGoal(PathfinderMob entityIn, Class<T> avoidClass, Predicate<Entity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
		this.builtTargetSelector = new Predicate<>() {
			@Override
			public boolean test(@Nullable Entity input) {
				return input != null && input.isAlive() &&
					AvoidAnyEntityGoal.this.entity.getSensing().hasLineOfSight(input) &&
					!AvoidAnyEntityGoal.this.entity.isAlliedTo(input);
			}
		};

		this.entity = entityIn;
		this.classToAvoid = avoidClass;
		this.avoidTargetSelector = targetPredicate;
		this.avoidDistance = distance;
		this.farSpeed = nearSpeedIn;
		this.nearSpeed = farSpeedIn;
		this.navigation = entityIn.getNavigation();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		List<T> list = this.entity.level().getEntitiesOfClass(this.classToAvoid, this.entity.getBoundingBox().inflate(this.avoidDistance, 3.0D, this.avoidDistance), EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(this.builtTargetSelector).and(this.avoidTargetSelector));

		if (!list.isEmpty()) {
			this.avoidTarget = list.get(0);
			Vec3 vec3d = DefaultRandomPos.getPosAway(this.entity, 16, 7, this.entity.position());

			if (vec3d == null) {
				return false;
			} else if (this.avoidTarget.distanceToSqr(vec3d.x(), vec3d.y(), vec3d.z()) < this.avoidTarget.distanceToSqr(this.entity)) {
				return false;
			} else {
				this.path = this.navigation.createPath(vec3d.x(), vec3d.y(), vec3d.z(), 0);
				return this.path != null;
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.navigation.isDone();
	}

	@Override
	public void start() {
		this.navigation.moveTo(this.path, this.farSpeed);
	}

	@Override
	public void stop() {
		this.avoidTarget = null;
	}

	@Override
	public void tick() {
		if (this.entity.distanceToSqr(this.avoidTarget) < 49.0D) {
			this.entity.getNavigation().setSpeedModifier(this.nearSpeed);
		} else {
			this.entity.getNavigation().setSpeedModifier(this.farSpeed);
		}
	}
}
