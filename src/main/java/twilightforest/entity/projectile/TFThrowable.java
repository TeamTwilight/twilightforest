package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public abstract class TFThrowable extends ThrowableProjectile implements ITFProjectile {

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn) {
		super(type, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, double x, double y, double z) {
		super(type, x, y, z, worldIn);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level worldIn, LivingEntity throwerIn) {
		super(type, throwerIn, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	public void makeTrail(ParticleOptions particle, int amount) {
		this.makeTrail(particle, 0.0D, 0.0D, 0.0D, amount);
	}

	public void makeTrail(ParticleOptions particle, double r, double g, double b, int amount) {
		for (int i = 0; i < amount; i++) {
			double dx = this.getX() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dy = this.getY() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			double dz = this.getZ() + 0.5 * (this.random.nextDouble() - this.random.nextDouble());
			this.level().addParticle(particle, dx, dy, dz, r, g, b);
		}
	}
}
