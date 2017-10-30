package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.IBreathAttacker;

import java.util.List;

public class EntityAITFBreathAttack extends EntityAIBase {

	private EntityLiving entityHost;
	private EntityLivingBase attackTarget;

	private double breathX;
	private double breathY;
	private double breathZ;

	private int maxDuration;
	private float attackChance;
	private float breathRange;

	private int durationLeft;

	public EntityAITFBreathAttack(EntityLiving par1EntityLiving, float speed, float range, int time, float chance) {
		this.entityHost = par1EntityLiving;
		this.breathRange = range;
		this.maxDuration = time;
		this.attackChance = chance;
		this.setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
		this.attackTarget = this.entityHost.getAttackTarget();

		if (this.attackTarget == null || this.entityHost.getDistance(attackTarget) > this.breathRange || !this.entityHost.getEntitySenses().canSee(attackTarget)) {
			return false;
		} else {
			breathX = attackTarget.posX;
			breathY = attackTarget.posY + attackTarget.getEyeHeight();
			breathZ = attackTarget.posZ;

			return this.entityHost.getRNG().nextFloat() < this.attackChance;
		}
	}

	/**
	 * Initialize counters
	 */
	@Override
	public void startExecuting() {
		this.durationLeft = this.maxDuration;

		// set breather flag
		if (this.entityHost instanceof IBreathAttacker) {
			((IBreathAttacker) entityHost).setBreathing(true);
		}
	}

	/**
	 * Keep breathing until the target dies, or moves out of range or line of sight
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return this.durationLeft > 0 && !this.entityHost.isDead && !this.attackTarget.isDead
				&& this.entityHost.getDistance(attackTarget) <= this.breathRange
				&& this.entityHost.getEntitySenses().canSee(attackTarget);
	}

	/**
	 * Update timers, deal damage
	 */
	@Override
	public void updateTask() {
		this.durationLeft--;

		// why do we need both of these?
		this.entityHost.getLookHelper().setLookPosition(breathX, breathY, breathZ, 100.0F, 100.0F);
		faceVec(breathX, breathY, breathZ, 100.0F, 100.0F);

		if ((this.maxDuration - this.durationLeft) > 5) {
			// anyhoo, deal damage
			Entity target = getHeadLookTarget();

			if (target != null) {
				((IBreathAttacker) entityHost).doBreathAttack(target);
			}
		}

	}

	@Override
	public void resetTask() {
		this.durationLeft = 0;

		// set breather flag
		if (this.entityHost instanceof IBreathAttacker) {
			((IBreathAttacker) entityHost).setBreathing(false);
		}
	}


	/**
	 * What, if anything, is the head currently looking at?
	 */
	private Entity getHeadLookTarget() {
		Entity pointedEntity = null;
		double range = 30.0D;
		double offset = 3.0D;
		Vec3d srcVec = new Vec3d(this.entityHost.posX, this.entityHost.posY + 0.25, this.entityHost.posZ);
		Vec3d lookVec = this.entityHost.getLook(1.0F);
		Vec3d destVec = srcVec.addVector(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		float var9 = 0.5F;
		List<Entity> possibleList = this.entityHost.world.getEntitiesWithinAABBExcludingEntity(this.entityHost, this.entityHost.getEntityBoundingBox().offset(lookVec.x * offset, lookVec.y * offset, lookVec.z * offset).grow(var9, var9, var9));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {
			if (possibleEntity.canBeCollidedWith() && possibleEntity != this.entityHost) {
				float borderSize = possibleEntity.getCollisionBorderSize();
				AxisAlignedBB collisionBB = possibleEntity.getEntityBoundingBox().grow((double) borderSize, (double) borderSize, (double) borderSize);
				RayTraceResult interceptPos = collisionBB.calculateIntercept(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos != null) {
					double possibleDist = srcVec.distanceTo(interceptPos.hitVec);

					if (possibleDist < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = possibleDist;
					}
				}
			}
		}
		return pointedEntity;
	}

	/**
	 * Face the head towards a specific Vector
	 */
	public void faceVec(double x, double y, double z, float yawConstraint, float pitchConstraint) {
		double xOffset = x - entityHost.posX;
		double zOffset = z - entityHost.posZ;
		double yOffset = (entityHost.posY + 0.25) - y;

		double distance = MathHelper.sqrt(xOffset * xOffset + zOffset * zOffset);
		float xyAngle = (float) ((Math.atan2(zOffset, xOffset) * 180D) / Math.PI) - 90F;
		float zdAngle = (float) (-((Math.atan2(yOffset, distance) * 180D) / Math.PI));
		entityHost.rotationPitch = -updateRotation(entityHost.rotationPitch, zdAngle, pitchConstraint);
		entityHost.rotationYaw = updateRotation(entityHost.rotationYaw, xyAngle, yawConstraint);

	}

	/**
	 * Arguments: current rotation, intended rotation, max increment.
	 */
	private float updateRotation(float par1, float par2, float par3) {
		float var4 = MathHelper.wrapDegrees(par2 - par1);

		if (var4 > par3) {
			var4 = par3;
		}

		if (var4 < -par3) {
			var4 = -par3;
		}

		return par1 + var4;
	}


}
