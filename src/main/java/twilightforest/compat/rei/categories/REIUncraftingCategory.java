package twilightforest.compat.rei.categories;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIServerPlugin;
import twilightforest.compat.rei.displays.REIUncraftingDisplay;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class REIUncraftingCategory implements DisplayCategory<REIUncraftingDisplay> {
	private final Renderer icon;
	private final Component localizedName;
	public static final Function<Boolean, ResourceLocation> TEXTURE = dark -> dark ? TwilightForestMod.getGuiTexture("uncrafting_jei_dark.png") : TwilightForestMod.getGuiTexture("uncrafting_jei.png");

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

		//background
		widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), bounds));
		List<Ingredient> outputs = new ArrayList<>(display.getRecipe().getIngredients()); //Collect each ingredient
		outputs.replaceAll(ingredient -> Ingredient.of(Arrays.stream(ingredient.getItems())
			.filter(o -> !o.is(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS))
			.filter(o -> !o.hasCraftingRemainingItem())));

		CraftingRecipe recipe = display.getRecipe();

		for (int j = 0, k = 0; j - k < outputs.size() && j < 9; j++) {
			int x = j % 3, y = j / 3;
			if ((recipe.canCraftInDimensions(x, 3) | recipe.canCraftInDimensions(3, y)) && !(recipe instanceof ShapelessRecipe)) {
				k++;
				continue;
			} //Skips empty spaces in shaped recipes
			widgets.add(Widgets.createSlot(new Point(bounds.getX() + x * 18 + 63, bounds.getY() + y * 18 + 1)).markOutput().disableBackground().entries(EntryIngredients.ofIngredient(outputs.get(j - k)))); //Set input as output and place in the grid
		}

		widgets.add(Widgets.createSlot(new Point(bounds.getX() + 5, bounds.getY() + 19)).markInput().disableBackground().entries(display.getInputEntries().getFirst()));//Set the outputs as inputs and draw the item you're uncrafting in the right spot as well

		int cost = display.getRecipe() instanceof UncraftingRecipe ur ? ur.getCost() : RecipeViewerConstants.getRecipeCost(display.getOutputEntries().stream().map(entryStacks -> entryStacks.getFirst().cheatsAs().getValue()).toList());
		if (cost > 0) {
			String costStr = cost + "";
			widgets.add(Widgets.createLabel(new Point(bounds.getX() + 45 - Minecraft.getInstance().font.width(costStr), bounds.getY() + 22), Component.literal(costStr)).color(RecipeViewerConstants.getXPColor(cost)));
		}

		return widgets;
	}
}
