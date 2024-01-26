package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.block.entity.CandelabraBlockEntity;

public class CandelabraTileEntityRenderer<T extends CandelabraBlockEntity> implements BlockEntityRenderer<T> {

	public CandelabraTileEntityRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState state = entity.getBlockState();
		Direction direction = state.getValue(CandelabraBlock.FACING);

		for (int i = 0; i < entity.getCandles().length; i++) {
			stack.pushPose();
			float offset = (0.315F - 0.315F * i);
			if (state.getValue(CandelabraBlock.ON_WALL)) {
				stack.translate(-Math.abs(direction.getStepZ()) * offset + (direction.getStepX() * 0.25D), 0.44F, -Math.abs(direction.getStepX()) * offset + (direction.getStepZ() * 0.25D));
			} else {
				stack.translate(-Math.abs(direction.getStepZ()) * offset, 0.44F, -Math.abs(direction.getStepX()) * offset);
			}
			BlockState candle = entity.getCandle(i).defaultBlockState();
			if (candle.hasProperty(CandleBlock.LIT))
				candle = candle.setValue(CandleBlock.LIT, state.getValue(CandelabraBlock.LIGHTING) == LightableBlock.Lighting.NORMAL);
			if (!candle.isAir()) {
				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(candle, stack, buffer, light, overlay);
			}
			stack.popPose();
		}
	}
}
