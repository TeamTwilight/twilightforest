package twilightforest.datagen.data.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.recipe.ComponentAttachingSmithingRecipe;

import java.util.*;
import java.util.function.Supplier;

public class ComponentSmithingRecipeBuilder {

	private final RecipeCategory category;
	private final Optional<Ingredient> template;
	private final Ingredient base;
	private final Optional<Ingredient> addition;
	private final List<TypedDataComponent<?>> additionalData = new ArrayList<>();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	private ComponentSmithingRecipeBuilder(RecipeCategory category, Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition) {
		this.category = category;
		this.template = template;
		this.base = base;
		this.addition = addition;
	}

	public static ComponentSmithingRecipeBuilder smithing(@Nullable Ingredient template, Ingredient base, @Nullable Ingredient addition, RecipeCategory category) {
		return new ComponentSmithingRecipeBuilder(category, Optional.ofNullable(template), base, Optional.ofNullable(addition));
	}

	public ComponentSmithingRecipeBuilder unlocks(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return this;
	}

	public <T> ComponentSmithingRecipeBuilder attachData(Supplier<DataComponentType<T>> type, T element) {
		return attachData(new TypedDataComponent<>(type.get(), element));
	}

	public ComponentSmithingRecipeBuilder attachData(TypedDataComponent<?> component) {
		this.additionalData.add(component);
		return this;
	}

	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
		this.ensureValid(id);
		Advancement.Builder advancement$builder = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		ComponentAttachingSmithingRecipe recipe = new ComponentAttachingSmithingRecipe(new Recipe.CommonInfo(false), this.template, this.base, this.addition, this.additionalData);
		output.accept(id, recipe, advancement$builder.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceKey<Recipe<?>> location) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}
