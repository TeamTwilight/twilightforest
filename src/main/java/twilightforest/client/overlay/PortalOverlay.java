package twilightforest.client.overlay;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import twilightforest.components.entity.TFPortalAttachment;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;

public class PortalOverlay {

	public static void render(GuiGraphicsExtractor graphics, Minecraft minecraft, Player player) {
		Window window = minecraft.getWindow();
		if (player != null) {
			TFPortalAttachment portal = player.getData(TFDataAttachments.TF_PORTAL_COOLDOWN);
			if (portal.getPortalTimer() > 0) {
//				RenderSystem.disableDepthTest();
//				RenderSystem.depthMask(false);
//				RenderSystem.enableBlend();
//				graphics.setColor(1.0F, 1.0F, 1.0F, (float) portal.getPortalTimer() / (float) TFPortalAttachment.MAX_TICKS);
//				TextureAtlasSprite textureatlassprite = minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState());
//				graphics.blit(0, 0, -90, window.getGuiScaledWidth(), window.getGuiScaledHeight(), textureatlassprite);
//				RenderSystem.disableBlend();
//				RenderSystem.depthMask(true);
//				RenderSystem.enableDepthTest();
//				graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
}
