package twilightforest.client.renderer.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;

public class GuiBlockRenderer extends PictureInPictureRenderer<GuiBlockRenderState> {

	private final BlockDisplayContext displayContext = BlockDisplayContext.create();
	private final BlockModelRenderState blockModelRenderState = new BlockModelRenderState();

	@Override
	public Class<GuiBlockRenderState> getRenderStateClass() {
		return GuiBlockRenderState.class;
	}

	@Override
	protected void renderToTexture(GuiBlockRenderState guiBlockRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
		poseStack.scale(1.0F, -1.0F, -1.0F);
		poseStack.translate(-0.5F, -0.5F, -0.5F);
		minecraft.getBlockModelResolver().update(blockModelRenderState, guiBlockRenderState.blockState(), displayContext);
		blockModelRenderState.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
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
