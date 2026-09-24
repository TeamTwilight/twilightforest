package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix3x2f;
import twilightforest.client.renderer.gui.GuiMapRenderState;

public class MapDisplay implements ItemDisplay {

	private static final Identifier MAP_BACKGROUND_CHECKERBOARD = Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png");
	private static final int MAP_INSET = 5;
	private static final int MAP_SIZE = 90;
	private final MapRenderState mapRenderState = new MapRenderState();

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		MapId mapid = item.get(DataComponents.MAP_ID);
		if (mapid == null)
			return;

		MapItemSavedData data = MapItem.getSavedData(item, minecraft.level);
		if (data == null)
			return;
		//render map background
		int start = Math.max(widestWidgetWidth / 2 - 50, 0);
		graphics.blit(RenderPipelines.GUI_TEXTURED, MAP_BACKGROUND_CHECKERBOARD, start, 0, 0.0F, 0.0F, 100, 100, 100, 100);
		//render map data
		minecraft.getMapRenderer().extractRenderState(mapid, data, mapRenderState);
		int x0 = start + MAP_INSET;
		graphics.submitPictureInPictureRenderState(new GuiMapRenderState(
			mapRenderState,
			new Matrix3x2f(graphics.pose()),
			x0,
			MAP_INSET,
			x0 + MAP_SIZE,
			MAP_INSET + MAP_SIZE,
			(float) MAP_SIZE / MapRenderer.WIDTH,
			graphics.peekScissorStack()
		));
	}

	@Override
	public DisplayPosition displayPosition() {
		return DisplayPosition.TOP;
	}

	@Override
	public Bounds getWidgetSize(ItemStack item, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		return new Bounds(Math.max(widestWidgetWidth / 2 - 50, 0), 0, 100, 102);
	}
}
