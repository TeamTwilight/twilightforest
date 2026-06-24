package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class TFBushBlock extends Block implements SnowLoggable {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;

	private final ResourceKey<LootTable> berryLoot;

	private static final VoxelShape SMALL_SNOWY_BUSH_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
	private static final VoxelShape MEDIUM_SNOWY_BUSH_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);

	private static final VoxelShape[] SMALL_BUSH_SHAPES = Util.make(new VoxelShape[9], shapes -> {
		shapes[0] = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
		for (int i = 1; i <= 8; i++) {
			shapes[i] = Shapes.or(SMALL_SNOWY_BUSH_SHAPE, SNOW_SHAPE_BY_LAYER[i]);
		}
	});

	private static final VoxelShape[] MEDIUM_BUSH_SHAPES = Util.make(new VoxelShape[9], shapes -> {
		shapes[0] = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
		for (int i = 1; i <= 8; i++) {
			shapes[i] = Shapes.or(MEDIUM_SNOWY_BUSH_SHAPE, SNOW_SHAPE_BY_LAYER[i]);
		}
	});

	public TFBushBlock(ResourceKey<LootTable> berryLoot, Properties properties) {
		super(properties);
		this.berryLoot = berryLoot;
		this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0).setValue(SNOW_LAYERS, 0));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState maybeSnow = context.getLevel().getBlockState(context.getClickedPos());
		if (maybeSnow.is(Blocks.SNOW) && maybeSnow.getValue(SnowLayerBlock.LAYERS) == 1) {
			return this.defaultBlockState().setValue(SNOW_LAYERS, 1);
		}

		return super.getStateForPlacement(context);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(SNOW_LAYERS) > 0) {
			if (!Blocks.SNOW.defaultBlockState().canSurvive(level, pos)) {
				//TODO cant do this anymore
				//level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.SNOW.defaultBlockState()));
				return state.setValue(SNOW_LAYERS, 0);
			}
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE).add(SNOW_LAYERS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AGE)) {
			case 1 -> MEDIUM_BUSH_SHAPES[state.getValue(SNOW_LAYERS)];
			case 2, 3 -> Shapes.block();
			default -> SMALL_BUSH_SHAPES[state.getValue(SNOW_LAYERS)];
		};
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(AGE) < MAX_AGE && random.nextInt(20) == 0 && canGrowAt(state, level, pos))
			this.grow(state, level, pos, state.getValue(AGE) + 1);
		meltSnow(state, level, pos);
	}

	protected void grow(BlockState state, ServerLevel level, BlockPos pos, int age) {
		level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_CLIENTS);
		if (age == 2 && state.getValue(SNOW_LAYERS) > 0) {
			BlockState aboveState = level.getBlockState(pos.above());
			if (aboveState.canBeReplaced()) {
				level.setBlock(pos.above(), Blocks.SNOW.defaultBlockState(), Block.UPDATE_CLIENTS);
			} else if (aboveState.getBlock() instanceof SnowLoggable && aboveState.getValue(SNOW_LAYERS) == 0) {
				level.setBlock(pos.above(), aboveState.setValue(SNOW_LAYERS, 1), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (state.getValue(AGE) == MAX_AGE) {
			if (level instanceof ServerLevel serverLevel) {
				Block.dropFromBlockInteractLootTable(serverLevel, this.berryLoot, state, level.getBlockEntity(pos), null, player, (level1, stack) -> Block.popResource(level1, pos, stack));

				level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
				BlockState newState = state.setValue(AGE, MAX_AGE - 1);
				level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
			}
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!stack.is(Items.SNOW) || state.getValue(SNOW_LAYERS) == MAX_SNOW_LAYERS || !Blocks.SNOW.defaultBlockState().canSurvive(level, pos))
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		BlockState newState = state.setValue(SNOW_LAYERS, state.getValue(SNOW_LAYERS) + 1);
		if (isColliding(player, pos, newState))
			return InteractionResult.FAIL;
		if (!player.hasInfiniteMaterials())
			stack.shrink(1);
		level.setBlock(pos, newState, Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
		level.playSound(player, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS);
		updateSnowBeneath(level, pos);
		return InteractionResult.SUCCESS;
	}

	private static void updateSnowBeneath(Level level, BlockPos pos) {
		if (level.getBlockState(pos.below()).hasProperty(BlockStateProperties.SNOWY)) {
			level.setBlock(pos.below(), level.getBlockState(pos.below()).trySetValue(BlockStateProperties.SNOWY, true), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
		if (!player.isSecondaryUseActive() && state.getValue(SNOW_LAYERS) > MIN_SNOW_LAYERS) {
			return;
		}
		super.spawnDestroyParticles(level, player, pos, state);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		if (!player.isSecondaryUseActive() && state.getValue(SNOW_LAYERS) > MIN_SNOW_LAYERS) {
			this.handleBreakingLogic(level, pos, state, player, null);
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
	}

	@Nullable
	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return state.getValue(AGE) < 2 ? PushReaction.DESTROY : null;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState stateBelow = level.getBlockState(pos.below());
		boolean isSameMatureBush = stateBelow.is(state.getBlock()) && stateBelow.getValue(AGE) >= MAX_AGE - 1;
		return this.canBePlacedAt(stateBelow) || isSameMatureBush;
	}

	public abstract boolean canBePlacedAt(BlockState state);

	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return canSurvive(state, level, pos);
	}
}
