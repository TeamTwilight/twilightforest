package twilightforest.datagen.data.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.apache.commons.lang3.StringUtils;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapedRecipe;
import twilightforest.item.recipe.travellers.TravellersGearModifierShapelessRecipe;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiable;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class TravellersGearComponentModifierBuilder {

	private final Map<ResourceKey<Recipe<?>>, Recipe<?>> recipes = new LinkedHashMap<>();

	private TravellersGearComponentModifierBuilder(Iterable<ShapedRecipePattern> patterns, ResourceKey<TravellersModifier> travellersModifier, Holder<TravellersModifier> holder, boolean isRotated) {
		for (ShapedRecipePattern pattern : patterns) {
			Identifier id = this.makeId(travellersModifier, pattern.ingredients().stream().flatMap(Optional::stream)).withSuffix(isRotated ? "_rotated" : "");
			this.recipes.put(ResourceKey.create(Registries.RECIPE, id), new TravellersGearModifierShapedRecipe(pattern, holder, isRotated));
		}
	}

	private TravellersGearComponentModifierBuilder(Iterable<NonNullList<Ingredient>> ingredients, ResourceKey<TravellersModifier> travellersModifier, Holder<TravellersModifier> holder) {
		for (NonNullList<Ingredient> recipeIngredients : ingredients) {
			Identifier id = this.makeId(travellersModifier, recipeIngredients.stream());
			this.recipes.put(ResourceKey.create(Registries.RECIPE, id), new TravellersGearModifierShapelessRecipe(recipeIngredients, holder));
		}
	}

	public static TravellersGearComponentModifierBuilder buildShaped(HolderGetter<TravellersModifier> modifiers, Iterable<ShapedRecipePattern> patterns, ResourceKey<TravellersModifier> travellersModifier) {
		return buildShaped(modifiers, patterns, travellersModifier, false);
	}

	public static TravellersGearComponentModifierBuilder buildShaped(HolderGetter<TravellersModifier> modifiers, Iterable<ShapedRecipePattern> patterns, ResourceKey<TravellersModifier> travellersModifier, boolean isRotated) {
		return new TravellersGearComponentModifierBuilder(patterns, travellersModifier, modifiers.getOrThrow(travellersModifier), isRotated);
	}

	public static TravellersGearComponentModifierBuilder buildShapeless(HolderGetter<TravellersModifier> modifiers, Iterable<NonNullList<Ingredient>> ingredients, ResourceKey<TravellersModifier> travellersModifier) {
		return new TravellersGearComponentModifierBuilder(ingredients, travellersModifier, modifiers.getOrThrow(travellersModifier));
	}

	public void save(RecipeOutput output) {
		this.recipes.forEach((id, recipe) -> output.accept(id, recipe, null));
	}

	private Identifier makeId(ResourceKey<TravellersModifier> travellersModifier, Stream<Ingredient> ingredients) {
		Item armor = ingredients
			.filter(ingredient -> !ingredient.isCustom())
			.flatMap(ingredient -> ingredient.getValues().unwrap().right().stream())
			.flatMap(List::stream)
			.map(Holder::value)
			.filter(item -> item instanceof TravellersModifiable modifiable && modifiable.getModifierSlots() > 0)
			.findFirst()
			.orElseThrow();
		return travellersModifier.identifier()
			.withPrefix("add_modifier_to_travellers_gear/" + StringUtils.substringAfterLast(armor.getDescriptionId(), '.') + "/")
			.withSuffix("_modifier");
	}
}
