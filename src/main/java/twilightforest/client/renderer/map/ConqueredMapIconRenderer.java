package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ConqueredMapIconRenderer implements IMapDecorationRenderer {
	@Override
	public boolean render(MapRenderState.MapDecorationRenderState decorationRenderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull MapRenderState mapRenderState, @NotNull TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		if (decorationRenderState.type == MapDecorationTypes.RED_X) {
			poseStack.pushPose();

			poseStack.translate(0.0F + decorationRenderState.x / 2.0F + 64.0F, 0.0F + decorationRenderState.y / 2.0F + 64.0F, 0.0F);
			poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees((decorationRenderState.rot * 360) / 16.0F));
			poseStack.scale(2.0F, 2.0F, 2.0F);
			poseStack.translate(-1.0F, -1.0F, -0.005F);

			TextureAtlasSprite xSprite = decorationSprites.getSprite(MapDecorationTypes.RED_X.value().assetId());

			if (xSprite != null) {
				float depth = -0.095F;
				float f2 = xSprite.getU0();
				float f3 = xSprite.getV0();
				float f4 = xSprite.getU1();
				float f5 = xSprite.getV1();

				Matrix4f matrix4f = new Matrix4f(poseStack.last().pose());

				RenderType mapRenderType = RenderTypes.text(xSprite.atlasLocation());

				submitNodeCollector.submitCustomGeometry(poseStack, mapRenderType, (PoseStack.Pose _, VertexConsumer consumer) -> {
					consumer.addVertex(matrix4f, -1.0F, 1.0F, depth).setColor(-1).setUv(f2, f3).setLight(packedLight);
					consumer.addVertex(matrix4f, 1.0F, 1.0F, depth).setColor(-1).setUv(f4, f3).setLight(packedLight);
					consumer.addVertex(matrix4f, 1.0F, -1.0F, depth).setColor(-1).setUv(f4, f5).setLight(packedLight);
					consumer.addVertex(matrix4f, -1.0F, -1.0F, depth).setColor(-1).setUv(f2, f5).setLight(packedLight);
				});
			}

			poseStack.popPose();
			return true;
		}

		return false;
	}
}
