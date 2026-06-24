package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SnowParticle extends SingleQuadParticle {

	final float initialParticleScale;

	public SnowParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, TextureAtlasSprite sprite) {
		this(level, x, y, z, vx, vy, vz, 1.0F, sprite);
	}

	public SnowParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float scale, TextureAtlasSprite sprite) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
		this.xd *= 0.1D;
		this.yd *= 0.1D;
		this.zd *= 0.1D;
		this.xd += vx * 0.4D;
		this.yd += vy * 0.4D;
		this.zd += vz * 0.4D;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.quadSize *= 0.75F;
		this.quadSize *= scale;
		this.initialParticleScale = this.quadSize;
		this.lifetime = (int) (6.0D / (Math.random() * 0.8D + 0.6D));
		this.lifetime = (int) (this.lifetime * scale);
		this.hasPhysics = true;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
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

		this.xd *= 0.7D;
		this.yd *= 0.7D;
		this.zd *= 0.7D;
		this.yd -= 0.02D;

		if (this.onGround) {
			this.xd *= 0.7D;
			this.zd *= 0.7D;
		}
	}

	@Override
	public int getLightCoords(float partialTicks) {
		return 240 | 240 << 16;
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			SnowParticle particle = new SnowParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite.get(random));
			return particle;
		}
	}
}
