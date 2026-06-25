package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.renderer.block.BrazierRenderer;

import java.util.function.Consumer;

public record BrazierSpecialRenderer(BrazierModel model) implements NoDataSpecialModelRenderer {

	@Override
	public void submit(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		stack.pushPose();
		stack.translate(0.5F, 1.5F, 0.5F);
		stack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));
		Identifier loc = BrazierRenderer.TEXTURE_OFF;
		collector.submitModel(this.model, Unit.INSTANCE, stack, this.model.renderType(loc), LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0, null);
		stack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		this.model.root().getExtentsForGui(poseStack, output);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<BrazierSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(BrazierSpecialRenderer.Unbaked::new);

		@Override
		public MapCodec<BrazierSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
			return new BrazierSpecialRenderer(new BrazierModel(context.entityModelSet().bakeLayer(TFModelLayers.BRAZIER)));
		}
	}
}
