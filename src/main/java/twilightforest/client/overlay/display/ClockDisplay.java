package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;

public class ClockDisplay implements ItemDisplay {

	@Override
	public void render(ItemStack item, GuiGraphics graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		FormattedCharSequence formattedcharsequence = this.getText(minecraft).getVisualOrderText();
		if (minecraft.level.dimensionType().natural()) {
			int k = this.getFrameForTime(minecraft.level.dimensionType().fixedTime().orElse(minecraft.level.getDayTime()));
			int xRow = k % 2;
			int yRow = k / 2 % 2;
			float xMin = xRow * 8;
			float yMin = yRow * 8;
			graphics.blit(TwilightForestMod.getGuiTexture("time.png"), (widestWidgetWidth / 2 - 5) - minecraft.font.width(formattedcharsequence) / 2, 0, xMin, yMin, 8, 8, 16, 16);
		}
		graphics.drawString(minecraft.font, formattedcharsequence, Math.max(0, (widestWidgetWidth / 2 + 5) - minecraft.font.width(formattedcharsequence) / 2), 0, 0xFFFFFF);
	}

	private int getFrameForTime(long time) {
		if (time >= 22000 || time <= 500) {
			return 0; //sunrise
		} else if (time >= 12500 && time < 14000) {
			return 2; //sunset
		} else if (time >= 14000) {
			return 3; //night
		}
		return 1; //day
	}

	private Component getText(Minecraft minecraft) {
		return getGameTime(minecraft.level, TFConfig.clockMilitaryTime);
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(minecraft));
		boolean natural = minecraft.level.dimensionType().natural();
		return new Bounds(Math.max(0, (widestWidgetWidth / 2 - (natural ? 5 : 0)) - (textWidth / 2)), 0, textWidth + (natural ? 10 : 0), minecraft.font.lineHeight);
	}

	private static Component getGameTime(Level level, boolean militaryTime) {
		if (level.dimensionType().natural()) {
			double ratio = 1000.0D / 60.0D;

			long rawTime = level.dimensionType().fixedTime().orElse(level.getDayTime());
			int dayTime = (int) ((rawTime + 6000L) % (militaryTime ? 24000L : 12000L));
			int hours = dayTime / 1000;
			int minutes = (int) ((dayTime % 1000) / ratio);

			if (!militaryTime && hours == 0) {
				hours = 12;
			}

			String timeOfDay = "";

			if (!militaryTime) {
				timeOfDay = rawTime % 24000L > 6000L && rawTime < 18000L ? " PM" : " AM";
			}

			return Component.literal((hours < 10 ? "0" : "") +
				hours + ":" +
				(minutes < 10 ? "0" : "") +
				minutes + timeOfDay);
		} else {
			return Component.translatable("travellers_gear.modifier.twilightforest.item_display.clock.unknown");
		}
	}
}
