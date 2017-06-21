// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package twilightforest.client.particle;


import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ParticleFirefly extends Particle {

	public ParticleFirefly(World world, double d, double d1, double d2,
						   float f, float f1, float f2) {
		this(world, d, d1, d2, 1.0F, f, f1, f2);
	}

	public ParticleFirefly(World world, double d, double d1, double d2,
						   float f, float f1, float f2, float f3) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX *= 2.10000000149011612D;
		motionY *= 2.10000000149011612D;
		motionZ *= 2.10000000149011612D;
		if (f1 == 0.0F) {
			f1 = 1.0F;
		}
		particleRed = particleGreen = 1.0F * f;
		particleRed *= 0.9F;
		particleBlue = 0.0F;
		particleScale *= 1.0F;
		particleScale *= f;
		fireflyParticleScale = particleScale;
		particleMaxAge = (int) (32D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		particleMaxAge *= f;
		fireflyHalfLife = particleMaxAge / 2;
		canCollide = true;
	}

	@Override
	public void renderParticle(VertexBuffer buffer, Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		float f6 = (float) this.particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = (float) this.particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * this.particleScale;


		particleScale = fireflyParticleScale;
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glColorMask(true, true, true, true);
		GL11.glBlendFunc(770, 1);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5f);

		float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) par2 - interpPosX);
		float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) par2 - interpPosY);
		float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) par2 - interpPosZ);
		float f14 = 1.0F;

		buffer.color(this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha);
		buffer.pos((double) (f11 - par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 - par5 * f10 - par7 * f10))
				.tex((double) f7, (double) f9)
				.endVertex();
		buffer.pos((double) (f11 - par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 - par5 * f10 + par7 * f10))
				.tex((double) f7, (double) f8)
				.endVertex();
		buffer.pos((double) (f11 + par3 * f10 + par6 * f10), (double) (f12 + par4 * f10), (double) (f13 + par5 * f10 + par7 * f10))
				.tex((double) f6, (double) f8)
				.endVertex();
		buffer.pos((double) (f11 + par3 * f10 - par6 * f10), (double) (f12 - par4 * f10), (double) (f13 + par5 * f10 - par7 * f10))
				.tex((double) f6, (double) f9)
				.endVertex();

		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setExpired();
		}
		if (particleAge < fireflyHalfLife) {
			// get bigger
			setParticleTextureIndex((particleAge * 8) / fireflyHalfLife);
		} else {
			// get smaller
			setParticleTextureIndex(7 - ((particleAge - fireflyHalfLife) * 8) / particleMaxAge);
		}
		move(motionX, motionY, motionZ);
		if (posY == prevPosY) {
			motionX *= 1.1000000000000001D;
			motionZ *= 1.1000000000000001D;
		}
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}

	float fireflyParticleScale;
	int fireflyHalfLife;
}
