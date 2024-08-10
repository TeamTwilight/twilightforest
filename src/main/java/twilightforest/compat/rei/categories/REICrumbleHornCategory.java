package twilightforest.compat.rei.categories;

import me.shedaniel.math.Dimension;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.displays.REICrumbleHornDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class REICrumbleHornCategory implements DisplayCategory<REICrumbleHornDisplay> {

	public static final CategoryIdentifier<REICrumbleHornDisplay> CRUMBLE_HORN = CategoryIdentifier.of(TwilightForestMod.ID, "crumble_horn");
	public static final Function<Boolean, ResourceLocation> TEXTURE = dark -> dark ? TwilightForestMod.getGuiTexture("crumble_horn_jei_dark.png") : TwilightForestMod.getGuiTexture("crumble_horn_jei.png");

	private final Renderer icon;
	private final Component localizedName;

	public REICrumbleHornCategory() {
		this.icon = EntryStacks.of(TFItems.CRUMBLE_HORN);
		this.localizedName = Component.translatable("gui.twilightforest.crumble_horn_jei");
	}

	@Override
	public CategoryIdentifier<? extends REICrumbleHornDisplay> getCategoryIdentifier() {
		return CRUMBLE_HORN;
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public Renderer getIcon() {
		return icon;
	}

	@Override
	public int getDisplayWidth(REICrumbleHornDisplay display) {
		return RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 8;
	}

	@Override
	public int getDisplayHeight() {
		return RecipeViewerConstants.GENERIC_RECIPE_HEIGHT + 8;
	}

	@Override
	public List<Widget> setupDisplay(REICrumbleHornDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		//background
		widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), new Rectangle(bounds.getX(), bounds.getY(), RecipeViewerConstants.GENERIC_RECIPE_WIDTH, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT)));

		//output slot (only renders if the block changes)
		if (!display.isResultAir) {
			widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), bounds.getX() + 76, bounds.getY() + 14, 116, 0, 26, 26));
		}

		//input
		widgets.add(Widgets.createSlot(offsetPoint(bounds, 19, 19))
			.markInput()
			.disableBackground()
			.entries(display.getInputEntries().getFirst())
		);

		//output
		if (!display.isResultAir) {
			widgets.add(Widgets.createSlot(offsetPoint(bounds, 81, 19))
				.markOutput()
				.disableBackground()
				.entries(display.getOutputEntries().getFirst())
			);
		} else {
			widgets.add(Widgets.createSlot(new Rectangle(offsetPoint(bounds, 75, 12), new Dimension(32, 32)))
				.markOutput()
				.disableHighlight()
				.disableBackground()
				.entries(display.getOutputEntries().getFirst())
			);
		}

		return widgets;
	}

	public static Point offsetPoint(Rectangle bounds, int x, int y) {
		return new Point(bounds.getX() + x, bounds.getY() + y);
	}
}
