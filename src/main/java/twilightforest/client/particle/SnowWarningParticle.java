package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SnowWarningParticle extends SnowParticle {

	SnowWarningParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float scale, int lifetime, TextureAtlasSprite sprite) {
		super(level, x, y, z, vx, vy, vz, scale, sprite);
		this.lifetime = lifetime;
	}

	@Override
	public void tick() {
		super.tick();
		this.yd -= 0.02D;
	}

	public record SimpleFactory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			SnowWarningParticle particle = new SnowWarningParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, 50, this.sprite.get(random));
			return particle;
		}
	}

	public record ExtendedFactory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			SnowWarningParticle particle = new SnowWarningParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, 100, this.sprite.get(random));
			return particle;
		}
	}
}
