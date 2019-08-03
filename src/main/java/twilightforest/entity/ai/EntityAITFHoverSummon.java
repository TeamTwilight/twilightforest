package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen.Phase;

public class EntityAITFHoverSummon extends EntityAITFHoverBase<EntityTFSnowQueen> {

	private static final int MAX_MINIONS_AT_ONCE = 4;

	private int seekTimer;

	private final int maxSeekTime;

	public EntityAITFHoverSummon(EntityTFSnowQueen snowQueen, double speed) {
		super(snowQueen, 6F, 6F);

		this.setMutexBits(3);
		this.maxSeekTime = 80;
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null) {
			return false;
		} else if (!target.isEntityAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else {
			return attacker.getEntitySenses().canSee(target);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase target = this.attacker.getAttackTarget();

		if (target == null || !target.isEntityAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.SUMMON) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else {
			return this.canEntitySee(this.attacker, hoverPosX, hoverPosY, hoverPosZ);
		}
	}

	@Override
	public void resetTask() {
	}

	@Override
	public void updateTask() {

		this.seekTimer++;
		EntityLivingBase target = this.attacker.getAttackTarget();

		// are we there yet?
		if (this.attacker.getDistanceSq(hoverPosX, hoverPosY, hoverPosZ) <= 1.0F) {
			this.checkAndSummon();

			this.makeNewHoverSpot(target);
		}

		// check if we are at our waypoint target
		double offsetX = this.hoverPosX - this.attacker.posX;
		double offsetY = this.hoverPosY - this.attacker.posY;
		double offsetZ = this.hoverPosZ - this.attacker.posZ;

		double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

		distanceDesired = (double) MathHelper.sqrt(distanceDesired);

		// add velocity
		double velX = offsetX / distanceDesired * 0.05D;
		double velY = offsetY / distanceDesired * 0.1D;
		double velZ = offsetZ / distanceDesired * 0.05D;

		// gravity offset
		velY += 0.05F;

		this.attacker.addVelocity(velX, velY, velZ);

		// look at target
		if (target != null) {
			this.attacker.faceEntity(target, 30.0F, 30.0F);
			this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
		}
	}

	@Override
	protected void makeNewHoverSpot(EntityLivingBase target) {
		super.makeNewHoverSpot(target);
		this.seekTimer = 0;
	}

	private void checkAndSummon() {
		if (this.attacker.getSummonsRemaining() > 0 && this.attacker.countMyMinions() < MAX_MINIONS_AT_ONCE) {
			this.attacker.summonMinionAt(this.attacker.getAttackTarget());
		}
	}
}
