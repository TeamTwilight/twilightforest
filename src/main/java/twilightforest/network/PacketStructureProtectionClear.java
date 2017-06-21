package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.world.WorldProviderTwilightForest;

public class PacketStructureProtectionClear implements IMessage {
	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class Handler implements IMessageHandler<PacketStructureProtectionClear, IMessage> {

		@Override
		public IMessage onMessage(PacketStructureProtectionClear message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World world = Minecraft.getMinecraft().world;

					// add weather box if needed
					if (world.provider instanceof WorldProviderTwilightForest) {
						TFWeatherRenderer weatherRenderer = (TFWeatherRenderer) world.provider.getWeatherRenderer();

						weatherRenderer.setProtectedBox(null);
					}
				}
			});

			return null;
		}
	}
}
