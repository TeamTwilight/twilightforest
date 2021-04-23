package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import twilightforest.entity.IBreathAttacker;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityAITFBreathAttack<T extends MobEntity & IBreathAttacker> extends Goal {

	private final T entityHost;
	private LivingEntity attackTarget;

	private double breathX;
	private double breathY;
	private double breathZ;

	private final int maxDuration;
	private final float attackChance;
	private final float breathRange;

	private int durationLeft;

	public EntityAITFBreathAttack(T living, float speed, float range, int time, float chance) {
		this.entityHost = living;
		this.breathRange = range;
		this.maxDuration = time;
		this.attackChance = chance;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP)); //TODO: I'm very uncertain about this
	}

	@Override
	public boolean shouldExecute() {
		this.attackTarget = this.entityHost.getRevengeTarget();

		if (this.attackTarget == null || this.entityHost.getDistance(attackTarget) > this.breathRange || !this.entityHost.getEntitySenses().canSee(attackTarget) || !EntityPredicates.CAN_HOSTILE_AI_TARGET.test(attackTarget)) {
			return false;
		} else {
			breathX = attackTarget.getPosX();
			breathY = attackTarget.getPosY() + attackTarget.getEyeHeight();
			breathZ = attackTarget.getPosZ();

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
		this.entityHost.setBreathing(true);
	}

	/**
	 * Keep breathing until the target dies, or moves out of range or line of sight
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return this.durationLeft > 0 && this.entityHost.isAlive() && this.attackTarget.isAlive()
				&& this.entityHost.getDistance(attackTarget) <= this.breathRange
				&& this.entityHost.getEntitySenses().canSee(attackTarget)
				&& EntityPredicates.CAN_HOSTILE_AI_TARGET.test(attackTarget);
	}

	/**
	 * Update timers, deal damage
	 */
	@Override
	public void tick() {
		this.durationLeft--;

		// why do we need both of these?
		this.entityHost.getLookController().setLookPosition(breathX, breathY, breathZ, 100.0F, 100.0F);
		faceVec(breathX, breathY, breathZ, 100.0F, 100.0F);

		if ((this.maxDuration - this.durationLeft) > 5) {
			// anyhoo, deal damage
			Entity target = getHeadLookTarget();
			if (target != null) {
				this.entityHost.doBreathAttack(target);
			}
		}
	}

	@Override
	public void resetTask() {
		this.durationLeft = 0;
		this.attackTarget = null;
		// set breather flag
		this.entityHost.setBreathing(false);
	}

	/**
	 * What, if anything, is the head currently looking at?
	 */
	private Entity getHeadLookTarget() {
		Entity pointedEntity = null;
		double range = 30.0D;
		double offset = 3.0D;
		Vector3d srcVec = new Vector3d(this.entityHost.getPosX(), this.entityHost.getPosY() + 0.25, this.entityHost.getPosZ());
		Vector3d lookVec = this.entityHost.getLook(1.0F);
		Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		float var9 = 0.5F;
		List<Entity> possibleList = this.entityHost.world.getEntitiesWithinAABBExcludingEntity(this.entityHost, this.entityHost.getBoundingBox().offset(lookVec.x * offset, lookVec.y * offset, lookVec.z * offset).grow(var9, var9, var9));
		double hitDist = 0;

		if(entityHost.isMultipartEntity())
		possibleList.removeAll(Arrays.asList(Objects.requireNonNull(entityHost.getParts())));

		for (Entity possibleEntity : possibleList) {
			if (possibleEntity.canBeCollidedWith() && possibleEntity != this.entityHost && EntityPredicates.CAN_HOSTILE_AI_TARGET.test(possibleEntity)) {
				float borderSize = possibleEntity.getCollisionBorderSize();
				AxisAlignedBB collisionBB = possibleEntity.getBoundingBox().grow(borderSize, borderSize, borderSize);
				Optional<Vector3d> interceptPos = collisionBB.rayTrace(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						pointedEntity = possibleEntity;
						hitDist = 0.0D;
					}
				} else if (interceptPos.isPresent()) {
					double possibleDist = srcVec.distanceTo(interceptPos.get());

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
		double xOffset = x - entityHost.getPosX();
		double zOffset = z - entityHost.getPosZ();
		double yOffset = (entityHost.getPosY() + 0.25) - y;

		double distance = MathHelper.sqrt(xOffset * xOffset + zOffset * zOffset);
		float xyAngle = (float) ((Math.atan2(zOffset, xOffset) * 180D) / Math.PI) - 90F;
		float zdAngle = (float) (-((Math.atan2(yOffset, distance) * 180D) / Math.PI));
		entityHost.rotationPitch = -updateRotation(entityHost.rotationPitch, zdAngle, pitchConstraint);
		entityHost.rotationYaw = updateRotation(entityHost.rotationYaw, xyAngle, yawConstraint);

	}

	/**
	 * Arguments: current rotation, intended rotation, max increment.
	 */
	private float updateRotation(float current, float target, float maxDelta) {
		float delta = MathHelper.wrapDegrees(target - current);

		if (delta > maxDelta) {
			delta = maxDelta;
		}

		if (delta < -maxDelta) {
			delta = -maxDelta;
		}

		return current + delta;
	}
}
