package twilightforest.client.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;

import java.util.ArrayList;
import java.util.List;

// modified ClientBundleTooltip
public class TravellersBeltTooltipComponent implements ClientTooltipComponent {
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
	private final List<ItemStack> contents = new ArrayList<>();

	public TravellersBeltTooltipComponent(TravellersArmorBeltItem.Tooltip tooltip) {
		ItemContainerContents contents = tooltip.contents();
		for (int i = 0; i < 9; i++) {
			ItemStack stack = contents.getSlots() <= i ? ItemStack.EMPTY : contents.getStackInSlot(i);
			this.contents.add(stack);
		}
	}

	@Override
	public void renderImage(@NotNull Font font, int x, int y, GuiGraphics guiGraphics) {
		guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		int k = 0;

		for (int gridY = 0; gridY < gridSizeY(); gridY++) {
			for (int gridX = 0; gridX < gridSizeX(); gridX++) {
				int renderX = x + gridX * 18 + 1;
				int renderY = y + gridY * 20 + 1;
				this.renderSlot(renderX, renderY, k++, guiGraphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphics guiGraphics, Font font) {
		if (itemIndex >= this.contents.size()) {
			this.blit(guiGraphics, x, y);
		} else {
			ItemStack itemstack = this.contents.get(itemIndex);
			this.blit(guiGraphics, x, y);
			guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
			guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
		}
	}

	private void blit(GuiGraphics guiGraphics, int x, int y) {
		guiGraphics.blitSprite(Texture.SLOT.sprite, x, y, 0, Texture.SLOT.w, Texture.SLOT.h);
	}

	private int backgroundWidth() {
		return this.gridSizeX() * 18 + 2;
	}

	private int backgroundHeight() {
		return this.gridSizeY() * 20 + 2;
	}

	private int gridSizeX() {
		return 9;
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

	enum Texture {
		SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), 18, 20);

		public final ResourceLocation sprite;
		public final int w;
		public final int h;

		Texture(ResourceLocation sprite, int w, int h) {
			this.sprite = sprite;
			this.w = w;
			this.h = h;
		}
	}
}
