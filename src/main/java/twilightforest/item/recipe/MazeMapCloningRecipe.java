package twilightforest.item.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class MazeMapCloningRecipe extends CustomRecipe {

	public MazeMapCloningRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < input.size(); j++) {
			ItemStack itemstack1 = input.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.is(TFItems.FILLED_MAZE_MAP.get())) {
					if (!itemstack.isEmpty()) {
						return false;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAZE_MAP.get())) {
						return false;
					}

					i++;
				}
			}
		}

		return !itemstack.isEmpty() && i > 0;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < input.size(); j++) {
			ItemStack itemstack1 = input.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.is(TFItems.FILLED_MAZE_MAP)) {
					if (!itemstack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAZE_MAP)) {
						return ItemStack.EMPTY;
					}

					i++;
				}
			}
		}

		if (!itemstack.isEmpty() && i >= 1) {
			ItemStack itemstack2 = itemstack.copy();
			itemstack2.setCount(i + 1);
			return itemstack2;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return x >= 3 && y >= 3;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.MAZE_MAP_CLONING_RECIPE.get();
	}
}
