package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;

@OnlyIn(Dist.CLIENT)
public class LoadingScreenListener {

	private final Minecraft client = Minecraft.getInstance();
	private int lastDimension = 0;

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.player == client.player) {
			lastDimension = event.player.dimension;
		}
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		if (event.getGui() instanceof DownloadTerrainScreen && client.player != null) {
			int tfDimension = TFConfig.dimension.dimensionID; //TODO: We're not ints, anymore
			if (client.player.dimension == tfDimension || lastDimension == tfDimension) {
				GuiTwilightForestLoading guiLoading = new GuiTwilightForestLoading();
				guiLoading.setEntering(client.player.dimension == tfDimension);
				event.setGui(guiLoading);
			}
		}
	}
}
