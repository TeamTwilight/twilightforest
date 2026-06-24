package twilightforest.compat.emi.recipes;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.emi.TFEmiCategories;
import twilightforest.compat.emi.widget.EmiBlockWidget;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

import java.util.List;

public class EmiDryingRecipe implements EmiRecipe {
	private static final int WIDTH = 70;
	private static final int HEIGHT = 30;

	private final RecipeHolder<DryingRecipe> recipe;
	private final Ingredient input;
	private final ItemStack output;

	public EmiDryingRecipe(RecipeHolder<DryingRecipe> recipe) {
		this.recipe = recipe;
		this.input = recipe.value().getInput();
		this.output = recipe.value().getResult();
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return TFEmiCategories.DRYING;
	}

	@Override
	public @Nullable Identifier getId() {
		return this.recipe.id();
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return List.of(EmiIngredient.of(this.input));
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
		widgets.addFillingArrow(22, 0, 20 * 60 * 50);
		widgets.addSlot(this.getInputs().getFirst(), 0, 0).drawBack(false);
		widgets.add(new EmiBlockWidget(TFBlocks.OAK_DRYING_RACK.get().defaultBlockState(), -1, 19));

		widgets.add(new EmiBlockWidget(TFBlocks.OAK_DRYING_RACK.get().defaultBlockState(), 50, 19));
		widgets.addSlot(this.getOutputs().getFirst(), 51, 0).drawBack(false).recipeContext(this);

		Component time = RecipeViewerConstants.getDryingTime(this.recipe.value().getDryingTime());
		widgets.addText(time, 35 - Minecraft.getInstance().font.width(time.getString()) / 2, 20, 0xFF808080, false);

		widgets.addTooltipText(List.of(Component.translatable("gui.twilightforest.drying_ticks", this.recipe.value().getDryingTime()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)), 22, 1, 24, 17);
	}
}
