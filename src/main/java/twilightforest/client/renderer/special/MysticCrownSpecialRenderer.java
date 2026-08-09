package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.joml.Vector3fc;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.renderer.entity.LichRenderer;

import java.util.function.Consumer;

public record MysticCrownSpecialRenderer(LichModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		stack.pushPose();
		stack.translate(0.5F, 0.0F, 0.5F);
		stack.scale(1.0F, -1.0F, -1.0F);
		collector.submitModelPart(this.model.hat, stack, this.model.renderType(LichRenderer.TEXTURE), light, overlay, null);
		stack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.hat.getExtentsForGui(poseStack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<MysticCrownSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(MysticCrownSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<MysticCrownSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(BakingContext context) {
			return new MysticCrownSpecialRenderer(new LichModel(context.entityModelSet().bakeLayer(TFModelLayers.LICH_TROPHY)));
		}
	}
}
