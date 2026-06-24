package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.joml.Matrix4f;
import twilightforest.item.mapdata.TFMagicMapData;

public class MagicMapPlayerIconRenderer implements IMapDecorationRenderer {

	//[VanillaCopy] of MapRenderer.RenderInstance.draw, but with a set depth offset instead of relying on index.
	//this allows the icon to render on top of everything else instead of sometimes on top, sometimes behind
	@Override
	public boolean render(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapItemSavedData mapData, MapDecorationTextureManager decorationTextures, boolean inItemFrame, int packedLight, int index) {
		if (mapData instanceof TFMagicMapData) {
			stack.pushPose();
			stack.translate(0.0F + (float)decoration.x() / 2.0F + 64.0F, 0.0F + (float)decoration.y() / 2.0F + 64.0F, -0.02F);
			stack.mulPose(Axis.ZP.rotationDegrees((float)(decoration.rot() * 360) / 16.0F));
			stack.scale(4.0F, 4.0F, 3.0F);
			stack.translate(-0.125F, 0.125F, 0.0F);
			Matrix4f matrix4f1 = stack.last().pose();
			TextureAtlasSprite textureatlassprite = decorationTextures.get(decoration);
			float f2 = textureatlassprite.getU0();
			float f3 = textureatlassprite.getV0();
			float f4 = textureatlassprite.getU1();
			float f5 = textureatlassprite.getV1();
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.text(textureatlassprite.atlasLocation()));
			consumer.addVertex(matrix4f1, -1.0F, 1.0F, -0.3F).setColor(-1).setUv(f2, f3).setLight(packedLight);
			consumer.addVertex(matrix4f1, 1.0F, 1.0F, -0.3F).setColor(-1).setUv(f4, f3).setLight(packedLight);
			consumer.addVertex(matrix4f1, 1.0F, -1.0F, -0.3F).setColor(-1).setUv(f4, f5).setLight(packedLight);
			consumer.addVertex(matrix4f1, -1.0F, -1.0F, -0.3F).setColor(-1).setUv(f2, f5).setLight(packedLight);
			stack.popPose();
			return true;
		}
		return false;
	}
}
