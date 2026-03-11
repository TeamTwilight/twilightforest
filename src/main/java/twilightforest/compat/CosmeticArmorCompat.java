package twilightforest.compat;

import lain.mods.cos.api.event.CosArmorDeathDrops;
import twilightforest.events.CharmEvents;

public class CosmeticArmorCompat {
	public static void keepCosmeticArmor(CosArmorDeathDrops event) {
		if (CharmEvents.getPlayerData(event.getEntityPlayer()).contains(CharmEvents.CHARM_INV_TAG)) {
			event.setCanceled(true);
		}
	}
}
