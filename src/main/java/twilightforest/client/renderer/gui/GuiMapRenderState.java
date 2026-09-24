package twilightforest.client.renderer.gui;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.Nullable;

public record GuiMapRenderState(
	MapRenderState mapRenderState,
	Matrix3x2f pose,
	int x0,
	int y0,
	int x1,
	int y1,
	float scale,
	@Nullable ScreenRectangle scissorArea,
	@Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {

	public GuiMapRenderState(MapRenderState mapRenderState, Matrix3x2f pose, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea) {
		this(mapRenderState, pose, x0, y0, x1, y1, scale, scissorArea, getBounds(pose, x0, y0, x1, y1, scissorArea));
	}

	@Nullable
	private static ScreenRectangle getBounds(Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
		ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose);
		return scissorArea != null ? scissorArea.intersection(bounds) : bounds;
	}
}
