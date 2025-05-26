package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

@SuppressWarnings("NonExtendableApiUsage")
public class NoTemplateSmithingExtension implements ISmithingCategoryExtension<NoTemplateSmithingRecipe> {

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {

	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(NoTemplateSmithingRecipe recipe, T acceptor) {
		recipe.baseIngredient().ifPresent(acceptor::add);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(NoTemplateSmithingRecipe recipe, T acceptor) {
		recipe.additionIngredient().ifPresent(acceptor::add);
	}
}
