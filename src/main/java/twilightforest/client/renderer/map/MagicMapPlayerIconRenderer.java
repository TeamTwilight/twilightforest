package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.function.BiConsumer;

public class MagicMapPlayerIconRenderer implements IMapDecorationRenderer, BiConsumer<MapItemSavedData, MapRenderState> {

	public static final ContextKey<Boolean> IS_MAGIC_MAP_KEY = new ContextKey<>(TwilightForestMod.prefix("is_magic_map"));

	@Override
	public void accept(MapItemSavedData mapData, MapRenderState mapRenderState) {
		mapRenderState.setRenderData(IS_MAGIC_MAP_KEY, mapData instanceof TFMagicMapData);
	}

	//[VanillaCopy] of MapRenderer.RenderInstance.draw, but with a set depth offset instead of relying on index.
	//this allows the icon to render on top of everything else instead of sometimes on top, sometimes behind
	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decoration, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		if (!Boolean.TRUE.equals(mapRenderState.getRenderData(IS_MAGIC_MAP_KEY)))
			return false;

		TextureAtlasSprite textureatlassprite = decoration.atlasSprite;
		if (textureatlassprite == null)
			return false;

		stack.pushPose();
		stack.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F, -0.02F);
		stack.mulPose(Axis.ZP.rotationDegrees(decoration.rot * 360 / 16.0F));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);
		submitNodeCollector.submitCustomGeometry(stack, RenderTypes.text(textureatlassprite.atlasLocation()), (pose, consumer) -> {
			consumer.addVertex(pose, -1.0F, 1.0F, -0.3F).setColor(-1).setUv(textureatlassprite.getU0(), textureatlassprite.getV0()).setLight(packedLight);
			consumer.addVertex(pose, 1.0F, 1.0F, -0.3F).setColor(-1).setUv(textureatlassprite.getU1(), textureatlassprite.getV0()).setLight(packedLight);
			consumer.addVertex(pose, 1.0F, -1.0F, -0.3F).setColor(-1).setUv(textureatlassprite.getU1(), textureatlassprite.getV1()).setLight(packedLight);
			consumer.addVertex(pose, -1.0F, -1.0F, -0.3F).setColor(-1).setUv(textureatlassprite.getU0(), textureatlassprite.getV1()).setLight(packedLight);
		});
		stack.popPose();
		return true;
	}
}
