package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TwilightForestMod;

public class PacketAnnihilateBlock implements IMessage {
	private BlockPos pos;

	public PacketAnnihilateBlock() {
	}

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
					TwilightForestMod.proxy.doBlockAnnihilateEffect(world, message.pos);
				}
			});

			return null;
		}
	}

}
