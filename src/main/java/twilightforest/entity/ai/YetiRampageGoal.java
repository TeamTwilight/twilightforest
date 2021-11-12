package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.entity.boss.AlphaYeti;

import java.util.EnumSet;

public class YetiRampageGoal extends Goal {

	private AlphaYeti yeti;
	private int currentTimeOut;
	private int currentDuration;
	private int maxTantrumTimeOut;
	private int tantrumDuration;

	public YetiRampageGoal(AlphaYeti entityTFYetiAlpha, int timeout, int duration) {
		this.yeti = entityTFYetiAlpha;
		this.currentTimeOut = timeout;
		this.maxTantrumTimeOut = timeout;
		this.tantrumDuration = duration;

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (this.yeti.getTarget() != null && this.yeti.canRampage()) {
			this.currentTimeOut--;
		}

		return this.currentTimeOut <= 0;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.currentDuration = this.tantrumDuration;
		this.yeti.setRampaging(true);
		this.yeti.playSound(TFSounds.ALPHAYETI_ROAR, 4F, 0.5F + yeti.getRandom().nextFloat() * 0.5F);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse() {
		return currentDuration > 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void tick() {
		this.currentDuration--;

        if (this.yeti.getTarget() != null) {
			this.yeti.getLookControl().setLookAt(this.yeti.getTarget(), 10.0F, this.yeti.getMaxHeadXRot());
		}

		if (this.yeti.isOnGround()) {
            this.yeti.setDeltaMovement(0, 0.4, 0);
		}

		this.yeti.destroyBlocksInAABB(this.yeti.getBoundingBox().inflate(1, 2, 1).move(0, 2, 0));

		// regular falling blocks
		if (this.currentDuration % 20 == 0) {
			this.yeti.makeRandomBlockFall();
		}

		// blocks target players
		if (this.currentDuration % 40 == 0) {
			this.yeti.makeBlockAboveTargetFall();
		}

		// blocks target players
		if (this.currentDuration < 40 && this.currentDuration % 10 == 0) {
			this.yeti.makeNearbyBlockFall();
		}

		if (currentDuration % 10 == 0) {
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE, yeti.level, yeti);
			Vec3 vec = new Vec3(0.5F + yeti.getRandom().nextFloat() * 0.5F, 0.5F + yeti.getRandom().nextFloat() * 0.3F, 0).yRot(yeti.getRandom().nextFloat() * 360F);
			ice.shoot(vec.x, vec.y, vec.z, 0.4F + yeti.getRandom().nextFloat() * 0.3F, 0);
			yeti.playSound(TFSounds.ALPHAYETI_ICE, 1.0F, 1.0F / (yeti.getRandom().nextFloat() * 0.4F + 0.8F));
			yeti.level.addFreshEntity(ice);
		}
	}

	/**
	 * Resets the task
	 */
	@Override
	public void stop() {
		this.currentTimeOut = this.maxTantrumTimeOut;
		this.yeti.setRampaging(false);
		this.yeti.setTired(true);

	}
}
