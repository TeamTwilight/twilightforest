package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ProtectionParticle extends SuspendedTownParticle {

	public ProtectionParticle(ClientLevel level, double x, double y, double z, double velX, double velY, double velZ, TextureAtlasSprite sprite) {
		super(level, x, y, z, velX, velY, velZ, sprite);
	}

	@Override
	public int getLightCoords(float partialTicks) {
		return 0xF000F0;
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz, RandomSource random) {
			ProtectionParticle particle = new ProtectionParticle(level, x, y, z, vx, vy, vz, this.sprite.get(random));
			particle.setColor(1.0F, 1.0F, 1.0F);
			return particle;
		}
	}

}
