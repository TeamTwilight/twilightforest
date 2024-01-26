package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnnihilateParticle extends TextureSheetParticle {

	public AnnihilateParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.xd *= 0.1D;
		this.yd *= 0.1D;
		this.zd *= 0.1D;
		this.xd += vx * 0.4D;
		this.yd += vy * 0.4D;
		this.zd += vz * 0.4D;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.quadSize *= 0.75F;
		this.quadSize *= scale;
		this.lifetime = (int) (60.0D / (Math.random() * 0.8D + 0.6D));
		this.lifetime = (int) (this.lifetime * scale);
		this.hasPhysics = true;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.move(this.xd, this.yd, this.zd);

		this.xd *= 0.96D;
		this.yd *= 0.96D;
		this.zd *= 0.96D;

		if (this.onGround) {
			this.xd *= 0.7D;
			this.zd *= 0.7D;
		}

		this.quadSize *= 0.97D;

		if (this.quadSize < 0.04F) {
			this.remove();
		}

		float blacken = 0.985F;

		this.rCol *= blacken;
		this.gCol *= blacken;
		this.bCol *= blacken;

	}

	@Override
	public int getLightColor(float partialTicks) {
		return 240 | 240 << 16;
	}

	@OnlyIn(Dist.CLIENT)
	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			AnnihilateParticle particle = new AnnihilateParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 0.75F);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
