package twilightforest.data.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import twilightforest.item.recipe.TravellersGearModifierRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.TravellersGearModifierShapelessRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersGearComponentModifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravellersGearComponentModifierBuilder {
	public static final Map<ResourceLocation, Integer> SAVE_COUNTERS = new HashMap<>();

	TravellersGearModifierRecipe recipe;

	private TravellersGearComponentModifierBuilder(ShapedRecipePattern pattern, TravellersGearComponentModifier travellersModifier) {
		recipe = new TravellersGearModifierShapedRecipe(pattern, travellersModifier);
	}

	private TravellersGearComponentModifierBuilder(NonNullList<Ingredient> ingredients, TravellersGearComponentModifier travellersModifier) {
		recipe = new TravellersGearModifierShapelessRecipe(ingredients, travellersModifier);
	}

	public static TravellersGearComponentModifierBuilder build(ShapedRecipePattern pattern, TravellersGearComponentModifier travellersModifier) {
		return new TravellersGearComponentModifierBuilder(pattern, travellersModifier);
	}

	public static TravellersGearComponentModifierBuilder build(List<Ingredient> ingredients, TravellersGearComponentModifier travellersModifier) {
		return new TravellersGearComponentModifierBuilder(NonNullList.copyOf(ingredients), travellersModifier);
	}

	public void save(RecipeOutput output) {
		int count = SAVE_COUNTERS.getOrDefault(recipe.getId(), 0);
		output.accept(recipe.getId().withSuffix("_" + count), recipe, null);
		SAVE_COUNTERS.put(recipe.getId(), count + 1);
	}
}
