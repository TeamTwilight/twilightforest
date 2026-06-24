package twilightforest.item.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.tags.TFItemTags;

public class EssenceRepairRecipe extends CustomRecipe {

	public EssenceRepairRecipe() {
		super();
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
	public ItemStack assemble(CraftingInput input) {
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
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.ESSENCE_REPAIR_RECIPE.get();
	}
}
