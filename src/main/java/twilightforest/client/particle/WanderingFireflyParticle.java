package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

//same glowy logic as FireflyParticle, but we movin'
public class WanderingFireflyParticle extends TextureSheetParticle {

	private final int halfLife;
	private final boolean fromJar;

	public WanderingFireflyParticle(ClientLevel level, double posX, double posY, double posZ, float movementX, float movementY, float movementZ, double additionalX, double additionalY, double additionalZ, SpriteSet set, boolean fromJar) {
		super(level, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.xd *= movementX;
		this.yd *= movementY;
		this.zd *= movementZ;
		this.xd += additionalX;
		this.yd += additionalY;
		this.zd += additionalZ;

		this.rCol = 0.9F;
		this.gCol = 1.0F;
		this.bCol = 0.0F;
		this.pickSprite(set);
		this.quadSize = 0.2f + (random.nextFloat() * 0.1f);
		this.lifetime = 30 + random.nextInt(21);
		this.halfLife = lifetime / 2;
		this.speedUpWhenYMotionIsBlocked = true;
		this.hasPhysics = true;
		this.fromJar = fromJar;
	}

	@Override
	public void tick() {

		if(!fromJar && this.level.getBrightness(LightLayer.SKY, new BlockPos(this.x, this.y, this.z)) < 1) {
			this.remove();
		}
		super.tick();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void render(VertexConsumer buffer, Camera entity, float partialTicks) {
		this.alpha = getGlowBrightness();
		super.render(buffer, entity, partialTicks);
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

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			Random rand = world.random;
			double speedX = (double)rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double)rand.nextFloat() * 0.1D;
			double speedY = (double)rand.nextFloat() * -0.25D * (double)rand.nextFloat() * 0.1D;
			double speedZ = (double)rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double)rand.nextFloat() * 0.1D;
			return new WanderingFireflyParticle(world, x, y, z, 0.1F, 0.1F, 0.1F, speedX, speedY, speedZ, this.spriteSet, false);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class FromJarFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public FromJarFactory(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			Random rand = world.random;
			double speedX = (double)rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double)rand.nextFloat() * 0.1D;
			double speedY = (double)rand.nextFloat() * -0.25D * (double)rand.nextFloat() * 0.1D;
			double speedZ = (double)rand.nextFloat() * (rand.nextBoolean() ? -3.9D : 3.9D) * (double)rand.nextFloat() * 0.1D;
			return new WanderingFireflyParticle(world, x, y, z, 0.1F, 0.1F, 0.1F, speedX, speedY, speedZ, this.spriteSet, true);
		}
	}
}
