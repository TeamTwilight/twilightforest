package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketThrowPlayer implements IMessage {
	private float motionX;
	private float motionY;
	private float motionZ;

	public PacketThrowPlayer() {
	}

	public PacketThrowPlayer(float motionX, float motionY, float motionZ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		motionX = buf.readFloat();
		motionY = buf.readFloat();
		motionZ = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(motionX);
		buf.writeFloat(motionY);
		buf.writeFloat(motionZ);
	}

	public static class Handler implements IMessageHandler<PacketThrowPlayer, IMessage> {

		@Override
		public IMessage onMessage(PacketThrowPlayer message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					Minecraft.getMinecraft().player.addVelocity(message.motionX, message.motionY, message.motionZ);
				}
			});

			return null;
		}
	}
}
