package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;

import java.util.ArrayList;
import java.util.List;

// modified ClientBundleTooltip
public class TravellersBeltTooltipComponent implements ClientTooltipComponent {
	private static final Identifier SLOT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
	private static final int SLOT_MARGIN = 4;
	private static final int SLOT_SIZE = 24;
	private final List<ItemStack> contents = new ArrayList<>();

	public TravellersBeltTooltipComponent(TravellersArmorBeltItem.Tooltip tooltip) {
		ItemContainerContents contents = tooltip.contents();
		for (int i = 0; i < 9; i++) {
			ItemStack stack = contents.getSlots() <= i ? ItemStack.EMPTY : contents.getStackInSlot(i);
			this.contents.add(stack);
		}
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor guiGraphics) {
		int k = 0;

		for (int gridY = 0; gridY < gridSizeY(); gridY++) {
			for (int gridX = 0; gridX < gridSizeX(); gridX++) {
				int renderX = x + gridX * SLOT_SIZE;
				int renderY = y + gridY * SLOT_SIZE;
				this.renderSlot(renderX, renderY, k++, guiGraphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphicsExtractor guiGraphics, Font font) {
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, x, y, SLOT_SIZE, SLOT_SIZE);
		if (itemIndex < this.contents.size()) {
			ItemStack itemstack = this.contents.get(itemIndex);
			guiGraphics.item(itemstack, x + SLOT_MARGIN, y + SLOT_MARGIN, itemIndex);
			guiGraphics.itemDecorations(font, itemstack, x + SLOT_MARGIN, y + SLOT_MARGIN);
		}
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_SIZE;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_SIZE;
	}

	private int gridSizeX() {
		return 9;
	}

	private int gridSizeY() {
		return 1;
	}

	@Override
	public int getHeight(Font font) {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(Font font) {
		return this.backgroundWidth();
	}
}
