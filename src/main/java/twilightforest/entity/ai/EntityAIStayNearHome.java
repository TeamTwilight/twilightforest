package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class EntityAIStayNearHome extends Goal {
	private final CreatureEntity entity;
	private final float speed;

	public EntityAIStayNearHome(CreatureEntity entityTFYetiAlpha, float sp) {
		this.entity = entityTFYetiAlpha;
		this.speed = sp;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return !this.entity.isWithinHomeDistanceCurrentPosition();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistanceSq(Vector3d.copy(this.entity.getHomePosition())) > 256.0D) {
			Vector3d vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vector3d(this.entity.getHomePosition().getX() + 0.5D, this.entity.getHomePosition().getY(), this.entity.getHomePosition().getZ() + 0.5D));

			if (vec3 != null) {
				this.entity.getNavigator().tryMoveToXYZ(vec3.x, vec3.y, vec3.z, speed);
			}
		} else {
			this.entity.getNavigator().tryMoveToXYZ(this.entity.getHomePosition().getX() + 0.5D, this.entity.getHomePosition().getY(), this.entity.getHomePosition().getZ() + 0.5D, speed);
		}
	}
}
