package twilightforest.data.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.recipe.DryingRecipe;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DryingRecipeBuilder implements RecipeBuilder {
	private final Ingredient input;
	private final ItemStack result;
	private final int time;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	private DryingRecipeBuilder(Ingredient input, ItemStack result, int dryingTime) {
		this.input = input;
		this.result = result;
		this.time = dryingTime;
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result) {
		return drying(Ingredient.of(input), new ItemStack(result));
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result, float dryingMinutes) {
		return drying(Ingredient.of(input), new ItemStack(result), dryingMinutes);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStack result) {
		return drying(input, result, 5);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStack result, float dryingMinutes) {
		return new DryingRecipeBuilder(input, result, (int) (20 * 60 * dryingMinutes));
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
	public Item getResult() {
		return this.result.getItem();
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		this.ensureValid(id);
		Advancement.Builder builder = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(builder::addCriterion);
		DryingRecipe recipe = new DryingRecipe(this.input, this.result, this.time);
		output.accept(id, recipe, builder.build(id.withPrefix("recipes/drying/")));
	}

	private void ensureValid(ResourceLocation location) {
		if (this.criteria.isEmpty() && !location.getPath().equals("stale_bread")) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}
