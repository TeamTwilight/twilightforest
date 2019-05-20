package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.world.WorldProviderTwilightForest;

public class PacketStructureProtectionClear implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<PacketStructureProtectionClear, IMessage> {
		@Override
		public IMessage onMessage(PacketStructureProtectionClear message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				WorldProvider provider = Minecraft.getMinecraft().world.provider;

				// add weather box if needed
				if (provider instanceof WorldProviderTwilightForest) {
					IRenderHandler weatherRenderer = provider.getWeatherRenderer();

					if (weatherRenderer instanceof TFWeatherRenderer) {
						((TFWeatherRenderer) weatherRenderer).setProtectedBox(null);
					}
				}
			});

			return null;
		}
	}
}
