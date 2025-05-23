package twilightforest.compat.rei.categories;

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
import twilightforest.compat.rei.displays.REITransformationPowderDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class REITransformationPowderCategory implements DisplayCategory<REITransformationPowderDisplay> {
	public static final CategoryIdentifier<REITransformationPowderDisplay> TRANSFORMATION = CategoryIdentifier.of(TwilightForestMod.ID, "transformation");
	public static final Function<Boolean, ResourceLocation> TEXTURE = dark -> dark ? TwilightForestMod.getGuiTexture("transformation_jei_dark.png") : TwilightForestMod.getGuiTexture("transformation_jei.png");

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

		//background
		widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), new Rectangle(bounds.getX(), bounds.getY(), RecipeViewerConstants.GENERIC_RECIPE_WIDTH, RecipeViewerConstants.GENERIC_RECIPE_HEIGHT)));

		//arrow
		if (display.isReversible) {
			widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), bounds.getX() + 46, bounds.getY() + 19, 116, 16, 23, 15));
		} else {
			widgets.add(Widgets.createTexturedWidget(TEXTURE.apply(REIRuntime.getInstance().isDarkThemeEnabled()), bounds.getX() + 46, bounds.getY() + 19, 116, 0, 23, 15));
		}

		//input
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 8, bounds.getY() + 11, 32, 32))
			.markInput()
			.disableHighlight()
			.entries(display.getInputEntries().getFirst())
			.disableBackground()
		);

		//output
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 76, bounds.getY() + 11, 32, 32))
			.markOutput()
			.disableHighlight()
			.entries(display.getOutputEntries().getFirst())
			.disableBackground()
		);

		return widgets;
	}
}