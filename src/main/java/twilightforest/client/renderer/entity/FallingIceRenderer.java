package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.projectile.FallingIce;

/**
 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.FallingBlockRenderer}
 */
public class FallingIceRenderer extends EntityRenderer<FallingIce, FallingBlockRenderState> {

	public FallingIceRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	@Override
	public boolean shouldRender(FallingIce entity, Frustum culler, double camX, double camY, double camZ) {
		return super.shouldRender(entity, culler, camX, camY, camZ) && entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
	}

	@Override
	public void submit(FallingBlockRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		BlockState blockState = state.movingBlockRenderState.blockState;
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			stack.pushPose();
			stack.translate(-0.5D, 0.0D, -0.5D);
			collector.submitMovingBlock(stack, state.movingBlockRenderState, state.outlineColor);
			stack.popPose();
			super.submit(state, stack, collector, camera);
		}
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(FallingIce entity, FallingBlockRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.movingBlockRenderState.randomSeedPos = entity.getStartPos();
		state.movingBlockRenderState.blockPos = pos;
		state.movingBlockRenderState.blockState = entity.getBlockState();
		if (entity.level() instanceof ClientLevel clientLevel) {
			state.movingBlockRenderState.biome = clientLevel.getBiome(pos);
			state.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting();
			state.movingBlockRenderState.lightEngine = clientLevel.getLightEngine();
		}
	}
}
