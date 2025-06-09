package twilightforest.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import twilightforest.init.TFSounds;

public abstract class TFThrowable extends ThrowableProjectile implements ITFProjectile {

	public TFThrowable(EntityType<? extends TFThrowable> type, Level level) {
		super(type, level);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level level, double x, double y, double z) {
		super(type, x, y, z, level);
	}

	public TFThrowable(EntityType<? extends TFThrowable> type, Level level, LivingEntity thrower) {
		super(type, thrower.getX(), thrower.getEyeY() - 0.1F, thrower.getZ(), level);
		this.setOwner(thrower);
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
