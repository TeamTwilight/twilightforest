package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

public class ItemDisplayTooltipComponent implements ClientTooltipComponent {
	private static final Identifier SLOT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
	private static final int SLOT_SIZE = 24;
	private static final int ITEM_INSET = 4;

	private final NonNullList<ItemStack> contents;

	public ItemDisplayTooltipComponent(TravellersGogglesItem.Tooltip tooltip) {
		this.contents = tooltip.contents().items();
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

	private void renderSlot(int x, int y, int itemIndex, GuiGraphicsExtractor graphics, Font font) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, x, y, SLOT_SIZE, SLOT_SIZE);

		if (itemIndex < this.contents.size()) {
			ItemStack itemstack = this.contents.get(itemIndex);
			if (itemstack.isEmpty()) {
				this.renderBlankSlot(graphics, itemIndex, x, y);
			} else {
				graphics.item(itemstack, x + ITEM_INSET, y + ITEM_INSET, itemIndex);
				graphics.itemDecorations(font, itemstack, x + ITEM_INSET, y + ITEM_INSET);
			}
		} else {
			this.renderBlankSlot(graphics, itemIndex, x, y);
		}
	}

	private void renderBlankSlot(GuiGraphicsExtractor graphics, int index, int x, int y) {
		if (index < 0 || index >= ItemDisplayContents.LAYOUT.size())
			return;
		ItemDisplayType type = ItemDisplayContents.LAYOUT.get(index).get();
		type.slotTexture().ifPresent(identifier -> graphics.blit(RenderPipelines.GUI_TEXTURED, identifier, x + ITEM_INSET, y + ITEM_INSET, 0, 0, 16, 16, 16, 16));
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_SIZE;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_SIZE;
	}

	private int gridSizeX() {
		return ItemDisplayContents.LAYOUT.size();
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
