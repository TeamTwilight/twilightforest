package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.TwilightForestMod;

public class PacketEnforceProgressionStatus implements IMessage {
	private boolean enforce;

	public PacketEnforceProgressionStatus() {
	}

	public PacketEnforceProgressionStatus(boolean enforce) {
		this.enforce = enforce;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		enforce = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(enforce);
	}

	public static class Handler implements IMessageHandler<PacketEnforceProgressionStatus, IMessage> {

		@Override
		public IMessage onMessage(PacketEnforceProgressionStatus message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getMinecraft().world;

					world.getGameRules().setOrCreateGameRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE, String.valueOf(message.enforce));
				}
			});

			return null;
		}
	}

}
