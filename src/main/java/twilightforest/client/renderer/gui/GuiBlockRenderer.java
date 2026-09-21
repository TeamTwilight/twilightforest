package twilightforest.client.renderer.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class GuiBlockRenderer extends PictureInPictureRenderer<GuiBlockRenderState> {

	private final BlockDisplayContext displayContext = BlockDisplayContext.create();
	private final BlockModelRenderState blockModelRenderState = new BlockModelRenderState();

	public GuiBlockRenderer(MultiBufferSource.BufferSource bufferSource) {
		super(bufferSource);
	}

	@Override
	public Class<GuiBlockRenderState> getRenderStateClass() {
		return GuiBlockRenderState.class;
	}

	@Override
	protected void renderToTexture(GuiBlockRenderState renderState, PoseStack poseStack) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
		poseStack.scale(1.0F, -1.0F, -1.0F);
		poseStack.translate(-0.5F, -0.5F, -0.5F);
		FeatureRenderDispatcher dispatcher = minecraft.gameRenderer.getFeatureRenderDispatcher();
		minecraft.getBlockModelResolver().update(blockModelRenderState, renderState.blockState(), displayContext);
		blockModelRenderState.submit(poseStack, dispatcher.getSubmitNodeStorage(), 15728880, OverlayTexture.NO_OVERLAY, 0);
		dispatcher.renderAllFeatures();
	}

	@Override
	protected float getTranslateY(int height, int guiScale) {
		return height / 2.0F;
	}

	@Override
	protected String getTextureLabel() {
		return "block";
	}
}
