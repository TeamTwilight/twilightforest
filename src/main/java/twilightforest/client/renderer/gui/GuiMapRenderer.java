package twilightforest.client.renderer.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.LightCoordsUtil;

public class GuiMapRenderer extends PictureInPictureRenderer<GuiMapRenderState> {

	@Override
	public Class<GuiMapRenderState> getRenderStateClass() {
		return GuiMapRenderState.class;
	}

	@Override
	protected void renderToTexture(GuiMapRenderState guiMapRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		Minecraft minecraft = Minecraft.getInstance();
		poseStack.translate(-MapRenderer.WIDTH / 2.0F, -MapRenderer.HEIGHT / 2.0F, 0.0F);
		minecraft.getMapRenderer().render(guiMapRenderState.mapRenderState(), poseStack, submitNodeCollector, false, LightCoordsUtil.FULL_BRIGHT);
	}

	@Override
	protected float getTranslateY(int height, int guiScale) {
		return height / 2.0F;
	}

	@Override
	protected String getTextureLabel() {
		return "map";
	}
}
