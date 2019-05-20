package twilightforest.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import twilightforest.client.particle.TFParticleType;
import twilightforest.network.PacketSpawnEntityParticles;
import twilightforest.network.TFPacketHandler;

public class ParticleHelper {

	public static void spawnParticles(World world, BlockPos pos, EnumParticleTypes type, int count, double speed, int... particleArgs) {
		if (world instanceof WorldServer) {
			((WorldServer) world).spawnParticle(type, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, count, 0.5, 0.5, 0.5, speed, particleArgs);
		}
	}

	public static void spawnParticles(Entity entity, EnumParticleTypes type, int count, double speed, int... particleArgs) {
		if (entity.world instanceof WorldServer) {
			((WorldServer) entity.world).spawnParticle(type, entity.posX, entity.posY + entity.height * 0.5, entity.posZ, count, entity.width * 0.5, entity.height * 0.5, entity.width * 0.5, speed, particleArgs);
		}
	}

	public static void spawnParticles(Entity entity, TFParticleType type, int count) {
		if (!entity.world.isRemote) {
			TFPacketHandler.CHANNEL.sendToAllAround(
					new PacketSpawnEntityParticles(type, entity, count),
					new NetworkRegistry.TargetPoint(entity.world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, 32.0)
			);
		}
	}
}
