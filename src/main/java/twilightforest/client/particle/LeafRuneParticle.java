package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class LeafRuneParticle extends TextureSheetParticle {

	LeafRuneParticle(ClientLevel level, double x, double y, double z, double velX, double velY, double velZ) {
		super(level, x, y, z, velX, velY, velZ);
		this.xd = velX;
		this.yd = velY;
		this.zd = velZ;
		this.quadSize = this.random.nextFloat() * 0.25F;
		this.lifetime = (int) (Math.random() * 10.0D) + 40;
		this.gravity = 0.15F + this.random.nextFloat() * 0.1F;
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = 0.9F * f;
		this.gCol = 0.9F * f;
		this.bCol = f;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			LeafRuneParticle particle = new LeafRuneParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
