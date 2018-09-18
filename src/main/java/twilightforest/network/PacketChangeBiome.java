package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeBiome implements IMessage {
	private BlockPos pos;
	private byte biomeId;

	public PacketChangeBiome() {
	}

	public PacketChangeBiome(BlockPos pos, byte biomeId) {
		this.pos = pos;
		this.biomeId = biomeId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), 0, buf.readInt());
		biomeId = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getZ());
		buf.writeByte(biomeId);
	}

	public static class Handler implements IMessageHandler<PacketChangeBiome, IMessage> {

		@Override
		public IMessage onMessage(PacketChangeBiome message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getMinecraft().world;
					Chunk chunkAt = world.getChunk(message.pos);

					chunkAt.getBiomeArray()[(message.pos.getZ() & 15) << 4 | (message.pos.getX() & 15)] = message.biomeId;

					world.markBlockRangeForRenderUpdate(message.pos, message.pos.up(255));
				}
			});

			return null;
		}
	}
}
