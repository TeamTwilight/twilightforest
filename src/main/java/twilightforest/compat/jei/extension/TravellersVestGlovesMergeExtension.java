package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.CasketRepairRecipe;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.ArrayList;
import java.util.List;

public class TravellersVestGlovesMergeExtension implements ICraftingCategoryExtension<TravellersVestGlovesMergeRecipe> {

	@Override
	public void setRecipe(RecipeHolder<TravellersVestGlovesMergeRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		craftingGridHelper.createAndSetInputs(builder, new ArrayList<>(List.of(List.of(TFItems.TRAVELLERS_CHESTPLATE.toStack()), List.of(TFItems.TRAVELLERS_GLOVES.toStack()))), 0, 0);
		builder.setShapeless();
		craftingGridHelper.createAndSetOutputs(builder, List.of(TFItems.TRAVELLERS_CHESTPLATE_GLOVES.toStack()));
	}
}
