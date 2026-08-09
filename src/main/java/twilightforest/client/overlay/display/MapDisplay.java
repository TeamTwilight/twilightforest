package twilightforest.client.overlay.display;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

public class MapDisplay implements ItemDisplay {

	private static final RenderType MAP_BACKGROUND = RenderTypes.text(Identifier.withDefaultNamespace("textures/map/map_background.png"));
	private static final Identifier MAP_BACKGROUND_CHECKERBOARD = Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png");

	@Override
	public void render(ItemStack item, GuiGraphicsExtractor graphics, Minecraft minecraft, Gui gui, Player player, int widestWidgetWidth) {
		PoseStack stack = new PoseStack();
		MapId mapid = item.get(DataComponents.MAP_ID);
		if (mapid == null)
			return;

		MapItemSavedData data = MapItem.getSavedData(item, minecraft.level);
		if (data == null)
			return;

		ByteBufferBuilder byteBuffer = new ByteBufferBuilder(256);
		VertexConsumer consumer = new BufferBuilder(byteBuffer, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

		Matrix4f matrix4f = stack.last().pose();

		//render map background
		float start = Math.max(widestWidgetWidth / 2 - 50, 0);
		consumer.addVertex(matrix4f, start, 100.0F, -2.0F).setColor(-1).setUv(0.0F, 1.0F).setLight(LightCoordsUtil.FULL_BRIGHT);
		consumer.addVertex(matrix4f, start + 100.0F, 100.0F, -2.0F).setColor(-1).setUv(1.0F, 1.0F).setLight(LightCoordsUtil.FULL_BRIGHT);
		consumer.addVertex(matrix4f, start + 100.0F, 0.0F, -2.0F).setColor(-1).setUv(1.0F, 0.0F).setLight(LightCoordsUtil.FULL_BRIGHT);
		consumer.addVertex(matrix4f, start, 0.0F, -2.0F).setColor(-1).setUv(0.0F, 0.0F).setLight(LightCoordsUtil.FULL_BRIGHT);

		byteBuffer.close();

		//render map data
		stack.pushPose();
		//these transformations are very important, otherwise icons render behind the map graphics
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.translate(-4.75F, -4.75F, -1.0F);
		stack.translate(-start, 0.0F, 0.0F);
		stack.scale(0.7075F, 0.7075F, -0.7075F);
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));

		MapRenderState renderState = new MapRenderState();
		minecraft.getMapRenderer().extractRenderState(mapid, data, renderState);
		renderState.decorations.forEach(state -> state.renderOnFrame = true);
		graphics.blit(MAP_BACKGROUND_CHECKERBOARD, (int) start, 0, (int) start + 100, 100, 0, 1, 0, 1);
		graphics.pose().translate(4.75F, 4.75F);
		graphics.pose().scale(0.7075F);
		graphics.map(renderState);

		stack.popPose();
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
