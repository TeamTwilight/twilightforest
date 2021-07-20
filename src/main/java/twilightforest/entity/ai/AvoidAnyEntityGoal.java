package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

//VanillaCopy of AvoidEntityGoal, but hacked together with EntityAIAvoidEntity from 1.12
//This is so that any Entity will be avoided, including non-living Entities
public class AvoidAnyEntityGoal<T extends Entity> extends Goal {

	private final Predicate<Entity> builtTargetSelector;
	protected final CreatureEntity entity;
	private final double farSpeed;
	private final double nearSpeed;
	protected T avoidTarget;
	protected final float avoidDistance;
	protected Path path;
	protected final PathNavigator navigation;
	/** Class of entity this behavior seeks to avoid */
	protected final Class<T> classToAvoid;
	protected final Predicate<Entity> avoidTargetSelector;

	public AvoidAnyEntityGoal(CreatureEntity entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		this(entityIn, classToAvoidIn, (entity) -> true, avoidDistanceIn, farSpeedIn, nearSpeedIn);
	}

	public AvoidAnyEntityGoal(CreatureEntity entityIn, Class<T> avoidClass, Predicate<Entity> targetPredicate, float distance, double nearSpeedIn, double farSpeedIn) {
		this.builtTargetSelector = new Predicate<Entity>() {
			@Override
			public boolean test(@Nullable Entity input) {
				return input.isAlive() && AvoidAnyEntityGoal.this.entity.getEntitySenses().canSee(input) && !AvoidAnyEntityGoal.this.entity.isOnSameTeam(input);
			}
		};

		this.entity = entityIn;
		this.classToAvoid = avoidClass;
		this.avoidTargetSelector = targetPredicate;
		this.avoidDistance = distance;
		this.farSpeed = nearSpeedIn;
		this.nearSpeed = farSpeedIn;
		this.navigation = entityIn.getNavigator();
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		List<T> list = this.entity.world.getEntitiesWithinAABB(this.classToAvoid, this.entity.getBoundingBox().grow(this.avoidDistance, 3.0D, this.avoidDistance), EntityPredicates.CAN_AI_TARGET.and(this.builtTargetSelector).and(avoidTargetSelector));

		if (!list.isEmpty()) {
			this.avoidTarget = list.get(0);
			Vector3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, this.entity.getPositionVec());

			if (vec3d == null) {
				return false;
			} else if (this.avoidTarget.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.avoidTarget.getDistanceSq(this.entity)) {
				return false;
			} else {
				this.path = this.navigation.pathfind(vec3d.x, vec3d.y, vec3d.z, 0);
				return this.path != null;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.navigation.noPath();
	}

	@Override
	public void startExecuting() {
		this.navigation.setPath(this.path, this.farSpeed);
	}

	@Override
	public void resetTask() {
		this.avoidTarget = null;
	}

	@Override
	public void tick() {
		if (this.entity.getDistanceSq(this.avoidTarget) < 49.0D) {
			this.entity.getNavigator().setSpeed(this.nearSpeed);
		} else {
			this.entity.getNavigator().setSpeed(this.farSpeed);
		}
	}
}
