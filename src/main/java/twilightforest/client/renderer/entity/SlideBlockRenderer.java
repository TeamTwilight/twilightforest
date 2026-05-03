package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.SlideBlock;

public class SlideBlockRenderer extends EntityRenderer<SlideBlock, FallingBlockRenderState> {

	public SlideBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0F;
	}

	// [VanillaCopy] FallingBlockRenderer.submit, with spin
	@Override
	public void submit(FallingBlockRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		BlockState blockstate = state.movingBlockRenderState.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			stack.pushPose();
			//spin
			if (blockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
				Direction.Axis axis = blockstate.getValue(RotatedPillarBlock.AXIS);
				float angle = state.ageInTicks * 60F;
				stack.translate(0.0D, 0.5D, 0.0D);
				if (axis == Direction.Axis.Y) {
					stack.mulPose(Axis.YP.rotationDegrees(angle));
				} else if (axis == Direction.Axis.X) {
					stack.mulPose(Axis.XP.rotationDegrees(angle));
				} else if (axis == Direction.Axis.Z) {
					stack.mulPose(Axis.ZP.rotationDegrees(angle));
				}
			}
			stack.translate(-0.5D, -0.5D, -0.5D);
			collector.submitMovingBlock(stack, state.movingBlockRenderState);
			stack.popPose();
			super.submit(state, stack, collector, camera);
		}
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(SlideBlock entity, FallingBlockRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.movingBlockRenderState.randomSeedPos = pos;
		state.movingBlockRenderState.blockPos = pos;
		state.movingBlockRenderState.blockState = entity.getBlockState();
		if (entity.level() instanceof ClientLevel clientLevel) {
			state.movingBlockRenderState.biome = clientLevel.getBiome(pos);
			state.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting();
			state.movingBlockRenderState.lightEngine = clientLevel.getLightEngine();
		}
	}
}
