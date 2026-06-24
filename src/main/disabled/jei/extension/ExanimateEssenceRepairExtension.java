package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.EssenceRepairRecipe;
import twilightforest.tags.TFItemTags;

import java.util.ArrayList;
import java.util.List;

public class ExanimateEssenceRepairExtension implements ICraftingCategoryExtension<@NotNull EssenceRepairRecipe> {
	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<@NotNull EssenceRepairRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<@NotNull EssenceRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<List<ItemStack>> inputs = new ArrayList<>();
		List<ItemStack> scepters = new ArrayList<>();
		for (DeferredHolder<@NotNull Item, ? extends @NotNull Item> entry : TFItems.ITEMS.getEntries()) {
			if (entry.is(TFItemTags.SCEPTERS)) scepters.add(entry.get().getDefaultInstance());
		}

		craftingGridHelper.createAndSetOutputs(builder, scepters);

		scepters.forEach(stack -> stack.setDamageValue(stack.getMaxDamage()));
		inputs.add(scepters);
		inputs.add(List.of(TFItems.EXANIMATE_ESSENCE.toStack()));

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<@NotNull EssenceRepairRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
		//prevent input slot from cycling if we have a certain scepter selected
		if (!focuses.getFocuses(RecipeIngredientRole.OUTPUT).toList().isEmpty()) {
			ItemStack damaged = focuses.getFocuses(RecipeIngredientRole.OUTPUT).findFirst().orElseGet(() -> focuses.getFocuses(RecipeIngredientRole.CRAFTING_STATION).findFirst().orElseThrow()).getTypedValue().getItemStack().orElseThrow().copy();
			damaged.setDamageValue(damaged.getMaxDamage());
			recipeSlots.get(1).createDisplayOverrides().add(damaged);
		}

		//the output scepter should always match whatever the input is. Doesn't matter if the input is cycling or not
		recipeSlots.getFirst().createDisplayOverrides().add(new ItemStack(recipeSlots.get(1).getDisplayedItemStack().orElse(ItemStack.EMPTY).getItem()));
	}
}
