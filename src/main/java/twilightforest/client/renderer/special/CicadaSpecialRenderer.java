package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.renderer.block.CicadaRenderer;

import java.util.function.Consumer;

public record CicadaSpecialRenderer(CicadaModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		// TODO: 26.1.2 - Parameter order may have changed in submitCicada
		CicadaRenderer.submitCicada(this.model(), BugModelAnimationHelper.currentYaw, 0.0F, Direction.NORTH, stack, collector, light, overlay, 0, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<CicadaSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(CicadaSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<CicadaSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			return new CicadaSpecialRenderer(new CicadaModel(context.entityModelSet().bakeLayer(TFModelLayers.CICADA)));
		}
	}
}
