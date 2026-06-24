package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.travellers.TravellersGearModifierRecipe;

import java.util.ArrayList;
import java.util.List;

public class TravellersGearModifierExtension implements ICraftingCategoryExtension<@NotNull TravellersGearModifierRecipe> {
	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<@NotNull TravellersGearModifierRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<@NotNull TravellersGearModifierRecipe> recipeHolder, @NotNull IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, @NotNull IFocusGroup focuses) {
		TravellersGearModifierRecipe recipe = recipeHolder.value();
		List<List<ItemStack>> inputs = new ArrayList<>(recipe.placementInfo().ingredients().stream().map(ingredient -> ingredient.getValues().stream().map(Holder::value).map(Item::getDefaultInstance).toList()).toList());
		List<ItemStack> outputs = new ArrayList<>();
		for (Ingredient ingredient : recipe.placementInfo().ingredients()) {
			for (ItemStack stack : ingredient.getValues().stream().map(Holder::value).map(Item::getDefaultInstance).toList()) {
				if (stack.has(TFDataComponents.IS_TRAVELLERS_GEAR) && !stack.is(TFItems.TRAVELLERS_BELT)) {
					outputs.add(recipe.applyModifier(stack.copy(), recipe.placementInfo().ingredients()));
				}
			}
		}

		outputs.stream().filter(ItemStack::isEmpty).forEach(outputs::remove);
		if (outputs.isEmpty())
			return;

		if (recipe.isShapeless()) builder.setShapeless();
		craftingGridHelper.createAndSetInputs(builder, inputs, recipe.getWidth(), recipe.getHeight());
		// output slot; use RENDER_ONLY to prevent displaying modifier recipes when using the "Show Recipe" key
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 95, 19).setOutputSlotBackground().addItemStacks(outputs);
	}
}
