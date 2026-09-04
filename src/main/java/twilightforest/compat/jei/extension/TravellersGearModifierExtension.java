package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearModifierExtension implements ICraftingCategoryExtension<TravellersGearModifierRecipe> {

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<TravellersGearModifierRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<TravellersGearModifierRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		TravellersGearModifierRecipe recipe = recipeHolder.value();
		List<Ingredient> ingredients = recipe.placementInfo().ingredients();
		List<List<ItemStack>> inputs = new ArrayList<>(ingredients.stream().map(ingredient -> ingredient.items().map(ItemStack::new).toList()).toList());

		List<ItemStack> representatives = inputs.stream().map(stacks -> stacks.isEmpty() ? ItemStack.EMPTY : stacks.getFirst()).toList();
		CraftingInput input = CraftingInput.of(1, representatives.size(), representatives);

		List<ItemStack> outputs = new ArrayList<>();
		for (List<ItemStack> stacks : inputs) {
			for (ItemStack stack : stacks) {
				if (stack.has(TFDataComponents.IS_TRAVELLERS_GEAR.get()) && !stack.is(TFItems.TRAVELLERS_BELT)) {
					outputs.add(recipe.applyModifier(stack.copy(), input));
				}
			}
		}

		outputs.removeIf(ItemStack::isEmpty);
		if (outputs.isEmpty())
			return;

		if (recipe.isShapeless()) builder.setShapeless();
		craftingGridHelper.createAndSetInputs(builder, inputs, recipe.getWidth(), recipe.getHeight());
		// output slot; use RENDER_ONLY to prevent displaying modifier recipes when using the "Show Recipe" key
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 95, 19).setOutputSlotBackground().addItemStacks(outputs);
	}
}
