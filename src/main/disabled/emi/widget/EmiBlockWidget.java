package twilightforest.compat.emi.widget;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.compat.RecipeViewerConstants;

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
		RecipeViewerConstants.renderFlatBlock(graphics.pose(), this.state, new Vec3(this.getBounds().x(), this.getBounds().y(), 201.0D), 20.0F);
	}
}
