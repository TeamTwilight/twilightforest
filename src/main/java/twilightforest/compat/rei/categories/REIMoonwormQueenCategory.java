package twilightforest.compat.rei.categories;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.MoonwormQueenRepairRecipe;

import java.util.List;
import java.util.Optional;

public class REIMoonwormQueenCategory implements DisplayCategory<DefaultCraftingDisplay<MoonwormQueenRepairRecipe>> {
	public static final CategoryIdentifier<DefaultCraftingDisplay<MoonwormQueenRepairRecipe>> MOONWORM_QUEEN = CategoryIdentifier.of(TwilightForestMod.ID, "moonworm_queen");

	private final Renderer icon;
	private final Component localizedName;

	public REIMoonwormQueenCategory() {
		this.icon = EntryStacks.of(TFItems.MOONWORM_QUEEN);
		this.localizedName = Component.translatable("gui.twilightforest.moonworm_queen_jei");
	}

	public static DefaultCraftingDisplay<MoonwormQueenRepairRecipe> createDisplay() {
		return new DefaultCraftingDisplay<>(List.of(EntryIngredient.of(EntryStacks.of(RecipeViewerConstants.DAMAGED_MOONWORM_QUEEN)),
			EntryIngredient.of(EntryStacks.of(TFItems.TORCHBERRIES)),
			EntryIngredient.of(RecipeViewerConstants.BERRY_2_LIST.stream().map(EntryStacks::of).toList()),
			EntryIngredient.of(RecipeViewerConstants.BERRY_3_LIST.stream().map(EntryStacks::of).toList()),
			EntryIngredient.of(RecipeViewerConstants.BERRY_4_LIST.stream().map(EntryStacks::of).toList())),
			List.of(EntryIngredient.of(RecipeViewerConstants.MOONWORM_QUEEN_LIST.stream().map(EntryStacks::of).toList())),
			Optional.of(new RecipeHolder<>(TFRecipes.MOONWORM_QUEEN_REPAIR_RECIPE.getId(), new MoonwormQueenRepairRecipe(CraftingBookCategory.MISC)))) {

			@Override
			public int getWidth() {
				return RecipeViewerConstants.GENERIC_RECIPE_WIDTH;
			}

			@Override
			public int getHeight() {
				return RecipeViewerConstants.GENERIC_RECIPE_HEIGHT;
			}
		};
	}

	@Override
	public CategoryIdentifier<? extends DefaultCraftingDisplay<MoonwormQueenRepairRecipe>> getCategoryIdentifier() {
		return MOONWORM_QUEEN;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public List<Widget> setupDisplay(DefaultCraftingDisplay<MoonwormQueenRepairRecipe> display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
		List<InputIngredient<EntryStack<?>>> input = display.getInputIngredients(3, 3);
		List<Slot> slots = Lists.newArrayList();
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++)
				slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
		for (InputIngredient<EntryStack<?>> ingredient : input) {
			slots.get(ingredient.getIndex()).entries(ingredient.get());
		}
		widgets.addAll(slots);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().getFirst()).disableBackground().markOutput());
		widgets.add(Widgets.createShapelessIcon(bounds));
		return widgets;
	}
}
