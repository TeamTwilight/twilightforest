package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ClockDisplay implements ItemDisplay {
	private static final long INITIAL_DAY_OFFSET_IN_TICKS = 6000L;
	private static final long MINECRAFT_DAY_LENGTH_IN_TICKS = 24000L;
	private static final long REAL_LIFE_DAY_LENGTH_IN_SECONDS = 86400L;
	private static final DateTimeFormatter FORMAT_24 = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter FORMAT_12 = DateTimeFormatter.ofPattern("hh:mm a");
	private static final NavigableMap<Long, TimeFrame> TIME_FRAMES = new TreeMap<>(Map.ofEntries(
		Map.entry(0L, TimeFrame.SUNRISE),
		Map.entry(501L, TimeFrame.DAY),
		Map.entry(12500L, TimeFrame.SUNSET),
		Map.entry(14000L, TimeFrame.NIGHT),
		Map.entry(22000L, TimeFrame.SUNRISE)
	));

	private static long getMinecraftClockTimeInTicks(long ticks) {
		return (ticks + INITIAL_DAY_OFFSET_IN_TICKS) % MINECRAFT_DAY_LENGTH_IN_TICKS;
	}

	private static Component getGameTime(Level level, boolean use24HourFormat) {
		if (!level.dimensionType().hasSkyLight())
			return Component.translatable("travellers_gear.modifier.twilightforest.item_display.clock.unknown");

		long rawTime = level.getDefaultClockTime();
		long ticksOfDay = getMinecraftClockTimeInTicks(rawTime);
		long secondsOfDay = (ticksOfDay * REAL_LIFE_DAY_LENGTH_IN_SECONDS) / MINECRAFT_DAY_LENGTH_IN_TICKS;
		LocalTime localTime = LocalTime.ofSecondOfDay(secondsOfDay);
		return Component.literal(localTime.format(use24HourFormat ? FORMAT_24 : FORMAT_12));
	}

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		FormattedCharSequence formattedcharsequence = this.getText(minecraft).getVisualOrderText();
		if (minecraft.level.dimensionType().hasSkyLight()) {
			int k = this.getFrameForTime(minecraft.level.getDefaultClockTime()).frame;
			int xRow = k % 2;
			int yRow = k / 2 % 2;
			float xMin = xRow * 8;
			float yMin = yRow * 8;
			graphics.blit(RenderPipelines.GUI_TEXTURED, TwilightForestMod.getGuiTexture("time.png"), (widestWidgetWidth / 2 - 5) - minecraft.font.width(formattedcharsequence) / 2, 0, xMin, yMin, 8, 8, 16, 16);
		}
		graphics.text(minecraft.font, formattedcharsequence, Math.max(0, (widestWidgetWidth / 2 + 5) - minecraft.font.width(formattedcharsequence) / 2), 0, 0xFFFFFF);
	}

	private TimeFrame getFrameForTime(long dayTimeInTicks) {
		long time = dayTimeInTicks % MINECRAFT_DAY_LENGTH_IN_TICKS;
		return TIME_FRAMES.floorEntry(time).getValue();
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(minecraft));
		boolean showClockFace = minecraft.level.dimensionType().hasSkyLight();
		return new Bounds(Math.max(0, (widestWidgetWidth / 2 - (showClockFace ? 5 : 0)) - (textWidth / 2)), 0, textWidth + (showClockFace ? 10 : 0), minecraft.font.lineHeight);
	}

	private Component getText(Minecraft minecraft) {
		return getGameTime(minecraft.level, TFConfig.clock24HourFormat);
	}

	private enum TimeFrame {
		SUNRISE(0),
		DAY(1),
		SUNSET(2),
		NIGHT(3);

		final int frame;

		TimeFrame(int frame) {
			this.frame = frame;
		}
	}
}
