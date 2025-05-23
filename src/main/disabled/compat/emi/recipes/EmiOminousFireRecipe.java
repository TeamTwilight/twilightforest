package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.EmiEntityWidget;
import twilightforest.compat.emi.TFEmiCompat;

import java.util.ArrayList;
import java.util.List;

public class EmiOminousFireRecipe implements EmiRecipe {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	public static final ResourceLocation TEXTURES = TwilightForestMod.getGuiTexture("ominous_fire_jei.png");
	public static final EmiTexture BACKGROUND = new EmiTexture(TEXTURES, 0, 0, WIDTH, HEIGHT);
	public static final EmiTexture SINGLE_ARROW = new EmiTexture(TEXTURES, 116, 0, 23, 15);

	private final EntityType<?> input;
	private final EntityType<?> output;

	public EmiOminousFireRecipe(EntityType<?> input, EntityType<?> output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return TFEmiCompat.EXANIMATE;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return null;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		List<EmiIngredient> inputs = new ArrayList<>();
		SpawnEggItem inputEgg = DeferredSpawnEggItem.byId(this.input);
		if (inputEgg != null) inputs.add(EmiStack.of(inputEgg));
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		List<EmiStack> outputs = new ArrayList<>();

		SpawnEggItem outputEgg = DeferredSpawnEggItem.byId(this.output);
		if (outputEgg != null) outputs.add(EmiStack.of(outputEgg));
		return outputs;
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
		widgets.add(new EmiEntityWidget(this.input, 7, 12, 32));
		widgets.addTexture(SINGLE_ARROW, 46, 19);
		widgets.add(new EmiEntityWidget(this.output, 75, 12, 32));
	}

	//doesn't make sense to have this on recipe trees
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
