package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;
import twilightforest.TFSounds;
import twilightforest.entity.boss.EntityTFIceBomb;
import twilightforest.entity.boss.EntityTFYetiAlpha;

public class EntityAITFYetiRampage extends EntityAIBase {

	private EntityTFYetiAlpha yeti;
	private int currentTimeOut;
	private int currentDuration;
	private int maxTantrumTimeOut;
	private int tantrumDuration;

	public EntityAITFYetiRampage(EntityTFYetiAlpha entityTFYetiAlpha, int timeout, int duration) {
		this.yeti = entityTFYetiAlpha;
		this.currentTimeOut = timeout;
		this.maxTantrumTimeOut = timeout;
		this.tantrumDuration = duration;

		this.setMutexBits(5);

	}

	@Override
	public boolean shouldExecute() {
		if (this.yeti.getAttackTarget() != null && this.yeti.canRampage()) {
			this.currentTimeOut--;
		}

		return this.currentTimeOut <= 0;
	}


	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.currentDuration = this.tantrumDuration;
		this.yeti.setRampaging(true);
		this.yeti.playSound(TFSounds.ALPHAYETI_ROAR, 4F, 0.5F + yeti.getRNG().nextFloat() * 0.5F);
	}


	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return currentDuration > 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		this.currentDuration--;

//    	int rx = MathHelper.floor(this.yeti.posX);
//    	int ry = MathHelper.floor(this.yeti.posY - 1);
//    	int rz = MathHelper.floor(this.yeti.posZ);
//    	
//		this.yeti.world.playAuxSFX(2004, rx, ry, rz, 0);

		if (this.yeti.getAttackTarget() != null) {
			this.yeti.getLookHelper().setLookPositionWithEntity(this.yeti.getAttackTarget(), 10.0F, (float) this.yeti.getVerticalFaceSpeed());
		}

		if (this.yeti.onGround) {
			this.yeti.motionX = 0;
			this.yeti.motionZ = 0;
			this.yeti.motionY = 0.4F;
		}

		this.yeti.destroyBlocksInAABB(this.yeti.getEntityBoundingBox().grow(1, 2, 1).offset(0, 2, 0));

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
			EntityTFIceBomb ice = new EntityTFIceBomb(yeti.world, yeti);
			Vec3d vec = new Vec3d(0.5F + yeti.getRNG().nextFloat() * 0.5F, 0.5F + yeti.getRNG().nextFloat() * 0.3F, 0).rotateYaw(yeti.getRNG().nextFloat() * 360F);
			ice.shoot(vec.x, vec.y, vec.z, 0.4F + yeti.getRNG().nextFloat() * 0.3F, 0);
			yeti.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (yeti.getRNG().nextFloat() * 0.4F + 0.8F));
			yeti.world.spawnEntity(ice);
		}
	}


	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		this.currentTimeOut = this.maxTantrumTimeOut;
		this.yeti.setRampaging(false);
		this.yeti.setTired(true);

	}
}
