package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlockEntities;

public class SkullChestBlock extends BaseEntityBlock implements BlockLoggingEnum.IMultiLoggable {

	public static final DirectionProperty FACING = TFHorizontalBlock.FACING;
	public static final MapCodec<SkullChestBlock> CODEC = simpleCodec(SkullChestBlock::new);
	private static final VoxelShape BOTTOM_X = Block.box(2.0D, 0.0D, 1.0D, 14.0D, 6.0D, 15.0D);
	private static final VoxelShape TOP_X = Block.box(1.0D, 6.0D, 0.0D, 15.0D, 14.0D, 16.0D);
	private static final VoxelShape BOTTOM_Z = Block.box(1.0D, 0.0D, 2.0D, 15.0D, 6.0D, 14.0D);
	private static final VoxelShape TOP_Z = Block.box(0.0D, 6.0D, 1.0D, 16.0D, 14.0D, 15.0D);
	private static final VoxelShape CASKET_X = Shapes.or(BOTTOM_X, TOP_X);
	private static final VoxelShape CASKET_Z = Shapes.or(BOTTOM_Z, TOP_Z);

	private static final VoxelShape SOLID = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	private static final VoxelShape TOPPER_X = Block.box(1.0D, 12.0D, 0.0D, 15.0D, 14.0D, 16.0D);
	private static final VoxelShape TOPPER_Z = Block.box(0.0D, 12.0D, 1.0D, 16.0D, 14.0D, 15.0D);
	private static final VoxelShape SOLID_X = Shapes.or(SOLID, TOPPER_X);
	private static final VoxelShape SOLID_Z = Shapes.or(SOLID, TOPPER_Z);

	public SkullChestBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends SkullChestBlock> codec() {
		return CODEC;
	}

	@Override
	@SuppressWarnings("deprecation")
	public RenderShape getRenderShape(BlockState state) {
		// ENTITYBLOCK_ANIMATED uses only the BlockEntityRender while MODEL uses both the BER and baked model
		return state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		if (state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() != Blocks.AIR && state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) {
			return direction.getAxis() == Direction.Axis.X ? SOLID_X : SOLID_Z;
		} else {
			return direction.getAxis() == Direction.Axis.X ? CASKET_X : CASKET_Z;
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullChestBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.SKULL_CHEST.get(), SkullChestBlockEntity::tick);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tileentity = level.getBlockEntity(pos);
			if (tileentity instanceof Container) {
				Containers.dropContents(level, pos, (Container) tileentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter getter, BlockPos pos, Explosion explosion) {
		return Float.MAX_VALUE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR || state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() != Fluids.EMPTY) {
			if (level.isClientSide()) {
				return ItemInteractionResult.SUCCESS;
			} else {
				MenuProvider menuProvider = this.getMenuProvider(state, level, pos);

				if (menuProvider != null) {
					player.openMenu(menuProvider);
				}
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide() && !player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof SkullChestBlockEntity chest) {
				ItemStack stack = new ItemStack(this);
				ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
				this.modifyDrop(state, stack);
				if (chest.hasCustomName()) {
					if (chest.owner != null)
						itementity.setCustomName(chest.getDisplayName());
					else itementity.setCustomName(chest.getCustomName());
				}
				if (state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) {
					Block block = state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock();
					if (block != Blocks.AIR) {
						ItemStack blockstack = new ItemStack(block);
						ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), blockstack);
						item.setDefaultPickUpDelay();
						level.addFreshEntity(item);
					}
				}
				itementity.setDefaultPickUpDelay();
				level.addFreshEntity(itementity);
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	protected void modifyDrop(BlockState state, ItemStack stack) {
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		this.reactWithNeighbors(level, pos, state);
		super.neighborChanged(state, level, pos, block, fromPos, isMoving);
	}

	//[VanillaCopy] of FlowingFluidBlock.reactWithNeighbors, adapted for blockstates
	private void reactWithNeighbors(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA) {
			boolean flag = level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);

			for (Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.relative(direction);
					if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
						level.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.OBSIDIAN));
						level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
					}

					if (flag && level.getBlockState(blockpos).is(Blocks.BLUE_ICE)) {
						level.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.BASALT));
						level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
					}
				}
			}
		} else if (state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.WATER) {
			for (Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.relative(direction);
					if (level.getFluidState(blockpos).is(FluidTags.LAVA)) {
						level.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.STONE));
						level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
					}
				}
			}
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockLoggingEnum.MULTILOGGED, FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(context.getLevel().getFluidState(context.getClickedPos()).getType()));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid().defaultFluidState();
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return false;
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}
}
