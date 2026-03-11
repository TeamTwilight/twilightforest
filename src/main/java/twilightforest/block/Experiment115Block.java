package twilightforest.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import twilightforest.init.TFItems;
import twilightforest.init.TFStats;

public class Experiment115Block extends Block {

	public static final IntegerProperty BITES_TAKEN = IntegerProperty.create("omnomnom", 0, 7);
	public static final BooleanProperty REGENERATE = BooleanProperty.create("regenerate");

	private static final VoxelShape QUARTER_SHAPE = box(1, 0, 1, 8, 8, 8);
	private static final VoxelShape HALF_SHAPE = box(1, 0, 1, 8, 8, 15);
	private static final VoxelShape THREE_QUARTER_SHAPE = Shapes.join(HALF_SHAPE, box(8, 0, 8, 15, 8, 15), BooleanOp.OR);
	private static final VoxelShape FULL_SHAPE = box(1, 0, 1, 15, 8, 15);

	public Experiment115Block(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(BITES_TAKEN, 7).setValue(REGENERATE, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(BITES_TAKEN)) {
			default -> FULL_SHAPE;
			case 2, 3 -> THREE_QUARTER_SHAPE;
			case 4, 5 -> HALF_SHAPE;
			case 6, 7 -> QUARTER_SHAPE;
		};
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int bitesTaken = state.getValue(BITES_TAKEN);

		if (!player.isSecondaryUseActive()) {
			if (stack.is(TFItems.EXPERIMENT_115.get())) {
				if (bitesTaken == 0) return ItemInteractionResult.FAIL;
				level.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken - 1));
				level.playSound(null, pos, state.getSoundType(level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);
				if (player instanceof ServerPlayer)
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			} else if (!state.getValue(REGENERATE) && bitesTaken == 0 && stack.is(Items.REDSTONE)) {
				level.setBlockAndUpdate(pos, state.setValue(REGENERATE, true));
				level.playSound(null, pos, state.getSoundType(level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(Items.REDSTONE));
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		} else {
			if (!state.getValue(REGENERATE)) {
				if (bitesTaken < 7) {
					level.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken + 1));
				} else {
					level.removeBlock(pos, false);
				}
				player.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.0F);
				if (!player.isCreative())
					ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TFItems.EXPERIMENT_115.get()));
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!player.canEat(false)) return InteractionResult.PASS;
		else {
			player.awardStat(TFStats.E115_SLICES_EATEN.get());
			player.getFoodData().eat(4, 0.3F);
			level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
			int i = state.getValue(BITES_TAKEN);

			if (i < 7) {
				level.setBlock(pos, state.setValue(BITES_TAKEN, i + 1), Block.UPDATE_ALL);
			} else {
				level.removeBlock(pos, false);
			}

			if (player instanceof ServerPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, new ItemStack(TFItems.EXPERIMENT_115.get(), 8 - i));
				player.awardStat(Stats.ITEM_USED.get(TFItems.EXPERIMENT_115.get()));
			}

			return InteractionResult.sidedSuccess(level.isClientSide());
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(REGENERATE) && state.getValue(BITES_TAKEN) != 0) {
			level.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, state.getValue(BITES_TAKEN) - 1));
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).isSolid();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES_TAKEN, REGENERATE);
	}

	@Override
	@SuppressWarnings("deprecation") // Fine to override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return (8 - state.getValue(BITES_TAKEN)) + (state.getValue(REGENERATE) ? 7 : 0);
	}

	@Override
	@SuppressWarnings("deprecation") // Fine to override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation") // Fine to override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(REGENERATE);
	}

	@Override
	@SuppressWarnings("deprecation") // Fine to override
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return state.getValue(REGENERATE) ? 15 - (state.getValue(BITES_TAKEN) * 2) : 0;
	}
}
