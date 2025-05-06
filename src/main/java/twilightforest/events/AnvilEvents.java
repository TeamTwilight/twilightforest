package twilightforest.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.TravellersArmorItem;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class AnvilEvents {
	@SubscribeEvent
	public static void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().getItem() instanceof TravellersArmorItem && event.getRight().getItem() instanceof TravellersArmorItem) {
			event.setCanceled(true);
		}
	}
}
