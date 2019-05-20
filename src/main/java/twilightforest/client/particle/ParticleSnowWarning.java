package twilightforest.client.particle;

import net.minecraft.world.World;

public class ParticleSnowWarning extends ParticleSnow {

	public ParticleSnowWarning(World world, double x, double y, double z, double vx, double vy, double vz, float scale) {
		super(world, x, y, z, vx, vy, vz, scale);
		this.particleMaxAge = 50;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.motionY -= 0.019999999552965164D;
	}
}
