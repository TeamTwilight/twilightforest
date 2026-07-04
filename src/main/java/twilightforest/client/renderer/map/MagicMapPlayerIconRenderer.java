package twilightforest.client.renderer.map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class MagicMapPlayerIconRenderer implements IMapDecorationRenderer {

	//[VanillaCopy] of MapRenderer.RenderInstance.draw, but with a set depth offset instead of relying on index.
	//this allows the icon to render on top of everything else instead of sometimes on top, sometimes behind
	@Override
	public boolean render(MapRenderState.@NotNull MapDecorationRenderState decorationRenderState, @NotNull PoseStack stack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull MapRenderState mapRenderState, @NotNull TextureAtlas decorationSprites, boolean inItemFrame, int packedLight, int index) {
		TextureAtlasSprite textureatlassprite = decorationRenderState.atlasSprite;
		if (textureatlassprite == null) {
			return false;
		}

		stack.pushPose();
		stack.translate(0.0F + (float)decorationRenderState.x / 2.0F + 64.0F, 0.0F + (float)decorationRenderState.y / 2.0F + 64.0F, -0.02F);
		stack.mulPose(Axis.ZP.rotationDegrees((float)(decorationRenderState.rot * 360) / 16.0F));
		stack.scale(4.0F, 4.0F, 3.0F);
		stack.translate(-0.125F, 0.125F, 0.0F);

		final Matrix4f finalMatrix = new Matrix4f(stack.last().pose());

		float f2 = textureatlassprite.getU0();
		float f3 = textureatlassprite.getV0();
		float f4 = textureatlassprite.getU1();
		float f5 = textureatlassprite.getV1();

		RenderType renderType = RenderTypes.text(decorationSprites.location());

		submitNodeCollector.submitCustomGeometry(stack, renderType, (_, consumer) -> {
			consumer.addVertex(finalMatrix, -1.0F, 1.0F, -0.3F).setColor(0xFFFFFFFF).setUv(f2, f3).setLight(packedLight);
			consumer.addVertex(finalMatrix, 1.0F, 1.0F, -0.3F).setColor(0xFFFFFFFF).setUv(f4, f3).setLight(packedLight);
			consumer.addVertex(finalMatrix, 1.0F, -1.0F, -0.3F).setColor(0xFFFFFFFF).setUv(f4, f5).setLight(packedLight);
			consumer.addVertex(finalMatrix, -1.0F, -1.0F, -0.3F).setColor(0xFFFFFFFF).setUv(f2, f5).setLight(packedLight);
		});

		stack.popPose();
		return true;
	}
}
