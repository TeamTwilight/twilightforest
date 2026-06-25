package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapDisplay implements ItemDisplay {

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		MapId mapid = item.get(DataComponents.MAP_ID);
		if (mapid == null) return;

		MapItemSavedData data = MapItem.getSavedData(item, minecraft.level);
		if (data == null) return;

		// Render map using MapTextureManager
		Identifier textureId = minecraft.getMapTextureManager().prepareMapTexture(mapid, data);
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, textureId, 0, 0, 100, 100);
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
