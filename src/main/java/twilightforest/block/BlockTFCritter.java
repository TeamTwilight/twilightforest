package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockTFCritter extends DirectionalBlock {
	private final float WIDTH = getWidth();

	private final VoxelShape DOWN_BB  = VoxelShapes.create(new AxisAlignedBB(0.5F -WIDTH, 1.0F -WIDTH * 2.0F, 0.2F, 0.5F +WIDTH, 1.0F, 0.8F));
	private final VoxelShape UP_BB    = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.0F, 0.2F, 0.5F + WIDTH, WIDTH * 2.0F, 0.8F));
	private final VoxelShape NORTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 1.0F - WIDTH * 2.0F, 0.5F + WIDTH, 0.8F, 1.0F));
	private final VoxelShape SOUTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 0.0F, 0.5F + WIDTH, 0.8F, WIDTH * 2.0F));
	private final VoxelShape WEST_BB  = VoxelShapes.create(new AxisAlignedBB(1.0F - WIDTH * 2.0F, 0.2F, 0.5F - WIDTH, 1.0F, 0.8F, 0.5F + WIDTH));
	private final VoxelShape EAST_BB  = VoxelShapes.create(new AxisAlignedBB(0.0F, 0.2F, 0.5F - WIDTH, WIDTH * 2.0F, 0.8F, 0.5F + WIDTH));

	protected BlockTFCritter(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));
	}

	public float getWidth() {
		return 0.15F;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
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

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction clicked = context.getFace();
		BlockState state = getDefaultState().with(FACING, clicked);

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
		return restingOn.isSolidSide(world, restingPos, facing);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	public abstract ItemStack getSquishResult(); // oh no!

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
}
