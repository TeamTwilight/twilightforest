package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

//TODO: Evaluate placement logic. I feel like something drastic happened
public abstract class BlockTFCritter extends Block {

	private final float WIDTH = getWidth();

	private final VoxelShape DOWN_BB  = VoxelShapes.create(new AxisAlignedBB(0.5F -WIDTH, 1.0F -WIDTH * 2.0F, 0.2F, 0.5F +WIDTH, 1.0F, 0.8F));
	private final VoxelShape UP_BB    = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.0F, 0.2F, 0.5F + WIDTH, WIDTH * 2.0F, 0.8F));
	private final VoxelShape NORTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 1.0F - WIDTH * 2.0F, 0.5F + WIDTH, 0.8F, 1.0F));
	private final VoxelShape SOUTH_BB = VoxelShapes.create(new AxisAlignedBB(0.5F - WIDTH, 0.2F, 0.0F, 0.5F + WIDTH, 0.8F, WIDTH * 2.0F));
	private final VoxelShape WEST_BB  = VoxelShapes.create(new AxisAlignedBB(1.0F - WIDTH * 2.0F, 0.2F, 0.5F - WIDTH, 1.0F, 0.8F, 0.5F + WIDTH));
	private final VoxelShape EAST_BB  = VoxelShapes.create(new AxisAlignedBB(0.0F, 0.2F, 0.5F - WIDTH, WIDTH * 2.0F, 0.8F, 0.5F + WIDTH));

	protected BlockTFCritter(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(DirectionalBlock.FACING, Direction.UP));
	}

	public float getWidth() {
		return 0.15F;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(DirectionalBlock.FACING);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(DirectionalBlock.FACING)) {
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

//	@Override
//	@Deprecated
//	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
//		return BlockFaceShape.UNDEFINED;
//	}

//	@Override
//	public boolean canPlaceBlockOnSide(World world, BlockPos pos, Direction side) {
//		return canPlaceAt(world, pos.offset(side.getOpposite()), side);
//	}
//
//	@Override
//	public boolean canPlaceBlockAt(World world, BlockPos pos) {
//		for (Direction side : Direction.values()) {
//			if (canPlaceAt(world, pos.offset(side.getOpposite()), side)) {
//				return true;
//			}
//		}
//		return false;
//	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = getDefaultState();

		//note: sideHit is Direction
		if (canPlaceAt(context.getWorld(), context.getPos().offset(context.getNearestLookingDirection().getOpposite()), context.getNearestLookingDirection())) {
			state = state.with(DirectionalBlock.FACING, context.getNearestLookingDirection());
		}

		return state;
	}

//	@Override
//	public void onBlockAdded(World world, BlockPos pos, BlockState state) {
//		world.scheduleUpdate(pos, this, 1);
//	}

	@Override
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		checkAndDrop(world, pos, state);
	}

	protected boolean checkAndDrop(World world, BlockPos pos, BlockState state) {
		Direction facing = state.get(DirectionalBlock.FACING);
		if (!canPlaceAt(world, pos.offset(facing.getOpposite()), facing)) {
			world.destroyBlock(pos, true);
			return false;
		}
		return true;
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		checkAndDrop(world, pos, state);
	}

//	protected boolean canPlaceAt(World world, BlockPos pos, Direction facing) {
//		BlockState state = world.getBlockState(pos);
//		return state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID
//				&& (!isExceptBlockForAttachWithPiston(state.getBlock())
//				|| state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.CACTUS);
//	}

	public static boolean canPlaceAt(IWorldReader world, BlockPos pos, Direction facing) {
		BlockPos blockpos = pos.offset(facing);
		return world.getBlockState(blockpos).isSideSolidFullSquare(world, blockpos, facing.getOpposite());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	public abstract ItemStack getSquishResult(); // oh no!
}
