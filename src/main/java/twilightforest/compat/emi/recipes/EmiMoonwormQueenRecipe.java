package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.Util;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCompat;
import twilightforest.compat.emi.OrderedSlotWidget;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

import java.util.List;

public class EmiMoonwormQueenRecipe extends TFEmiRecipe<MoonwormQueenRepairRecipe> {

	public EmiMoonwormQueenRecipe() {
		super(TFEmiCompat.MOONWORM_QUEEN, new RecipeHolder<>(TFRecipes.MOONWORM_QUEEN_REPAIR_RECIPE.getId(), new MoonwormQueenRepairRecipe(CraftingBookCategory.MISC)), RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 2, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT);
	}

	@Override
	protected void addInputs(List<EmiIngredient> inputs) {
		inputs.add(EmiStack.of(TFItems.MOONWORM_QUEEN));
	}

	@Override
	protected void addOutputs(List<EmiStack> outputs) {
		outputs.add(EmiStack.of(TFItems.MOONWORM_QUEEN));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
		widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);
		widgets.addSlot(EmiStack.of(RecipeViewerConstants.DAMAGED_MOONWORM_QUEEN), 0, 0);
		widgets.addSlot(EmiStack.of(TFItems.TORCHBERRIES), 18, 0);

		widgets.add(new OrderedSlotWidget(RecipeViewerConstants.BERRY_2_LIST.stream().map(EmiStack::of).toList(), 36, 0, 1000L));
		widgets.add(new OrderedSlotWidget(RecipeViewerConstants.BERRY_3_LIST.stream().map(EmiStack::of).toList(), 0, 18, 1000L));
		widgets.add(new OrderedSlotWidget(RecipeViewerConstants.BERRY_4_LIST.stream().map(EmiStack::of).toList(), 18, 18, 1000L));

		for (int i = 5; i < 9; ++i) {
			widgets.addSlot(EmiStack.EMPTY, i % 3 * 18, i / 3 * 18);
		}

		widgets.add(new OrderedSlotWidget(RecipeViewerConstants.MOONWORM_QUEEN_LIST.stream().map(EmiStack::of).toList(), 92, 14, 1000L)
			.appendTooltip(RecipeViewerConstants.MOONWORM_QUEEN_TOOLTIP).large(true).recipeContext(this));
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
