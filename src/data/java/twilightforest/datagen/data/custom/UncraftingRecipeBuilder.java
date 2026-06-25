package twilightforest.datagen.data.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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

	private final HolderGetter<Item> items;
	private final Ingredient input;
	private final int count;
	private int cost = -1;
	private final List<String> rows = new ArrayList<>();
	private final Map<Character, Ingredient> key = new LinkedHashMap<>();

	public UncraftingRecipeBuilder(HolderGetter<Item> getter, Ingredient input, int count) {
		this.items = getter;
		this.input = input;
		this.count = count;
	}

	public static UncraftingRecipeBuilder uncrafting(HolderGetter<Item> getter, ItemLike input) {
		return uncrafting(getter, Ingredient.of(input), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(HolderGetter<Item> getter, TagKey<Item> input) {
		return uncrafting(getter, Ingredient.of(getter.getOrThrow(input)), 1);
	}

	public static UncraftingRecipeBuilder uncrafting(HolderGetter<Item> getter, ItemLike input, int count) {
		return uncrafting(getter, Ingredient.of(input), count);
	}

	public static UncraftingRecipeBuilder uncrafting(HolderGetter<Item> getter, TagKey<Item> input, int count) {
		return uncrafting(getter, Ingredient.of(getter.getOrThrow(input)), count);
	}

	public static UncraftingRecipeBuilder uncrafting(HolderGetter<Item> getter, Ingredient input, int count) {
		return new UncraftingRecipeBuilder(getter, input, count);
	}

	public UncraftingRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(this.items.getOrThrow(tag)));
	}

	public UncraftingRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
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
	public UncraftingRecipeBuilder unlockedBy(String name, Criterion<?> trigger) {
		return this;
	}

	@Override
	public UncraftingRecipeBuilder group(@Nullable String group) {
		return this;
	}

	public ItemInstance getResult() {
		return new ItemStackTemplate(this.input.getValues().get(0), this.count);
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return ResourceKey.create(Registries.RECIPE, TwilightForestMod.prefix("uncrafting/" + RecipeBuilder.getDefaultRecipeId(this.getResult()).identifier().getPath()));
	}

	@Override
	public void save(RecipeOutput output) {
		this.save(output, this.defaultId());
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
		UncraftingRecipe recipe = new UncraftingRecipe(this.cost, this.input, this.count, pattern);
		output.accept(id, recipe, null);
	}
}
