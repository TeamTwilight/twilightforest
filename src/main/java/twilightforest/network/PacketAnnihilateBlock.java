package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.client.particle.TFParticleType;

import java.util.function.Supplier;

public class PacketAnnihilateBlock {

	private final BlockPos pos;

	public PacketAnnihilateBlock(PacketBuffer buf) {
		pos = buf.readBlockPos();
	}

	public PacketAnnihilateBlock(BlockPos pos) {
		this.pos = pos;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBlockPos(pos);
	}

	public static class Handler {
		public static boolean onMessage(PacketAnnihilateBlock message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getInstance().world;
					for (int dx = 0; dx < 4; ++dx) {
						for (int dy = 0; dy < 4; ++dy) {
							for (int dz = 0; dz < 4; ++dz) {

								double x = message.pos.getX() + (dx + 0.5D) / 4;
								double y = message.pos.getY() + (dy + 0.5D) / 4;
								double z = message.pos.getZ() + (dz + 0.5D) / 4;

								double vx = world.rand.nextGaussian() * 0.2D;
								double vy = world.rand.nextGaussian() * 0.2D;
								double vz = world.rand.nextGaussian() * 0.2D;

								world.addParticle(TFParticleType.ANNIHILATE.get(), x, y, z, vx, vy, vz);
							}
						}
					}
				}
			});
			return true;
		}
	}
}
