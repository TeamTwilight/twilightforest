package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface TransferableTravellersModifier extends InsertableTravellersModifier {
	boolean transfer(ItemStack stack, List<Ingredient> input);
}
