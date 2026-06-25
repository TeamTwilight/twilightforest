package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.joml.Matrix4f;

public class ConqueredMapIconRenderer implements IMapDecorationRenderer {

	private boolean isConquered(MapRenderState.MapDecorationRenderState decoration) {
		// TFMagicMapData is not directly accessible from the render state in 26.1.2.
		// The conquered structures check needs to be stored and accessed via MapRenderState custom data.
		// For now, we cannot determine conquered state from the render state alone.
		// TODO: Re-implement conquered structure check using NeoForge render state extensions
		return false;
	}

	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decorationState, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int light, int index) {
		if (this.isConquered(decorationState)) {
			stack.pushPose();
			stack.translate(0.0F + decorationState.x / 2.0F + 64.0F, 0.0F + decorationState.y / 2.0F + 64.0F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees((decorationState.rot * 360) / 16.0F));
			stack.scale(2.0F, 2.0F, 2.0F);
			stack.translate(-1.0F, -1.0F, -0.005F);
			Matrix4f matrix4f = stack.last().pose();
			float depth = -0.095F;
			TextureAtlasSprite xSprite = decorationSprites.getSprite(MapDecorationTypes.RED_X.value().assetId());
			float f2 = xSprite.getU0();
			float f3 = xSprite.getV0();
			float f4 = xSprite.getU1();
			float f5 = xSprite.getV1();
			submitNodeCollector.submitCustomGeometry(stack, net.minecraft.client.renderer.rendertype.RenderTypes.text(xSprite.atlasLocation()), (pose, buffer) -> {
				buffer.addVertex(matrix4f, -1.0F, 1.0F, depth).setColor(-1).setUv(f2, f3).setLight(light);
				buffer.addVertex(matrix4f, 1.0F, 1.0F, depth).setColor(-1).setUv(f4, f3).setLight(light);
				buffer.addVertex(matrix4f, 1.0F, -1.0F, depth).setColor(-1).setUv(f4, f5).setLight(light);
				buffer.addVertex(matrix4f, -1.0F, -1.0F, depth).setColor(-1).setUv(f2, f5).setLight(light);
			});
			stack.popPose();
		}

		return false;
	}
}
