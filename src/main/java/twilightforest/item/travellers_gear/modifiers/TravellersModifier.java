package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;

public interface TravellersModifier {
	boolean hasModifier(ItemStack stack);
	String getTooltipTranslationKey();
}
