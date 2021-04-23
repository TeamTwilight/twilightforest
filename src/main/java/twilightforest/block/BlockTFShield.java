package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import twilightforest.util.EntityUtil;

import javax.annotation.Nullable;

public class BlockTFShield extends DirectionalBlock {

	public BlockTFShield(Block.Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.DOWN));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	@Deprecated
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
		BlockRayTraceResult ray = EntityUtil.rayTrace(player, range -> range + 1.0);

		Direction hitFace = ray.getFace();
		boolean upOrDown = state.get(DirectionalBlock.FACING) == Direction.UP || state.get(DirectionalBlock.FACING) == Direction.DOWN;
		Direction sideFace = state.get(DirectionalBlock.FACING).getOpposite();
		Direction upFace = state.get(DirectionalBlock.FACING);

		if (hitFace == (upOrDown ? upFace : sideFace)) {
			return player.getDigSpeed(Blocks.STONE.getDefaultState(), pos) / 1.5F / 100F;
		} else {
			return super.getPlayerRelativeBlockHardness(state, player, world, pos);
		}
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return false;
	}
}
