package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Locale;

public class WroughtIronFenceBlock extends Block implements SimpleWaterloggedBlock {

	public static final EnumProperty<PostState> POST = EnumProperty.create("post", PostState.class);
	public static final EnumProperty<FenceSide> EAST_FENCE = EnumProperty.create("east_fence", FenceSide.class);
	public static final EnumProperty<FenceSide> NORTH_FENCE = EnumProperty.create("north_fence", FenceSide.class);
	public static final EnumProperty<FenceSide> SOUTH_FENCE = EnumProperty.create("south_fence", FenceSide.class);
	public static final EnumProperty<FenceSide> WEST_FENCE = EnumProperty.create("west_fence", FenceSide.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape FINIAL_HEAD_SHAPE = Block.box(6.5D, 17.0D, 6.5D, 9.5D, 20.0D, 9.5D);
	private static final VoxelShape FINIAL_NECK_SHAPE = Block.box(7.5D, 16.0D, 7.5D, 8.5D, 17.0D, 8.5D);
	private static final VoxelShape FINIAL_SHAPE = Shapes.or(FINIAL_HEAD_SHAPE, FINIAL_NECK_SHAPE);
	private static final VoxelShape POST_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
	private static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 8.0D);
	private static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 0.0D, 8.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape EAST_SHAPE = Block.box(8.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	private static final VoxelShape WEST_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 8.0D, 16.0D, 9.0D);

