package twilightforest.events;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemAttributeModifierEvents {
	@SubscribeEvent
	public static void activateAndDeactivateTravellersModifiers(ItemAttributeModifierEvent event) {
		ItemStack armor = event.getItemStack();
		if (!armor.has(TFDataComponents.IS_TRAVELLERS_GEAR))
			return;

		if (armor.getMaxDamage() - 1 <= armor.getDamageValue()) {
			TravellersModifiers.ENTRY_MODIFIERS.forEach(modifier -> modifier.deactivate(event));
		} else {
			TravellersModifiers.ENTRY_MODIFIERS.forEach(modifier -> modifier.activate(event));
		}
	}
}
