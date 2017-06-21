package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIStayNearHome extends EntityAIBase {
	private final EntityCreature entity;
	private final float speed;

	public EntityAIStayNearHome(EntityCreature entityTFYetiAlpha, float sp) {
		this.entity = entityTFYetiAlpha;
		this.speed = sp;
		this.setMutexBits(1);
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
		if (this.entity.getDistanceSq(this.entity.getHomePosition()) > 256.0D) {
			Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d(this.entity.getHomePosition().getX() + 0.5D, this.entity.getHomePosition().getY(), this.entity.getHomePosition().getZ() + 0.5D));

			if (vec3 != null) {
				this.entity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, speed);
			}
		} else {
			this.entity.getNavigator().tryMoveToXYZ(this.entity.getHomePosition().getX() + 0.5D, this.entity.getHomePosition().getY(), this.entity.getHomePosition().getZ() + 0.5D, speed);
		}
	}

}
