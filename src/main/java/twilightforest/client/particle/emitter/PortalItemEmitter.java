package twilightforest.client.particle.emitter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.world.entity.Entity;

public class PortalItemEmitter extends TrackingEmitter {
	private final Entity entity;
	private final ClientLevel level;

	public PortalItemEmitter(ClientLevel level, Entity entity) {
		super(level, entity, new SimpleParticleType(false), Integer.MAX_VALUE); // The particleType and lifeTime values here will not be used
		this.entity = entity;
		this.level = level;
	}

	@Override
	public void tick() {
		if (this.entity == null) // TrackingEmitter will call the overridden tick method from its ctor, so we need a guard
			return;

		if (this.entity.isRemoved() || this.entity.level() != this.level) {
			this.remove();
			return;
		}

		for (int i = 0; i < 2; i++) {
			double vx = level.getRandom().nextGaussian() * 0.02D;
			double vy = level.getRandom().nextGaussian() * 0.02D;
			double vz = level.getRandom().nextGaussian() * 0.02D;
			level.addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F),  entity.getX(), entity.getY() + 0.2, entity.getZ(), vx, vy, vz);
		}
	}

	public boolean isTrackingEntity(Entity entity) {
		return this.entity == entity;
	}
}
