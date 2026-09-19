package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import net.neoforged.neoforge.client.renderstate.MapDecorationRenderStateModifier;
import twilightforest.TwilightForestMod;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

public class ConqueredMapIconRenderer implements IMapDecorationRenderer, MapDecorationRenderStateModifier {

	public static final ContextKey<Boolean> CONQUERED_KEY = new ContextKey<>(TwilightForestMod.prefix("conquered"));

	@Override
	public void accept(MapItemSavedData mapData, MapRenderState mapRenderState, MapRenderState.MapDecorationRenderState decoration) {
		if (mapData instanceof TFMagicMapData map)
			decoration.setRenderData(CONQUERED_KEY, map.conqueredStructures.contains(MagicMapItem.makeName(decoration.type, decoration.x, decoration.y)));
	}

	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decoration, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int light, int index) {
		if (!Boolean.TRUE.equals(decoration.getRenderData(CONQUERED_KEY)))
			return false;

		stack.pushPose();
		stack.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F, 0.0F);
		stack.mulPose(Axis.ZP.rotationDegrees((decoration.rot * 360) / 16.0F));
		stack.scale(2.0F, 2.0F, 2.0F);
		stack.translate(-1.0F, -1.0F, -0.005F);
		float depth = -0.095F;
		TextureAtlasSprite xSprite = decorationSprites.getSprite(MapDecorationTypes.RED_X.value().assetId());
		submitNodeCollector.submitCustomGeometry(stack, RenderTypes.text(xSprite.atlasLocation()), (pose, consumer) -> {
			consumer.addVertex(pose, -1.0F, 1.0F, depth).setColor(-1).setUv(xSprite.getU0(), xSprite.getV0()).setLight(light);
			consumer.addVertex(pose, 1.0F, 1.0F, depth).setColor(-1).setUv(xSprite.getU1(), xSprite.getV0()).setLight(light);
			consumer.addVertex(pose, 1.0F, -1.0F, depth).setColor(-1).setUv(xSprite.getU1(), xSprite.getV1()).setLight(light);
			consumer.addVertex(pose, -1.0F, -1.0F, depth).setColor(-1).setUv(xSprite.getU0(), xSprite.getV1()).setLight(light);
		});
		stack.popPose();

		return false;
	}
}
