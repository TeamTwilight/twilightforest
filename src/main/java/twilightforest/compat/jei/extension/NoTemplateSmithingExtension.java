package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.common.platform.Services;
import mezz.jei.library.plugins.vanilla.anvil.SmithingCategoryExtension;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

public class NoTemplateSmithingExtension extends SmithingCategoryExtension<NoTemplateSmithingRecipe> {

	public NoTemplateSmithingExtension() {
		super(Services.PLATFORM.getRecipeHelper());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
	}
}
