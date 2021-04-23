package twilightforest.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class ParticleProtection extends SuspendedTownParticle {

	ParticleProtection(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 0xF000F0;
	}

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle makeParticle(BasicParticleType data, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
			ParticleProtection particle = new ParticleProtection(world, x, y, z, vx, vy, vz);
			particle.selectSpriteRandomly(this.spriteSet);
			particle.setColor(1.0F, 1.0F, 1.0F);
			return particle;
		}
	}

}
