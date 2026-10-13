package twilightforest.client.particle.emitter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.world.entity.Entity;

public class PortalItemEmitter extends NoRenderParticle {

	private final SpellParticleOption particle = SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F);
	private final Entity entity;

	public PortalItemEmitter(ClientLevel level, Entity entity, int lifetime) {
		super(level, entity.getX(), entity.getY(), entity.getZ());
		this.entity = entity;
		setLifetime(lifetime);
	}

	@Override
	public void tick() {
		if (age++ >= lifetime || entity.isRemoved() || entity.level() != level) {
			remove();
			return;
		}

		for (int i = 0; i < 2; i++) {
			double vx = random.nextGaussian() * 0.02D;
			double vy = random.nextGaussian() * 0.02D;
			double vz = random.nextGaussian() * 0.02D;
			level.addParticle(particle, entity.getX(), entity.getY() + 0.2, entity.getZ(), vx, vy, vz);
		}
	}
}
