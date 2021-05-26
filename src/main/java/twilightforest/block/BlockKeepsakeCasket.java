package twilightforest.block;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityKeepsakeCasket;

import javax.annotation.Nullable;
import java.util.List;

public class BlockKeepsakeCasket extends ContainerBlock implements BlockLoggingEnum.IMultiLoggable {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final IntegerProperty BREAKAGE = IntegerProperty.create("damage", 0, 2);

	private static final VoxelShape BOTTOM_X = Block.makeCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 6.0D, 15.0D);
	private static final VoxelShape TOP_X = Block.makeCuboidShape(1.0D, 6.0D, 0.0D, 15.0D, 14.0D, 16.0D);
	private static final VoxelShape BOTTOM_Z = Block.makeCuboidShape(1.0D, 0.0D, 2.0D, 15.0D, 6.0D, 14.0D);
	private static final VoxelShape TOP_Z = Block.makeCuboidShape(0.0D, 6.0D, 1.0D, 16.0D, 14.0D, 15.0D);
	private static final VoxelShape CASKET_X = VoxelShapes.or(BOTTOM_X, TOP_X);
	private static final VoxelShape CASKET_Z = VoxelShapes.or(BOTTOM_Z, TOP_Z);

	private static final VoxelShape SOLID = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	private static final VoxelShape TOPPER_X = Block.makeCuboidShape(1.0D, 12.0D, 0.0D, 15.0D, 14.0D, 16.0D);
	private static final VoxelShape TOPPER_Z = Block.makeCuboidShape(0.0D, 12.0D, 1.0D, 16.0D, 14.0D, 15.0D);
	private static final VoxelShape SOLID_X = VoxelShapes.or(SOLID, TOPPER_X);
	private static final VoxelShape SOLID_Z = VoxelShapes.or(SOLID, TOPPER_Z);

	protected BlockKeepsakeCasket() {
		super(Block.Properties.create(Material.IRON, MaterialColor.BLACK).notSolid().setRequiresTool().hardnessAndResistance(50.0F, 1200.0F).sound(SoundType.NETHERITE));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(BREAKAGE, 0));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("twilightforest.misc.wip0"));
		tooltip.add(new TranslationTextComponent("twilightforest.misc.wip1"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return (state.get(BlockLoggingEnum.MULTILOGGED).getBlock() != Blocks.AIR && state.get(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) ? BlockRenderType.MODEL : BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		Direction direction = state.get(BlockStateProperties.HORIZONTAL_FACING);
		if(state.get(BlockLoggingEnum.MULTILOGGED).getBlock() != Blocks.AIR && state.get(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) {
			return direction.getAxis() == Direction.Axis.X ? SOLID_X : SOLID_Z;
		} else {
			return direction.getAxis() == Direction.Axis.X ? CASKET_X : CASKET_Z;
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityKeepsakeCasket();
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.isIn(newState.getBlock())) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
		return 1000000000F;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		boolean flag = false;
		if(state.get(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR || state.get(BlockLoggingEnum.MULTILOGGED).getFluid() != Fluids.EMPTY) {
			ItemStack stack = player.getHeldItem(handIn);
			if (!(stack.getItem() == TFItems.charm_of_keeping_3.get())) {
				if (worldIn.isRemote) {
					return ActionResultType.SUCCESS;
				} else {
					INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);

					if (inamedcontainerprovider != null) {
						player.openContainer(inamedcontainerprovider);
					}
					flag = true;
				}
			} else {
				if (stack.getItem() == TFItems.charm_of_keeping_3.get() && state.get(BREAKAGE) > 0) {
					if (!player.isCreative()) stack.shrink(1);
					worldIn.setBlockState(pos, state.with(BREAKAGE, state.get(BREAKAGE) - 1));
					worldIn.playSound(null, pos, TFSounds.CASKET_REPAIR, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
					flag = true;
				}
			}
		}
		return flag ? ActionResultType.CONSUME : ActionResultType.PASS;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote && !player.isCreative() && worldIn.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileEntityKeepsakeCasket) {
				TileEntityKeepsakeCasket casket = (TileEntityKeepsakeCasket) tile;
				ItemStack stack = new ItemStack(this);
				String nameCheck = new StringTextComponent(casket.name + "'s " + casket.getDisplayName()).getString();
				ItemEntity itementity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
				CompoundNBT nbt = new CompoundNBT();
				nbt.putInt("damage", state.get(BREAKAGE));
				stack.setTagInfo("BlockStateTag", nbt);
				if (casket.hasCustomName()) {
					if (nameCheck.equals(casket.getCustomName().getString()))
						itementity.setCustomName(casket.getDisplayName());
					else itementity.setCustomName(casket.getCustomName());
				}
				if (state.get(BlockLoggingEnum.MULTILOGGED).getFluid() == Fluids.EMPTY) {
					Block block = state.get(BlockLoggingEnum.MULTILOGGED).getBlock();
					if (block != Blocks.AIR) {
						ItemStack blockstack = new ItemStack(block);
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), blockstack);
						item.setDefaultPickupDelay();
						worldIn.addEntity(item);
					}
				}
				itementity.setDefaultPickupDelay();
				worldIn.addEntity(itementity);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains("BlockStateTag")) {
			CompoundNBT damageNbt = nbt.getCompound("BlockStateTag");
			if (damageNbt.contains("damage")) {
				worldIn.setBlockState(pos, state.with(BREAKAGE, damageNbt.getInt("damage")), 2);
			}
		}
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityKeepsakeCasket) {
				((TileEntityKeepsakeCasket) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		reactWithNeighbors(worldIn, pos, state);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
	}

	//[VanillaCopy] of FlowingFluidBlock.reactWithNeighbors, adapted for blockstates
	private void reactWithNeighbors(World worldIn, BlockPos pos, BlockState state) {
		if (state.get(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA) {
			boolean flag = worldIn.getBlockState(pos.down()).isIn(Blocks.SOUL_SOIL);

			for(Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.offset(direction);
					if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
						worldIn.setBlockState(pos, state.with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.OBSIDIAN));
						worldIn.playEvent(1501, pos, 0);
					}

					if (flag && worldIn.getBlockState(blockpos).isIn(Blocks.BLUE_ICE)) {
						worldIn.setBlockState(pos, state.with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.BASALT));
						worldIn.playEvent(1501, pos, 0);
					}
				}
			}
		} else if (state.get(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.WATER) {
			for(Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockpos = pos.offset(direction);
					if (worldIn.getFluidState(blockpos).isTagged(FluidTags.LAVA)) {
						worldIn.setBlockState(pos, state.with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.STONE));
						worldIn.playEvent(1501, pos, 0);
					}
				}
			}
		}
	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockLoggingEnum.MULTILOGGED, FACING, BREAKAGE);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(context.getWorld().getFluidState(context.getPos()).getFluid()));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(BlockLoggingEnum.MULTILOGGED).getFluid().getDefaultState();
	}

	//[VanillCopy] of ChestBlock.getLidRotationCallback, uses TileEntityKeepsakeCasket instead
	@OnlyIn(Dist.CLIENT)
	public static TileEntityMerger.ICallback<TileEntityKeepsakeCasket, Float2FloatFunction> getLidRotationCallback(final IChestLid lid) {
		return new TileEntityMerger.ICallback<TileEntityKeepsakeCasket, Float2FloatFunction>() {
			public Float2FloatFunction func_225539_a_(TileEntityKeepsakeCasket p_225539_1_, TileEntityKeepsakeCasket p_225539_2_) {
				return (angle) -> {
					return Math.max(p_225539_1_.getLidAngle(angle), p_225539_2_.getLidAngle(angle));
				};
			}

			public Float2FloatFunction func_225538_a_(TileEntityKeepsakeCasket p_225538_1_) {
				return p_225538_1_::getLidAngle;
			}

			public Float2FloatFunction func_225537_b_() {
				return lid::getLidAngle;
			}
		};
	}
}
