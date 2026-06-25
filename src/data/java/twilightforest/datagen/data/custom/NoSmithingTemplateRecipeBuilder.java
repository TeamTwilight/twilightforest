package twilightforest.datagen.data.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

import java.util.*;
import java.util.function.Supplier;

public class NoSmithingTemplateRecipeBuilder {

	private final RecipeCategory category;
	private final Ingredient base;
	private final Ingredient addition;
	private List<TypedDataComponent<?>> additionalData = new ArrayList<>();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	private NoSmithingTemplateRecipeBuilder(RecipeCategory category, Ingredient base, Ingredient addition) {
		this.category = category;
		this.base = base;
		this.addition = addition;
	}

	public static NoSmithingTemplateRecipeBuilder noTemplate(Ingredient base, Ingredient addition, RecipeCategory category) {
		return new NoSmithingTemplateRecipeBuilder(category, base, addition);
	}

	public NoSmithingTemplateRecipeBuilder unlocks(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return this;
	}

	public <T> NoSmithingTemplateRecipeBuilder attachData(Supplier<DataComponentType<T>> type, T element) {
		return attachData(new TypedDataComponent<>(type.get(), element));
	}

	public NoSmithingTemplateRecipeBuilder attachData(TypedDataComponent<?> component) {
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
		NoTemplateSmithingRecipe smithingtrimrecipe = new NoTemplateSmithingRecipe(this.base, this.addition, this.additionalData);
		output.accept(id, smithingtrimrecipe, advancement$builder.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceKey<Recipe<?>> location) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}
