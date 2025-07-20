package twilightforest.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
		if (!event.isCanceled()) {
			event.getArmorMap().forEach((slot, entry) -> {
				ItemStack damagedStack = event.getArmorItemStack(slot);
				if (damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
					if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage()) {
						event.setCanceled(true);
					} else if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage() - 1 && event.getEntity() instanceof ServerPlayer player) {
						player.playNotifySound(SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, player.getVoicePitch());
					}
				}
			});
		}
	}
}
