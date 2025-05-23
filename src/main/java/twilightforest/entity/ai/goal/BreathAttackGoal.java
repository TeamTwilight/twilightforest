package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.IBreathAttacker;

import java.util.*;

public class BreathAttackGoal<T extends Mob & IBreathAttacker> extends Goal {

	private final T entityHost;
	private LivingEntity attackTarget;

	private Vec3 breathPos;

	private final int maxDuration;
	private final float attackChance;
	private final float breathRange;

	private int durationLeft;

	public BreathAttackGoal(T living, float range, int time, float chance) {
		this.entityHost = living;
		this.breathRange = range;
		this.maxDuration = time;
		this.attackChance = chance;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		this.attackTarget = this.entityHost.getLastHurtByMob();

		if (this.attackTarget == null || this.entityHost.distanceTo(attackTarget) > this.breathRange || !this.entityHost.getSensing().hasLineOfSight(attackTarget) || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(attackTarget)) {
			return false;
		} else {
			this.breathPos = this.attackTarget.getEyePosition();

			return this.entityHost.getRandom().nextFloat() < this.attackChance;
		}
	}

	/**
	 * Initialize counters
	 */
	@Override
	public void start() {
		this.durationLeft = this.maxDuration;
		// set breather flag
		this.entityHost.setBreathing(true);
	}

	/**
	 * Keep breathing until the target dies, or moves out of range or line of sight
	 */
	@Override
	public boolean canContinueToUse() {
		return this.durationLeft > 0 && this.entityHost.isAlive() && this.attackTarget.isAlive()
			&& this.entityHost.distanceTo(this.attackTarget) <= this.breathRange
			&& this.entityHost.getSensing().hasLineOfSight(this.attackTarget)
			&& EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(attackTarget);
	}

	/**
	 * Update timers, deal damage
	 */
	@Override
	public void tick() {
		this.durationLeft--;

		// why do we need both of these?
		this.entityHost.getLookControl().setLookAt(this.breathPos);
		this.faceVec(this.breathPos, 100.0F, 100.0F);

		if ((this.maxDuration - this.durationLeft) > 5) {
			// anyhoo, deal damage
			Entity target = this.getHeadLookTarget();
			if (target != null) {
				this.entityHost.doBreathAttack((ServerLevel) this.entityHost.level(), target);
				this.entityHost.gameEvent(GameEvent.PROJECTILE_SHOOT);
			}
		}
	}

	@Override
	public void stop() {
		this.durationLeft = 0;
		this.attackTarget = null;
		// set breather flag
		this.entityHost.setBreathing(false);
	}

	/**
	 * What, if anything, is the head currently looking at?
	 */
	@Nullable
	private Entity getHeadLookTarget() {
		Entity pointedEntity = null;
		double range = 30.0D;
		double offset = 3.0D;
		Vec3 srcVec = new Vec3(this.entityHost.getX(), this.entityHost.getY() + 0.25, this.entityHost.getZ());
		Vec3 lookVec = this.entityHost.getViewVector(1.0F);
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
		float var9 = 0.5F;
		List<Entity> possibleList = this.entityHost.level().getEntities(this.entityHost, this.entityHost.getBoundingBox().move(lookVec.x() * offset, lookVec.y() * offset, lookVec.z() * offset).inflate(var9, var9, var9));
		double hitDist = 0;

		if (this.entityHost.isMultipartEntity())
			possibleList.removeAll(Arrays.asList(Objects.requireNonNull(this.entityHost.getParts())));

		for (Entity possibleEntity : possibleList) {
			if (possibleEntity.isPickable() && possibleEntity != this.entityHost && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(possibleEntity)) {
				float borderSize = possibleEntity.getPickRadius();
				AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
				Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);

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
	public void faceVec(Vec3 pos, float yawConstraint, float pitchConstraint) {
		double xOffset = pos.x() - this.entityHost.getX();
		double zOffset = pos.z() - this.entityHost.getZ();
		double yOffset = (this.entityHost.getY() + 0.25) - pos.y();

		double distance = Mth.sqrt((float) (xOffset * xOffset + zOffset * zOffset));
		float xyAngle = (float) ((Math.atan2(zOffset, xOffset) * 180D) / Math.PI) - 90F;
		float zdAngle = (float) (-((Math.atan2(yOffset, distance) * 180D) / Math.PI));
		this.entityHost.setXRot(-updateRotation(this.entityHost.getXRot(), zdAngle, pitchConstraint));
		this.entityHost.setYRot(updateRotation(this.entityHost.getYRot(), xyAngle, yawConstraint));

	}

	/**
	 * Arguments: current rotation, intended rotation, max increment.
	 */
	private float updateRotation(float current, float target, float maxDelta) {
		float delta = Mth.wrapDegrees(target - current);

		if (delta > maxDelta) {
			delta = maxDelta;
		}

		if (delta < -maxDelta) {
			delta = -maxDelta;
		}

		return current + delta;
	}
}
