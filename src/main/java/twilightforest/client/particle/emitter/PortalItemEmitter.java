package twilightforest.client.particle.emitter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.world.entity.Entity;

public class PortalItemEmitter extends TrackingEmitter {
	public PortalItemEmitter(ClientLevel level, Entity entity) {
		super(level, entity, new SimpleParticleType(false), Integer.MAX_VALUE); // The particleType and lifeTime values here will not be used
	}

	@Override
	public void tick() {
		if (this.entity.isRemoved() || this.entity.level() != this.level) {
			this.remove();
			return;
		}

		for (int i = 0; i < 2; i++) {
			double vx = this.level.getRandom().nextGaussian() * 0.02D;
			double vy = this.level.getRandom().nextGaussian() * 0.02D;
			double vz = this.level.getRandom().nextGaussian() * 0.02D;
			this.level.addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F),  this.entity.getX(), this.entity.getY() + 0.2, this.entity.getZ(), vx, vy, vz);
		}
	}

	public boolean isTrackingEntity(Entity entity) {
		return this.entity == entity;
	}
}
