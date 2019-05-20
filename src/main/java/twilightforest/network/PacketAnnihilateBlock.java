package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

public class PacketAnnihilateBlock implements IMessage {

	private BlockPos pos;

	public PacketAnnihilateBlock() {}

	public PacketAnnihilateBlock(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
	}

	public static class Handler implements IMessageHandler<PacketAnnihilateBlock, IMessage> {
		@Override
		public IMessage onMessage(PacketAnnihilateBlock message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getMinecraft().world;
					for (int dx = 0; dx < 4; ++dx) {
						for (int dy = 0; dy < 4; ++dy) {
							for (int dz = 0; dz < 4; ++dz) {

								double x = message.pos.getX() + (dx + 0.5D) / 4;
								double y = message.pos.getY() + (dy + 0.5D) / 4;
								double z = message.pos.getZ() + (dz + 0.5D) / 4;

								double vx = world.rand.nextGaussian() * 0.2D;
								double vy = world.rand.nextGaussian() * 0.2D;
								double vz = world.rand.nextGaussian() * 0.2D;

								TwilightForestMod.proxy.spawnParticle(TFParticleType.ANNIHILATE, x, y, z, vx, vy, vz);
							}
						}
					}
				}
			});
			return null;
		}
	}
}
