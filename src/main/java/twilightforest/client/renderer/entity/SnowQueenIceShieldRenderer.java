package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.asmhooks.MultipartHooks;
import twilightforest.entity.boss.SnowQueenIceShield;

public class SnowQueenIceShieldRenderer extends EntityRenderer<SnowQueenIceShield, FallingBlockRenderState> {
	public SnowQueenIceShieldRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void submit(FallingBlockRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		BlockState blockstate = state.movingBlockRenderState.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			stack.pushPose();
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
	public void extractRenderState(SnowQueenIceShield entity, FallingBlockRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		MultipartHooks.registerStateRenderer(state, SnowQueenIceShield.RENDERER);
		BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.movingBlockRenderState.randomSeedPos = pos;
		state.movingBlockRenderState.blockPos = pos;
		state.movingBlockRenderState.blockState = Blocks.PACKED_ICE.defaultBlockState();
		if (entity.level() instanceof ClientLevel clientLevel) {
			state.movingBlockRenderState.biome = clientLevel.getBiome(pos);
			state.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting();
			state.movingBlockRenderState.lightEngine = clientLevel.getLightEngine();
		}
	}
}
