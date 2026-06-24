package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class PerfectDodgeParticle extends PortalParticle {
	public static final int LIFE_TIME = 10;
	public PerfectDodgeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
		this.lifetime = (int) (LIFE_TIME * (1 + this.level.getRandom().nextFloat() / 1.5));
	}

	@Override
	public void tick() {
		super.tick();
		float f = (float) this.age / this.lifetime;
		this.y -= 1.0F - f;  // remove anti gravitation of the particle
		this.setPos(this.x, this.y, this.z);
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(
			SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			PerfectDodgeParticle perfectDodgeParticle = new PerfectDodgeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite.get(random));
			return perfectDodgeParticle;
		}
	}
}
