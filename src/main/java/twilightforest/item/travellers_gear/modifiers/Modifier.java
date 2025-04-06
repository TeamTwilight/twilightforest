package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;

public interface Modifier {
	void addModifier(ItemStack stack);
	void removeModifier(ItemStack stack);
	String getTooltipString();
}
