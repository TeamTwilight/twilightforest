package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.List;

public record FullBrightSpecialRenderer(RenderType type, BakedModel model, int[] tintArray, List<Integer> fullbrightFaces) implements NoDataSpecialModelRenderer {

	@Override
	public void render(ItemDisplayContext context, PoseStack stack, MultiBufferSource source, int light, int overlay, boolean foil) {
		this.renderItem(
			stack,
			source,
			light,
			overlay,
			this.tintArray,
			this.model,
			this.type,
			foil ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE
		);
	}

	public void renderItem(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int[] tintLayers, BakedModel model, RenderType renderType, ItemStackRenderState.FoilType foilType) {
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, renderType, true, foilType != ItemStackRenderState.FoilType.NONE);
		this.renderModelLists(model, tintLayers, packedLight, packedOverlay, poseStack, vertexconsumer);
	}

	private void renderModelLists(BakedModel model, int[] tintLayers, int packedLight, int packedOverlay, PoseStack poseStack, VertexConsumer buffer) {
		RandomSource randomsource = RandomSource.create();

		for (Direction direction : Direction.values()) {
			randomsource.setSeed(42L);
			this.renderQuadList(poseStack, buffer, model.getQuads(null, direction, randomsource, ModelData.EMPTY, null), tintLayers, packedLight, packedOverlay);
		}

		randomsource.setSeed(42L);
		this.renderQuadList(poseStack, buffer, model.getQuads(null, null, randomsource, ModelData.EMPTY, null), tintLayers, packedLight, packedOverlay);
	}

	public void renderQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, int[] tintLayers, int packedLight, int packedOverlay) {
		PoseStack.Pose pose = poseStack.last();

		for (BakedQuad bakedquad : quads) {
			float a, r, g, b;
			if (bakedquad.isTinted()) {
				int color = getLayerColorSafe(tintLayers, bakedquad.getTintIndex());
				a = (float)ARGB.alpha(color) / 255.0F;
				r = (float)ARGB.red(color) / 255.0F;
				g = (float)ARGB.green(color) / 255.0F;
				b = (float)ARGB.blue(color) / 255.0F;
			} else {
				a = 1.0F;
				r = 1.0F;
				g = 1.0F;
				b = 1.0F;
			}

			buffer.putBulkData(pose, bakedquad, r, g, b, a, this.fullbrightFaces.contains(bakedquad.getTintIndex()) ? 15728850 : packedLight, packedOverlay, true);
		}
	}

	private static int getLayerColorSafe(int[] tintLayers, int index) {
		return index >= tintLayers.length ? -1 : tintLayers[index];
	}
}
