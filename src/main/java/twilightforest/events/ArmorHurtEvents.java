package twilightforest.events;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = TwilightForestMod.ID)
public class ArmorHurtEvents {
	@SubscribeEvent
	public static void stopDamagingTravellersGear(ArmorHurtEvent event) {
		event.getArmorMap().entrySet().forEach((equipmentSlotArmorEntryEntry -> {
			ArmorHurtEvent.ArmorEntry armorEntry = equipmentSlotArmorEntryEntry.getValue();
			ItemStack damagedStack = armorEntry.armorItemStack.copy();
			damagedStack.setDamageValue((int) Math.ceil(armorEntry.originalDamage + damagedStack.getDamageValue()));
			if (damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && damagedStack.getDamageValue() == damagedStack.getMaxDamage()) {
				event.setNewDamage(equipmentSlotArmorEntryEntry.getKey(), damagedStack.getDamageValue() - armorEntry.armorItemStack.getDamageValue() - 1);
			}
		}));
	}
}
