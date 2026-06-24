package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCategories;
import twilightforest.compat.emi.widget.EmiBlockWidget;
import twilightforest.compat.emi.widget.EmiEntityWidget;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.List;

public class EmiOminousFireRecipe implements EmiRecipe {
	private static final int WIDTH = RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
	//height is adjusted slightly to allow 2 entries per page
	private static final int HEIGHT = RecipeViewerConstants.GENERIC_RECIPE_HEIGHT - 8;

	public static final EmiTexture SLOT = new EmiTexture(TwilightForestMod.getGuiTexture("big_slot.png"), 0, 0, 34, 34, 34, 34, 34, 34);

	private final EntityType<?> input;
	private final EntityType<?> output;

	public EmiOminousFireRecipe(EntityType<?> input, EntityType<?> output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return TFEmiCategories.EXANIMATE;
	}

	@Override
	public @Nullable Identifier getId() {
		return TwilightForestMod.prefix("/ominous/" + BuiltInRegistries.ENTITY_TYPE.getKey(this.output).getNamespace() + "/" + BuiltInRegistries.ENTITY_TYPE.getKey(this.output).getPath());
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
		widgets.addTexture(SLOT, 7, 10);
		widgets.add(new EmiEntityWidget(this.input, 7, 12, 32));
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 46, 19);
		widgets.add(new EmiBlockWidget(TFBlocks.OMINOUS_FIRE.get().defaultBlockState(), 47, 44));
		widgets.addTexture(SLOT, 75, 10);
		widgets.add(new EmiEntityWidget(this.output, 75, 12, 32));
	}

	//doesn't make sense to have this on recipe trees
	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
