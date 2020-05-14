package twilightforest.util;

import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//TODO: Fix methods
public class ParticleHelper {

	public static void spawnParticles(World world, BlockPos pos, IParticleData type, int count, double speed, int... particleArgs) {
		world.addParticle(type, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5, 0.5, 0.5);
	}

	public static void spawnParticles(Entity entity, IParticleData type) {
		entity.world.addParticle(type, entity.getX(), entity.getY() + entity.getHeight() * 0.5, entity.getZ(), entity.getWidth() * 0.5, entity.getHeight() * 0.5, entity.getWidth() * 0.5);
	}
}
