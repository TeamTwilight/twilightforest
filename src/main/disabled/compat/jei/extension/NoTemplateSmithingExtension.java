package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoTemplateSmithingExtension implements ISmithingCategoryExtension<NoTemplateSmithingRecipe> {

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {

	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(NoTemplateSmithingRecipe recipe, T acceptor) {
		acceptor.addIngredients(recipe.getBase());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(NoTemplateSmithingRecipe recipe, T acceptor) {
		acceptor.addIngredients(recipe.getAddition());
	}
}
