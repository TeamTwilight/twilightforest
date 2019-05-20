package twilightforest.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleLargeFlame extends Particle {

	private float flameScale;

	public ParticleLargeFlame(World world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, vx, vy, vz);
		this.motionX = this.motionX * 0.009999999776482582D + vx;
		this.motionY = this.motionY * 0.009999999776482582D + vy;
		this.motionZ = this.motionZ * 0.009999999776482582D + vz;
		this.particleScale *= 5.0D;
		this.flameScale = this.particleScale;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		//this.noClip = true;
		this.setParticleTextureIndex(48);
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks,
	                           float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

		float var8 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
		this.particleScale = this.flameScale * (1.0F - var8 * var8 * 0.5F);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	@Override
	public int getBrightnessForRender(float partialTicks) {
		float var2 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;

		if (var2 < 0.0F) {
			var2 = 0.0F;
		}

		if (var2 > 1.0F) {
			var2 = 1.0F;
		}

		int var3 = super.getBrightnessForRender(partialTicks);
		int var4 = var3 & 255;
		int var5 = var3 >> 16 & 255;
		var4 += (int) (var2 * 15.0F * 16.0F);

		if (var4 > 240) {
			var4 = 240;
		}

		return var4 | var5 << 16;
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		this.motionY += 0.004D;

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}
}
