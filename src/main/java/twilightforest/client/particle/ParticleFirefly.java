// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package twilightforest.client.particle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class ParticleFirefly extends Particle {

	private int lifeTime;
	private int halfLife;

	public ParticleFirefly(World world, double d, double d1, double d2, double f, double f1, double f2) {
		this(world, d, d1, d2, 1.0F, f, f1, f2);
	}

	public ParticleFirefly(World world, double d, double d1, double d2, float f, double f1, double f2, double f3) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
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
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		particleAlpha = getGlowBrightness();
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
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
	public int getBrightnessForRender(float p_189214_1_) {
		return 0xF000F0;
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}
