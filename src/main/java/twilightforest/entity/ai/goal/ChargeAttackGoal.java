package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.ITFCharger;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.util.entities.EntityUtil;

import java.util.EnumSet;

public class ChargeAttackGoal extends Goal {

	private static final double MIN_RANGE_SQ = 16.0D;
	private static final double MAX_RANGE_SQ = 64.0D;
	private static final int FREQ = 10;

	private final PathfinderMob charger;
	private LivingEntity chargeTarget;
	private Vec3 chargePos;

	protected final float speed;
	private final boolean canBreak;
	private int windup;
	private boolean hasAttacked;

	public ChargeAttackGoal(PathfinderMob mob, float f, boolean canBreak) {
		this.charger = mob;
		this.speed = f;
		this.canBreak = canBreak;
		this.windup = 0;
		this.hasAttacked = false;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		this.chargeTarget = this.charger.getTarget();

		if (this.chargeTarget == null) {
			return false;
		} else {
			double distance = this.charger.distanceToSqr(this.chargeTarget);
			//minoshroom will charge when the target is further away than the normal minotaur
			double minoshroomBonus = this.charger instanceof Minoshroom ? 9.0D : 0.0D;
			if (distance < MIN_RANGE_SQ + minoshroomBonus || distance > MAX_RANGE_SQ + minoshroomBonus) {
				return false;
			} else if (!this.charger.onGround()) {
				return false;
			} else {
				Vec3 chargePos = findChargePoint(this.charger, this.chargeTarget);
				boolean canSeeTargetFromDest = this.charger.getSensing().hasLineOfSight(this.chargeTarget);
				if (!canSeeTargetFromDest) {
					return false;
				} else {
					this.chargePos = chargePos;

					return this.charger.getRandom().nextInt(FREQ) == 0;
				}
			}
		}
	}

	@Override
	public void start() {
		this.windup = 15 + this.charger.getRandom().nextInt(30);
		this.charger.setSprinting(true);
	}

	@Override
	public boolean canContinueToUse() {
		return this.windup > 0 || !this.charger.getNavigation().isDone();
	}

	@Override
	public void tick() {
		// look where we're going
		this.charger.getLookControl().setLookAt(this.chargePos.x(), this.chargePos.y() - 1, this.chargePos.z(), 10.0F, this.charger.getMaxHeadXRot());

		if (this.windup > 0) {
			if (--this.windup == 0) {
				// actually charge!

				this.charger.getNavigation().moveTo(this.chargePos.x(), this.chargePos.y(), this.chargePos.z(), this.speed);
			} else {
				this.charger.walkAnimation.setSpeed(this.charger.walkAnimation.speed() + 0.8F);

				if (this.charger instanceof ITFCharger chargeMob) {
					chargeMob.setCharging(true);
				}
			}
		} else if (this.canBreak) {
			if (!this.charger.level().isClientSide() && EventHooks.canEntityGrief(getServerLevel(this.charger), this.charger)) {

				AABB bb = this.charger.getBoundingBox();
				int minx = Mth.floor(bb.minX - 0.75D);
				int miny = Mth.floor(bb.minY + 0.0D);
				int minz = Mth.floor(bb.minZ - 0.75D);
				int maxx = Mth.floor(bb.maxX + 0.75D);
				int maxy = Mth.floor(bb.maxY + 0.15D);
				int maxz = Mth.floor(bb.maxZ + 0.75D);

				BlockPos min = new BlockPos(minx, miny, minz);
				BlockPos max = new BlockPos(maxx, maxy, maxz);

				if (this.charger.level().hasChunksAt(min, max)) {
					for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
						if (EntityUtil.canDestroyBlock(this.charger.level(), pos, this.charger) && this.charger.level().getBlockEntity(pos) == null) {
							this.charger.level().destroyBlock(pos, true);
						}
					}
				}
			}
		}

		// attack the target when we get in range
		double rangeSq = this.charger instanceof Minoshroom ? 5.0D : this.charger.getBbWidth() * 2.0F * this.charger.getBbWidth() * 2.0F + this.chargeTarget.getBbWidth();

		if (this.charger.distanceToSqr(this.chargeTarget.getX(), this.chargeTarget.getBoundingBox().minY, this.chargeTarget.getZ()) <= rangeSq) {
			if (!this.hasAttacked) {
				this.hasAttacked = true;
				this.charger.doHurtTarget(getServerLevel(this.charger), this.chargeTarget);
			}
		}
	}

	@Override
	public void stop() {
		this.windup = 0;
		this.chargeTarget = null;
		this.hasAttacked = false;
		this.charger.setSprinting(false);

		if (this.charger instanceof ITFCharger chargeMob) {
			chargeMob.setCharging(false);
		}
	}


	/**
	 * Finds a point that is overshoot blocks "beyond" the target from our position.
	 */
	protected Vec3 findChargePoint(Entity attacker, Entity target) {

		// compute angle
		double vecx = target.getX() - attacker.getX();
		double vecz = target.getZ() - attacker.getZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		double distance = Mth.sqrt((float) (vecx * vecx + vecz * vecz));
		double overshoot = 2.1D;

		// figure out where we're headed from the target angle
		double dx = Mth.cos(rangle) * (distance + overshoot);
		double dz = Mth.sin(rangle) * (distance + overshoot);

		// add that to the target entity's position, and we have our destination
		return new Vec3(attacker.getX() + dx, target.getY(), attacker.getZ() + dz);
	}
}
