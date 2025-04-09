package twilightforest.data.custom;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.*;

public class PatternBuilder {
	private final List<String> rows = new ArrayList<>();
	private final Map<Character, Ingredient> key = new LinkedHashMap<>();

	public PatternBuilder define(Character symbol, Ingredient ingredient) {
		if (key.containsKey(symbol))
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined");
		if (symbol == ' ')
			throw new IllegalArgumentException("Symbol ' ' is reserved and cannot be defined");
		key.put(symbol, ingredient);
		return this;
	}

	public PatternBuilder pattern(String pattern) {
		if (!rows.isEmpty() && pattern.length() != rows.getFirst().length())
			throw new IllegalArgumentException("Pattern must be the same width on every line");
		rows.add(pattern);
		return this;
	}

	public ShapedRecipePattern build() {
		if (rows.isEmpty())
			throw new IllegalStateException("No pattern defined");
		return ShapedRecipePattern.of(key, rows);
	}

	public static PatternBuilder create() {
		return new PatternBuilder();
	}
}
