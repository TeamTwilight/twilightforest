package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty X = BooleanProperty.create("x");
	public static final BooleanProperty Y = BooleanProperty.create("y");
	public static final BooleanProperty Z = BooleanProperty.create("z");
	protected static final VoxelShape X_SHAPE = Block.box(0.0, 6.5, 6.5, 16.0, 9.5, 9.5);
	protected static final VoxelShape Y_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
	protected static final VoxelShape Z_SHAPE = Block.box(6.5, 6.5, 0.0, 9.5, 9.5, 16.0);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public RopeBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(WATERLOGGED, false)
			.setValue(Y, true)
			.setValue(X, false)
			.setValue(Z, false));
	}

	public static boolean canConnectTo(BlockState state, Direction dir, LevelReader level, BlockPos pos) {
		if (dir == Direction.DOWN) return false;
		return state.getBlock() instanceof LeavesBlock || (state.getBlock() instanceof RopeBlock && hasAxis(state, dir.getAxis())) || state.isFaceSturdy(level, pos, dir.getOpposite(), SupportType.CENTER);
	}

	public static boolean hasAxis(BlockState state, Direction.Axis axis) {
		return switch (axis) {
			case X -> state.getValue(X);
			case Y -> state.getValue(Y);
			case Z -> state.getValue(Z);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, Y, X, Z);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();

		if (state.getValue(X)) shape = Shapes.or(shape, X_SHAPE);
		if (state.getValue(Y)) shape = Shapes.or(shape, Y_SHAPE);
		if (state.getValue(Z)) shape = Shapes.or(shape, Z_SHAPE);

		return shape;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		if (state.getValue(X) && context.isAbove(X_SHAPE, pos, false) && !context.isDescending()) shape = Shapes.or(shape, X_SHAPE);
		if (state.getValue(Z) && context.isAbove(Z_SHAPE, pos, false) && !context.isDescending()) shape = Shapes.or(shape, Z_SHAPE);
		return shape;
	}

	@Override
	public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return true;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return context.getItemInHand().is(this.asItem());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		Direction direction = context.getClickedFace();
		return this.defaultBlockState()
			.setValue(WATERLOGGED, level.getFluidState(blockpos).getType() == Fluids.WATER)
			.setValue(X, direction.getAxis() == Direction.Axis.X)
			.setValue(Y, direction.getAxis() == Direction.Axis.Y)
			.setValue(Z, direction.getAxis() == Direction.Axis.Z);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState otherState, boolean isMoving) {
		if (!level.isClientSide) level.scheduleTick(pos, this, 1);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) access.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(reader));
		if (!reader.isClientSide()) access.scheduleTick(pos, this, 1);
		return state;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		boolean flag = true;
		int drops = 0;
		BlockState newState = state;
		if (state.getValue(X)) {
			if (!this.checkConnection(level, pos, Direction.WEST) && !this.checkConnection(level, pos, Direction.EAST)) {
				newState = newState.setValue(X, false);
				drops++;
			} else flag = false;
		}
		if (state.getValue(Y)) {
			if (!this.checkConnection(level, pos, Direction.UP)) {
				newState = newState.setValue(Y, false);
				drops++;
			} else flag = false;
		}
		if (state.getValue(Z)) {
			if (!this.checkConnection(level, pos, Direction.NORTH) && !this.checkConnection(level, pos, Direction.SOUTH)) {
				newState = newState.setValue(Z, false);
				drops++;
			} else flag = false;
		}

		if (flag) {
			level.destroyBlock(pos, true);
		} else if (drops > 0) {
			level.setBlockAndUpdate(pos, newState);
			for (int i = 0; i < drops; i++) dropResources(this.defaultBlockState(), level, pos);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(X)) {
			if (canConnectTo(level.getBlockState(pos.relative(Direction.WEST)), Direction.WEST, level, pos)) return true;
			if (canConnectTo(level.getBlockState(pos.relative(Direction.EAST)), Direction.EAST, level, pos)) return true;
		}
		if (state.getValue(Y)) {
			if (canConnectTo(level.getBlockState(pos.relative(Direction.UP)), Direction.UP, level, pos)) return true;
		}
		if (state.getValue(Z)) {
			if (canConnectTo(level.getBlockState(pos.relative(Direction.NORTH)), Direction.NORTH, level, pos)) return true;
			return canConnectTo(level.getBlockState(pos.relative(Direction.SOUTH)), Direction.SOUTH, level, pos);
		}
		return false;
	}

	protected boolean checkConnection(LevelReader level, BlockPos pos, Direction dir) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		while (true) {
			if (!level.getBlockState(mutable).is(this)) return true;
			mutable.move(dir);
			if (!canConnectTo(level.getBlockState(mutable), dir, level, mutable)) return false;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
			return state.setValue(X, state.getValue(Z)).setValue(Z, state.getValue(X));
		}

		return state;
	}
}
