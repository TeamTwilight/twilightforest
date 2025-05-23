package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.entity.boss.SnowQueen.Phase;

import java.util.EnumSet;

public class HoverThenDropGoal extends HoverBaseGoal<SnowQueen> {

	private int hoverTimer;
	private int dropTimer;
	private int seekTimer;

	private final int maxHoverTime;
	private final int maxDropTime;
	private final int maxSeekTime;

	private double dropY;

	public HoverThenDropGoal(SnowQueen snowQueen, int hoverTime, int dropTime) {
		super(snowQueen, 6F, 0F);

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		this.maxHoverTime = hoverTime;
		this.maxSeekTime = hoverTime;
		this.maxDropTime = dropTime;

		this.hoverTimer = 0;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else {
			return this.attacker.getCurrentPhase() == Phase.DROP;
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = this.attacker.getTarget();

		if (target == null || !target.isAlive()) {
			return false;
		} else if (this.attacker.getCurrentPhase() != Phase.DROP) {
			return false;
		} else if (this.seekTimer > this.maxSeekTime) {
			return false;
		} else if (this.attacker.distanceToSqr(this.hoverPosX, this.hoverPosY, this.hoverPosZ) <= 1.0F) {
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
	public void stop() {
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
			double offsetX = this.hoverPosX - this.attacker.getX();
			double offsetY = this.hoverPosY - this.attacker.getY();
			double offsetZ = this.hoverPosZ - this.attacker.getZ();

			double distanceDesired = offsetX * offsetX + offsetY * offsetY + offsetZ * offsetZ;

			distanceDesired = Mth.sqrt((float) distanceDesired);

			// add velocity
			double velX = offsetX / distanceDesired * 0.05D;
			double velY = offsetY / distanceDesired * 0.1D;
			double velZ = offsetZ / distanceDesired * 0.05D;

			// gravity offset
			velY += 0.05D;

			this.attacker.push(velX, velY, velZ);

			// look at target
			LivingEntity target = this.attacker.getTarget();
			if (target != null) {
				this.attacker.lookAt(target, 30.0F, 30.0F);
				this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
			}
		} else {
			// drop!
			this.dropTimer++;
			if (this.attacker.getY() > this.dropY) {
				this.attacker.destroyBlocksInAABB(getServerLevel(this.attacker), this.attacker.getBoundingBox().inflate(1, 0.5F, 1));
			}
		}
	}

	@Override
	protected void makeNewHoverSpot(LivingEntity target) {
		super.makeNewHoverSpot(target);
		this.dropY = target.getY() - 1F;
		this.seekTimer = 0;
	}
}