	public WroughtIronFenceBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(POST, PostState.POST).setValue(EAST_FENCE, FenceSide.NONE).setValue(NORTH_FENCE, FenceSide.NONE).setValue(SOUTH_FENCE, FenceSide.NONE).setValue(WEST_FENCE, FenceSide.NONE).setValue(WATERLOGGED, false));
	}

	private static boolean isConnected(BlockState state, Property<FenceSide> side) {
		return state.getValue(side) != FenceSide.NONE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape finalShape = Shapes.empty();
		if (state.getValue(POST) != PostState.NONE) {
			finalShape = POST_SHAPE;
			if (state.getValue(POST) == PostState.CAPPED) {
				finalShape = Shapes.or(finalShape, FINIAL_SHAPE);
			}
		}
		if (state.getValue(NORTH_FENCE) != FenceSide.NONE) {
			finalShape = Shapes.or(finalShape, NORTH_SHAPE);
		}
		if (state.getValue(SOUTH_FENCE) != FenceSide.NONE) {
			finalShape = Shapes.or(finalShape, SOUTH_SHAPE);
		}
		if (state.getValue(WEST_FENCE) != FenceSide.NONE) {
			finalShape = Shapes.or(finalShape, WEST_SHAPE);
		}
		if (state.getValue(EAST_FENCE) != FenceSide.NONE) {
			finalShape = Shapes.or(finalShape, EAST_SHAPE);
		}
		//make it a full block if for some reason theres not a single piece of fence showing. That way people can still interact with the block
		if (finalShape.isEmpty()) finalShape = Shapes.block();
		return finalShape;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("block.twilightforest.wrought_iron_fence.cap").withStyle(ChatFormatting.GRAY));
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType computationType) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		LevelReader level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		//Sides
		BlockState north = level.getBlockState(pos.north());
		BlockState east = level.getBlockState(pos.east());
		BlockState south = level.getBlockState(pos.south());
		BlockState west = level.getBlockState(pos.west());
		BlockState above = level.getBlockState(pos.above());
		boolean northFace = this.connectsTo(north, north.isFaceSturdy(level, pos.north(), Direction.SOUTH));
		boolean eastFace = this.connectsTo(east, east.isFaceSturdy(level, pos.east(), Direction.WEST));
		boolean southFace = this.connectsTo(south, south.isFaceSturdy(level, pos.south(), Direction.NORTH));
		boolean westFace = this.connectsTo(west, west.isFaceSturdy(level, pos.west(), Direction.EAST));
		//Waterlogged
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		BlockState state = this.defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
		return this.fenceShape(level, state, pos.above(), northFace, eastFace, southFace, westFace);
	}

	private boolean connectsTo(BlockState state, boolean solid) {
		return state.is(BlockTags.WALLS) || !isExceptionForConnection(state) && solid || state.getBlock() instanceof IronBarsBlock;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return direction.getAxis() == Direction.Axis.Y ? this.updateTop(level, state, pos) : this.updateSide(level, pos, state, neighborPos, neighbor, direction);
	}

	private BlockState updateSide(LevelReader level, BlockPos pos, BlockState firstState, BlockPos secondPos, BlockState secondState, Direction direction) {
		Direction opposite = direction.getOpposite();
		boolean north = direction == Direction.NORTH ? this.connectsTo(secondState, secondState.isFaceSturdy(level, secondPos, opposite)) : isConnected(firstState, NORTH_FENCE);
		boolean east = direction == Direction.EAST ? this.connectsTo(secondState, secondState.isFaceSturdy(level, secondPos, opposite)) : isConnected(firstState, EAST_FENCE);
		boolean south = direction == Direction.SOUTH ? this.connectsTo(secondState, secondState.isFaceSturdy(level, secondPos, opposite)) : isConnected(firstState, SOUTH_FENCE);
		boolean west = direction == Direction.WEST ? this.connectsTo(secondState, secondState.isFaceSturdy(level, secondPos, opposite)) : isConnected(firstState, WEST_FENCE);
		return this.fenceShape(level, firstState, pos, north, east, south, west);
	}

	private BlockState updateTop(LevelReader level, BlockState state, BlockPos pos) {
		boolean north = isConnected(state, NORTH_FENCE);
		boolean east = isConnected(state, EAST_FENCE);
		boolean south = isConnected(state, SOUTH_FENCE);
		boolean west = isConnected(state, WEST_FENCE);
		return this.fenceShape(level, state, pos, north, east, south, west);
	}

	private BlockState fenceShape(LevelReader level, BlockState state, BlockPos pos, boolean north, boolean east, boolean south, boolean west) {
		BlockState blockstate = this.updateSides(level, state, pos, north, east, south, west);
		return blockstate.setValue(POST, this.makePost(level, blockstate, pos));
	}

	private BlockState updateSides(LevelReader level, BlockState state, BlockPos pos, boolean north, boolean east, boolean south, boolean west) {
		return state.setValue(NORTH_FENCE, north ? this.makeFenceState(Direction.NORTH, level, pos) : FenceSide.NONE)
			.setValue(EAST_FENCE, east ? this.makeFenceState(Direction.EAST, level, pos) : FenceSide.NONE)
			.setValue(SOUTH_FENCE, south ? this.makeFenceState(Direction.SOUTH, level, pos) : FenceSide.NONE)
			.setValue(WEST_FENCE, west ? this.makeFenceState(Direction.WEST, level, pos) : FenceSide.NONE);
	}

	private FenceSide makeFenceState(Direction direction, LevelReader level, BlockPos pos) {
		BlockPos up = pos.above();
		BlockState upState = level.getBlockState(up.relative(direction));
		BlockPos down = pos.below();
		BlockState downState = level.getBlockState(down.relative(direction));

		boolean above = level.getBlockState(up).is(this) && (upState.is(this) || this.connectsTo(upState, upState.isFaceSturdy(level, up.relative(direction), direction.getOpposite())));
		boolean below = level.getBlockState(down).is(this) && (downState.is(this) || this.connectsTo(downState, downState.isFaceSturdy(level, down.relative(direction), direction.getOpposite())));

		if (above && below) return FenceSide.MIDDLE;
		if (!above && below) return FenceSide.TOP;
		if (above) return FenceSide.BOTTOM;
		return FenceSide.FULL;
	}

	private PostState makePost(LevelReader level, BlockState state, BlockPos pos) {
		BlockState above = level.getBlockState(pos.above());

		boolean shouldAnyBePost = shouldAnyBePost(level, pos);
		if (state.getValue(POST) == PostState.CAPPED && above.canBeReplaced()) return PostState.CAPPED;
		if (shouldAnyBePost) return PostState.POST;
		else return shouldBePost(state) ? PostState.POST : PostState.NONE;
	}

	private boolean shouldAnyBePost(LevelReader level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.above().mutable();
		while (true) {
			BlockState checkState = level.getBlockState(mutable);
			if (!checkState.is(this)) break;
			mutable.move(Direction.UP);
			if (shouldBePost(checkState) || checkState.getValue(POST) == PostState.CAPPED) return true;
		}

		mutable.set(pos.below());
		while (true) {
			BlockState checkState = level.getBlockState(mutable);
			if (!checkState.is(this)) break;
			mutable.move(Direction.DOWN);
			if (shouldBePost(checkState)) return true;
		}

		return false;
	}

	private boolean shouldBePost(BlockState state) {
		FenceSide nSide = state.getValue(NORTH_FENCE);
		FenceSide sSide = state.getValue(SOUTH_FENCE);
		FenceSide eSide = state.getValue(EAST_FENCE);
		FenceSide wSide = state.getValue(WEST_FENCE);
		boolean north = nSide == FenceSide.NONE;
		boolean south = sSide == FenceSide.NONE;
		boolean east = eSide == FenceSide.NONE;
		boolean west = wSide == FenceSide.NONE;

		return north && south && east && west || north != south || east != west || !north && !south && !east && !west;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return !pState.getValue(WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 -> state.setValue(NORTH_FENCE, state.getValue(SOUTH_FENCE)).setValue(EAST_FENCE, state.getValue(WEST_FENCE)).setValue(SOUTH_FENCE, state.getValue(NORTH_FENCE)).setValue(WEST_FENCE, state.getValue(EAST_FENCE));
			case COUNTERCLOCKWISE_90 -> state.setValue(NORTH_FENCE, state.getValue(EAST_FENCE)).setValue(EAST_FENCE, state.getValue(SOUTH_FENCE)).setValue(SOUTH_FENCE, state.getValue(WEST_FENCE)).setValue(WEST_FENCE, state.getValue(NORTH_FENCE));
			case CLOCKWISE_90 -> state.setValue(NORTH_FENCE, state.getValue(WEST_FENCE)).setValue(EAST_FENCE, state.getValue(NORTH_FENCE)).setValue(SOUTH_FENCE, state.getValue(EAST_FENCE)).setValue(WEST_FENCE, state.getValue(SOUTH_FENCE));
			default -> state;
		};
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state.setValue(NORTH_FENCE, state.getValue(SOUTH_FENCE)).setValue(SOUTH_FENCE, state.getValue(NORTH_FENCE));
			case FRONT_BACK -> state.setValue(EAST_FENCE, state.getValue(WEST_FENCE)).setValue(WEST_FENCE, state.getValue(EAST_FENCE));
			default -> super.mirror(state, mirror);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POST, NORTH_FENCE, EAST_FENCE, WEST_FENCE, SOUTH_FENCE, WATERLOGGED);
	}

	public enum FenceSide implements StringRepresentable {
		NONE,
		MIDDLE,
		BOTTOM,
		TOP,
		FULL;

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}

	public enum PostState implements StringRepresentable {
		NONE,
		POST,
		CAPPED;

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
