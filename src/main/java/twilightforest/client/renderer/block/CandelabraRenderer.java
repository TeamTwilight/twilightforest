package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.entity.CandelabraBlockEntity;
import twilightforest.components.item.CandelabraData;

public class CandelabraRenderer<T extends CandelabraBlockEntity> implements BlockEntityRenderer<T> {

	public CandelabraRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		renderCandles(entity.getBlockState(), entity.getCandles(), stack, buffer, light, overlay);
	}

	public static void renderCandles(BlockState state, CandelabraData data, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		Direction direction = state.getValue(CandelabraBlock.FACING);

		for (int i = 0; i < data.ordered().size(); i++) {
			stack.pushPose();
			float offset = (0.315F - 0.315F * i);
			if (state.getValue(CandelabraBlock.ON_WALL)) {
				stack.translate(-Math.abs(direction.getStepZ()) * offset + (direction.getStepX() * 0.25D), 0.44F, -Math.abs(direction.getStepX()) * offset + (direction.getStepZ() * 0.25D));
			} else {
				stack.translate(-Math.abs(direction.getStepZ()) * offset, 0.44F, -Math.abs(direction.getStepX()) * offset);
			}
			BlockState candle = CandelabraData.getItem(data.ordered(), i).orElse(Blocks.AIR).defaultBlockState();
			if (candle.hasProperty(CandleBlock.LIT))
				candle = candle.setValue(CandleBlock.LIT, state.getValue(CandelabraBlock.LIGHTING) == LightableBlock.Lighting.NORMAL);
			if (!candle.isAir()) {
				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(candle, stack, buffer, light, overlay);
			}
			stack.popPose();
		}
	}
}
