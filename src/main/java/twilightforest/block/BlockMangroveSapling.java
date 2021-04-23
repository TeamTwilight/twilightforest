package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.trees.Tree;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMangroveSapling extends BlockTFSapling implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected BlockMangroveSapling(Tree tree) {
		super(tree);
		this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!isInWater(state, worldIn, pos)) {
			worldIn.setBlockState(pos, this.getDefaultState().with(WATERLOGGED, false), 2);
		}
	}

	//VanillaCopy of AbstractCoralPlantBlock.isInWater
	protected static boolean isInWater(BlockState state, IBlockReader worldIn, BlockPos pos) {
		if (state.get(WATERLOGGED)) {
			return true;
		} else {
			for(Direction direction : Direction.values()) {
				if (worldIn.getFluidState(pos.offset(direction)).isTagged(FluidTags.WATER)) {
					return true;
				}
			}

			return false;
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(WATERLOGGED, fluidstate.isTagged(FluidTags.WATER) && fluidstate.getLevel() == 8);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, STAGE);
	}
}
