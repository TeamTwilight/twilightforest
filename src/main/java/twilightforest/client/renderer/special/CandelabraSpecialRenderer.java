package twilightforest.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3fc;
import twilightforest.client.renderer.block.CandelabraRenderer;
import twilightforest.components.item.CandelabraData;
import twilightforest.init.TFDataComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record CandelabraSpecialRenderer() implements SpecialModelRenderer<List<BlockState>> {

	@Override
	public List<BlockState> extractArgument(ItemStack stack) {
		List<BlockState> candles = new ArrayList<>();
		for (int i = 0; i < stack.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY).ordered().size(); i++) {
			BlockState candle = CandelabraData.getItem(stack.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY).ordered(), i).orElse(Blocks.AIR).defaultBlockState();
			if (candle.hasProperty(CandleBlock.LIT)) {
				candle = candle.setValue(CandleBlock.LIT, false);
			}
			candles.add(candle);
		}
		return candles;
	}

	@Override
	public void submit(List<BlockState> data, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		List<BlockModelRenderState> candleStates = new ArrayList<>();
		var modelSet = Minecraft.getInstance().getModelManager().getBlockModelSet();
		for (BlockState candle : data) {
			BlockModelRenderState modelState = new BlockModelRenderState();
			if (!candle.isAir()) {
				var model = modelSet.get(candle);
				model.update(modelState, candle, BlockDisplayContext.create(), 42L);
			}
			candleStates.add(modelState);
		}
		CandelabraRenderer.submitCandles(Direction.NORTH, false, candleStates, stack, collector, light, overlay);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {

	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked<List<BlockState>> {
		public static final MapCodec<CandelabraSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(CandelabraSpecialRenderer.Unbaked::new);

		public MapCodec<CandelabraSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<List<BlockState>> bake(BakingContext context) {
			return new CandelabraSpecialRenderer();
		}
	}
}
