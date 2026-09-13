package twilightforest.datagen.data.custom;

import net.minecraft.core.HolderGetter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CartesianShapedRecipeBuilder extends AbstractCartesianRecipeBuilder<ShapedRecipePattern> {

	private final HolderGetter<Item> getter;
	private final List<String> rows = new ArrayList<>();
	private final LinkedHashMap<Character, List<Ingredient>> key = new LinkedHashMap<>();

	private CartesianShapedRecipeBuilder(HolderGetter<Item> getter, Predicate<Ingredient> shouldSplit) {
		super(shouldSplit);
		this.getter = getter;
	}

	public static CartesianShapedRecipeBuilder create(HolderGetter<Item> getter, Predicate<Ingredient> shouldSplit) {
		return new CartesianShapedRecipeBuilder(getter, shouldSplit);
	}

	public CartesianShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol))
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
		if (symbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' is reserved and cannot be defined");
		this.key.put(symbol, this.wrap(ingredient));
		return this;
	}

	public CartesianShapedRecipeBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, Ingredient.of(item));
	}

	public CartesianShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, Ingredient.of(this.getter.getOrThrow(tag)));
	}

	public CartesianShapedRecipeBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.getFirst().length())
			throw new IllegalArgumentException("Pattern must be the same width on every line");
		this.rows.add(pattern);
		return this;
	}

	@Override
	protected List<List<Ingredient>> getSlots() {
		return new ArrayList<>(this.key.values());
	}

	@Override
	protected ShapedRecipePattern assemble(List<Ingredient> combo) {
		Map<Character, Ingredient> mapping = new LinkedHashMap<>();
		int i = 0;
		for (Character symbol : this.key.keySet()) {
			mapping.put(symbol, combo.get(i++));
		}
		return ShapedRecipePattern.of(mapping, this.rows);
	}

	@Override
	public Iterable<ShapedRecipePattern> build() {
		if (this.rows.isEmpty())
			throw new IllegalStateException("No pattern defined");
		return super.build();
	}
}
