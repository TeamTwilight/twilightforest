package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.CasketRepairRecipe;
import twilightforest.item.recipe.travellers.TravellersBeltWingsMergeRecipe;

import java.util.ArrayList;
import java.util.List;

public class TravellersWingsBeltMergeExtension implements ICraftingCategoryExtension<TravellersBeltWingsMergeRecipe> {

	@Override
	public void setRecipe(RecipeHolder<TravellersBeltWingsMergeRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		craftingGridHelper.createAndSetInputs(builder, new ArrayList<>(List.of(List.of(TFItems.TRAVELLERS_WINGS.toStack()), List.of(TFItems.TRAVELLERS_BELT.toStack()))), 0, 0);
		builder.setShapeless();
		craftingGridHelper.createAndSetOutputs(builder, List.of(TFItems.TRAVELLERS_WINGS_BELT.toStack()));
	}
}
