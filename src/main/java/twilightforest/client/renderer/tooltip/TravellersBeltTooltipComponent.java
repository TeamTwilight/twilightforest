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
	private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/background");
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
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
		int k = 0;

		for (int gridY = 0; gridY < gridSizeY(); gridY++) {
			for (int gridX = 0; gridX < gridSizeX(); gridX++) {
				int renderX = x + gridX * 18 + 1;
				int renderY = y + gridY * 20 + 1;
				this.renderSlot(renderX, renderY, k++, guiGraphics, font);
			}
		}
	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphicsExtractor guiGraphics, Font font) {
		if (itemIndex >= this.contents.size()) {
			this.blit(guiGraphics, x, y);
		} else {
			ItemStack itemstack = this.contents.get(itemIndex);
			this.blit(guiGraphics, x, y);
			guiGraphics.item(itemstack, x + 1, y + 1, itemIndex);
			guiGraphics.itemDecorations(font, itemstack, x + 1, y + 1);
		}
	}

	private void blit(GuiGraphicsExtractor guiGraphics, int x, int y) {
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Texture.SLOT.sprite, x, y, 0, Texture.SLOT.w, Texture.SLOT.h);
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
	public int getHeight(Font font) {
		return this.backgroundHeight() + 4;
	}

	@Override
	public int getWidth(Font font) {
		return this.backgroundWidth();
	}

	enum Texture {
		SLOT(Identifier.withDefaultNamespace("container/bundle/slot"), 18, 20);

		public final Identifier sprite;
		public final int w;
		public final int h;

		Texture(Identifier sprite, int w, int h) {
			this.sprite = sprite;
			this.w = w;
			this.h = h;
		}
	}
}
