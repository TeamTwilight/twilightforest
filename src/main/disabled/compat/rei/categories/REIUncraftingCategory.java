package twilightforest.compat.rei.categories;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIServerPlugin;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.tags.TFItemTags;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class REIUncraftingCategory implements DisplayCategory<REIUncraftingDisplay> {
	private final Renderer icon;
	private final Component localizedName;

	public REIUncraftingCategory() {
		this.icon = EntryStacks.of(TFBlocks.UNCRAFTING_TABLE);
		this.localizedName = Component.translatable("gui.twilightforest.uncrafting_jei");
	}

	@Override
	public CategoryIdentifier<REIUncraftingDisplay> getCategoryIdentifier() {
		return TFREIServerPlugin.UNCRAFTING;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public int getDisplayHeight() {
		return RecipeViewerConstants.GENERIC_RECIPE_HEIGHT + 10;
	}

	@Override
	public int getDisplayWidth(REIUncraftingDisplay display) {
		return RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 10;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public List<Widget> setupDisplay(REIUncraftingDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(5, 5);

		widgets.add(Widgets.createArrow(new Point(bounds.getX() + 29, bounds.getY() + 18)));

		List<Ingredient> outputs = new ArrayList<>(display.getRecipe().getIngredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(Arrays.stream(ingredient.getItems())
			.filter(o -> !o.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS))
			.filter(o -> !o.hasCraftingRemainingItem())));

		CraftingRecipe recipe = display.getRecipe();

		//create all 9 slots to fill with items below
		List<Slot> inputSlots = new ArrayList<>();
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				inputSlots.add(Widgets.createSlot(new Point(bounds.getX() + x * 18 + 63, bounds.getY() + y * 18 + 1)).markOutput());
			}
		}

		for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
			int x = j % 3, y = j / 3;
			if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
				k++;
				continue;
			} //Skips empty spaces in shaped recipes
			inputSlots.get(RecipeViewerConstants.getCraftingIndex(recipe, j - k)).entries(EntryIngredients.ofIngredient(outputs.get(j - k))); //Set input as output and place in the grid
		}

		widgets.addAll(inputSlots);

		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.getX() + 5, bounds.getY() + 19)));
		widgets.add(Widgets.createSlot(new Point(bounds.getX() + 5, bounds.getY() + 19)).markInput().disableBackground().entries(display.getInputEntries().getFirst()));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well

		int cost = display.getRecipe() instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(display.getOutputEntries().stream().filter(ingredient -> !ingredient.isEmpty()).map(ingredient -> (ItemStack)ingredient.getFirst().castValue()).toList());
		if (cost > 0) {
			String costStr = cost + "";
			widgets.add(Widgets.createLabel(new Point(bounds.getX() + 45 - Minecraft.getInstance().font.width(costStr), bounds.getY() + 22), Component.literal(costStr)).color(RecipeViewerConstants.getXPColor(cost)));
		}

		return widgets;
	}
}
