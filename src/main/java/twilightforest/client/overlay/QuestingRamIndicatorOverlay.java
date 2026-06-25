package twilightforest.client.overlay;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import tamaized.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.entity.passive.QuestRam;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

public class QuestingRamIndicatorOverlay {

	private static final Identifier QUESTING_RAM_CHECK_SPRITE = TwilightForestMod.prefix("questing_ram_check");
	private static final Identifier QUESTING_RAM_X_SPRITE = TwilightForestMod.prefix("questing_ram_x");

	@Autowired(dist = Dist.CLIENT)
	private static QuestingRamCurrentContext questingRamCurrentContext;

	public static void render(Minecraft minecraft, GuiGraphicsExtractor graphics, Gui gui, Player player) {
		if (player != null && !minecraft.options.hideGui && TFConfig.showQuestRamCrosshairIndicator) {
			if (minecraft.options.getCameraType().isFirstPerson() && (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || gui.canRenderCrosshairForSpectator(minecraft.hitResult)) && minecraft.crosshairPickEntity instanceof QuestRam ram) {
				ItemStack stack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
				if (!stack.isEmpty()) {
					for (var questEntry : questingRamCurrentContext.getContext().questItems().entrySet()) {
						if (questEntry.getValue().test(stack)) {
							int j = ((graphics.guiHeight() - 1) / 2) - 11;
							int k = ((graphics.guiWidth() - 1) / 2) - 3;
							if (!ram.isColorPresent(questEntry.getKey())) {
								graphics.blitSprite(RenderPipelines.GUI_TEXTURED, QUESTING_RAM_X_SPRITE, k, j, 7, 7);
							} else {
								graphics.blitSprite(RenderPipelines.GUI_TEXTURED, QUESTING_RAM_CHECK_SPRITE, k, j, 7, 7);
							}
							break;
						}
					}
				}
			}
		}
	}
}
