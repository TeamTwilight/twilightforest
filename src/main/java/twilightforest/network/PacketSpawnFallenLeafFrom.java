package twilightforest.network;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.client.particle.LeafParticleData;

import java.util.Random;
import java.util.function.Supplier;

public class PacketSpawnFallenLeafFrom {

	private final BlockPos pos;
	private final Vector3d motion;

	public PacketSpawnFallenLeafFrom(PacketBuffer buf) {
		pos = buf.readBlockPos();
		motion = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public PacketSpawnFallenLeafFrom(BlockPos pos, Vector3d motion) {
		this.pos = pos;
		this.motion = motion;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeDouble(motion.x);
		buf.writeDouble(motion.y);
		buf.writeDouble(motion.z);
	}

	public static class Handler {
		public static boolean onMessage(PacketSpawnFallenLeafFrom message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					Random rand = new Random();
					World world = Minecraft.getInstance().world;
					int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.getDefaultState(), world, message.pos, 0);
					int r = MathHelper.clamp(((color >> 16) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					int g = MathHelper.clamp(((color >> 8) & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					int b = MathHelper.clamp((color & 0xFF) + rand.nextInt(0x22) - 0x11, 0x00, 0xFF);
					world.addParticle(new LeafParticleData(r, g, b),
							message.pos.getX() + world.rand.nextFloat(),
							message.pos.getY(),
							message.pos.getZ() + world.rand.nextFloat(),

							(world.rand.nextFloat() * -0.5F) * message.motion.getX(),
							world.rand.nextFloat() * 0.5F + 0.25F,
							(world.rand.nextFloat() * -0.5F) * message.motion.getZ()
					);
				}
			});
			return true;
		}
	}
}
