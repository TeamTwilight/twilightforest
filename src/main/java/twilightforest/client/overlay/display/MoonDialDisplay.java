package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.MoonDialComponent;
import twilightforest.init.TFDataComponents;
import twilightforest.item.MoonDialItem;

public class MoonDialDisplay implements ItemDisplay {

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		MoonDialComponent data = item.get(TFDataComponents.MOON_DIAL);
		int k = data != null ? data.phase().index() : 0;
		FormattedCharSequence formattedcharsequence = this.getText(item).getVisualOrderText();
		int xRow = k % 4;
		int yRow = k / 4 % 2;
		int xMin = xRow * 8;
		int yMin = yRow * 8;
		graphics.blit(TwilightForestMod.getGuiTexture("moon.png"), (widestWidgetWidth / 2 - 5) - minecraft.font.width(formattedcharsequence) / 2, 0, xMin, yMin, 8, 8, 32, 16);
		graphics.text(minecraft.font, formattedcharsequence, Math.max(0, (widestWidgetWidth / 2 + 5) - minecraft.font.width(formattedcharsequence) / 2), 0, 0xFFFFFFFF);
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(item).getVisualOrderText());
		return new Bounds(Math.max(0, (widestWidgetWidth / 2 - 5) - (textWidth / 2)), 0, textWidth + 10, minecraft.font.lineHeight);
	}

	private Component getText(ItemStack item) {
		MoonDialComponent data = item.get(TFDataComponents.MOON_DIAL);
		return MoonDialItem.getMoonPhaseComponent(data != null ? data.phase() : null);
	}
}
