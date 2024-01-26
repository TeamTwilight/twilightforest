package twilightforest.compat.rei.displays;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;
import twilightforest.item.recipe.CrumbleRecipe;

import java.util.List;
import java.util.Optional;

public class REICrumbleHornDisplay extends BasicDisplay {

	public final boolean isResultAir;

	private REICrumbleHornDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, ResourceLocation recipeId, boolean isResultAir) {
		super(inputs, outputs, Optional.of(recipeId));

		this.isResultAir = isResultAir;
	}

	public static REICrumbleHornDisplay of(RecipeHolder<CrumbleRecipe> recipe) {
		boolean isResultAir = recipe.value().result().defaultBlockState().isAir();

		List<EntryIngredient> inputs = List.of(EntryIngredient.of(EntryStacks.of(recipe.value().input())));
		List<EntryIngredient> outputs = isResultAir
				? List.of(EntryIngredient.of(EntryStack.of(TFREIClientPlugin.ENTITY_DEFINITION, TFREIClientPlugin.createItemEntity(recipe.value().input()))))
				: List.of(EntryIngredient.of(EntryStacks.of(recipe.value().result())));

		return new REICrumbleHornDisplay(inputs, outputs, recipe.id(), isResultAir);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICrumbleHornCategory.CRUMBLE_HORN;
	}
}
