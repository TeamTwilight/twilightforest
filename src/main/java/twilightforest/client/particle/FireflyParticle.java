package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;

public class FireflyParticle extends TextureSheetParticle {

	private final int halfLife;
	private final boolean checkSkylight;

	public FireflyParticle(ClientLevel level, double x, double y, double z, float movementX, float movementY, float movementZ, int minlife, boolean checkSkylight) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.xd *= movementX;
		this.yd *= movementY;
		this.zd *= movementZ;

		this.rCol = 0.5F + (this.random.nextFloat() * 0.25f);
		this.gCol = 0.85F - (this.random.nextFloat() * 0.25f);
		this.bCol = 0.0F;
		this.quadSize = 0.2f + (this.random.nextFloat() * 0.1f);
		this.lifetime = minlife + this.random.nextInt(21);
		this.halfLife = this.lifetime / 2;
		this.hasPhysics = true;
		this.checkSkylight = checkSkylight;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
		this.alpha = this.getGlowBrightness();
		super.render(buffer, camera, partialTicks);
	}

	@Override
	public void tick() {
		if (this.checkSkylight && this.level.getBrightness(LightLayer.SKY, BlockPos.containing(this.x, this.y, this.z)) <= 12) {
			this.remove();
		}
		super.tick();
	}

	public float getGlowBrightness() {
		int lifeTime = this.lifetime - this.age;
		if (lifeTime <= this.halfLife) {
			return (float) lifeTime / (float) this.halfLife;
		} else {
			return Math.max(1.0f - (((float) lifeTime - this.halfLife) / this.halfLife), 0);
		}
	}

	@Override
	public int getLightColor(float partialTicks) {
		return 0xF000F0;
	}

	public record StationaryProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.0F, 0.0F, 0.0F, 10, false);
			particle.pickSprite(this.sprite());
			return particle;
		}
	}

	public record WanderingProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.1F, 0.1F, 0.1F, 30, true);
			RandomSource rand = level.getRandom();
			particle.xd += (double) rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double) rand.nextFloat() * 0.1D;
			particle.yd += (double) rand.nextFloat() * -0.25D * (double) rand.nextFloat() * 0.1D;
			particle.zd += (double) rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double) rand.nextFloat() * 0.1D;
			particle.pickSprite(this.sprite());
			return particle;
		}
	}

	public record ParticleSpawnerProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.1F, 0.1F, 0.1F, 30, false);
			RandomSource rand = level.getRandom();
			particle.xd += (double) rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double) rand.nextFloat() * 0.1D;
			particle.yd += (double) rand.nextFloat() * -0.25D * (double) rand.nextFloat() * 0.1D;
			particle.zd += (double) rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double) rand.nextFloat() * 0.1D;
			particle.pickSprite(this.sprite());
			return particle;
		}
	}
}
