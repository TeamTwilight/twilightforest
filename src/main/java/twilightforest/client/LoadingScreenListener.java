package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;

@SideOnly(Side.CLIENT)
public class LoadingScreenListener {
	private final Minecraft client = FMLClientHandler.instance().getClient();
	private int lastDimension = 0;

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent evt) {
		if (evt.phase.equals(TickEvent.Phase.END) && evt.player == client.player)
			lastDimension = evt.player.dimension;
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiDownloadTerrain
				&& client.player != null) {

			GuiTwilightForestLoading guiLoading = new GuiTwilightForestLoading();

			if (lastDimension == TFConfig.dimension.dimensionID && client.player.dimension != TFConfig.dimension.dimensionID)
				guiLoading.setEntering(false);
			else guiLoading.setEntering(true);

			if (client.player.dimension == TFConfig.dimension.dimensionID
					|| lastDimension == TFConfig.dimension.dimensionID)
				event.setGui(guiLoading);
		}
	}
}
