package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCompat;
import twilightforest.compat.emi.EmiItemEntityWidget;
import twilightforest.item.recipe.CrumbleRecipe;

import java.util.List;

public class EmiCrumbleHornRecipe extends TFEmiRecipe<CrumbleRecipe> {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	public static final ResourceLocation TEXTURES = TwilightForestMod.getGuiTexture("crumble_horn_jei.png");
	public static final EmiTexture BACKGROUND = new EmiTexture(TEXTURES, 0, 4, WIDTH, HEIGHT);
	public static final EmiTexture SLOT = new EmiTexture(TEXTURES, 116, 0, 26, 26);

	public EmiCrumbleHornRecipe(RecipeHolder<CrumbleRecipe> recipe) {
		super(TFEmiCompat.CRUMBLE_HORN, recipe, WIDTH, HEIGHT);
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		inputs.add(EmiStack.of(this.getRecipe().value().input()));
	}

	@Override
	protected void addOutputs(List<EmiStack> outputs) {
		Block result = this.getRecipe().value().result();
		if (!result.defaultBlockState().isAir()) {
			outputs.add(EmiStack.of(result));
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(BACKGROUND, 0, 0);
		widgets.addSlot(this.getInputs().get(0), 14, 10).large(true).drawBack(false);
		if (!this.getOutputs().isEmpty()) {
			EmiStack output = this.getOutputs().get(0);
			widgets.addTexture(SLOT, 76, 10);
			widgets.addSlot(output, 76, 10).large(true).drawBack(false);
		} else {
			Block block = this.getRecipe().value().input();
			widgets.add(new EmiItemEntityWidget(block, 76, 10));
		}
	}
}
