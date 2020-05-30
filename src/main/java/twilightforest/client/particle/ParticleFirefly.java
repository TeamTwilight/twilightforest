package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleFirefly extends SpriteTexturedParticle {

	private int lifeTime;
	private int halfLife;

	public ParticleFirefly(World world, double x, double y, double z, float f, double f1, double f2, double f3) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		motionX *= 2.10000000149011612D;
		motionY *= 2.10000000149011612D;
		motionZ *= 2.10000000149011612D;
		particleRed = particleGreen = 1.0F * f;
		particleRed *= 0.9F;
		particleBlue = 0.0F;
		particleScale = 1.0f + (rand.nextFloat() * 0.6f);
		particleScale *= f;
		lifeTime = maxAge = 10 + rand.nextInt(21);
		maxAge *= f;
		halfLife = lifeTime / 2;
		canCollide = true;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void buildGeometry(IVertexBuilder buffer, ActiveRenderInfo entity, float partialTicks) {
		particleAlpha = getGlowBrightness();
		super.buildGeometry(buffer, entity, partialTicks);
	}

	@Override
	public void tick() {
		if (lifeTime <= 1) {
			setExpired();
		} else {
			lifeTime--;
		}
	}

	public float getGlowBrightness() {
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
		public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ParticleFirefly particle = new ParticleFirefly(worldIn, x, y, z, 1.0F, xSpeed, ySpeed, zSpeed);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
