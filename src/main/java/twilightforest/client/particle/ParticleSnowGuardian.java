package twilightforest.client.particle;

import net.minecraft.world.World;

public class ParticleSnowGuardian extends ParticleSnow {

	public ParticleSnowGuardian(World world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, vx, vy, vz, scale);
		this.particleMaxAge = 10 + this.rand.nextInt(15);
		this.particleRed = this.particleGreen = this.particleBlue = 0.75F + this.rand.nextFloat() * 0.25F;
	}
}
