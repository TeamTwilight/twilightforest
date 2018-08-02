package twilightforest.item;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import twilightforest.TwilightForestMod;
import twilightforest.block.RegisterBlockEvent;
import twilightforest.client.ModelRegisterCallback;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class TFRegisterItemRenderingEvent {
	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		for (ModelRegisterCallback b : RegisterBlockEvent.getBlockModels()) b.registerModel();

		for (ModelRegisterCallback i : TFRegisterItemEvent.ItemRegistryHelper.getItemModels()) i.registerModel();
	}
}
