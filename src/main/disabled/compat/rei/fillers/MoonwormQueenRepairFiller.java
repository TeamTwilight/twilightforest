package twilightforest.compat.rei.fillers;

import me.shedaniel.rei.api.client.entry.type.BuiltinClientEntryTypes;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFItems;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

import java.util.Collection;
import java.util.List;

public class MoonwormQueenRepairFiller implements CraftingRecipeFiller<MoonwormQueenRepairRecipe> {

	@Override
	public Collection<Display> apply(RecipeHolder<MoonwormQueenRepairRecipe> recipe) {
		return List.of(
			new DefaultCustomShapelessDisplay(recipe,
				List.of(
					EntryIngredients.of(RecipeViewerConstants.DAMAGED_MOONWORM_QUEEN),
					EntryIngredient.of(EntryStack.of(BuiltinClientEntryTypes.RENDERING, EntryStack.of(VanillaEntryTypes.ITEM, TFItems.TORCHBERRIES.toStack()))),
					ofRendering(RecipeViewerConstants.BERRY_2_LIST),
					ofRendering(RecipeViewerConstants.BERRY_3_LIST),
					ofRendering(RecipeViewerConstants.BERRY_4_LIST)),
				List.of(ofRendering(RecipeViewerConstants.MOONWORM_QUEEN_LIST), EntryIngredients.of(TFItems.MOONWORM_QUEEN))));
	}

	private static EntryIngredient ofRendering(Collection<ItemStack> values) {
		if (values.size() == 0) return EntryIngredient.empty();
		if (values.size() == 1) return EntryIngredient.of(EntryStack.of(BuiltinClientEntryTypes.RENDERING, EntryStack.of(VanillaEntryTypes.ITEM, values.iterator().next())));
		EntryIngredient.Builder result = EntryIngredient.builder(values.size());
		for (ItemStack value : values) {
			result.add(EntryStack.of(BuiltinClientEntryTypes.RENDERING, EntryStack.of(VanillaEntryTypes.ITEM, value)));
		}
		return result.build();
	}

	@Override
	public Class<MoonwormQueenRepairRecipe> getRecipeClass() {
		return MoonwormQueenRepairRecipe.class;
	}
}
