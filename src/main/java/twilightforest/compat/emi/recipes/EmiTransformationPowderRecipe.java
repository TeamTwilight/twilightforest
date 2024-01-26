package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCompat;
import twilightforest.compat.emi.EmiEntityWidget;
import twilightforest.item.recipe.TransformPowderRecipe;

import java.util.List;

public class EmiTransformationPowderRecipe extends TFEmiRecipe<TransformPowderRecipe> {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	public static final ResourceLocation TEXTURES = TwilightForestMod.getGuiTexture("transformation_jei.png");
	public static final EmiTexture BACKGROUND = new EmiTexture(TEXTURES, 0, 0, WIDTH, HEIGHT);
	public static final EmiTexture SINGLE_ARROW = new EmiTexture(TEXTURES, 116, 0, 23, 15);
	public static final EmiTexture DOUBLE_ARROW = new EmiTexture(TEXTURES, 116, 16, 23, 15);

	public final EmiTexture arrow;

	public EmiTransformationPowderRecipe(RecipeHolder<TransformPowderRecipe> recipe) {
		super(TFEmiCompat.TRANSFORMATION, recipe, WIDTH, HEIGHT);
		this.arrow = recipe.value().isReversible() ? DOUBLE_ARROW : SINGLE_ARROW;
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(this.getRecipe().value().input());
		SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(this.getRecipe().value().result());
		if (inputEgg != null) {
			inputs.add(EmiStack.of(inputEgg));
			if (this.getRecipe().value().isReversible() && outputEgg != null) {
				inputs.add(EmiStack.of(outputEgg));
			}
		}
	}

	@Override
	protected void addOutputs(List<EmiStack> outputs) {
		SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(this.getRecipe().value().input());
		SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(this.getRecipe().value().result());
		if (outputEgg != null) {
			outputs.add(EmiStack.of(outputEgg));
			if (this.getRecipe().value().isReversible() && inputEgg != null) {
				outputs.add(EmiStack.of(inputEgg));
			}
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(BACKGROUND, 0, 0);
		widgets.add(new EmiEntityWidget(this.getRecipe().value().input(), 7, 12, 32));
		widgets.addTexture(this.arrow, 46, 19);
		widgets.add(new EmiEntityWidget(this.getRecipe().value().result(), 75, 12, 32));
	}

	//doesn't make sense to have this on recipe trees
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
