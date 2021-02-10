package twilightforest.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFSnowQueen.Phase;

import java.util.EnumSet;

public class EntityAITFHoverThenDrop extends EntityAITFHoverBase<EntityTFSnowQueen> {

	private int hoverTimer;
	private int dropTimer;
	private int seekTimer;

	private final int maxHoverTime;
	private final int maxDropTime;
	private final int maxSeekTime;

	private double dropY;

	public EntityAITFHoverThenDrop(EntityTFSnowQueen snowQueen, int hoverTime, int dropTime) {
		super(snowQueen, 6F, 0F);

		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		this.maxHoverTime = hoverTime;
		this.maxSeekTime = hoverTime;
		this.maxDropTime = dropTime;

		this.hoverTimer = 0;
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity target = this.attacker.getAttackTarget();

		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.DROP) {
			return false;
		} else {
			return true;//attacker.canEntityBeSeen(target);
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		LivingEntity target = this.attacker.getAttackTarget();

		if (target == null || !target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.DROP) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else if (this.attacker.getDistanceSq(hoverPosX, hoverPosY, hoverPosZ) <= 1.0F) {
			// are we there yet?
			this.hoverTimer++;
			return true;
		} else if (this.dropTimer < this.maxDropTime) {
			return true;
		} else {
			// max drop time!
			this.attacker.incrementSuccessfulDrops();
			return false;
		}
	}

	@Override
	public void resetTask() {
		this.hoverTimer = 0;
		this.dropTimer = 0;
	}

	@Override
	public void tick() {

		// if we have hit the drop spot, start dropping
		if (this.hoverTimer > 0) {
			this.hoverTimer++;
		} else {
			this.seekTimer++;
		}

		if (this.hoverTimer < this.maxHoverTime) {

			// check if we are at our waypoint target
			double offsetX = this.hoverPosX - this.attacker.getPosX();
			double offsetY = this.hoverPosY - this.attacker.getPosY();
			double offsetZ = this.hoverPosZ - this.attacker.getPosZ();

			double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

			distanceDesired = MathHelper.sqrt(distanceDesired);

			// add velocity
			double velX = offsetX / distanceDesired * 0.05D;
			double velY = offsetY / distanceDesired * 0.1D;
			double velZ = offsetZ / distanceDesired * 0.05D;

			// gravity offset
			velY += 0.05F;

			this.attacker.addVelocity(velX, velY, velZ);

			// look at target
			LivingEntity target = this.attacker.getAttackTarget();
			if (target != null) {
				this.attacker.faceEntity(target, 30.0F, 30.0F);
				this.attacker.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
			}
		} else {
			// drop!
			this.dropTimer++;

			if (this.attacker.getPosY() > this.dropY) {
				this.attacker.destroyBlocksInAABB(this.attacker.getBoundingBox().grow(1, 0.5F, 1));
			}
		}
	}

	@Override
	protected void makeNewHoverSpot(LivingEntity target) {
		super.makeNewHoverSpot(target);
		this.dropY = target.getPosY() - 1F;
		this.seekTimer = 0;
	}
}
