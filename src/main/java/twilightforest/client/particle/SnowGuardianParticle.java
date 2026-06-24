package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SnowGuardianParticle extends SnowParticle {

	public SnowGuardianParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, float scale, TextureAtlasSprite sprite) {
		super(world, x, y, z, vx, vy, vz, scale, sprite);
		this.lifetime = 10 + this.random.nextInt(15);
		this.rCol = this.gCol = this.bCol = 0.75F + this.random.nextFloat() * 0.25F;
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			SnowGuardianParticle particle = new SnowGuardianParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 0.75F, this.sprite.get(random));
			return particle;
		}
	}
}
