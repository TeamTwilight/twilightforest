package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomTextureParticle extends TextureSheetParticle {
    private final boolean fullBright;
	private final float uo;
	private final float vo;

	protected CustomTextureParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean fullBright) {
		this(level, x, y, z, fullBright);
		this.xd *= 0.1F;
		this.yd *= 0.1F;
		this.zd *= 0.1F;
		this.xd += xSpeed;
		this.yd += ySpeed;
		this.zd += zSpeed;
	}

	protected CustomTextureParticle(ClientLevel level, double x, double y, double z, boolean fullBright) {
		super(level, x, y, z, 0.0, 0.0, 0.0);
		this.gravity = 1.0F;
		this.quadSize /= 2.0F;
		this.uo = this.random.nextFloat() * 3.0F;
		this.vo = this.random.nextFloat() * 3.0F;
		this.fullBright = fullBright;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public int getLightColor(float partialTick) {
		return this.fullBright ? 0xF000F0 : super.getLightColor(partialTick);
	}

	@Override
	protected float getU0() {
		return this.sprite.getU((this.uo + 1.0F) / 4.0F);
	}

	@Override
	protected float getU1() {
		return this.sprite.getU(this.uo / 4.0F);
	}

	@Override
	protected float getV0() {
		return this.sprite.getV(this.vo / 4.0F);
	}

	@Override
	protected float getV1() {
		return this.sprite.getV((this.vo + 1.0F) / 4.0F);
	}

	@OnlyIn(Dist.CLIENT)
	public record Factory(SpriteSet sprite, boolean fullBright) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			CustomTextureParticle particle = new CustomTextureParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.fullBright);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public record ShieldBreak(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			CustomTextureParticle particle = new CustomTextureParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, true);
			particle.pickSprite(this.sprite);
			particle.scale(0.75F);
			particle.gravity = 0.0F;
			particle.lifetime = (int)(8.0F / (particle.random.nextFloat() * 0.5F + 0.5F) * 0.5F);
			if (particle.lifetime <= 1) particle.lifetime = 2;
			return particle;
		}
	}
}
