package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

import java.util.List;

public class NoTemplateSmithingExtension implements ISmithingCategoryExtension<NoTemplateSmithingRecipe> {

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
		ingredientAcceptor.add(recipe.baseIngredient());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
		recipe.additionIngredient().ifPresent(ingredientAcceptor::add);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setOutput(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
		ContextMap contextMap = ingredientAcceptor.getContextMap();
		ItemStack addition = recipe.additionIngredient()
			.map(ingredient -> ingredient.display().resolveForFirstStack(contextMap))
			.orElse(ItemStack.EMPTY);

		List<ItemStack> baseStacks = recipe.baseIngredient()
			.display()
			.resolveForStacks(contextMap);

		for (ItemStack base : baseStacks)
			ingredientAcceptor.add(recipe.assemble(new SmithingRecipeInput(ItemStack.EMPTY, base, addition)));
	}
}
