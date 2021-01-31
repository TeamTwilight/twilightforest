package twilightforest.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleSmokeScale extends SmokeParticle {

	ParticleSmokeScale(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ, float scale, IAnimatedSprite sprite) {
		super(world, x, y, z, velX, velY, velZ, scale, sprite);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleSmokeScale(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 4.0F, this.spriteSet);
		}
	}
}
