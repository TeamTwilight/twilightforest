package twilightforest.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

public class PortalOverlay {

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Player player) {
		if (player != null) {
			TFPortalAttachment portal = player.getData(TFDataAttachments.TF_PORTAL_COOLDOWN);
			if (portal.getPortalTimer() > 0) {
				int color = ARGB.color(255 * portal.getPortalTimer() / TFPortalAttachment.MAX_TICKS, 255, 255, 255);
				TextureAtlasSprite sprite = minecraft.getModelManager().getBlockStateModelSet().getParticleMaterial(TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState()).sprite();
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
			}
		}
	}
}
