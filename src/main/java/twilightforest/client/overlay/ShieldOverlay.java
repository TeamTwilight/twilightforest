package twilightforest.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;

public class ShieldOverlay {

	private static final Identifier FORTIFICATION_SHIELD_SPRITE = TwilightForestMod.prefix("fortification_shield");

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, @Nullable Player player) {
		if (player != null && !minecraft.options.hideGui && (minecraft.gameMode.canHurtPlayer() || TFConfig.showFortificationShieldIndicatorInCreative) && player.hasData(TFDataAttachments.FORTIFICATION_SHIELDS) && player.getData(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft() > 0 && TFConfig.showFortificationShieldIndicator) {
			int shieldCount = player.getData(TFDataAttachments.FORTIFICATION_SHIELDS).shieldsLeft();
			for (int i = 0; i < Math.min(shieldCount, 10); i++) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTIFICATION_SHIELD_SPRITE, graphics.guiWidth() / 2 - 91 + (i * 8), graphics.guiHeight() - gui.leftHeight, 9, 9);
			}
			gui.leftHeight += 10;
		}
	}
}
