package twilightforest.item.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class CasketRepairRecipe extends CustomRecipe {

	public CasketRepairRecipe() {
		super();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean casket = false;
		boolean charm = false;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stackInQuestion = input.getItem(i);
			if (!stackInQuestion.isEmpty()) {
				if (stackInQuestion.is(TFItems.KEEPSAKE_CASKET) && stackInQuestion.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0) > 0) {
					if (casket) return false;
					casket = true;
				} else if (stackInQuestion.is(TFItems.CHARM_OF_KEEPING_3.get())) {
					if (charm) return false;
					charm = true;
				} else {
					return false;
				}
			}
		}
		return casket && charm;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack casket = null;
		for (int i = 0; i < input.size(); ++i) {
			ItemStack itemstack = input.getItem(i);
			if (!itemstack.isEmpty()) {
				if (itemstack.is(TFItems.KEEPSAKE_CASKET)) {
					if (casket == null) {
						casket = itemstack;
					} else {
						//Only accept 1 casket
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (casket != null && casket.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0) > 0) {
			ItemStack repaired = casket.copy();
			repaired.update(TFDataComponents.CASKET_DAMAGE, casket.get(TFDataComponents.CASKET_DAMAGE), integer -> integer - 1);
			return repaired;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return TFRecipes.CASKET_REPAIR_RECIPE.get();
	}
}
