package twilightforest.client.particle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;

public class ParticleSmokeScale extends ParticleSmokeNormal {

	public ParticleSmokeScale(World world, double x, double y, double z, double velX, double velY, double velZ, float scale) {
		super(world, x, y, z, velX, velY, velZ, scale);
	}
}
