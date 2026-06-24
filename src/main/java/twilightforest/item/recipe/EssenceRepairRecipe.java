package twilightforest.item.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class EssenceRepairRecipe extends CustomRecipe {

	public EssenceRepairRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean scepter = false;
		boolean essence = false;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(TFItemTags.SCEPTERS) && stackInQuestion.isDamaged()) {
					if (scepter) return false;
					scepter = true;
				} else if (stackInQuestion.is(TFItems.EXANIMATE_ESSENCE.get())) {
					if (essence) return false;
					essence = true;
				} else {
					return false;
				}
			}
		}
		return scepter && essence;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
		ItemStack scepter = null;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack itemstack = input.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.is(TFItemTags.SCEPTERS)) {
					if (scepter == null) {
						scepter = itemstack;
					} else {
						//Only accept 1 scepter
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (scepter != null && scepter.isDamaged()) {
			ItemStack repaired = scepter.copy();
			repaired.setDamageValue(0);
			return repaired;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.ESSENCE_REPAIR_RECIPE.get();
	}
}
