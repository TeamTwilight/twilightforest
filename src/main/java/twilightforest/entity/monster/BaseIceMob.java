package twilightforest.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFParticleType;

public abstract class BaseIceMob extends Monster {
	public BaseIceMob(EntityType<? extends BaseIceMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void aiStep() {
		if (!this.onGround() && this.getDeltaMovement().y() < 0.0D) {
			Vec3 motion = getDeltaMovement();
			this.setDeltaMovement(motion.x(), motion.y() * 0.6D, motion.z());
		}
		super.aiStep();
		if (this.level().isClientSide() && !this.isDeadOrDying()) {
			// make snow particles
			for (int i = 0; i < 3; i++) {
				float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.5F;
				float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.3F;

				this.level().addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				if (this.level().getBiome(this.blockPosition()).is(BiomeTags.SNOW_GOLEM_MELTS)) {
					if (this.random.nextInt(4) == 0) this.level().addParticle(ParticleTypes.CLOUD, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0.1F, 0);
					if (this.random.nextBoolean()) this.level().addParticle(ParticleTypes.FALLING_WATER, this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
				}
			}
		}
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);
		if (this.tickCount % 20 == 0 && level.getBiome(this.blockPosition()).is(BiomeTags.SNOW_GOLEM_MELTS)) {
			//BURN!!!
			this.hurtServer(level, this.damageSources().onFire(), 1.0F);
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}
}
