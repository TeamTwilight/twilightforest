package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;

public interface TravellersModifier {
	void addModifier(ItemStack stack);
	void removeModifier(ItemStack stack);
	boolean hasModifier(ItemStack stack);
	String getTooltipString();
}
