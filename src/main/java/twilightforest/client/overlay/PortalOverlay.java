package twilightforest.client.overlay;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.ARGB;
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
				RenderPipeline pipeline = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
					.withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
					.build();
				Material.Baked textureatlassprite = minecraft.getModelManager().getBlockStateModelSet().getParticleMaterial(TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState());
				graphics.blit(pipeline, textureatlassprite.sprite().atlasLocation(), 0, 0, 0, 0, -90, window.getGuiScaledWidth(), window.getGuiScaledHeight(), ARGB.color(portal.getPortalTimer() / TFPortalAttachment.MAX_TICKS, 0, 0, 0));
			}
		}
	}
}
