package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class LargeFlameParticle extends SingleQuadParticle {

	private final float flameScale;

	public LargeFlameParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, TextureAtlasSprite sprite) {
		super(level, x, y, z, vx, vy, vz, sprite);
		this.xd = this.xd * 0.01D + vx;
		this.yd = this.yd * 0.01D + vy;
		this.zd = this.zd * 0.01D + vz;
		this.quadSize *= 5.0F;
		this.flameScale = this.quadSize;
		this.rCol = this.gCol = this.bCol = 1.0F;
		this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.hasPhysics = false;
	}

	@Override
	protected Layer getLayer() {
		return Layer.OPAQUE;
	}

	@Override
	public float getQuadSize(float partialTicks) {
		float relativeAge = (this.age + partialTicks) / this.lifetime;
		return this.flameScale * (1.0F - relativeAge * relativeAge * 0.5F);
	}

	@Override
	public int getLightCoords(float partialTicks) {
		float var2 = (this.age + partialTicks) / this.lifetime;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		int var3 = super.getLightCoords(partialTicks);
		int var4 = var3 & 255;
		int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240) {
			var4 = 240;
		}

		return var4 | var5 << 16;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd += 0.004D;

		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.96D;
		this.yd *= 0.96D;
		this.zd *= 0.96D;

		if (this.onGround) {
			this.xd *= 0.7D;
			this.zd *= 0.7D;
		}
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			LargeFlameParticle particle = new LargeFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite.get(random));
			return particle;
		}
	}
}
