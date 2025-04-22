package twilightforest.compat.emi.recipes;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import twilightforest.item.recipe.TravellersGearModifierRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapedRecipe;

public class EmiTravellersGearModifierShapedRecipe extends EmiPatternCraftingRecipe {
	private final TravellersGearModifierShapedRecipe recipe;
	private final int uniqueSeed = EmiUtil.RANDOM.nextInt();

	public EmiTravellersGearModifierShapedRecipe(TravellersGearModifierShapedRecipe recipe) {
		super(
			recipe.getIngredients().stream()
				.map(EmiIngredient::of)
				.toList(),
			// dummy output; actual result is generated dynamically
			EmiStack.EMPTY,
			recipe.getId()
		);
		this.recipe = recipe;
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		if (slot < input.size()) {
			return new SlotWidget(input.get(slot), x, y);
		}
		return new SlotWidget(EmiStack.EMPTY, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(
			rand -> EmiStack.of(recipe.applyModifier(TravellersGearModifierRecipe.getTravellersArmorFromIngredients(recipe.getIngredients()))),
			uniqueSeed,
			x, y
		);
	}
}
