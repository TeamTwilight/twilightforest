package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class GhastTrapParticle extends SingleQuadParticle {

	private final float reddustParticleScale;

	private final double originX;
	private final double originY;
	private final double originZ;

	public GhastTrapParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, TextureAtlasSprite sprite) {
		this(level, x, y, z, 3.0F, vx, vy, vz, sprite);
	}

	public GhastTrapParticle(ClientLevel level, double x, double y, double z, float scale, double mx, double my, double mz, TextureAtlasSprite sprite) {
		super(level, x + mx, y + my, z + mz, mx, my, mz, sprite);
		this.xd = mx;
		this.yd = my;
		this.zd = mz;

		this.originX = x;
		this.originY = y;
		this.originZ = z;
		float brightness = (float) Math.random() * 0.4F;
		this.rCol = 1.0F;
		this.gCol = ((float) (Math.random() * 0.2D) + 0.8F) * brightness;
		this.bCol = ((float) (Math.random() * 0.2D) + 0.8F) * brightness;
		this.quadSize *= 0.75F * scale;
		this.reddustParticleScale = this.quadSize;
		this.lifetime = (int) (10.0D / (Math.random() * 0.8D + 0.2D));
		this.hasPhysics = true;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		float f6 = (this.age + partialTicks) / this.lifetime * 32.0F;
		f6 = Mth.clamp(f6, 0.0F, 1.0F);

		return this.reddustParticleScale * f6;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		float proportion = (float) this.age / (float) this.lifetime;
		proportion = 1.0F - proportion;
		this.x = this.originX + this.xd * proportion;
		this.y = this.originY + this.yd * proportion;
		this.z = this.originZ + this.zd * proportion;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			GhastTrapParticle particle = new GhastTrapParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite.get(random));
			particle.setSpriteFromAge(this.sprite);
			return particle;
		}
	}
}
