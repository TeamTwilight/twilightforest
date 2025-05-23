package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.entity.projectile.FallingIce;

/**
 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.FallingBlockRenderer}
 */
public class FallingIceRenderer extends EntityRenderer<FallingIce, FallingBlockRenderState> {

	private final BlockRenderDispatcher dispatcher;

	public FallingIceRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public boolean shouldRender(FallingIce entity, Frustum frustum, double x, double y, double z) {
		return super.shouldRender(entity, frustum, x, y, z) && entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
	}

	@Override
	public void render(FallingBlockRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		BlockState blockstate = state.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			stack.pushPose();
			stack.translate(-0.5D, 0.0, -0.5D);
			var model = this.dispatcher.getBlockModel(blockstate);
			for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(state.startBlockPos)), ModelData.EMPTY))
				this.dispatcher.getModelRenderer().tesselateBlock(
					state,
					this.dispatcher.getBlockModel(blockstate),
					blockstate,
					state.blockPos,
					stack,
					buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)),
					false,
					RandomSource.create(),
					blockstate.getSeed(state.startBlockPos),
					OverlayTexture.NO_OVERLAY,
					net.neoforged.neoforge.client.model.data.ModelData.EMPTY,
					renderType
				);
			stack.popPose();
			super.render(state, stack, buffer, light);
		}
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(FallingIce entity, FallingBlockRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.startBlockPos = entity.getStartPos();
		state.blockPos = blockpos;
		state.blockState = entity.getBlockState();
		state.biome = entity.level().getBiome(blockpos);
		state.level = entity.level();
	}
}
