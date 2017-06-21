package twilightforest.client.particle;

import net.minecraft.world.World;

public class ParticleSnowGuardian extends ParticleSnow {

	public ParticleSnowGuardian(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14) {
		super(par1World, par2, par4, par6, par8, par10, par12, par14);
		this.particleMaxAge = 10 + this.rand.nextInt(15);
		this.particleRed = this.particleGreen = this.particleBlue = 0.75F + this.rand.nextFloat() * 0.25F;
	}


}
