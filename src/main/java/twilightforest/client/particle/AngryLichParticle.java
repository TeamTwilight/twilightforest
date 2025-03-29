package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AngryLichParticle extends HeartParticle {
	protected AngryLichParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);

		this.lifetime = 10;

		this.yd -= 0.05;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			AngryLichParticle lichParticle = new AngryLichParticle(level, x, y, z);
			lichParticle.pickSprite(this.sprite);
			lichParticle.setColor(1.0F, 1.0F, 1.0F);
			lichParticle.scale(0.75F);
			return lichParticle;
		}
	}
}
