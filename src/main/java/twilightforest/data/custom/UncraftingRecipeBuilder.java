package twilightforest.data.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UncraftingRecipeBuilder implements RecipeBuilder {

	private final Ingredient input;
	private final int count;
	private int cost = -1;
	private final List<String> rows = new ArrayList<>();
	private final Map<Character, Ingredient> key = new LinkedHashMap<>();

	public UncraftingRecipeBuilder(Ingredient input, int count) {
		this.input = input;
		this.count = count;
	}

	public static UncraftingRecipeBuilder uncrafting(ItemLike input) {
		return uncrafting(Ingredient.of(input), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(TagKey<Item> input) {
		return uncrafting(Ingredient.of(input), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(ItemLike input, int count) {
		return uncrafting(Ingredient.of(input), count);
	}

	public static UncraftingRecipeBuilder uncrafting(TagKey<Item> input, int count) {
		return uncrafting(Ingredient.of(input), count);
	}

	public static UncraftingRecipeBuilder uncrafting(Ingredient input, int count) {
		return new UncraftingRecipeBuilder(input, count);
	}

	public UncraftingRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
		return this.define(pSymbol, Ingredient.of(pTag));
	}

	public UncraftingRecipeBuilder define(Character pSymbol, ItemLike pItem) {
		return this.define(pSymbol, Ingredient.of(pItem));
	}

	public UncraftingRecipeBuilder setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public UncraftingRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol)) {
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
		} else if (symbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(symbol, ingredient);
			return this;
		}
	}

	public UncraftingRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pattern);
			return this;
		}
	}

	@Override
	public RecipeBuilder unlockedBy(String name, Criterion<?> trigger) {
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String group) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.input.getItems()[0].getItem();
	}

	@Override
	public void save(RecipeOutput output) {
		this.save(output, TwilightForestMod.prefix("uncrafting/" + RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath()));
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
		UncraftingRecipe recipe = new UncraftingRecipe(this.cost, this.input, this.count, pattern);
		output.accept(id, recipe, null);
	}
}
