package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EmiUncraftingRecipe<T extends CraftingRecipe> extends TFEmiRecipe<T> {

	@Nullable
	private List<EmiIngredient> displayedOutputs;

	public EmiUncraftingRecipe(RecipeHolder<T> recipe) {
		super(TFEmiCompat.UNCRAFTING, recipe, RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 2, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		if (this.getRecipe().value() instanceof UncraftingRecipe uncraftingRecipe) {
			inputs.add(EmiIngredient.of(uncraftingRecipe.getInput(), uncraftingRecipe.getCount()));//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			inputs.add(EmiStack.of(this.getRecipe().value().getResultItem(Minecraft.getInstance().level.registryAccess())));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}
	}

	@Override
	protected void addOutputs(List<EmiStack> finalOutput) {
		this.displayedOutputs = new ArrayList<>();
		List<Ingredient> outputs = new ArrayList<>(this.getRecipe().value().getIngredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(Arrays.stream(ingredient.getItems())
			.filter(o -> !o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS))
			.filter(o -> o.getCraftingRemainingItem().isEmpty())));

		for (int index = 0, offset = 0; index - offset < outputs.size() && index < 9; index++) {
			int x = index % 3, y = index / 3;
			if ((this.getRecipe().value().canCraftInDimensions(x, 3) | this.getRecipe().value().canCraftInDimensions(3, y)) && !(this.getRecipe().value() instanceof ShapelessRecipe)) {
				offset++;
				this.displayedOutputs.add(EmiStack.EMPTY);
				continue;
			} //Skips empty spaces in shaped recipes
			Ingredient ingredient = outputs.get(index - offset);
			this.displayedOutputs.add(EmiIngredient.of(ingredient));
			for (ItemStack output : ingredient.getItems())
				finalOutput.add(EmiStack.of(output)); //Set input as output and place in the grid
		}

		while (this.displayedOutputs.size() < 9) {
			this.displayedOutputs.add(EmiStack.EMPTY);
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 35, 18);

		for (int i = 0; i < this.displayedOutputs.size(); i++) {
			int x = i % 3;
			int y = i / 3;
			widgets.addSlot(this.displayedOutputs.get(i), x * 18 + 63, y * 18);
		}

		if (this.getRecipe().value() instanceof UncraftingRecipe uncraftingRecipe) {
			ItemStack[] stacks = uncraftingRecipe.getInput().getItems();
			ItemStack[] stackedStacks = new ItemStack[stacks.length];
			for (int i = 0; i < stacks.length; i++) stackedStacks[i] = new ItemStack(stacks[0].getItem(), uncraftingRecipe.getCount());
			widgets.addSlot(new ListEmiIngredient(Stream.of(stackedStacks).map(EmiStack::of).toList(), uncraftingRecipe.getCount()), 5, 19);//If the recipe is an uncrafting recipe, we need to get the ingredient instead of an itemStack
		} else {
			widgets.addSlot(EmiStack.of(this.getRecipe().value().getResultItem(Minecraft.getInstance().level.registryAccess())), 5, 14).large(true).recipeContext(this); //Set the outputs as inputs and draw the item you're uncrafting in the right spot as well
		}

		int cost = this.getRecipe().value() instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(this.displayedOutputs.stream().map(ingredient -> ingredient.getEmiStacks().getFirst().getItemStack()).toList());
		if (cost > 0) {
			String costStr = cost + "";
			widgets.addText(Component.literal(costStr), 48 - Minecraft.getInstance().font.width(costStr), 22, RecipeViewerConstants.getXPColor(cost), true);
		}
	}

	//things get a little too insane when this is true so im gonna leave it false for now
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
