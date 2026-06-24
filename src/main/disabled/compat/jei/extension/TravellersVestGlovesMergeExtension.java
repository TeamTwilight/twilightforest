package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.travellers.TravellersVestGlovesMergeRecipe;

import java.util.ArrayList;
import java.util.List;

public class TravellersVestGlovesMergeExtension implements ICraftingCategoryExtension<TravellersVestGlovesMergeRecipe> {

	@Override
	public void setRecipe(RecipeHolder<TravellersVestGlovesMergeRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		craftingGridHelper.createAndSetInputs(builder, new ArrayList<>(List.of(List.of(TFItems.TRAVELLERS_VEST.toStack()), List.of(TFItems.TRAVELLERS_GLOVES.toStack()))), 0, 0);
		builder.setShapeless();
		craftingGridHelper.createAndSetOutputs(builder, List.of(new ItemStack(TFItems.TRAVELLERS_VEST, 1, DataComponentPatch.builder().set(TFDataComponents.TRAVELLERS_HAS_GLOVES.get(), Unit.INSTANCE).build())));
	}
}
