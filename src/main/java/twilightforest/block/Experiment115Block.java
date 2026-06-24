package twilightforest.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import twilightforest.init.TFItems;
import twilightforest.init.TFStats;
import twilightforest.inventory.InventoryUtil;

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
			case 2, 3 -> THREE_QUARTER_SHAPE;
			case 4, 5 -> HALF_SHAPE;
			case 6, 7 -> QUARTER_SHAPE;
			default -> FULL_SHAPE;
		};
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int bitesTaken = state.getValue(BITES_TAKEN);

		if (!player.isSecondaryUseActive()) {
			if (stack.is(TFItems.EXPERIMENT_115.get())) {
				if (bitesTaken == 0) return InteractionResult.FAIL;
				level.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken - 1));
				level.playSound(null, pos, state.getSoundType(level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);
				if (player instanceof ServerPlayer)
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
				return InteractionResult.SUCCESS;
			} else if (!state.getValue(REGENERATE) && bitesTaken == 0 && stack.is(Items.REDSTONE)) {
				level.setBlockAndUpdate(pos, state.setValue(REGENERATE, true));
				level.playSound(null, pos, state.getSoundType(level, pos, player).getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
				stack.consume(1, player);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(Items.REDSTONE));
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (!state.getValue(REGENERATE)) {
				if (bitesTaken < 7) {
					level.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken + 1));
				} else {
					level.removeBlock(pos, false);
				}
				player.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.0F);
				if (!player.isCreative()) {
					InventoryUtil.giveItemToPlayer(player, new ItemStack(TFItems.EXPERIMENT_115.get()));
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		ItemStack experStack = new ItemStack(TFItems.EXPERIMENT_115.get());
		FoodProperties props = experStack.get(DataComponents.FOOD);
		Consumable consumable = experStack.getOrDefault(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD);
		if (props != null && consumable.canConsume(player, experStack.copy())) {
			props.onConsume(level, player, experStack.copy(), consumable);
			consumable.onConsume(level, player, experStack.copy());
			int i = state.getValue(BITES_TAKEN);

			if (i < 7) {
				level.setBlock(pos, state.setValue(BITES_TAKEN, i + 1), Block.UPDATE_ALL);
			} else {
				level.removeBlock(pos, false);
			}

			if (player instanceof ServerPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, experStack);
				player.awardStat(Stats.ITEM_USED.get(experStack.getItem()));
				player.awardStat(TFStats.E115_SLICES_EATEN.get());

			}

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
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
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return directionToNeighbor == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES_TAKEN, REGENERATE);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return (8 - state.getValue(BITES_TAKEN)) + (state.getValue(REGENERATE) ? 7 : 0);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(REGENERATE);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return state.getValue(REGENERATE) ? 15 - (state.getValue(BITES_TAKEN) * 2) : 0;
	}
}
