package twilightforest.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.TravellersArmorItem;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = TwilightForestMod.ID)
public class ArmorHurtEvents {
	@SubscribeEvent
	public static void stopDamagingTravellersGear(ArmorHurtEvent event) {
		event.getArmorMap().entrySet().forEach((equipmentSlotArmorEntryEntry -> {
			ArmorHurtEvent.ArmorEntry armorEntry = equipmentSlotArmorEntryEntry.getValue();
			if (TravellersArmorItem.isTravellersArmorAndBroken(armorEntry.armorItemStack))
				armorEntry.newDamage = 0;
		}));
	}
}
