package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.joml.Matrix4f;
import org.jspecify.annotations.NonNull;

public class MagicMapPlayerIconRenderer implements IMapDecorationRenderer {

	//[VanillaCopy] of MapRenderer.RenderInstance.draw, but with a set depth offset instead of relying on index.
	//this allows the icon to render on top of everything else instead of sometimes on top, sometimes behind
	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decoration, @NonNull PoseStack stack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull MapRenderState mapRenderState, @NonNull TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		// FIXME find a new way to get mapData
//		if (!(mapData instanceof TFMagicMapData)) {
//			return false;
//		}

		if (decoration.atlasSprite == null) return false;

		stack.pushPose();
		stack.translate(0.0F + (float)decoration.x / 2.0F + 64.0F, 0.0F + (float)decoration.y / 2.0F + 64.0F, -0.02F);
		stack.mulPose(Axis.ZP.rotationDegrees((float)(decoration.rot * 360) / 16.0F));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);
		Matrix4f matrix4f1 = stack.last().pose();
		TextureAtlasSprite textureatlassprite = decoration.atlasSprite;
		float f2 = textureatlassprite.getU0();
		float f3 = textureatlassprite.getV0();
		float f4 = textureatlassprite.getU1();
		float f5 = textureatlassprite.getV1();
		submitNodeCollector.submitCustomGeometry(stack, RenderTypes.text(textureatlassprite.atlasLocation()), (_, consumer) -> {
			consumer.addVertex(matrix4f1, -1.0F, 1.0F, -0.3F).setColor(-1).setUv(f2, f3).setLight(packedLight);
			consumer.addVertex(matrix4f1, 1.0F, 1.0F, -0.3F).setColor(-1).setUv(f4, f3).setLight(packedLight);
			consumer.addVertex(matrix4f1, 1.0F, -1.0F, -0.3F).setColor(-1).setUv(f4, f5).setLight(packedLight);
			consumer.addVertex(matrix4f1, -1.0F, -1.0F, -0.3F).setColor(-1).setUv(f2, f5).setLight(packedLight);
		});
		return true;
	}
}
