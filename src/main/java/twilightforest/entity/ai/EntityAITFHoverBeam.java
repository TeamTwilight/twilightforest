package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen.Phase;

import java.util.List;

public class EntityAITFHoverBeam extends EntityAIBase {

	private static final float HOVER_HEIGHT = 3F;
	private static final float HOVER_RADIUS = 4F;
	private Class<? extends EntityLivingBase> classTarget;
	private EntityTFSnowQueen attacker;

	private double hoverPosX;
	private double hoverPosY;
	private double hoverPosZ;
	private int hoverTimer;
	private int beamTimer;
	private int maxHoverTime;
	private int maxBeamTime;
	private int seekTimer;
	private int maxSeekTime;
	private double beamY;
	private boolean isInPosition;

	public EntityAITFHoverBeam(EntityTFSnowQueen entityTFSnowQueen, Class<EntityPlayer> class1, int hoverTime, int dropTime) {
		this.attacker = entityTFSnowQueen;
		this.classTarget = class1;
		this.setMutexBits(3);
		this.maxHoverTime = hoverTime;
		this.maxSeekTime = hoverTime;
		this.maxBeamTime = dropTime;

		this.hoverTimer = 0;
		this.isInPosition = false;
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (this.classTarget != null && !this.classTarget.isAssignableFrom(target.getClass())) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.BEAM) {
			return false;
		} else {
			return true;//attacker.canEntityBeSeen(target);
		}

	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null || !target.isEntityAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.BEAM) {
			return false;
		} else if (this.seekTimer >= this.maxSeekTime) {
			return false;
		} else if (this.beamTimer >= this.maxBeamTime) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void startExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target != null) {
			// find a spot above the player
			makeNewHoverSpot(target);
		}
	}

	@Override
	public void resetTask() {
		this.seekTimer = 0;
		this.hoverTimer = 0;
		this.beamTimer = 0;
		this.isInPosition = false;

		this.attacker.setBreathing(false);
	}

	@Override
	public void updateTask() {

		// check if we're in position
		if (this.attacker.getDistanceSq(hoverPosX, hoverPosY, hoverPosZ) <= 1.0F) {
			this.isInPosition = true;
		}

		// update timers
		if (this.isInPosition) {
			this.hoverTimer++;
		} else {
			this.seekTimer++;
		}

		// drop once the hover timer runs out
		if (this.hoverTimer >= this.maxHoverTime) {
			this.beamTimer++;
			this.attacker.setBreathing(true);

			// anyhoo, deal damage
			doRayAttack();

			// descend
			this.hoverPosY -= 0.05F;

			if (this.hoverPosY < this.beamY) {
				this.hoverPosY = this.beamY;
			}
		}

		// check if we are at our waypoint target
		double offsetX = this.hoverPosX - this.attacker.posX;
		double offsetY = this.hoverPosY - this.attacker.posY;
		double offsetZ = this.hoverPosZ - this.attacker.posZ;

		double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

		distanceDesired = (double) MathHelper.sqrt(distanceDesired);

		if (distanceDesired > 0.5) {

			// add velocity
			double velX = offsetX / distanceDesired * 0.05D;
			double velY = offsetY / distanceDesired * 0.1D;
			double velZ = offsetZ / distanceDesired * 0.05D;

			// gravity offset
			velY += 0.02F;


			this.attacker.addVelocity(velX, velY, velZ);
		}


		// look at target
		EntityLivingBase target = this.attacker.getAttackTarget();
		if (target != null) {
			float tracking = this.isInPosition ? 1F : 20.0F;

			this.attacker.faceEntity(target, tracking, tracking);
			this.attacker.getLookHelper().setLookPositionWithEntity(target, tracking, tracking);
		}
	}

	private void doRayAttack() {
		double range = 20.0D;
		double offset = 10.0D;
		Vec3d srcVec = new Vec3d(this.attacker.posX, this.attacker.posY + 0.25, this.attacker.posZ);
		Vec3d lookVec = this.attacker.getLook(1.0F);
		Vec3d destVec = srcVec.addVector(lookVec.x * range, lookVec.y * range, lookVec.z * range);
		List<Entity> possibleList = this.attacker.world.getEntitiesWithinAABBExcludingEntity(this.attacker, this.attacker.getEntityBoundingBox().offset(lookVec.x * offset, lookVec.y * offset, lookVec.z * offset).grow(range, range, range));
		double hitDist = 0;

		for (Entity possibleEntity : possibleList) {
			if (possibleEntity.canBeCollidedWith() && possibleEntity != this.attacker) {
				float borderSize = possibleEntity.getCollisionBorderSize();
				AxisAlignedBB collisionBB = possibleEntity.getEntityBoundingBox().grow((double) borderSize, (double) borderSize, (double) borderSize);
				RayTraceResult interceptPos = collisionBB.calculateIntercept(srcVec, destVec);

				if (collisionBB.contains(srcVec)) {
					if (0.0D < hitDist || hitDist == 0.0D) {
						attacker.doBreathAttack(possibleEntity);
						hitDist = 0.0D;
					}
				} else if (interceptPos != null) {
					double possibleDist = srcVec.distanceTo(interceptPos.hitVec);

					if (possibleDist < hitDist || hitDist == 0.0D) {
						attacker.doBreathAttack(possibleEntity);
						hitDist = possibleDist;
					}
				}
			}
		}
	}


	/**
	 * Make a new spot to hover at!
	 */
	private void makeNewHoverSpot(EntityLivingBase target) {
		double hx = 0, hy = 0, hz = 0;

		int tries = 100;

		for (int i = 0; i < tries; i++) {
			hx = target.posX + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * HOVER_RADIUS;
			hy = target.posY + HOVER_HEIGHT;
			hz = target.posZ + (this.attacker.getRNG().nextFloat() - this.attacker.getRNG().nextFloat()) * HOVER_RADIUS;

			if (!isPositionOccupied(hx, hy, hz) && this.canEntitySee(this.attacker, hx, hy, hz) && this.canEntitySee(target, hx, hy, hz)) {
				break;
			}
		}

		this.hoverPosX = hx;
		this.hoverPosY = hy;
		this.hoverPosZ = hz;

		this.beamY = target.posY;

		// reset seek timer
		this.seekTimer = 0;
	}

	private boolean isPositionOccupied(double hx, double hy, double hz) {
		float radius = this.attacker.width / 2F;
		AxisAlignedBB aabb = new AxisAlignedBB(hx - radius, hy, hz - radius, hx + radius, hy + this.attacker.height, hz + radius);

		boolean isOccupied = this.attacker.world.getCollisionBoxes(attacker, aabb).isEmpty();

		return isOccupied;
	}

	/**
	 * Can the specified entity see the specified location?
	 */
	protected boolean canEntitySee(Entity entity, double dx, double dy, double dz) {
		return entity.world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), new Vec3d(dx, dy, dz)) == null;

	}
}
