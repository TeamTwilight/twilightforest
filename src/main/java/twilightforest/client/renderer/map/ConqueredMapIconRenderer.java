package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.joml.Matrix4f;
import twilightforest.item.MagicMapItem;
import twilightforest.item.mapdata.TFMagicMapData;

public class ConqueredMapIconRenderer implements IMapDecorationRenderer {

	private boolean isConquered(MapRenderState state, MapRenderState.MapDecorationRenderState decoration) {
		if (state.getRenderData(TFMagicMapData.CONQUERED_STRUCTURES_KEY) != null && !state.getRenderData(TFMagicMapData.CONQUERED_STRUCTURES_KEY).isEmpty()) {
			return state.getRenderData(TFMagicMapData.CONQUERED_STRUCTURES_KEY).contains(MagicMapItem.makeName(decoration.type, decoration.x, decoration.y));
		}
		return false;
	}

	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decoState, PoseStack stack, MultiBufferSource source, MapRenderState state, MapDecorationTextureManager manager, boolean inItemFrame, int light, int index) {
		if (this.isConquered(state, decoState)) {
			stack.pushPose();
			stack.translate(0.0F + decoState.x / 2.0F + 64.0F, 0.0F + decoState.y / 2.0F + 64.0F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees((decoState.rot * 360) / 16.0F));
			stack.scale(2.0F, 2.0F, 2.0F);
			stack.translate(-1.0F, -1.0F, -0.005F);
			Matrix4f matrix4f = stack.last().pose();
			float depth = -0.095F;
			TextureAtlasSprite xSprite = Minecraft.getInstance().getMapDecorationTextures().getSprite(MapDecorationTypes.RED_X.value().assetId());
			float f2 = xSprite.getU0();
			float f3 = xSprite.getV0();
			float f4 = xSprite.getU1();
			float f5 = xSprite.getV1();
			VertexConsumer consumer = source.getBuffer(RenderType.text(xSprite.atlasLocation()));
			consumer.addVertex(matrix4f, -1.0F, 1.0F, depth).setColor(-1).setUv(f2, f3).setLight(light);
			consumer.addVertex(matrix4f, 1.0F, 1.0F, depth).setColor(-1).setUv(f4, f3).setLight(light);
			consumer.addVertex(matrix4f, 1.0F, -1.0F, depth).setColor(-1).setUv(f4, f5).setLight(light);
			consumer.addVertex(matrix4f, -1.0F, -1.0F, depth).setColor(-1).setUv(f2, f5).setLight(light);
			stack.popPose();
		}

		return false;
	}
}
