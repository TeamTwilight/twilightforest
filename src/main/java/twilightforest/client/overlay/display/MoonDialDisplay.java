package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.MoonPhase;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDataComponents;
import twilightforest.item.MoonDialItem;

import java.util.Optional;

public class MoonDialDisplay implements ItemDisplay {

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		Optional<MoonPhase> phase = item.getOrDefault(TFDataComponents.MOON_DIAL_PHASE, Optional.empty());
		int phaseIndex = (phase.isPresent()) ? phase.orElseThrow().index() : 0;
		FormattedCharSequence formattedcharsequence = this.getText(item).getVisualOrderText();
		int xRow = phaseIndex % 4;
		int yRow = phaseIndex / 4 % 2;
		int xMin = xRow * 8;
		int yMin = yRow * 8;
		graphics.blit(RenderPipelines.GUI_TEXTURED, TwilightForestMod.getGuiTexture("moon.png"), (widestWidgetWidth / 2 - 5) - minecraft.font.width(formattedcharsequence) / 2, 0, xMin, yMin, 8, 8, 32, 16);
		graphics.text(minecraft.font, formattedcharsequence, Math.max(0, (widestWidgetWidth / 2 + 5) - minecraft.font.width(formattedcharsequence) / 2), 0, 0xFFFFFFFF);
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		int textWidth = minecraft.font.width(this.getText(item).getVisualOrderText());
		return new Bounds(Math.max(0, (widestWidgetWidth / 2 - 5) - (textWidth / 2)), 0, textWidth + 10, minecraft.font.lineHeight);
	}

	private Component getText(ItemStack item) {
		Optional<MoonPhase> moonPhase = item.getOrDefault(TFDataComponents.MOON_DIAL_PHASE, Optional.empty());
		return MoonDialItem.getMoonPhaseComponent(moonPhase.orElse(null));
	}

}
