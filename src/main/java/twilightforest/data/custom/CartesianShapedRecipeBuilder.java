package twilightforest.data.custom;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.function.Predicate;

/**
 * Builds shaped recipe patterns, splitting ingredient definitions when predicate matches
 */
public class CartesianShapedRecipeBuilder extends AbstractCartesianRecipeBuilder<ShapedRecipePattern> {
	private final List<String> rows = new ArrayList<>();
	private final LinkedHashMap<Character, List<Ingredient>> key = new LinkedHashMap<>();

	private CartesianShapedRecipeBuilder(Predicate<Ingredient> shouldSplit) {
		super(shouldSplit);
	}

	public static CartesianShapedRecipeBuilder create(Predicate<Ingredient> shouldSplit) {
		return new CartesianShapedRecipeBuilder(shouldSplit);
	}

	public CartesianShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (key.containsKey(symbol))
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
		if (symbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' is reserved and cannot be defined");
		key.put(symbol, wrap(ingredient));
		return this;
	}

	public CartesianShapedRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	public CartesianShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(tag));
	}

	public CartesianShapedRecipeBuilder pattern(String pattern) {
		if (!rows.isEmpty() && pattern.length() != rows.get(0).length())
			throw new IllegalArgumentException("Pattern must be the same width on every line");
		rows.add(pattern);
		return this;
	}

	@Override
	protected List<List<Ingredient>> getSlots() {
		return new ArrayList<>(key.values());
	}

	@Override
	protected ShapedRecipePattern assemble(List<Ingredient> combo) {
		Map<Character, Ingredient> mapping = new LinkedHashMap<>();
		int i = 0;
		for (Character symbol : key.keySet()) {
			mapping.put(symbol, combo.get(i++));
		}
		return ShapedRecipePattern.of(mapping, rows);
	}

	@Override
	public Iterable<ShapedRecipePattern> build() {
		if (rows.isEmpty())
			throw new IllegalStateException("No pattern defined");
		return super.build();
	}
}
