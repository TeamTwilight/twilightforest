package twilightforest.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class ParticleFirefly extends Particle {

	private int lifeTime;
	private int halfLife;

	public ParticleFirefly(World world, double x, double y, double z, double f, double f1, double f2) {
		this(world, x, y, z, 1.0F, f, f1, f2);
	}

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
		lifeTime = particleMaxAge = 10 + rand.nextInt(21);
		particleMaxAge *= f;
		halfLife = lifeTime / 2;
		canCollide = true;

		this.particleTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(new ResourceLocation(TwilightForestMod.ID, "particles/firefly").toString());
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks,
	                           float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		particleAlpha = getGlowBrightness();
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	@Override
	public void onUpdate() {
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

	@Override
	public int getFXLayer() {
		return 1;
	}
}
