package twilightforest.block;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.item.TFItems;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFBlockEntities;

public class KeepsakeCasketBlock extends BaseEntityBlock implements BlockLoggingEnum.IMultiLoggable {

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

	protected KeepsakeCasketBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).noOcclusion().requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(BREAKAGE, 0));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		// ENTITYBLOCK_ANIMATED uses only the BlockEntityRender while MODEL uses both the BER and baked model
		return state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		if(state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() != Blocks.AIR && state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) {
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
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof Container) {
				Containers.dropContents(worldIn, pos, (Container) tileentity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
		return 1000000000F;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		boolean flag = false;
		if(state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR || state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() != Fluids.EMPTY) {
			ItemStack stack = player.getItemInHand(handIn);
			if (!(stack.getItem() == TFItems.CHARM_OF_KEEPING_3.get())) {
				if (worldIn.isClientSide) {
					return InteractionResult.SUCCESS;
				} else {
					MenuProvider inamedcontainerprovider = this.getMenuProvider(state, worldIn, pos);

					if (inamedcontainerprovider != null) {
						player.openMenu(inamedcontainerprovider);
					}
					flag = true;
				}
			} else {
				if (stack.getItem() == TFItems.CHARM_OF_KEEPING_3.get() && state.getValue(BREAKAGE) > 0) {
					if (!player.isCreative()) stack.shrink(1);
					worldIn.setBlockAndUpdate(pos, state.setValue(BREAKAGE, state.getValue(BREAKAGE) - 1));
					worldIn.playSound(null, pos, TFSounds.CASKET_REPAIR, SoundSource.BLOCKS, 0.5F, worldIn.random.nextFloat() * 0.1F + 0.9F);
					flag = true;
				}
			}
		}
		return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
	}

	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
		if (!worldIn.isClientSide && !player.isCreative() && worldIn.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof KeepsakeCasketBlockEntity) {
				KeepsakeCasketBlockEntity casket = (KeepsakeCasketBlockEntity) tile;
				ItemStack stack = new ItemStack(this);
				String nameCheck = new TextComponent(casket.name + "'s " + casket.getDisplayName()).getString();
				ItemEntity itementity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
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
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), blockstack);
						item.setDefaultPickUpDelay();
						worldIn.addFreshEntity(item);
					}
				}
				itementity.setDefaultPickUpDelay();
				worldIn.addFreshEntity(itementity);
			}
		}
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (nbt.contains("BlockStateTag")) {
			CompoundTag damageNbt = nbt.getCompound("BlockStateTag");
			if (damageNbt.contains("damage")) {
				worldIn.setBlock(pos, state.setValue(BREAKAGE, damageNbt.getInt("damage")), 2);
			}
		}
		if (stack.hasCustomHoverName()) {
			BlockEntity tileentity = worldIn.getBlockEntity(pos);
			if (tileentity instanceof KeepsakeCasketBlockEntity) {
				((KeepsakeCasketBlockEntity) tileentity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		reactWithNeighbors(worldIn, pos, state);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
	}

	//[VanillaCopy] of FlowingFluidBlock.reactWithNeighbors, adapted for blockstates
	private void reactWithNeighbors(Level worldIn, BlockPos pos, BlockState state) {
		if (state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA) {
			boolean flag = worldIn.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);

			for(Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.relative(direction);
					if (worldIn.getFluidState(blockpos).is(FluidTags.WATER)) {
						worldIn.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.OBSIDIAN));
						worldIn.levelEvent(1501, pos, 0);
					}

					if (flag && worldIn.getBlockState(blockpos).is(Blocks.BLUE_ICE)) {
						worldIn.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.BASALT));
						worldIn.levelEvent(1501, pos, 0);
					}
				}
			}
		} else if (state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.WATER) {
			for(Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.relative(direction);
					if (worldIn.getFluidState(blockpos).is(FluidTags.LAVA)) {
						worldIn.setBlockAndUpdate(pos, state.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.STONE));
						worldIn.levelEvent(1501, pos, 0);
					}
				}
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
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

	//[VanillCopy] of ChestBlock.getLidRotationCallback, uses TileEntityKeepsakeCasket instead
	@OnlyIn(Dist.CLIENT)
	public static DoubleBlockCombiner.Combiner<KeepsakeCasketBlockEntity, Float2FloatFunction> getLidRotationCallback(final LidBlockEntity lid) {
		return new DoubleBlockCombiner.Combiner<KeepsakeCasketBlockEntity, Float2FloatFunction>() {
			public Float2FloatFunction acceptDouble(KeepsakeCasketBlockEntity p_225539_1_, KeepsakeCasketBlockEntity p_225539_2_) {
				return (angle) -> {
					return Math.max(p_225539_1_.getOpenNess(angle), p_225539_2_.getOpenNess(angle));
				};
			}

			public Float2FloatFunction acceptSingle(KeepsakeCasketBlockEntity p_225538_1_) {
				return p_225538_1_::getOpenNess;
			}

			public Float2FloatFunction acceptNone() {
				return lid::getOpenNess;
			}
		};
	}

	//FIXME
	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.empty();
	}
}
