package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.List;

public class ScepterRepairExtension implements ICraftingCategoryExtension<ScepterRepairRecipe> {

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<ScepterRepairRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<ScepterRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		ScepterRepairRecipe recipe = recipeHolder.value();
		ItemStack scepter = new ItemStack(recipe.getScepter());
		scepter.setDamageValue(scepter.getMaxDamage());
		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(List.of(scepter));
		inputs.addAll(recipe.getRepairItems().stream().map(ingredient -> ingredient.items().map(ItemStack::new).toList()).toList());

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();

		ItemStack repairedScepter = new ItemStack(recipe.getScepter());
		repairedScepter.setDamageValue(scepter.getMaxDamage() - recipe.getRepairDurability());
		craftingGridHelper.createAndSetOutputs(builder, List.of(repairedScepter));
	}
}
