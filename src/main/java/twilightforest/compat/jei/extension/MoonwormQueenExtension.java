package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

import java.util.ArrayList;
import java.util.List;

public class MoonwormQueenExtension implements ICraftingCategoryExtension<MoonwormQueenRepairRecipe> {

	@Override
	public void setRecipe(RecipeHolder<MoonwormQueenRepairRecipe> recipe, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(List.of(RecipeViewerConstants.DAMAGED_MOONWORM_QUEEN));
		inputs.add(List.of(TFItems.TORCHBERRIES.toStack()));
		inputs.add(RecipeViewerConstants.BERRY_2_LIST);
		inputs.add(RecipeViewerConstants.BERRY_3_LIST);
		inputs.add(RecipeViewerConstants.BERRY_4_LIST);
		builder.setShapeless();

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		craftingGridHelper.createAndSetOutputs(builder, List.of(TFItems.MOONWORM_QUEEN.toStack()));
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<MoonwormQueenRepairRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
		List<ItemStack> berries = recipeSlots.subList(2, 5).stream().map(drawable -> drawable.getDisplayedItemStack().orElse(ItemStack.EMPTY)).filter(stack -> !stack.isEmpty()).toList();

		recipeSlots.getFirst().createDisplayOverrides().addItemStack(RecipeViewerConstants.MOONWORM_QUEEN_LIST.get(berries.size()));
	}
}
