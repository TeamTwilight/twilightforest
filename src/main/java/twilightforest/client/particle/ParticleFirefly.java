package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleFirefly extends SpriteTexturedParticle {

	private final int halfLife;

	ParticleFirefly(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		motionX *= 2.1;
		motionY *= 2.1;
		motionZ *= 2.1;
		particleRed = 0.9F;
		particleGreen = 1.0F;
		particleBlue = 0.0F;
		particleScale = 0.2f + (rand.nextFloat() * 0.1f);
		maxAge = 10 + rand.nextInt(21);
		halfLife = maxAge / 2;
		canCollide = true;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo entity, float partialTicks) {
		particleAlpha = getGlowBrightness();
		super.renderParticle(buffer, entity, partialTicks);
	}

	@Override
	public void tick() {
		if (age++ >= maxAge) {
			setExpired();
		}
	}

	public float getGlowBrightness() {
		int lifeTime = maxAge - age;
		if (lifeTime <= halfLife) {
			return (float) lifeTime / (float) halfLife;
		} else {
			return Math.max(1.0f - (((float) lifeTime - halfLife) / halfLife), 0);
		}
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		return 0xF000F0;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleFirefly particle = new ParticleFirefly(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
