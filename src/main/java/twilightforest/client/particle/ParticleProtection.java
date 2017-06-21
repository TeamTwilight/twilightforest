package twilightforest.client.particle;

import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleProtection extends ParticleSuspendedTown {

	public ParticleProtection(World world, double x, double y, double z, double velX, double velY, double velZ) {
		super(world, x, y, z, velX, velY, velZ);
		this.setParticleTextureIndex(82);
		this.setRBGColorF(1.0F, 1.0F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float par1) {
		return 0xF000F0;
	}
}
