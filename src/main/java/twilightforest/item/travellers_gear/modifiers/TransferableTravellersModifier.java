package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public interface TransferableTravellersModifier extends InsertableTravellersModifier {
	boolean transfer(ItemStack stack, CraftingInput input);
}
