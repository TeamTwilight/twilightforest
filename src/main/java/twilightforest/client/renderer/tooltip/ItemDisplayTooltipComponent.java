package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;

public class ItemDisplayTooltipComponent implements ClientTooltipComponent {
	private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/background");
	private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot");
	private static final int SLOT_WIDTH = 18;
	private static final int SLOT_HEIGHT = 20;

	private final NonNullList<ItemStack> contents;

	public ItemDisplayTooltipComponent(TravellersGogglesItem.Tooltip tooltip) {
		this.contents = tooltip.contents().items();
	}

	@Override
	public void renderImage(@NotNull Font font, int x, int y, GuiGraphics guiGraphics) {
		guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		int k = 0;

		for (int gridY = 0; gridY < gridSizeY(); gridY++) {
			for (int gridX = 0; gridX < gridSizeX(); gridX++) {
				int renderX = x + gridX * SLOT_WIDTH + 1;
				int renderY = y + gridY * SLOT_HEIGHT + 1;
				this.renderSlot(renderX, renderY, k++, guiGraphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphics graphics, Font font) {
		graphics.blitSprite(SLOT_SPRITE, x, y, 0, SLOT_WIDTH, SLOT_HEIGHT);

		if (itemIndex < this.contents.size()) {
			ItemStack itemstack = this.contents.get(itemIndex);
			if (itemstack.isEmpty()) {
				this.renderBlankSlot(graphics, itemIndex, x, y);
			} else {
				graphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
				graphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
			}
		} else {
			this.renderBlankSlot(graphics, itemIndex, x, y);
		}
	}

	private void renderBlankSlot(GuiGraphics graphics, int index, int x, int y) {
		if (index < 0 || index >= ItemDisplayContents.LAYOUT.size()) return;
		ItemDisplayType type = ItemDisplayContents.LAYOUT.get(index).get();
		type.slotTexture().ifPresent(identifier -> graphics.blit(identifier, x + 1, y + 1, 0, 0, 16, 16, 16, 16));
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_WIDTH + 2;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_HEIGHT + 2;
	}

	private int gridSizeX() {
		return ItemDisplayContents.LAYOUT.size();
	}

	private int gridSizeY() {
		return 1;
	}

	@Override
	public int getHeight() {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(@NotNull Font font) {
		return this.backgroundWidth();
	}
}
