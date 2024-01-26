package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class MangroveSaplingBlock extends SaplingBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public MangroveSaplingBlock(TreeGrower tree, BlockBehaviour.Properties properties) {
		super(tree, properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!isInWater(state, level, pos)) {
			level.setBlock(pos, this.defaultBlockState().setValue(WATERLOGGED, false), 2);
		}
	}

	//VanillaCopy of AbstractCoralPlantBlock.isInWater
	protected static boolean isInWater(BlockState state, BlockGetter getter, BlockPos pos) {
		if (state.getValue(WATERLOGGED)) {
			return true;
		} else {
			for (Direction direction : Direction.values()) {
				if (getter.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
					return true;
				}
			}

			return false;
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return facing == Direction.DOWN && !this.canSurvive(stateIn, accessor, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, STAGE);
	}
}
