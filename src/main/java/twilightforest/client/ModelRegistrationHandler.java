package twilightforest.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import twilightforest.TwilightForestMod;
import twilightforest.block.RegisterBlockEvent;
import twilightforest.item.RegisterItemEvent;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class ModelRegistrationHandler {

	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		RegisterBlockEvent.getBlockModels().forEach(ModelRegisterCallback::registerModel);
		RegisterItemEvent.getItemModels().forEach(ModelRegisterCallback::registerModel);
	}
}
