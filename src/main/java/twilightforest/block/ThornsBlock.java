package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDamageTypes;
import twilightforest.tags.TFItemTags;

public class ThornsBlock extends ConnectableRotatedPillarBlock implements SimpleWaterloggedBlock {

	protected static final VoxelShape BASE_SHAPE = Block.box(3.0D, 3.0D, 3.0D, 13.0D, 13.0D, 13.0D);

	protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 3.0D, 3.0D, 3.0D, 13.0D, 13.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(13.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 3.0D, 13.0D);
	protected static final VoxelShape UP_SHAPE = Block.box(3.0D, 13.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 3.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(3.0D, 3.0D, 13.0D, 13.0D, 13.0D, 16.0D);

	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final float THORN_DAMAGE = 4.0F;

	public ThornsBlock(Properties properties) {
		super(properties, 10);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false).setValue(AXIS, Direction.Axis.Y)
			.setValue(DOWN, false).setValue(UP, false)
			.setValue(NORTH, false).setValue(SOUTH, false)
			.setValue(WEST, false).setValue(EAST, false));
	}

	@Override
	public boolean canConnectTo(Direction.Axis thisAxis, Direction facing, BlockState facingState, boolean solidSide) {
		return (facingState.getBlock() instanceof ThornsBlock
			|| facingState.getBlock().equals(TFBlocks.THORN_ROSE.get())
			|| facingState.getBlock().equals(TFBlocks.THORN_LEAVES.get())
			|| facingState.getBlock().equals(TFBlocks.WEATHERED_DEADROCK.get()));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		VoxelShape shape = BASE_SHAPE;

		if (state.getValue(DOWN)) shape = Shapes.or(shape, DOWN_SHAPE);
		if (state.getValue(UP)) shape = Shapes.or(shape, UP_SHAPE);
		if (state.getValue(NORTH)) shape = Shapes.or(shape, NORTH_SHAPE);
		if (state.getValue(SOUTH)) shape = Shapes.or(shape, SOUTH_SHAPE);
		if (state.getValue(WEST)) shape = Shapes.or(shape, WEST_SHAPE);
		if (state.getValue(EAST)) shape = Shapes.or(shape, EAST_SHAPE);

		return shape;
	}

	@Nullable
	@Override
	public PathType getBlockPathType(BlockState state, BlockGetter getter, BlockPos pos, @Nullable Mob entity) {
		return PathType.DAMAGING;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if ((!(entity instanceof ItemEntity item) || !item.getItem().is(TFItemTags.IMMUNE_TO_THORNS)) && level instanceof ServerLevel sl) {
			entity.hurtServer(sl, TFDamageTypes.getDamageSource(level, TFDamageTypes.THORNS), THORN_DAMAGE);
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (state.getBlock() instanceof ThornsBlock && state.getValue(AXIS) == Direction.Axis.Y) {
			this.entityInside(state, level, pos, entity, InsideBlockEffectApplier.NOOP, true);
		}

		super.stepOn(level, pos, state, entity);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		if (!player.isCreative()) {
			if (!level.isClientSide()) {
				// grow more
				this.doThornBurst(level, pos, state);
			}
			return false;
		} else {
			return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
		}
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(Level level, BlockPos pos, BlockState state) {
		switch (state.getValue(AXIS)) {
			case Y -> {
				this.growThorns(level, pos, Direction.UP);
				this.growThorns(level, pos, Direction.DOWN);
			}
			case X -> {
				this.growThorns(level, pos, Direction.EAST);
				this.growThorns(level, pos, Direction.WEST);
			}
			case Z -> {
				this.growThorns(level, pos, Direction.NORTH);
				this.growThorns(level, pos, Direction.SOUTH);
			}
		}

		// also try three random directions
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
		this.growThorns(level, pos, Direction.getRandom(level.getRandom()));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(Level level, BlockPos pos, Direction dir) {
		int length = 1 + level.getRandom().nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (level.isEmptyBlock(dPos)) {
				level.setBlock(dPos, TFBlocks.GREEN_THORNS.get().defaultBlockState().setValue(AXIS, dir.getAxis()), Block.UPDATE_CLIENTS);
			} else {
				break;
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction.Axis axis = context.getClickedFace().getAxis();
		BlockState state = this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
		BlockPos pos = context.getClickedPos();

		for (Direction direction : Direction.values()) {
			BlockPos relativePos = pos.relative(direction);
			BlockState relativeState = context.getLevel().getBlockState(relativePos);
			state = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), this.canConnectTo(axis, direction, relativeState, true));
		}

		return state.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		if (neighborState.is(Blocks.AIR)) return state;

		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(WATERLOGGED));
	}
}
