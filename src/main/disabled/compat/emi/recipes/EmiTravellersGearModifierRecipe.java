package twilightforest.compat.emi.recipes;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;

public class EmiTravellersGearModifierRecipe extends EmiPatternCraftingRecipe {
	private final TravellersGearModifierRecipe recipe;
	private final int uniqueSeed = EmiUtil.RANDOM.nextInt();

	public EmiTravellersGearModifierRecipe(TravellersGearModifierRecipe recipe) {
		super(
			recipe.getIngredients().stream()
				.map(EmiIngredient::of)
				.toList(),
			// dummy output; actual result is generated dynamically
			EmiStack.EMPTY,
			recipe.getId(), recipe.isShapeless());
		this.recipe = recipe;
	}

	@Override
	public Identifier getId() {
		return super.getId().withPrefix("/");
	}

	@Override
	public SlotWidget getInputWidget(int slot, int x, int y) {
		if (slot < this.input.size()) {
			return new SlotWidget(this.input.get(slot), x, y);
		}
		return new SlotWidget(EmiStack.EMPTY, x, y);
	}

	@Override
	public SlotWidget getOutputWidget(int x, int y) {
		return new GeneratedSlotWidget(
			rand -> EmiStack.of(this.recipe.applyModifier(Minecraft.getInstance().level.registryAccess(), TravellersGearModifierRecipe.getModifiableArmorFromIngredients(recipe.getIngredients()), this.recipe.getIngredients())),
			this.uniqueSeed,
			x, y
		);
	}
}
