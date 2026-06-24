package twilightforest.compat.rei.categories;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class REITransformationPowderCategory implements DisplayCategory<REITransformationPowderDisplay> {
	public static final CategoryIdentifier<REITransformationPowderDisplay> TRANSFORMATION = CategoryIdentifier.of(TwilightForestMod.ID, "transformation");
	public static final Function<Boolean, Identifier> SLOT = dark -> dark ? TwilightForestMod.getGuiTexture("big_slot_dark.png") : TwilightForestMod.getGuiTexture("big_slot.png");
	public static final Identifier ARROW = TwilightForestMod.getGuiTexture("transformation_arrow.png");
	public static final Identifier DOUBLE_ARROW = TwilightForestMod.getGuiTexture("transformation_double_arrow.png");

	private final Renderer icon;
	private final Component localizedName;

	public REITransformationPowderCategory() {
		this.icon = EntryStacks.of(TFItems.TRANSFORMATION_POWDER);
		this.localizedName = Component.translatable("gui.twilightforest.transformation_jei");
	}

	@Override
	public CategoryIdentifier<? extends REITransformationPowderDisplay> getCategoryIdentifier() {
		return TRANSFORMATION;
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
	public int getDisplayHeight() {
		return RecipeViewerConstants.GENERIC_RECIPE_HEIGHT + 8;
	}

	@Override
	public int getDisplayWidth(REITransformationPowderDisplay display) {
		return RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 8;
	}

	@Override
	public List<Widget> setupDisplay(REITransformationPowderDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		//arrow
		if (display.isReversible) {
			widgets.add(Widgets.createTexturedWidget(DOUBLE_ARROW, new Rectangle(bounds.getX() + 47, bounds.getY() + 7, 23, 30), 0, 0, 23, 30));
		} else {
			widgets.add(Widgets.createTexturedWidget(ARROW, new Rectangle(bounds.getX() + 47, bounds.getY() + 7, 23, 30), 0, 0, 23, 30));
		}

		createEntitySlots(display, widgets, bounds);

		return widgets;
	}

	public static void createEntitySlots(BasicDisplay display, List<Widget> widgets, Rectangle bounds) {
		//input
		widgets.add(Widgets.createTexturedWidget(SLOT.apply(REIRuntime.getInstance().isDarkThemeEnabled()), new Rectangle(bounds.getX() + 8, bounds.getY() + 11, 34, 34), 0, 0, 34, 34));
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 8, bounds.getY() + 11, 32, 32))
			.markInput()
			.disableHighlight()
			.entries(display.getInputEntries().getFirst())
			.disableBackground()
		);

		//output
		widgets.add(Widgets.createTexturedWidget(SLOT.apply(REIRuntime.getInstance().isDarkThemeEnabled()), new Rectangle(bounds.getX() + 76, bounds.getY() + 11, 34, 34), 0, 0, 34, 34));
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 76, bounds.getY() + 11, 32, 32))
			.markOutput()
			.disableHighlight()
			.entries(display.getOutputEntries().getFirst())
			.disableBackground()
		);
	}
}