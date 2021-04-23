package twilightforest.network;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.client.IWeatherRenderHandler;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.TwilightForestMod;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.client.renderer.TFWeatherRenderer;

import java.util.function.Supplier;

public class PacketStructureProtectionClear {

	public PacketStructureProtectionClear() {}

	public PacketStructureProtectionClear(PacketBuffer unused) {}

	public void encode(PacketBuffer unused) {}

	public static class Handler {
		public static boolean onMessage(PacketStructureProtectionClear message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				DimensionRenderInfo info = DimensionRenderInfo.field_239208_a_.get(TwilightForestMod.prefix("renderer"));

				// add weather box if needed
				if (info instanceof TwilightForestRenderInfo) {
					IWeatherRenderHandler weatherRenderer = info.getWeatherRenderHandler();

					if (weatherRenderer instanceof TFWeatherRenderer) {
						((TFWeatherRenderer) weatherRenderer).setProtectedBox(null);
					}
				}
			});

			return true;
		}
	}
}
