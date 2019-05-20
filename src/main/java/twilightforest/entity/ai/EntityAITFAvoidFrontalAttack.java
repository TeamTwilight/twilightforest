package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.EntityTFRedcap;

public class EntityAITFAvoidFrontalAttack extends EntityAIBase {

	private final EntityLiving me;
	final float speed;
	private final boolean lefty;

	private EntityLivingBase entityTarget;
	private double xPosition;
	private double yPosition;
	private double zPosition;

	private double minDistance = 3.0;
	private double maxDistance = 6.0;

	public EntityAITFAvoidFrontalAttack(EntityTFRedcap entityTFRedcap, float moveSpeed) {
		this.me = entityTFRedcap;
		this.speed = moveSpeed;
		this.lefty = me.world.rand.nextBoolean();
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.me.getAttackTarget();

		if (attackTarget == null || attackTarget.getDistance(me) > maxDistance || attackTarget.getDistance(me) < minDistance || !isTargetLookingAtMe(attackTarget)) {
			return false;
		} else {
			this.entityTarget = attackTarget;
			Vec3d avoidPos = findCirclePoint(me, entityTarget, 5, lefty ? 1 : -1);

			if (avoidPos == null) {
				return false;
			} else {
				this.xPosition = avoidPos.x;
				this.yPosition = avoidPos.y;
				this.zPosition = avoidPos.z;
				return true;
			}
		}

	}

	@Override
	public void startExecuting() {
		this.me.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.me.getAttackTarget();

		if (attackTarget == null) {
			return false;
		} else if (!this.entityTarget.isEntityAlive()) {
			return false;
		} else if (this.me.getNavigator().noPath()) {
			return false;
		}

		return attackTarget.getDistance(me) < maxDistance && attackTarget.getDistance(me) > minDistance && isTargetLookingAtMe(attackTarget);
	}

	@Override
	public void updateTask() {
		this.me.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
	}

	@Override
	public void resetTask() {
		this.entityTarget = null;
		this.me.getNavigator().clearPath();
	}

	/**
	 * Finds a point that allows us to circle the player clockwise.
	 */
	protected Vec3d findCirclePoint(Entity circler, Entity toCircle, double radius, double rotation) {

		// compute angle
		double vecx = circler.posX - toCircle.posX;
		double vecz = circler.posZ - toCircle.posZ;
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles
		rangle += rotation;

		// figure out where we're headed from the target angle
		double dx = MathHelper.cos(rangle) * radius;
		double dz = MathHelper.sin(rangle) * radius;

		// add that to the target entity's position, and we have our destination
		return new Vec3d(toCircle.posX + dx, circler.getEntityBoundingBox().minY, toCircle.posZ + dz);
	}


	/**
	 * Fairly straightforward.  Returns true in a 120 degree arc in front of the target's view.
	 *
	 * @return
	 */
	public boolean isTargetLookingAtMe(EntityLivingBase attackTarget) {
		// find angle of approach
		double dx = me.posX - attackTarget.posX;
		double dz = me.posZ - attackTarget.posZ;
		float angle = (float) ((Math.atan2(dz, dx) * 180D) / 3.1415927410125732D) - 90F;

		float difference = MathHelper.abs((attackTarget.rotationYaw - angle) % 360);

		return difference < 60 || difference > 300;
	}

}
