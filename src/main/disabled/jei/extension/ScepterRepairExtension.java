package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScepterRepairExtension implements ICraftingCategoryExtension<@NotNull ScepterRepairRecipe> {
	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<@NotNull ScepterRepairRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<@NotNull ScepterRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		var scepter = new ItemStack(recipeHolder.value().getScepter());
		scepter.setDamageValue(scepter.getMaxDamage());
		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(List.of(scepter));
		inputs.addAll(recipeHolder.value().getRepairItems().stream().map(ingredient -> ingredient.getValues().stream().map(Holder::value).map(Item::getDefaultInstance).toList()).toList());

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();

		var repairedScepter = new ItemStack(recipeHolder.value().getScepter());
		repairedScepter.setDamageValue(scepter.getMaxDamage() - recipeHolder.value().getRepairDurability());
		craftingGridHelper.createAndSetOutputs(builder, List.of(repairedScepter));
	}
}
