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

public class MagicMapCloningRecipe extends CustomRecipe {

	public MagicMapCloningRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < input.size(); j++) {
			ItemStack itemstack1 = input.getItem(j);
			if (!itemstack1.isEmpty()) {
				if (itemstack1.is(TFItems.FILLED_MAGIC_MAP.get())) {
					if (!itemstack.isEmpty()) {
						return false;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAGIC_MAP.get())) {
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
				if (itemstack1.is(TFItems.FILLED_MAGIC_MAP.get())) {
					if (!itemstack.isEmpty()) {
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1;
				} else {
					if (!itemstack1.is(TFItems.MAGIC_MAP.get())) {
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
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.MAGIC_MAP_CLONING_RECIPE.get();
	}
}
