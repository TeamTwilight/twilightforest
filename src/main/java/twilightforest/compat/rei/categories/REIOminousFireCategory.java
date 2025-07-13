package twilightforest.compat.rei.categories;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.displays.REIOminousFireDisplay;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.List;

public class REIOminousFireCategory implements DisplayCategory<REIOminousFireDisplay> {
	public static final CategoryIdentifier<REIOminousFireDisplay> OMINOUS_FIRE = CategoryIdentifier.of(TwilightForestMod.ID, "ominous_fire");

	private final Renderer icon;
	private final Component localizedName;

	public REIOminousFireCategory() {
		this.icon = EntryStacks.of(TFItems.EXANIMATE_ESSENCE);
		this.localizedName = Component.translatable("gui.twilightforest.ominous_fire_jei");
	}

	@Override
	public CategoryIdentifier<? extends REIOminousFireDisplay> getCategoryIdentifier() {
		return OMINOUS_FIRE;
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
	public int getDisplayWidth(REIOminousFireDisplay display) {
		return RecipeViewerConstants.GENERIC_RECIPE_WIDTH + 8;
	}

	@Override
	public List<Widget> setupDisplay(REIOminousFireDisplay display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		//arrow
        widgets.add(Widgets.createArrow(new Point(bounds.getX() + 46, bounds.getY() + 19)));
		widgets.add(Widgets.createSlot(new Point(bounds.getX() + 48, bounds.getY() + 45))
			.disableBackground()
			.disableHighlight()
			.notInteractable()
			.entries(display.getInputEntries().get(2)));

		REITransformationPowderCategory.createEntitySlots(display, widgets, bounds);

		return widgets;
	}
}