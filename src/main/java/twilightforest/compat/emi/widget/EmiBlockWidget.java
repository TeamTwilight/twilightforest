package twilightforest.compat.emi.widget;

import com.mojang.blaze3d.platform.Lighting;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

public class EmiBlockWidget extends Widget {

	private final BlockState state;
	private final Bounds bounds;

	public EmiBlockWidget(BlockState state, int x, int y) {
		this.state = state;
		this.bounds = new Bounds(x, y, 16, 16);
	}

	@Override
	public Bounds getBounds() {
		return this.bounds;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		graphics.pose().pushPose();
		Lighting.setupForFlatItems();
		graphics.pose().translate(this.getBounds().x(), this.getBounds().y(), 201.0D);
		graphics.pose().scale(16.0F, -16.0F, 16.0F);
		minecraft.getBlockRenderer().renderSingleBlock(this.state, graphics.pose(), bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		graphics.pose().popPose();
		bufferSource.endBatch();
		Lighting.setupFor3DItems();
	}
}
