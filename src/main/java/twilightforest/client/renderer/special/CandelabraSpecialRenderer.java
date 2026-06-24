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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;
import twilightforest.client.renderer.block.CandelabraRenderer;
import twilightforest.components.item.CandelabraData;
import twilightforest.init.TFDataComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public record CandelabraSpecialRenderer() implements SpecialModelRenderer<@NotNull List<BlockModelRenderState>> {
	@Override
	public List<BlockModelRenderState> extractArgument(ItemStack stack) {
		List<BlockModelRenderState> models = new ArrayList<>();
		for (int i = 0; i < stack.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY).ordered().size(); i++) {
			BlockModelRenderState state = new BlockModelRenderState();
			BlockState candle = CandelabraData.getItem(stack.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY).ordered(), i).orElse(Blocks.AIR).defaultBlockState();
			Minecraft.getInstance().getBlockModelResolver().update(state, candle, BlockDisplayContext.create());
			models.add(state);
		}

		return models;
	}

	@Override
	public void submit(List<BlockModelRenderState> data, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
		CandelabraRenderer.submitCandles(Direction.NORTH, false, data, stack, collector, light, overlay);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {

	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked<@NotNull List<BlockModelRenderState>> {
		public static final MapCodec<CandelabraSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(CandelabraSpecialRenderer.Unbaked::new);

		public MapCodec<CandelabraSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<@NotNull List<BlockModelRenderState>> bake(BakingContext context) {
			return new CandelabraSpecialRenderer();
		}
	}
}
