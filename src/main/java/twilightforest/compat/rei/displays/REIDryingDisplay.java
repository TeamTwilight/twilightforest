package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REIDryingCategory;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

import java.util.List;

public class REIDryingDisplay extends BasicDisplay {

	private final int dryingTime;

	public REIDryingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int dryingTime) {
		super(inputs, outputs);
		this.dryingTime = dryingTime;
	}

	public static REIDryingDisplay of(DryingRecipe recipe) {
		List<EntryIngredient> inputs = List.of(EntryIngredients.ofIngredient(recipe.getInput()), EntryIngredient.of(EntryStack.of(TFREIClientPlugin.BLOCKSTATE_DEFINITION, TFBlocks.OAK_DRYING_RACK.get().defaultBlockState())));
		List<EntryIngredient> outputs = List.of(EntryIngredients.of(recipe.getResult()));
		return new REIDryingDisplay(inputs, outputs, recipe.getDryingTime());
	}

	public int getDryingTime() {
		return this.dryingTime;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIDryingCategory.DRYING;
	}

	public static BasicDisplay.Serializer<REIDryingDisplay> serializer() {
		return BasicDisplay.Serializer.of((input, output, location1, tag) -> new REIDryingDisplay(input, output, tag.getInt("drying_time")), (display, tag) -> tag.putInt("drying_time", display.getDryingTime()));
	}
}
