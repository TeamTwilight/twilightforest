package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCategories;
import twilightforest.compat.emi.widget.EmiItemEntityWidget;

import java.util.List;

public class EmiCrumbleHornRecipe implements EmiRecipe {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	private final Block input;
	private final Block output;

	public EmiCrumbleHornRecipe(Block input, Block output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return TFEmiCategories.CRUMBLE_HORN;
	}

	@Override
	public @Nullable Identifier getId() {
		return TwilightForestMod.prefix("/crumbling/" + BuiltInRegistries.BLOCK.getKey(this.input).getNamespace() + "/" + BuiltInRegistries.BLOCK.getKey(this.input).getPath());
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return List.of(EmiStack.of(this.input));
	}

	@Override
	public List<EmiStack> getOutputs() {
		return List.of(EmiStack.of(this.output));
	}

	@Override
	public int getDisplayWidth() {
		return WIDTH;
	}

	@Override
	public int getDisplayHeight() {
		return HEIGHT;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 44, 15);
		if (this.output != Blocks.AIR) {
			widgets.addSlot(this.getInputs().getFirst(), 14, 14);
			widgets.addSlot(this.getOutputs().getFirst(), 76, 10).large(true).recipeContext(this);
		} else {
			widgets.addSlot(this.getInputs().getFirst(), 14, 14).recipeContext(this);
			widgets.add(new EmiItemEntityWidget(this.input, 76, 10));
		}
	}
}
