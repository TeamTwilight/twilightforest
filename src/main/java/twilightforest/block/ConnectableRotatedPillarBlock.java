package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ConnectableRotatedPillarBlock extends RotatedPillarBlock {
	protected static final BooleanProperty NORTH = PipeBlock.NORTH;
	protected static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	protected static final BooleanProperty WEST = PipeBlock.WEST;
	protected static final BooleanProperty EAST = PipeBlock.EAST;
	protected static final BooleanProperty UP = PipeBlock.UP;
	protected static final BooleanProperty DOWN = PipeBlock.DOWN;

	final double boundingBoxWidthLower;
	final double boundingBoxWidthUpper;

	public ConnectableRotatedPillarBlock(Properties properties, double width) {
		super(properties);

		if (width >= 16.0D) {
			this.boundingBoxWidthLower = 0.0D;
			this.boundingBoxWidthUpper = 16.0D;
		} else {
			this.boundingBoxWidthLower = 8.0D - (width / 2.0D);
			this.boundingBoxWidthUpper = 16.0D - this.boundingBoxWidthLower;
		}

		this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.Y)
			.setValue(NORTH, false).setValue(WEST, false)
			.setValue(SOUTH, false).setValue(EAST, false)
			.setValue(DOWN, false).setValue(UP, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(NORTH, EAST, SOUTH, WEST, DOWN, UP));
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), this.canConnectTo(state.getValue(AXIS), direction, facingState, facingState.isFaceSturdy(reader, facingPos, direction.getOpposite())));
	}

	public boolean canConnectTo(Direction.Axis thisAxis, Direction facing, BlockState facingState, boolean solidSide) {
		return !isExceptionForConnection(facingState) && solidSide;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter iblockreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.south();
		BlockPos blockpos3 = blockpos.west();
		BlockPos blockpos4 = blockpos.east();
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);

		Direction.Axis axis = context.getClickedFace().getAxis();
		return this.defaultBlockState().setValue(AXIS, axis)
			.setValue(NORTH, this.canConnectTo(axis, Direction.NORTH, blockstate, blockstate.isFaceSturdy(iblockreader, blockpos1, Direction.SOUTH)))
			.setValue(SOUTH, this.canConnectTo(axis, Direction.SOUTH, blockstate1, blockstate1.isFaceSturdy(iblockreader, blockpos2, Direction.NORTH)))
			.setValue(WEST, this.canConnectTo(axis, Direction.WEST, blockstate2, blockstate2.isFaceSturdy(iblockreader, blockpos3, Direction.EAST)))
			.setValue(EAST, this.canConnectTo(axis, Direction.EAST, blockstate3, blockstate3.isFaceSturdy(iblockreader, blockpos4, Direction.WEST)));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AXIS)) {
			case X -> box(
				0.0D,
				state.getValue(NORTH) ? 0.0D : this.boundingBoxWidthLower,
				state.getValue(WEST) ? 0.0D : this.boundingBoxWidthLower,
				16.0D,
				state.getValue(SOUTH) ? 16.0D : this.boundingBoxWidthUpper,
				state.getValue(EAST) ? 16.0D : this.boundingBoxWidthUpper
			);
			case Z -> box(
				state.getValue(EAST) ? 0.0D : this.boundingBoxWidthLower,
				state.getValue(SOUTH) ? 0.0D : this.boundingBoxWidthLower,
				0.0D,
				state.getValue(WEST) ? 16.0D : this.boundingBoxWidthUpper,
				state.getValue(NORTH) ? 16.0D : this.boundingBoxWidthUpper,
				16.0D
			);
			default -> box(
				state.getValue(WEST) ? 0.0D : this.boundingBoxWidthLower,
				0.0D,
				state.getValue(NORTH) ? 0.0D : this.boundingBoxWidthLower,
				state.getValue(EAST) ? 16.0D : this.boundingBoxWidthUpper,
				16.0D,
				state.getValue(SOUTH) ? 16.0D : this.boundingBoxWidthUpper
			);
		};
	}
}
