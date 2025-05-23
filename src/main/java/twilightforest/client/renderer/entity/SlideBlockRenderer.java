package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.entity.SlideBlock;

public class SlideBlockRenderer extends EntityRenderer<SlideBlock, FallingBlockRenderState> {

	public SlideBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0F;
	}

	@Override
	public boolean shouldRender(SlideBlock entity, Frustum frustum, double x, double y, double z) {
		return super.shouldRender(entity, frustum, x, y, z) && entity.getBlockState() != entity.level().getBlockState(entity.blockPosition());
	}

	// [VanillaCopy] FallingBlockRenderer, with spin
	@Override
	public void render(FallingBlockRenderState state, PoseStack stack, MultiBufferSource buffer, int light) {
		BlockState blockstate = state.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			stack.pushPose();
			// spin
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
				stack.translate(-0.5D, -0.5D, -0.5D);
			}
			BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
			var model = dispatcher.getBlockModel(blockstate);
			for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(state.startBlockPos)), ModelData.EMPTY))
				dispatcher.getModelRenderer().tesselateBlock(state, model, blockstate, state.blockPos, stack, buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)), false, RandomSource.create(), blockstate.getSeed(state.startBlockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
			stack.popPose();
			super.render(state, stack, buffer, light);
		}
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(SlideBlock entity, FallingBlockRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		state.startBlockPos = blockpos;
		state.blockPos = blockpos;
		state.blockState = entity.getBlockState();
		state.biome = entity.level().getBiome(blockpos);
		state.level = entity.level();
	}
}
