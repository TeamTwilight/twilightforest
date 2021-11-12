package twilightforest.entity.monster;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import twilightforest.client.particle.TFParticleType;

public abstract class BaseIceMob extends Monster {
	public BaseIceMob(EntityType<? extends BaseIceMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void aiStep() {
		if (!this.onGround && this.getDeltaMovement().y() < 0.0D) {
			Vec3 motion = getDeltaMovement();
			this.setDeltaMovement(motion.x, motion.y * 0.6D, motion.z);
		}
		super.aiStep();
		if (this.level.isClientSide) {
			// make snow particles
			for (int i = 0; i < 3; i++) {
				float px = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.random.nextFloat() - this.random.nextFloat()) * 0.5F;
				float pz = (this.random.nextFloat() - this.random.nextFloat()) * 0.3F;

				level.addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				if (this.level.getBiome(this.blockPosition()).getTemperature(this.blockPosition()) > 1.0F || this.isOnFire()) {
					this.level.addParticle(ParticleTypes.CLOUD, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0.1F, 0);
					this.level.addParticle(ParticleTypes.DRIPPING_WATER, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				}
			}
		}
		if (this.level.getBiome(this.blockPosition()).getTemperature(this.blockPosition()) > 1.0F && this.tickCount % 20 == 0) {
			//BURN!!!
			this.hurt(DamageSource.ON_FIRE, 1.0F);
		}
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}
}
