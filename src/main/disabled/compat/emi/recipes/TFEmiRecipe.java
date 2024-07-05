package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.emi.TFEmiRecipeCategory;

import java.util.ArrayList;
import java.util.List;

public abstract class TFEmiRecipe<T extends Recipe<?>> implements EmiRecipe {

	private final EmiRecipeCategory category;
	private final RecipeHolder<T> recipe;
	private final ResourceLocation id;
	private final int width;
	private final int height;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;

	public TFEmiRecipe(TFEmiRecipeCategory category, RecipeHolder<T> recipe, int width, int height) {
		this.category = category;
		this.recipe = recipe;
		this.width = width;
		this.height = height;

		ResourceLocation recipeId = recipe.id();
		String path = String.format("emi/%s/%s/%s", category.name, recipeId.getNamespace(), recipeId.getPath());
		this.id = TwilightForestMod.prefix(path);

		this.inputs = new ArrayList<>();
		this.addInputs(this.inputs);
		this.outputs = new ArrayList<>();
		this.addOutputs(this.outputs);
	}

	protected abstract void addInputs(List<EmiIngredient> inputs);

	protected abstract void addOutputs(List<EmiStack> outputs);

	public RecipeHolder<T> getRecipe() {
		return this.recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return this.category;
	}

	@Override
	@Nullable
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public int getDisplayWidth() {
		return this.width;
	}

	@Override
	public int getDisplayHeight() {
		return this.height;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return this.inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return this.outputs;
	}
}
