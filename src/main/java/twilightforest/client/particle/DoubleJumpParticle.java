package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DoubleJumpParticle extends ExplodeParticle {
	protected DoubleJumpParticle(
		ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites
	) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
		this.quadSize *= 0.5F;
	}

	@OnlyIn(Dist.CLIENT)
	public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(
			SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new DoubleJumpParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
		}
	}
}
