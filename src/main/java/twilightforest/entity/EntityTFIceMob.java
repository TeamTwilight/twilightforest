package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import twilightforest.client.particle.TFParticleType;

public abstract class EntityTFIceMob extends MonsterEntity {
	public EntityTFIceMob(EntityType<? extends EntityTFIceMob> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public void livingTick() {
		if (!this.onGround && this.getMotion().getY() < 0.0D) {
			Vector3d motion = getMotion();
			this.setMotion(motion.x, motion.y * 0.6D, motion.z);
		}
		super.livingTick();
		if (this.world.isRemote) {
			// make snow particles
			for (int i = 0; i < 3; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
				float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

				world.addParticle(TFParticleType.SNOW_GUARDIAN.get(), this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
				if (this.world.getBiome(this.getPosition()).getTemperature(this.getPosition()) > 1.0F || this.isBurning()) {
					this.world.addParticle(ParticleTypes.CLOUD, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0.1F, 0);
					this.world.addParticle(ParticleTypes.DRIPPING_WATER, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
				}
			}
		}
		if (this.world.getBiome(this.getPosition()).getTemperature(this.getPosition()) > 1.0F && this.ticksExisted % 20 == 0) {
			//BURN!!!
			this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
		}
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}
}
