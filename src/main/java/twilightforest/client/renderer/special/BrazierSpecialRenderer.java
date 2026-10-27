package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.joml.Vector3fc;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.renderer.block.BrazierRenderer;
import twilightforest.client.state.block.BrazierRenderState;

import java.util.function.Consumer;

public record BrazierSpecialRenderer(BrazierModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		BrazierRenderState state = new BrazierRenderState();
		state.lightCoords = light;
		BrazierRenderer.submitBrazier(model(), state, stack, collector, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack stack = new PoseStack();
		BrazierRenderer.applyBrazierPose(stack);
		this.model.root().getExtentsForGui(stack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<BrazierSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(BrazierSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<BrazierSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			return new BrazierSpecialRenderer(new BrazierModel(context.entityModelSet().bakeLayer(TFModelLayers.BRAZIER)));
		}
	}
}
