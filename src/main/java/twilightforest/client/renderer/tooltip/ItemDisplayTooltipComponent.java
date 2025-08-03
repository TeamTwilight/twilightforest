package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.item.travellers_gear.TravellersGogglesItem;

import java.util.List;

public class ItemDisplayTooltipComponent implements ClientTooltipComponent {
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
	private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot");
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
			var holder = TFRegistries.ITEM_DISPLAY_TYPE.asHolderIdMap().byId(itemIndex);
			if (this.contents.get(itemIndex).isEmpty()) {
				if (holder != null && holder.value().slotTexture().isPresent()) {
					graphics.blit(holder.value().slotTexture().get(), x + 1, y + 1, 0, 0, 16, 16, 16, 16);
				}
			} else {
				ItemStack itemstack = this.contents.get(itemIndex);
				graphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
				graphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
			}
		}
	}

	private int backgroundWidth() {
		return this.gridSizeX() * SLOT_WIDTH + 2;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * SLOT_HEIGHT + 2;
	}

	private int gridSizeX() {
		return TFRegistries.ITEM_DISPLAY_TYPE.size();
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
