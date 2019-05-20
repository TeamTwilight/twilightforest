package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;

@SideOnly(Side.CLIENT)
public class LoadingScreenListener {

	private final Minecraft client = Minecraft.getMinecraft();
	private int lastDimension = 0;

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.player == client.player) {
			lastDimension = event.player.dimension;
		}
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiDownloadTerrain && client.player != null) {
			int tfDimension = TFConfig.dimension.dimensionID;
			if (client.player.dimension == tfDimension || lastDimension == tfDimension) {
				GuiTwilightForestLoading guiLoading = new GuiTwilightForestLoading();
				guiLoading.setEntering(client.player.dimension == tfDimension);
				event.setGui(guiLoading);
			}
		}
	}
}
