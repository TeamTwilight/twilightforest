package twilightforest.item.recipe;

import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class EmperorsClothRecipe extends CustomRecipe {

	public EmperorsClothRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean foundCloth = false;
		boolean foundItem = false;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(TFItems.EMPERORS_CLOTH.get()) && !foundCloth) {
					foundCloth = true;
				} else if (!foundItem) {
					if (stack.is(TFItemTags.EMPERORS_CLOTH_APPLICABLE) && stack.getCraftingRemainder() == null && stack.get(TFDataComponents.EMPERORS_CLOTH) == null) {
						foundItem = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return foundCloth && foundItem;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!stack.isEmpty() && stack.is(TFItemTags.EMPERORS_CLOTH_APPLICABLE) && item.isEmpty()) {
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
