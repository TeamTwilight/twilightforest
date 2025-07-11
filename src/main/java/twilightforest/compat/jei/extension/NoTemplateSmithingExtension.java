package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.common.platform.Services;
import mezz.jei.library.plugins.vanilla.anvil.SmithingCategoryExtension;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

import java.util.List;

public class NoTemplateSmithingExtension extends SmithingCategoryExtension<NoTemplateSmithingRecipe> {

	public NoTemplateSmithingExtension() {
		super(Services.PLATFORM.getRecipeHelper());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setTemplate(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {

	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setBase(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
		ingredientAcceptor.addIngredients(recipe.getBase());
	}

	@Override
	public <T extends IIngredientAcceptor<T>> void setAddition(NoTemplateSmithingRecipe recipe, T ingredientAcceptor) {
		ingredientAcceptor.addIngredients(recipe.getAddition());
	}

	@Override
	public void onDisplayedIngredientsUpdate(NoTemplateSmithingRecipe recipe, IRecipeSlotDrawable templateSlot, IRecipeSlotDrawable baseSlot, IRecipeSlotDrawable additionSlot, IRecipeSlotDrawable outputSlot, IFocusGroup focuses) {
		List<IFocus<?>> outputFocuses = focuses.getFocuses(RecipeIngredientRole.OUTPUT).toList();
		if (outputFocuses.isEmpty()) {
			ItemStack base = baseSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
			ItemStack addition = additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);

			SmithingRecipeInput recipeInput = new SmithingRecipeInput(ItemStack.EMPTY, base, addition);
			ItemStack output = RecipeUtil.assembleResultItem(recipeInput, recipe);
			outputSlot.createDisplayOverrides().addItemStack(output);
		} else {
			ItemStack output = outputSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);
			ItemStack base = new ItemStack(output.getItem());
			ItemStack addition = additionSlot.getDisplayedItemStack().orElse(ItemStack.EMPTY);

			baseSlot.createDisplayOverrides().addItemStack(base);

			SmithingRecipeInput recipeInput = new SmithingRecipeInput(ItemStack.EMPTY, base, addition);
			output = RecipeUtil.assembleResultItem(recipeInput, recipe);
			outputSlot.createDisplayOverrides().addItemStack(output);
		}
	}
}
