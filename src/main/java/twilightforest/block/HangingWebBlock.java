package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;
import twilightforest.enums.WebShape;
import twilightforest.init.TFBlocks;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HangingWebBlock extends Block implements IShearable {
	public static final MapCodec<HangingWebBlock> CODEC = simpleCodec(HangingWebBlock::new);
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final EnumProperty<WebShape> NORTH = EnumProperty.create("web_north", WebShape.class);
	public static final EnumProperty<WebShape> EAST = EnumProperty.create("web_east", WebShape.class);
	public static final EnumProperty<WebShape> SOUTH = EnumProperty.create("web_south", WebShape.class);
	public static final EnumProperty<WebShape> WEST = EnumProperty.create("web_west", WebShape.class);
	public static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	public static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
	public static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
	public static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	public static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
	public static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
	private final Map<BlockState, VoxelShape> shapesCache;

	public HangingWebBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(UP, false)
			.setValue(DOWN, false)
			.setValue(NORTH, WebShape.NONE)
			.setValue(EAST, WebShape.NONE)
			.setValue(SOUTH, WebShape.NONE)
			.setValue(WEST, WebShape.NONE)
		);
		this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), HangingWebBlock::calculateShape)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}

	@Override
	public MapCodec<HangingWebBlock> codec() {
		return CODEC;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof Spider) return;
		if (entity instanceof LivingEntity livingentity && (livingentity.hasEffect(MobEffects.WEAVING) || livingentity.isShiftKeyDown())) return;
		entity.makeStuckInBlock(state, new Vec3(0.95, 0.95, 0.95));

		// dissolve
		if (!level.isClientSide() && entity instanceof LivingEntity living && living.getRandom().nextInt(20) == 1) {
			level.destroyBlock(pos, false);
		}
	}

	public static VoxelShape calculateShape(BlockState state) {
		VoxelShape voxelshape = Shapes.empty();
		if (state.getValue(UP)) {
			voxelshape = UP_AABB;
		}

		if (state.getValue(DOWN)) {
			voxelshape = Shapes.or(voxelshape, DOWN_AABB);
		}

		if (state.getValue(NORTH) != WebShape.NONE) {
			voxelshape = Shapes.or(voxelshape, NORTH_AABB);
		}

		if (state.getValue(SOUTH) != WebShape.NONE) {
			voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
		}

		if (state.getValue(EAST) != WebShape.NONE) {
			voxelshape = Shapes.or(voxelshape, EAST_AABB);
		}

		if (state.getValue(WEST) != WebShape.NONE) {
			voxelshape = Shapes.or(voxelshape, WEST_AABB);
		}

		return voxelshape.isEmpty() ? Shapes.empty() : voxelshape;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapesCache.get(state);
	}

	@Override
	protected boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return this.hasFaces(this.getUpdatedState(state, level, pos));
	}

	private boolean hasFaces(BlockState state) {
		return this.countFaces(state) > 0;
	}

	public static boolean checkFace(BlockState state, Direction direction) {
		if (state.is(TFBlocks.HANGING_WEB)) {
			return switch (direction) {
				case DOWN -> state.getValue(DOWN);
				case UP -> state.getValue(UP);
				case NORTH -> state.getValue(NORTH) != WebShape.NONE;
				case SOUTH -> state.getValue(SOUTH) != WebShape.NONE;
				case WEST -> state.getValue(WEST) != WebShape.NONE;
				case EAST -> state.getValue(EAST) != WebShape.NONE;
			};
		} else return state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
	}

	private int countFaces(BlockState state) {
		int i = 0;
		for (Direction direction : Direction.values()) if (checkFace(state, direction)) ++i;
		return i;
	}

	public boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
		BlockPos blockpos = pos.relative(direction);
		if (isAcceptableNeighbour(level, blockpos, direction)) {
			return true;
		} else if (direction.getAxis() == Direction.Axis.Y) {
			return false;
		} else {
			BlockState blockstate = level.getBlockState(pos.above());
			return blockstate.is(this) && checkFace(blockstate, direction);
		}
	}

	public static boolean isAcceptableNeighbour(BlockGetter blockReader, BlockPos neighborPos, Direction attachedFace) {
		return MultifaceBlock.canAttachTo(blockReader, attachedFace, neighborPos, blockReader.getBlockState(neighborPos));
	}

	private BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos) {
		BlockPos blockpos = pos.above();
		if (state.getValue(UP)) {
			state = state.setValue(UP, isAcceptableNeighbour(level, blockpos, Direction.DOWN));
		}
		if (state.getValue(DOWN)) {
			state = state.setValue(DOWN, isAcceptableNeighbour(level, pos.below(), Direction.UP));
		}

		BlockState blockstate = null;
		Iterator<Direction> iterator = Direction.Plane.HORIZONTAL.iterator();

		while(true) {
			Direction direction;
			EnumProperty<WebShape> booleanproperty;
			do {
				if (!iterator.hasNext()) {
					return state;
				}

				direction = iterator.next();
				booleanproperty = getPropertyForFace(direction);
			} while(state.getValue(booleanproperty) == WebShape.NONE);

			boolean flag = this.canSupportAtFace(level, pos, direction);
			if (!flag) {
				if (blockstate == null) {
					blockstate = level.getBlockState(blockpos);
				}

				flag = (blockstate.is(this) && blockstate.getValue(booleanproperty) != WebShape.NONE) || (blockstate.is(TFBlocks.WEBWORM) && blockstate.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction)));
			}

			if (!flag) state = state.setValue(booleanproperty, WebShape.NONE);
			else {
				state = state.setValue(getPropertyForFace(direction), getPropertyForPlacement(level, pos, direction));
			}
		}
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		BlockState blockstate = this.getUpdatedState(state, level, pos);
		return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
	}

	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		BlockState blockstate = useContext.getLevel().getBlockState(useContext.getClickedPos());
		return blockstate.is(this) ? this.countFaces(blockstate) < 6 : super.canBeReplaced(state, useContext);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
		boolean flag = blockstate.is(this);
		BlockState blockstate1 = flag ? blockstate : this.defaultBlockState();

		for (Direction direction : context.getNearestLookingDirections()) {
			boolean flag1 = flag && checkFace(blockstate, direction);
			if (!flag1 && this.canSupportAtFace(context.getLevel(), context.getClickedPos(), direction)) {
				if (direction.getAxis().isHorizontal()) {
					return blockstate1.setValue(getPropertyForFace(direction), getPropertyForPlacement(context.getLevel(), context.getClickedPos(), direction));
				} else {
					return blockstate1.setValue(direction == Direction.UP ? UP : DOWN, true);
				}
			}
		}

		return flag ? blockstate1 : null;
	}


	public static boolean isAWeb(BlockState state) {
		return state.is(TFBlocks.HANGING_WEB) || state.is(TFBlocks.WEBWORM);
	}

	public static WebShape getPropertyForPlacement(BlockGetter level, BlockPos pos, Direction face) {
		BlockState belowState = level.getBlockState(pos.below());
		return isAWeb(belowState) && checkFace(belowState, face) ? WebShape.TALL : WebShape.SHORT;
	}

	public static EnumProperty<WebShape> getPropertyForFace(Direction direction) {
		return switch (direction) {
			case NORTH -> NORTH;
			case SOUTH -> SOUTH;
			case WEST -> WEST;
			case EAST -> EAST;
			default -> throw new IllegalArgumentException("Unexpected parameter direction: " + direction);
		};
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotate) {
		return switch (rotate) {
			case CLOCKWISE_180 ->
				state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 ->
				state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 ->
				state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default -> super.mirror(state, mirror);
		};
	}
}
