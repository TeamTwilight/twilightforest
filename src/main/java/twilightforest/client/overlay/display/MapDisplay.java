package twilightforest.client.overlay.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix3x2fStack;

public class MapDisplay implements ItemDisplay {

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
		graphics.blit(RenderPipelines.GUI_TEXTURED, MAP_BACKGROUND_CHECKERBOARD, start, 0, 0.0F, 0.0F, 100, 100, 100, 100);
		//render map data
		stack.pushMatrix();
		stack.translate(start + 4.75F, 4.75F);
		stack.scale(0.7075F, 0.7075F);
		minecraft.getMapRenderer().extractRenderState(mapid, data, this.mapRenderState);
		graphics.map(this.mapRenderState);
		renderNonFrameDecorations(graphics, minecraft.getTextureManager());
		stack.popMatrix();
	}

	private void renderNonFrameDecorations(GuiGraphicsExtractor graphics, TextureManager textureManager) {
		Matrix3x2fStack stack = graphics.pose();
		for (MapRenderState.MapDecorationRenderState decoration : this.mapRenderState.decorations) {
			TextureAtlasSprite sprite = decoration.atlasSprite;
			if (decoration.renderOnFrame || sprite == null)
				continue;

			AbstractTexture texture = textureManager.getTexture(sprite.atlasLocation());
			stack.pushMatrix();
			stack.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F);
			stack.rotate((float) (Math.PI / 180.0) * decoration.rot * 360.0F / 16.0F);
			stack.scale(4.0F, 4.0F);
			stack.translate(-0.125F, 0.125F);
			graphics.blit(texture.getTextureView(), texture.getSampler(), -1, -1, 1, 1, sprite.getU0(), sprite.getU1(), sprite.getV1(), sprite.getV0());
			stack.popMatrix();
		}
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
