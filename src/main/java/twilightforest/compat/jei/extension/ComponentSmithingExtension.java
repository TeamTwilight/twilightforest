package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.recipe.category.extensions.vanilla.smithing.ISmithingCategoryExtension;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import twilightforest.item.recipe.ComponentAttachingSmithingRecipe;

import java.util.List;

public class ComponentSmithingExtension implements ISmithingCategoryExtension<ComponentAttachingSmithingRecipe> {

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(ComponentAttachingSmithingRecipe recipe, T ingredientAcceptor) {
		recipe.templateIngredient().ifPresent(ingredientAcceptor::add);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(ComponentAttachingSmithingRecipe recipe, T ingredientAcceptor) {
		ingredientAcceptor.add(recipe.baseIngredient());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(ComponentAttachingSmithingRecipe recipe, T ingredientAcceptor) {
		recipe.additionIngredient().ifPresent(ingredientAcceptor::add);
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setOutput(ComponentAttachingSmithingRecipe recipe, T ingredientAcceptor) {
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
