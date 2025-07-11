package twilightforest.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class AnvilEvents {
	@SubscribeEvent
	public static void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			event.setCanceled(true);
		}
	}
}
