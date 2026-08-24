package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix3x2fStack;

public class MapDisplay implements ItemDisplay {

	private static final Identifier MAP_BACKGROUND = Identifier.withDefaultNamespace("textures/map/map_background.png");
	private static final Identifier MAP_BACKGROUND_CHECKERBOARD = Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png");
	private final MapRenderState mapRenderState = new MapRenderState();

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		Matrix3x2fStack stack = graphics.pose();
		MapId mapid = item.get(DataComponents.MAP_ID);
		if (mapid == null)
			return;

		MapItemSavedData data = MapItem.getSavedData(item, minecraft.level);
		if (data == null)
			return;
		//render map background
		int start = Math.max(widestWidgetWidth / 2 - 50, 0);
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MAP_BACKGROUND_CHECKERBOARD, 0, 0, 100, 100);
		//render map data
		stack.pushMatrix();
		//these transformations are very important, otherwise icons render behind the map graphics
		stack.translate(-4.75F, -4.75F);
		stack.translate(-start, 0.0F);
		stack.scale(0.7075F, 0.7075F);
		minecraft.getMapRenderer().extractRenderState(mapid, data, this.mapRenderState);
		graphics.map(this.mapRenderState);
		stack.popMatrix();
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
