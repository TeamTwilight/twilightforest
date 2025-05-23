package twilightforest.item.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class EmperorsClothRecipe extends CustomRecipe {

	public EmperorsClothRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean foundInk = false;
		boolean foundItem = false;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(TFItems.EMPERORS_CLOTH.get()) && !foundInk) {
					foundInk = true;
				} else if (!foundItem) {
					if (stack.getItem() instanceof ArmorItem && stack.getCraftingRemainder().isEmpty() && stack.get(TFDataComponents.EMPERORS_CLOTH) == null) {
						foundItem = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return foundInk && foundItem;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && item.isEmpty()) {
				item = stack;
			}
		}

		ItemStack copy = item.copy();
		copy.set(TFDataComponents.EMPERORS_CLOTH, Unit.INSTANCE);
		return copy;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.EMPERORS_CLOTH_RECIPE.get();
	}
}
