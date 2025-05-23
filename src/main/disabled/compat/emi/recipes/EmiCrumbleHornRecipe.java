package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.EmiItemEntityWidget;
import twilightforest.compat.emi.TFEmiCompat;

import java.util.List;

public class EmiCrumbleHornRecipe implements EmiRecipe {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	public static final ResourceLocation TEXTURES = TwilightForestMod.getGuiTexture("crumble_horn_jei.png");
	public static final EmiTexture BACKGROUND = new EmiTexture(TEXTURES, 0, 4, WIDTH, HEIGHT);
	public static final EmiTexture SLOT = new EmiTexture(TEXTURES, 116, 0, 26, 26);

	private final Block input;
	private final Block output;

	public EmiCrumbleHornRecipe(Block input, Block output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return TFEmiCompat.CRUMBLE_HORN;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return null;
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
		widgets.addTexture(BACKGROUND, 0, 0);
		widgets.addSlot(this.getInputs().get(0), 14, 10).large(true).drawBack(false);
		if (this.output != Blocks.AIR) {
			widgets.addTexture(SLOT, 76, 10);
			widgets.addSlot(this.getOutputs().get(0), 76, 10).large(true).drawBack(false);
		} else {
			widgets.add(new EmiItemEntityWidget(this.input, 76, 10));
		}
	}
}
