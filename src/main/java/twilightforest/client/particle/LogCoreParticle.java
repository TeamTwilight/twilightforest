package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;

public class LogCoreParticle extends RisingParticle {
	LogCoreParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, TextureAtlasSprite sprite) {
		super(pLevel, pX, pY, pZ, 0.0, 0.02, 0.0, sprite);
		this.rCol = (float) pXSpeed;
		this.gCol = (float) pYSpeed;
		this.bCol = (float) pZSpeed;
		this.alpha = 0;
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Override
	public void move(double pX, double pY, double pZ) {
		this.setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
		this.setLocationFromBoundingbox();
	}

	@Override
	public float getQuadSize(float pScaleFactor) {
		float f = ((float) this.age + pScaleFactor) / (float) this.lifetime;
		return this.quadSize * (1.0F - f * f * 0.5F);
	}

	@Override
	public int getLightCoords(float partialTicks) {
		float f = ((float) this.age + partialTicks) / (float) this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightCoords(partialTicks);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}

	@Override
	public void tick() {
		super.tick();
		float f = (float) this.age / (float) this.lifetime;
		this.alpha = Math.min(f * 1.35F, 1F);
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double x2, double y2, double z2, RandomSource random) {
			LogCoreParticle logCoreParticle = new LogCoreParticle(level, x, y, z, x2, y2, z2, this.sprite.get(random));
			return logCoreParticle;
		}
	}

	public AABB getRenderBoundingBox(float partialTicks) {
		return AABB.INFINITE;
	}
}