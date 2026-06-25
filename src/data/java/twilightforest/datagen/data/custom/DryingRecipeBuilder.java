package twilightforest.datagen.data.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;
import twilightforest.item.recipe.DryingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class DryingRecipeBuilder implements RecipeBuilder {
	private final Ingredient input;
	private final ItemStackTemplate result;
	private final int time;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	private DryingRecipeBuilder(Ingredient input, ItemStackTemplate result, int dryingTime) {
		this.input = input;
		this.result = result;
		this.time = dryingTime;
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result) {
		return drying(Ingredient.of(input), new ItemStackTemplate(result.asItem()), 20 * 60 * 5);
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result, int dryingMinutes) {
		return drying(Ingredient.of(input), new ItemStackTemplate(result.asItem()), 20 * 60 * dryingMinutes);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemLike result) {
		return drying(input, new ItemStackTemplate(result.asItem()), 20 * 60 * 5);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemLike result, int dryingMinutes) {
		return drying(input, new ItemStackTemplate(result.asItem()), 20 * 60 * dryingMinutes);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStack result) {
		return drying(input, ItemStackTemplate.fromNonEmptyStack(result), 20 * 60 * 5);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStack result, int dryingMinutes) {
		return drying(input, ItemStackTemplate.fromNonEmptyStack(result), 20 * 60 * dryingMinutes);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStackTemplate result) {
		return new DryingRecipeBuilder(input, result, 20 * 60 * 5);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStackTemplate result, int dryingTicks) {
		return new DryingRecipeBuilder(input, result, dryingTicks);
	}

	public DryingRecipeBuilder unlockedBy(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return this;
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(this.result);
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id.identifier());
		}
		RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
		this.criteria.forEach(advancementBuilder::unlockedBy);
		output.accept(id, new DryingRecipe(this.input, this.result, this.time), advancementBuilder.build(output, id, "drying"));
	}
}
