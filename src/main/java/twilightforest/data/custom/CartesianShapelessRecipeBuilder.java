package twilightforest.data.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Predicate;

/**
 * Builds lists of ingredients, splitting slots when predicate matches
 */
public class CartesianShapelessRecipeBuilder extends AbstractCartesianRecipeBuilder<NonNullList<Ingredient>> {
	private CartesianShapelessRecipeBuilder(Predicate<Ingredient> shouldSplit) {
		super(shouldSplit);
	}

	public static CartesianShapelessRecipeBuilder create(Predicate<Ingredient> shouldSplit) {
		return new CartesianShapelessRecipeBuilder(shouldSplit);
	}

	public CartesianShapelessRecipeBuilder ingredient(Ingredient ingredient) {
		addSlot(ingredient);
		return this;
	}

	public CartesianShapelessRecipeBuilder ingredient(ItemLike item) {
		return ingredient(Ingredient.of(item));
	}

	@Override
	protected NonNullList<Ingredient> assemble(List<Ingredient> combo) {
		NonNullList<Ingredient> list = NonNullList.create();
		list.addAll(combo);
		return list;
	}
}
