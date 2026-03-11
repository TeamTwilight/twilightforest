package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.item.MoonDialItem;

public class MoonDialDisplay implements ItemDisplay {

	@Override
	public void render(ItemStack item, GuiGraphics graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int k = minecraft.level.getMoonPhase();
		FormattedCharSequence formattedcharsequence = this.getText(minecraft).getVisualOrderText();
		int xRow = k % 4;
		int yRow = k / 4 % 2;
		float xMin = xRow * 8;
		float yMin = yRow * 8;
		graphics.blit(TwilightForestMod.getGuiTexture("moon.png"), (widestWidgetWidth / 2 - 5) - minecraft.font.width(formattedcharsequence) / 2, 0, xMin, yMin, 8, 8, 32, 16);
		graphics.drawString(minecraft.font, formattedcharsequence, Math.max(0, (widestWidgetWidth / 2 + 5) - minecraft.font.width(formattedcharsequence) / 2), 0, 0xFFFFFF);
	}

	private Component getText(Minecraft minecraft) {
		return MoonDialItem.getMoonPhase(minecraft.level);
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(minecraft));
		return new Bounds(Math.max(0, (widestWidgetWidth / 2 - 5) - (textWidth / 2)), 0, textWidth + 10, minecraft.font.lineHeight);
	}
}
