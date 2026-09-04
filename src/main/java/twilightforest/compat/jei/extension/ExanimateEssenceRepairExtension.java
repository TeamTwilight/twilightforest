package twilightforest.compat.jei.extension;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.EssenceRepairRecipe;

import java.util.ArrayList;
import java.util.List;

public class ExanimateEssenceRepairExtension implements ICraftingCategoryExtension<EssenceRepairRecipe> {

	@Override
	public List<SlotDisplay> getIngredients(RecipeHolder<EssenceRepairRecipe> recipeHolder) {
		return List.of();
	}

	@Override
	public void setRecipe(RecipeHolder<EssenceRepairRecipe> recipeHolder, IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
		List<ItemStack> repaired = new ArrayList<>();
		List<ItemStack> damaged = new ArrayList<>();
		for (Holder<Item> scepter : BuiltInRegistries.ITEM.getTagOrEmpty(TFItemTags.SCEPTERS)) {
			repaired.add(new ItemStack(scepter));
			ItemStack broken = new ItemStack(scepter);
			broken.setDamageValue(broken.getMaxDamage());
			damaged.add(broken);
		}

		craftingGridHelper.createAndSetOutputs(builder, repaired);

		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(damaged);
		inputs.add(List.of(TFItems.EXANIMATE_ESSENCE.toStack()));

		craftingGridHelper.createAndSetInputs(builder, inputs, 0, 0);
		builder.setShapeless();
	}

	@Override
	public void onDisplayedIngredientsUpdate(RecipeHolder<EssenceRepairRecipe> recipeHolder, List<IRecipeSlotDrawable> recipeSlots, IFocusGroup focuses) {
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
