package twilightforest.data.custom;

import com.google.common.collect.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.*;
import java.util.function.Predicate;

public class PatternsBuilder {
	private final Predicate<Ingredient> shouldSplitIngredient;
	private final List<String> rows = new ArrayList<>();
	private final Map<Character, List<Ingredient>> key = new LinkedHashMap<>();

	private PatternsBuilder(Predicate<Ingredient> shouldSplitIngredient) {
		this.shouldSplitIngredient = shouldSplitIngredient;
	}

	public PatternsBuilder define(Character symbol, Ingredient ingredient) {
		if (key.containsKey(symbol))
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
		if (symbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' is reserved and cannot be defined");
		List<Ingredient> ingredients = shouldSplitIngredient.test(ingredient) ? Arrays.stream(ingredient.getItems()).map(Ingredient::of).toList() :List.of(ingredient);
		key.put(symbol, ingredients);
		return this;
	}

	public PatternsBuilder pattern(String pattern) {
		if (!rows.isEmpty() && pattern.length() != rows.getFirst().length())
			throw new IllegalArgumentException("Pattern must be the same width on every line");
		rows.add(pattern);
		return this;
	}

	public Iterable<ShapedRecipePattern> build() {
		if (rows.isEmpty())
			throw new IllegalStateException("No pattern defined");

		// snapshot values so iterator remains stable
		List<List<Ingredient>> lists = new ArrayList<>(key.values());
		List<Character> symbols = new ArrayList<>(key.keySet());
		Iterable<List<Ingredient>> product = Lists.cartesianProduct(lists);

		return () -> Iterators.transform(
			product.iterator(),
			combo -> {
				Map<Character, Ingredient> mapping = new LinkedHashMap<>();
				for (int i = 0; i < combo.size(); i++) {
					mapping.put(symbols.get(i), combo.get(i));
				}
				return ShapedRecipePattern.of(mapping, rows);
			}
		);
	}

	public static PatternsBuilder create(Predicate<Ingredient> shouldSplitIngredient) {
		return new PatternsBuilder(shouldSplitIngredient);
	}
}
