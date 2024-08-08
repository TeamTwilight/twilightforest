package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ForceFieldBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	public static final BooleanProperty EAST = PipeBlock.EAST;
	protected static final VoxelShape BASE_SHAPE = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);
	protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 7.0D, 9.0D, 9.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(9.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);
	protected static final VoxelShape UP_SHAPE = Block.box(7.0D, 9.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 7.0D, 9.0D, 9.0D, 9.0D, 16.0D);
	protected static final VoxelShape DOWN_WEST_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 7.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_EAST_SHAPE = Block.box(9.0D, 0.0D, 7.0D, 16.0D, 7.0D, 9.0D);
	protected static final VoxelShape DOWN_NORTH_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 7.0D, 7.0D);
	protected static final VoxelShape DOWN_SOUTH_SHAPE = Block.box(7.0D, 0.0D, 9.0D, 9.0D, 7.0D, 16.0D);
	protected static final VoxelShape UP_WEST_SHAPE = Block.box(0.0D, 9.0D, 7.0D, 7.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_EAST_SHAPE = Block.box(9.0D, 9.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	protected static final VoxelShape UP_NORTH_SHAPE = Block.box(7.0D, 9.0D, 0.0D, 9.0D, 16.0D, 7.0D);
	protected static final VoxelShape UP_SOUTH_SHAPE = Block.box(7.0D, 9.0D, 9.0D, 9.0D, 16.0D, 16.0D);
	protected static final VoxelShape NORTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 0.0D, 7.0D, 9.0D, 7.0D);
	protected static final VoxelShape NORTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 0.0D, 16.0D, 9.0D, 7.0D);
	protected static final VoxelShape SOUTH_WEST_SHAPE = Block.box(0.0D, 7.0D, 9.0D, 7.0D, 9.0D, 16.0D);
	protected static final VoxelShape SOUTH_EAST_SHAPE = Block.box(9.0D, 7.0D, 9.0D, 16.0D, 9.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public ForceFieldBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false)
			.setValue(DOWN, false).setValue(UP, false)
			.setValue(NORTH, false).setValue(SOUTH, false)
			.setValue(WEST, false).setValue(EAST, false));
	}

	public static boolean cornerConnects(BlockGetter getter, BlockPos pos, Direction dir1, Direction dir2) {
		Vec3i vec31 = dir1.getNormal();
		Vec3i vec32 = dir2.getNormal();

		return fullFaceOrSimilarForceField(getter, pos.offset(vec31), dir1, dir2) ||
			fullFaceOrSimilarForceField(getter, pos.offset(vec32), dir2, dir1);
	}

	private static boolean fullFaceOrSimilarForceField(BlockGetter getter, BlockPos pos, Direction relative, Direction similar) {
		BlockState state = getter.getBlockState(pos);
		return state.isFaceSturdy(getter, pos, relative.getOpposite()) ||
			(state.getBlock() instanceof ForceFieldBlock && state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(similar)));
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape = BASE_SHAPE;

		boolean down = state.getValue(DOWN);
		boolean up = state.getValue(UP);
		boolean north = state.getValue(NORTH);
		boolean south = state.getValue(SOUTH);
		boolean west = state.getValue(WEST);
		boolean east = state.getValue(EAST);

		if (down) {
			shape = Shapes.or(shape, DOWN_SHAPE);
			if (north && ForceFieldBlock.cornerConnects(getter, pos, Direction.DOWN, Direction.NORTH)) shape = Shapes.or(shape, DOWN_NORTH_SHAPE);
			if (south && ForceFieldBlock.cornerConnects(getter, pos, Direction.DOWN, Direction.SOUTH)) shape = Shapes.or(shape, DOWN_SOUTH_SHAPE);
			if (west && ForceFieldBlock.cornerConnects(getter, pos, Direction.DOWN, Direction.WEST)) shape = Shapes.or(shape, DOWN_WEST_SHAPE);
			if (east && ForceFieldBlock.cornerConnects(getter, pos, Direction.DOWN, Direction.EAST)) shape = Shapes.or(shape, DOWN_EAST_SHAPE);
		}
		if (up) {
			shape = Shapes.or(shape, UP_SHAPE);
			if (north && ForceFieldBlock.cornerConnects(getter, pos, Direction.UP, Direction.NORTH)) shape = Shapes.or(shape, UP_NORTH_SHAPE);
			if (south && ForceFieldBlock.cornerConnects(getter, pos, Direction.UP, Direction.SOUTH)) shape = Shapes.or(shape, UP_SOUTH_SHAPE);
			if (west && ForceFieldBlock.cornerConnects(getter, pos, Direction.UP, Direction.WEST)) shape = Shapes.or(shape, UP_WEST_SHAPE);
			if (east && ForceFieldBlock.cornerConnects(getter, pos, Direction.UP, Direction.EAST)) shape = Shapes.or(shape, UP_EAST_SHAPE);
		}
		if (north) {
			shape = Shapes.or(shape, NORTH_SHAPE);
			if (west && ForceFieldBlock.cornerConnects(getter, pos, Direction.NORTH, Direction.WEST)) shape = Shapes.or(shape, NORTH_WEST_SHAPE);
			if (east && ForceFieldBlock.cornerConnects(getter, pos, Direction.NORTH, Direction.EAST)) shape = Shapes.or(shape, NORTH_EAST_SHAPE);
		}
		if (south) {
			shape = Shapes.or(shape, SOUTH_SHAPE);
			if (west && ForceFieldBlock.cornerConnects(getter, pos, Direction.SOUTH, Direction.WEST)) shape = Shapes.or(shape, SOUTH_WEST_SHAPE);
			if (east && ForceFieldBlock.cornerConnects(getter, pos, Direction.SOUTH, Direction.EAST)) shape = Shapes.or(shape, SOUTH_EAST_SHAPE);
		}
		if (west) shape = Shapes.or(shape, WEST_SHAPE);
		if (east) shape = Shapes.or(shape, EAST_SHAPE);

		return shape;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
		return true;
	}

	public boolean canConnectTo(@Nullable BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		BlockState relative = getter.getBlockState(pos.relative(direction));
		if (relative.is(this)) return true;

		int betterConnections = 0;
		if (state == null) {
			for (Direction face : Direction.values()) {
				if (getter.getBlockState(pos.relative(face)).is(this)) betterConnections++;
			}
		} else {
			for (Direction face : Direction.values()) {
				if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face))) betterConnections++;
			}
		}
		if (betterConnections >= 3) return false;

		return !isExceptionForConnection(relative) && relative.isFaceSturdy(getter, pos.relative(direction), direction.getOpposite());
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		Direction clicked = context.getClickedFace();

		BlockState state = this.defaultBlockState();

		for (Direction dir : Direction.values()) {
			state = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), clicked.getOpposite() == dir ||
				(clicked != dir ? this.canConnectTo(null, level, pos, dir) : !context.isSecondaryUseActive()));
		}

		return state;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor accessor, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		return this.canConnectTo(state, accessor, pos, direction) ? state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true) : state;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

}
