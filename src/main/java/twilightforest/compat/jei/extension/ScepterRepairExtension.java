package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScepterRepairExtension implements ICraftingCategoryExtension<ScepterRepairRecipe> {

	@Override
	public void setRecipe(RecipeHolder<ScepterRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		var scepter = new ItemStack(recipeHolder.value().getScepter());
		scepter.setDamageValue(scepter.getMaxDamage());
		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(List.of(scepter));
		inputs.addAll(recipeHolder.value().getRepairItems().stream().map(ingredient -> Arrays.stream(ingredient.getItems()).toList()).toList());

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();

		var repairedScepter = new ItemStack(recipeHolder.value().getScepter());
		repairedScepter.setDamageValue(scepter.getMaxDamage() - recipeHolder.value().getRepairDurability());
		craftingGridHelper.createAndSetOutputs(builder, List.of(repairedScepter));
	}
}
