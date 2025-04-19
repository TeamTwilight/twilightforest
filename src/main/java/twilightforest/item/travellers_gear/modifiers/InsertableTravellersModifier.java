package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;

public interface InsertableTravellersModifier extends TravellersModifier {
	void addModifier(ItemStack stack);
	void removeModifier(ItemStack stack);
}
