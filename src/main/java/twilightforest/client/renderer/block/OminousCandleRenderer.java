package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.block.OminousCandleBlock;
import twilightforest.block.entity.OminousCandleBlockEntity;
import twilightforest.client.state.block.OminousCandleRenderState;

import java.util.ArrayList;
import java.util.List;

public class OminousCandleRenderer implements BlockEntityRenderer<OminousCandleBlockEntity, OminousCandleRenderState> {

	protected final BlockModelResolver blockRenderer;

	public OminousCandleRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRenderer = context.blockModelResolver();
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public void submit(OminousCandleRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		stack.pushPose();
		stack.translate(0.5D, 0.0D, 0.5D);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));

		for (int i = 0; i < state.candles.size(); i++) {
			stack.pushPose();
			Vec3 offset = state.offsets.get(i);
			stack.translate(offset.x, offset.y, offset.z);
			state.candles.get(i).submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			stack.popPose();
		}
		stack.popPose();
	}

	@Override
	public OminousCandleRenderState createRenderState() {
		return new OminousCandleRenderState();
	}

	@Override
	public void extractRenderState(OminousCandleBlockEntity blockEntity, OminousCandleRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		if (blockEntity.getLevel() != null) {
			state.time = blockEntity.getLevel().getGameTime();
		}

		int candles = blockEntity.getBlockState().getValue(OminousCandleBlock.CANDLES);

		List<Vec2> vec2s = OminousCandleBlock.CANDLE_OFFSETS.get(candles);

		List<Vec3> offsets = new ArrayList<>();
		List<BlockModelRenderState> candleModels = new ArrayList<>();

		for (int i = 0; i < candles; i++) {
			double targetHeight = OminousCandleBlock.getCurrentY(state.time, partialTicks, blockEntity.getBlockPos(), i + 1);
			double yHeight = this.exponentialDecay(blockEntity.getVisualHeight(i), targetHeight, 0.05, partialTicks);
			offsets.add(new Vec3(-vec2s.get(i).x, yHeight, -vec2s.get(i).y));
			blockEntity.setVisualHeightScalar(yHeight, i);
			BlockModelRenderState modelState = new BlockModelRenderState();
			BlockState candle = (blockEntity.getBlockState().getBlock() instanceof OminousCandleBlock candleBlock ? candleBlock.candle : Blocks.CANDLE).defaultBlockState().setValue(CandleBlock.LIT, true);
			this.blockRenderer.update(modelState, candle, BlockDisplayContext.create());
			candleModels.add(modelState);
		}

		state.offsets = offsets;
		state.candles = candleModels;
	}

	/**
	 * Yoinked from <a href="https://www.youtube.com/watch?v=LSNQuFEDOyQ&t=2982s">"Lerp smoothing is broken" by Freya</a>
	 * @param prevValue Value from previous frame
	 * @param targetValue Value to reach
	 * @param decayFactor Higher value = faster progression towards targetValue
	 * @return A value towards targetValue in respect to decayFactor
	 */
	private double exponentialDecay(double prevValue, double targetValue, double decayFactor, float partialTick) {
		return targetValue + (prevValue - targetValue) * Math.exp(-decayFactor * partialTick);
	}
}
