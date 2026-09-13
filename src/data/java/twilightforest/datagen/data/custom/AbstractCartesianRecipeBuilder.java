package twilightforest.datagen.data.custom;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract class AbstractCartesianRecipeBuilder<R> {

	protected final Predicate<Ingredient> shouldSplit;
	private final List<List<Ingredient>> slots = new ArrayList<>();

	protected AbstractCartesianRecipeBuilder(Predicate<Ingredient> shouldSplit) {
		this.shouldSplit = shouldSplit;
	}

	protected List<Ingredient> wrap(Ingredient ingredient) {
		return this.shouldSplit.test(ingredient)
			? ingredient.getValues().stream().map(Holder::value).map(Ingredient::of).toList()
			: List.of(ingredient);
	}

	protected void addSlot(Ingredient ingredient) {
		this.slots.add(this.wrap(ingredient));
	}

	protected List<List<Ingredient>> getSlots() {
		return this.slots;
	}

	protected abstract R assemble(List<Ingredient> combo);

	public Iterable<R> build() {
		return () -> Iterators.transform(Lists.cartesianProduct(this.getSlots()).iterator(), this::assemble);
	}
}
