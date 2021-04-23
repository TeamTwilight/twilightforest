package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.projectile.EntityTFMoonwormShot;

import javax.annotation.Nullable;
import javax.swing.*;

public abstract class BlockTFCritter extends DirectionalBlock implements IWaterLoggable {
	private final float WIDTH = getWidth();
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private final VoxelShape DOWN_BB  = VoxelShapes.create(new AxisAlignedBB(0.5F -WIDTH, 1.0F -WIDTH * 2.0F, 0.2F, 0.5F +WIDTH, 1.0F, 0.8F));
	private final VoxelShape UP_BB    = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.0F, 0.2F, 0.5F + WIDTH, WIDTH * 2.0F, 0.8F));
	private final VoxelShape NORTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 1.0F - WIDTH * 2.0F, 0.5F + WIDTH, 0.8F, 1.0F));
	private final VoxelShape SOUTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 0.0F, 0.5F + WIDTH, 0.8F, WIDTH * 2.0F));
	private final VoxelShape WEST_BB  = VoxelShapes.create(new AxisAlignedBB(1.0F - WIDTH * 2.0F, 0.2F, 0.5F - WIDTH, 1.0F, 0.8F, 0.5F + WIDTH));
	private final VoxelShape EAST_BB  = VoxelShapes.create(new AxisAlignedBB(0.0F, 0.2F, 0.5F - WIDTH, WIDTH * 2.0F, 0.8F, 0.5F + WIDTH));

	protected BlockTFCritter(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, Boolean.valueOf(false)));
	}

	public float getWidth() {
		return 0.15F;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
			case DOWN:
				return DOWN_BB;
			case UP:
			default:
				return UP_BB;
			case NORTH:
				return NORTH_BB;
			case SOUTH:
				return SOUTH_BB;
			case WEST:
				return WEST_BB;
			case EAST:
				return EAST_BB;
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction clicked = context.getFace();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		BlockState state = getDefaultState().with(FACING, clicked).with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));

		if (isValidPosition(state, context.getWorld(), context.getPos())) {
			return state;
		}

		for (Direction dir : context.getNearestLookingDirections()) {
			state = getDefaultState().with(FACING, dir.getOpposite());
			if (isValidPosition(state, context.getWorld(), context.getPos())) {
				return state;
			}
		}
		return null;
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (!isValidPosition(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return super.updatePostPlacement(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction facing = state.get(DirectionalBlock.FACING);
		BlockPos restingPos = pos.offset(facing.getOpposite());
		BlockState restingOn = world.getBlockState(restingPos);
		return restingOn.isSolidSide(world, restingPos, facing) || hasEnoughSolidSide(world, pos, facing);
	}

	public abstract ItemStack getSquishResult(); // oh no!

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (newState.getBlock() instanceof AnvilBlock) {
			worldIn.playSound(null, pos, TFSounds.BUG_SQUISH, SoundCategory.BLOCKS, 1, 1);
			ItemEntity squish = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ());
			squish.entityDropItem(this.getSquishResult().getStack());
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(handIn);
		if(stack.getItem() == Items.GLASS_BOTTLE) {
			if(this == TFBlocks.firefly.get()) {
				if(!player.isCreative()) stack.shrink(1);
				player.inventory.addItemStackToInventory(new ItemStack(TFBlocks.firefly_jar.get()));
				worldIn.setBlockState(pos,state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
				return ActionResultType.SUCCESS;
			} else if(this == TFBlocks.cicada.get()) {
				if(!player.isCreative()) stack.shrink(1);
				player.inventory.addItemStackToInventory(new ItemStack(TFBlocks.cicada_jar.get()));
				worldIn.setBlockState(pos,state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof ProjectileEntity && !(entityIn instanceof EntityTFMoonwormShot)) {
			worldIn.setBlockState(pos, state.get(WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
			ItemEntity squish = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ());
			squish.entityDropItem(this.getSquishResult().getStack());
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, WATERLOGGED);
	}

}
