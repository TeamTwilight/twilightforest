package twilightforest.data.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import twilightforest.item.recipe.TravellersGearModifierRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapelessRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersComponentModifier;

import java.util.*;

public class TravellersGearComponentModifierBuilder {
	public static final Map<ResourceLocation, Integer> SAVE_COUNTERS = new HashMap<>();

	List<TravellersGearModifierRecipe> recipes = new ArrayList<>();

	private TravellersGearComponentModifierBuilder(Iterable<ShapedRecipePattern> pattern, TravellersComponentModifier travellersModifier) {
		pattern.forEach(shapedRecipePattern -> recipes.add(new TravellersGearModifierShapedRecipe(shapedRecipePattern, travellersModifier)));
	}

	private TravellersGearComponentModifierBuilder(NonNullList<Ingredient> ingredients, TravellersComponentModifier travellersModifier) {
		recipes.add(new TravellersGearModifierShapelessRecipe(ingredients, travellersModifier));
	}

	public static TravellersGearComponentModifierBuilder buildShaped(Iterable<ShapedRecipePattern> pattern, TravellersComponentModifier travellersModifier) {
		return new TravellersGearComponentModifierBuilder(pattern, travellersModifier);
	}

	// TODO: add shapeless recipes
	public static TravellersGearComponentModifierBuilder buildShapeless(List<Ingredient> ingredients, TravellersComponentModifier travellersModifier) {
		return new TravellersGearComponentModifierBuilder(NonNullList.copyOf(ingredients), travellersModifier);
	}

	public void save(RecipeOutput output) {
		for (TravellersGearModifierRecipe recipe : recipes) {
			int count = SAVE_COUNTERS.getOrDefault(recipe.getId(), 0);
			output.accept(recipe.getId().withSuffix("_" + count), recipe, null);
			SAVE_COUNTERS.put(recipe.getId(), count + 1);
		}
	}
}
