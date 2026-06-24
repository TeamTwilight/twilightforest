package twilightforest.compat.emi.recipes;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class TFEmiRecipe<T extends Recipe<?>> implements EmiRecipe {

	private final EmiRecipeCategory category;
	private final RecipeHolder<T> recipe;
	private final Identifier id;
	private final int width;
	private final int height;
	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;

	public TFEmiRecipe(EmiRecipeCategory category, RecipeHolder<T> recipe, String prefix, int width, int height) {
		this(category, recipe, EmiPort.getId(recipe.value()).withPrefix(prefix), width, height);
	}

	public TFEmiRecipe(EmiRecipeCategory category, RecipeHolder<T> recipe, Identifier id, int width, int height) {
		this.category = category;
		this.recipe = recipe;
		this.width = width;
		this.height = height;

		this.id = id;
		this.inputs = new ArrayList<>();
		this.inputs.addAll(recipe.value().getIngredients().stream().map(EmiIngredient::of).toList());
		this.addAdditionalInputs(this.inputs);
		this.outputs = new ArrayList<>();
		this.outputs.add(EmiStack.of(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())));
		this.addAdditionalOutputs(this.outputs);
	}

	protected abstract void addAdditionalInputs(List<EmiIngredient> inputs);

	protected abstract void addAdditionalOutputs(List<EmiStack> outputs);

	public RecipeHolder<T> getRecipe() {
		return this.recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return this.category;
	}

	@Override
	@Nullable
	public Identifier getId() {
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
