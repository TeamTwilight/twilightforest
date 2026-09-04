package twilightforest.datagen.data.custom;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.recipe.DryingRecipe;

public class DryingRecipeBuilder implements RecipeBuilder {

	private final Ingredient input;
	private final ItemStackTemplate result;
	private final int time;
	private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
	private boolean hasCriteria;

	private DryingRecipeBuilder(Ingredient input, ItemStackTemplate result, int dryingTime) {
		this.input = input;
		this.result = result;
		this.time = dryingTime;
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result) {
		return drying(Ingredient.of(input), new ItemStackTemplate(result.asItem()));
	}

	public static DryingRecipeBuilder drying(ItemLike input, ItemLike result, float dryingMinutes) {
		return drying(Ingredient.of(input), new ItemStackTemplate(result.asItem()), dryingMinutes);
	}

	public static DryingRecipeBuilder drying(HolderGetter<Item> getter, TagKey<Item> input, ItemLike result, float dryingMinutes) {
		return drying(Ingredient.of(getter.getOrThrow(input)), new ItemStackTemplate(result.asItem()), dryingMinutes);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStackTemplate result) {
		return drying(input, result, 5);
	}

	public static DryingRecipeBuilder drying(Ingredient input, ItemStackTemplate result, float dryingMinutes) {
		return new DryingRecipeBuilder(input, result, (int) (20 * 60 * dryingMinutes));
	}

	@Override
	public DryingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.advancementBuilder.unlockedBy(name, criterion);
		this.hasCriteria = true;
		return this;
	}

	@Override
	public DryingRecipeBuilder group(@Nullable String group) {
		return this;
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(this.result);
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		DryingRecipe recipe = new DryingRecipe(RecipeBuilder.createCraftingCommonInfo(false), this.input, this.result, this.time);
		output.accept(id, recipe, this.hasCriteria ? this.advancementBuilder.build(output, id, "drying") : null);
	}
}
