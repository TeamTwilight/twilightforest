package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.BrazierBlockEntity;
import twilightforest.enums.BrazierLight;
import twilightforest.init.TFBlockEntities;

public class BrazierBlock extends BaseEntityBlock {

	public static final MapCodec<BrazierBlock> CODEC = simpleCodec(BrazierBlock::new);
	private static final VoxelShape UPPER_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private static final VoxelShape LOWER_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final EnumProperty<BrazierLight> LIGHT = EnumProperty.create("light", BrazierLight.class);
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public BrazierBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHT, BrazierLight.OFF).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			return new BrazierBlockEntity(pos, state);
		}
		return null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.BRAZIER.get(), BrazierBlockEntity::tick);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIGHT, HALF);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (directionToNeighbor.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (directionToNeighbor == Direction.UP)) {
			return half == DoubleBlockHalf.LOWER && directionToNeighbor == Direction.DOWN && !state.canSurvive(level, pos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
		} else {
			return neighborState.getBlock() instanceof BrazierBlock && neighborState.getValue(HALF) != half
				? neighborState.setValue(HALF, half)
				: Blocks.AIR.defaultBlockState();
		}
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide() && (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos))) {
			DoubleBlockHalf half = state.getValue(HALF);
			if (half == DoubleBlockHalf.UPPER) {
				BlockPos below = pos.below();
				BlockState blockstate = level.getBlockState(below);
				if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
					BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					level.setBlock(below, blockstate1, 35);
					level.levelEvent(player, 2001, below, Block.getId(blockstate));
				}
			}
		}

		return super.playerWillDestroy(level, pos, state, player);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		if (blockpos.getY() < level.getMaxY() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
			return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
		} else {
			return null;
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState blockstate = level.getBlockState(below);
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(level, below, Direction.UP) : blockstate.is(this);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			if (state.getValue(LIGHT) != BrazierLight.FULL && stack.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT)) {
				level.setBlock(pos, state.cycle(LIGHT), 11);
				level.getBlockState(pos.below()).cycle(LIGHT);
				if (stack.is(Items.FLINT_AND_STEEL)) {
					stack.hurtAndBreak(1, player, hand);
				} else {
					stack.consume(1, player);
				}
				level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS);
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
				return InteractionResult.SUCCESS;
			}

			if (state.getValue(LIGHT).isLit()) {
				if (FluidUtil.getFirstStackContained(stack).is(Fluids.WATER)) {
					ItemAccess access = ItemAccess.forPlayerInteraction(player, hand);
					ResourceHandler<FluidResource> handler = access.oneByOne().getCapability(Capabilities.Fluid.ITEM);
					try (var tx = Transaction.openRoot()) {
						if (handler != null && handler.extract(handler.getResource(0), FluidType.BUCKET_VOLUME, tx) == FluidType.BUCKET_VOLUME) {
							level.setBlock(pos, state.setValue(LIGHT, BrazierLight.OFF), 11);
							level.setBlock(pos.below(), level.getBlockState(pos.below()).setValue(LIGHT, BrazierLight.OFF), 11);
							level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);
							player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
							tx.commit();
							return InteractionResult.SUCCESS;
						} else {
							return InteractionResult.FAIL;
						}
					}
				}
			}
		}

		return super.useItemOn(stack, state, level, pos, player, hand, result);
	}
}
