package twilightforest.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleSnowWarning extends ParticleSnow {

	ParticleSnowWarning(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, vx, vy, vz, scale);
		this.maxAge = 50;
	}

	@Override
	public void tick() {
		super.tick();
		this.motionY -= 0.019999999552965164D;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleSnowWarning particle = new ParticleSnowWarning(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 1);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
