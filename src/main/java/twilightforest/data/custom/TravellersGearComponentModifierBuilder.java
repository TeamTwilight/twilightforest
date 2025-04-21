package twilightforest.data.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import twilightforest.item.recipe.TravellersGearModifierRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapelessRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersComponentModifier;

import java.util.*;

public class TravellersGearComponentModifierBuilder {
	List<TravellersGearModifierRecipe> recipes = new ArrayList<>();
	private TravellersGearComponentModifierBuilder(Iterable<ShapedRecipePattern> pattern, TravellersComponentModifier travellersModifier, boolean isRotated) {
		pattern.forEach(shapedRecipePattern -> recipes.add(new TravellersGearModifierShapedRecipe(shapedRecipePattern, travellersModifier, isRotated)));
	}

	private TravellersGearComponentModifierBuilder(NonNullList<Ingredient> ingredients, TravellersComponentModifier travellersModifier) {
		recipes.add(new TravellersGearModifierShapelessRecipe(ingredients, travellersModifier));
	}

	public static TravellersGearComponentModifierBuilder buildShaped(Iterable<ShapedRecipePattern> pattern, TravellersComponentModifier travellersModifier) {
		return buildShaped(pattern, travellersModifier, false);
	}

	public static TravellersGearComponentModifierBuilder buildShaped(Iterable<ShapedRecipePattern> pattern, TravellersComponentModifier travellersModifier, boolean isRotated) {
		return new TravellersGearComponentModifierBuilder(pattern, travellersModifier, isRotated);
	}

	// TODO: add shapeless recipes
	public static TravellersGearComponentModifierBuilder buildShapeless(List<Ingredient> ingredients, TravellersComponentModifier travellersModifier) {
		return new TravellersGearComponentModifierBuilder(NonNullList.copyOf(ingredients), travellersModifier);
	}

	public void save(RecipeOutput output) {
		for (TravellersGearModifierRecipe recipe : recipes) {
			output.accept(recipe.getId(), recipe, null);
		}
	}
}
