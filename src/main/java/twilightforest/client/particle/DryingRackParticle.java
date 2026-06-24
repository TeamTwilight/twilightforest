package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class DryingRackParticle extends BaseAshSmokeParticle {

	public DryingRackParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float quadSizeMultiplier, SpriteSet sprites) {
		super(level, x, y, z, 0.0F, 0.0F, 0.0F, xSpeed, ySpeed, zSpeed, quadSizeMultiplier, sprites, 0.3F, 16, -0.025F, true);
		this.rCol = 0.7294118F;
		this.gCol = 0.69411767F;
		this.bCol = 0.7607843F;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new DryingRackParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 0.5F, this.sprites);
		}
	}
}
