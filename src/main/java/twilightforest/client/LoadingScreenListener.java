package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.world.TFDimensions;

@OnlyIn(Dist.CLIENT)
public class LoadingScreenListener {

	private final Minecraft client = Minecraft.getInstance();
	private RegistryKey<World> lastDimension = World.OVERWORLD; //overworld

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.player == client.player) {
			lastDimension = event.player.getEntityWorld().getDimensionKey();
		}
	}

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		if (event.getGui() instanceof DownloadTerrainScreen && client.player != null) {
			if (client.player.getEntityWorld().getDimensionKey() == TFDimensions.twilightForest || lastDimension == TFDimensions.twilightForest) {
				GuiTwilightForestLoading guiLoading = new GuiTwilightForestLoading();
				guiLoading.setEntering(client.player.getEntityWorld().getDimensionKey() == TFDimensions.twilightForest);
				event.setGui(guiLoading);
			}
		}
	}
}
