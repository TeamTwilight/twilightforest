package twilightforest.block;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.Optional;

public class KeepsakeCasketBlock extends BaseEntityBlock implements BlockLoggingEnum.IMultiLoggable {

	public static final MapCodec<KeepsakeCasketBlock> CODEC = simpleCodec(KeepsakeCasketBlock::new);

	public static final DirectionProperty FACING = TFHorizontalBlock.FACING;
	public static final IntegerProperty BREAKAGE = IntegerProperty.create("damage", 0, 2);

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

	public KeepsakeCasketBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(BREAKAGE, 0));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new KeepsakeCasketBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.KEEPSAKE_CASKET.get(), KeepsakeCasketBlockEntity::tick);
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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		boolean flag = false;
		if (state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR || state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() != Fluids.EMPTY) {
			ItemStack stack = player.getItemInHand(hand);
			if (!(stack.getItem() == TFItems.CHARM_OF_KEEPING_3.get())) {
				if (level.isClientSide()) {
					return InteractionResult.SUCCESS;
				} else {
					MenuProvider inamedcontainerprovider = this.getMenuProvider(state, level, pos);

					if (inamedcontainerprovider != null) {
						player.openMenu(inamedcontainerprovider);
					}
					flag = true;
				}
			} else {
				if (stack.getItem() == TFItems.CHARM_OF_KEEPING_3.get() && state.getValue(BREAKAGE) > 0) {
					if (!player.isCreative()) stack.shrink(1);
					level.setBlockAndUpdate(pos, state.setValue(BREAKAGE, state.getValue(BREAKAGE) - 1));
					level.playSound(null, pos, TFSounds.CASKET_REPAIR.get(), SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
					flag = true;
				}
			}
		}
		return flag ? InteractionResult.sidedSuccess(level.isClientSide()) : InteractionResult.PASS;
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide() && !player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			BlockEntity tile = level.getBlockEntity(pos);
			if (tile instanceof KeepsakeCasketBlockEntity casket) {
				ItemStack stack = new ItemStack(this);
				String nameCheck = Component.literal(casket.name + "'s " + casket.getDisplayName()).getString();
				ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
				CompoundTag nbt = new CompoundTag();
				nbt.putInt("damage", state.getValue(BREAKAGE));
				stack.addTagElement("BlockStateTag", nbt);
				if (casket.hasCustomName()) {
					if (nameCheck.equals(casket.getCustomName().getString()))
						itementity.setCustomName(casket.getDisplayName());
					else itementity.setCustomName(casket.getCustomName());
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

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains("BlockStateTag")) {
			CompoundTag damageNbt = nbt.getCompound("BlockStateTag");
			if (damageNbt.contains("damage")) {
				level.setBlock(pos, state.setValue(BREAKAGE, damageNbt.getInt("damage")), 2);
			}
		}
		if (stack.hasCustomHoverName()) {
			BlockEntity tileentity = level.getBlockEntity(pos);
			if (tileentity instanceof KeepsakeCasketBlockEntity) {
				((KeepsakeCasketBlockEntity) tileentity).setCustomName(stack.getHoverName());
			}
		}
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
						level.levelEvent(1501, pos, 0);
					}

					if (flag && level.getBlockState(blockpos).is(Blocks.BLUE_ICE)) {
						level.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.BASALT));
						level.levelEvent(1501, pos, 0);
					}
				}
			}
		} else if (state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.WATER) {
			for (Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.relative(direction);
					if (level.getFluidState(blockpos).is(FluidTags.LAVA)) {
						level.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.STONE));
						level.levelEvent(1501, pos, 0);
					}
				}
			}
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockLoggingEnum.MULTILOGGED, FACING, BREAKAGE);
	}

	@Nullable
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

	@OnlyIn(Dist.CLIENT)
	public static DoubleBlockCombiner.Combiner<KeepsakeCasketBlockEntity, Float2FloatFunction> getLidRotationCallback(final LidBlockEntity lid) {
		return new DoubleBlockCombiner.Combiner<>() {
			public Float2FloatFunction acceptDouble(KeepsakeCasketBlockEntity casket, KeepsakeCasketBlockEntity oldCasket) {
				return (angle) -> Math.max(casket.getOpenNess(angle), oldCasket.getOpenNess(angle));
			}

			public Float2FloatFunction acceptSingle(KeepsakeCasketBlockEntity casket) {
				return casket::getOpenNess;
			}

			public Float2FloatFunction acceptNone() {
				return lid::getOpenNess;
			}
		};
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.empty();
	}
}
