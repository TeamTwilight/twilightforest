package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;

@OnlyIn(Dist.CLIENT)
public class LoadingScreenListener {

	private final Minecraft client = Minecraft.getInstance();

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
		if (event.getGui() instanceof ReceivingLevelScreen && client.player != null) {
			ResourceKey<Level> tfDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()));
			if (client.player.getCommandSenderWorld().getBlockState(client.player.blockPosition().below()) == TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState() || client.player.getCommandSenderWorld().dimension() == tfDimension) {
				LoadingScreenGui guiLoading = new LoadingScreenGui();
				guiLoading.setEntering(client.player.getCommandSenderWorld().dimension() == Level.OVERWORLD);
				event.setGui(guiLoading);
			}
		}
	}
}
