package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;
import twilightforest.item.travellers_gear.TravellersArmorItem;

public interface TravellersModifier {
	boolean hasModifier(ItemStack stack);
	String getTooltipTranslationKey();

	default boolean isActive(ItemStack stack) {
		return hasModifier(stack) && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiers.ALWAYS_ACTIVE.contains(this));
	}
}
