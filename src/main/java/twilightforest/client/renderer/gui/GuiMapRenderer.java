package twilightforest.client.renderer.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.util.LightCoordsUtil;

public class GuiMapRenderer extends PictureInPictureRenderer<GuiMapRenderState> {

	public GuiMapRenderer(MultiBufferSource.BufferSource bufferSource) {
		super(bufferSource);
	}

	@Override
	public Class<GuiMapRenderState> getRenderStateClass() {
		return GuiMapRenderState.class;
	}

	@Override
	protected void renderToTexture(GuiMapRenderState renderState, PoseStack poseStack) {
		Minecraft minecraft = Minecraft.getInstance();
		poseStack.translate(-MapRenderer.WIDTH / 2.0F, -MapRenderer.HEIGHT / 2.0F, 0.0F);
		FeatureRenderDispatcher dispatcher = minecraft.gameRenderer.getFeatureRenderDispatcher();
		minecraft.getMapRenderer().render(renderState.mapRenderState(), poseStack, dispatcher.getSubmitNodeStorage(), false, LightCoordsUtil.FULL_BRIGHT);
		dispatcher.renderAllFeatures();
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
