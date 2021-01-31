package twilightforest.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleSnowGuardian extends ParticleSnow {

	ParticleSnowGuardian(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, vx, vy, vz, scale);
		this.maxAge = 10 + this.rand.nextInt(15);
		this.particleRed = this.particleGreen = this.particleBlue = 0.75F + this.rand.nextFloat() * 0.25F;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleSnowGuardian particle = new ParticleSnowGuardian(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 0.75F);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
