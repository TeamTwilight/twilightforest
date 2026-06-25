package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.client.state.block.CandelabraRenderState;
import twilightforest.components.item.CandelabraData;

import java.util.List;

public class CandelabraRenderer implements BlockEntityRenderer<CandelabraBlockEntity, CandelabraRenderState> {

	private final BlockModelResolver blockModelResolver;

	public CandelabraRenderer(BlockEntityRendererProvider.Context context) {
		this.blockModelResolver = context.blockModelResolver();
	}

	@Override
	public void submit(CandelabraRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		submitCandles(state.facing, state.onWall, state.candleStates, stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	public static void submitCandles(Direction facing, boolean onWall, List<BlockModelRenderState> candleStates, PoseStack stack, SubmitNodeCollector collector, int light, int overlay) {
		for (int i = 0; i < candleStates.size(); i++) {
			BlockModelRenderState candleState = candleStates.get(i);

			stack.pushPose();
			float offset = (0.315F - 0.315F * i);
			if (onWall) {
				stack.translate(-Math.abs(facing.getStepZ()) * offset + (facing.getStepX() * 0.25D), 0.44F, -Math.abs(facing.getStepX()) * offset + (facing.getStepZ() * 0.25D));
			} else {
				stack.translate(-Math.abs(facing.getStepZ()) * offset, 0.44F, -Math.abs(facing.getStepX()) * offset);
			}

			candleState.submitMultiLayer(stack, collector, light, overlay, 0);

			stack.popPose();
		}
	}

	@Override
	public CandelabraRenderState createRenderState() {
		return new CandelabraRenderState();
	}

	@Override
	public void extractRenderState(CandelabraBlockEntity entity, CandelabraRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(entity, state, partialTick, cameraPosition, breakProgress);
		BlockState bs = entity.getBlockState();
		if (bs.getBlock() instanceof CandelabraBlock) {
			state.facing = bs.getValue(CandelabraBlock.FACING);
			state.onWall = bs.getValue(CandelabraBlock.ON_WALL);
		} else {
			state.facing = Direction.NORTH;
			state.onWall = false;
		}

		CandelabraData data = entity.getCandles();
		List<Block> candles = data.ordered();

		if (state.candleStates.size() != candles.size()) {
			state.candleStates = new java.util.ArrayList<>();
			for (int i = 0; i < candles.size(); i++) {
				state.candleStates.add(new BlockModelRenderState());
			}
		}

		for (int i = 0; i < candles.size(); i++) {
			Block candleBlock = candles.get(i);
			BlockModelRenderState modelState = state.candleStates.get(i);
			modelState.clear();
			if (!candleBlock.defaultBlockState().isAir()) {
				BlockState candle = candleBlock.defaultBlockState().setValue(CandleBlock.CANDLES, 1);
				if (bs.getBlock() instanceof LightableBlock lightable) {
					candle = candle.setValue(CandleBlock.LIT, bs.getValue(LightableBlock.LIGHTING) != LightableBlock.Lighting.NONE);
				}
				this.blockModelResolver.update(modelState, candle, BlockDisplayContext.create());
			}
		}
	}
}