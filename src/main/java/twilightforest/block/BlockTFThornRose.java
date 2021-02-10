package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class BlockTFThornRose extends Block {

	private static final float RADIUS = 0.4F;
	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.5F -RADIUS, 0.5F -RADIUS, 0.5F -RADIUS, 0.5F +RADIUS, .5F +RADIUS, 0.5F +RADIUS));

	protected BlockTFThornRose(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		for (Direction d : Direction.values()) {
			if (world.getBlockState(pos.offset(d)).getBlock() instanceof BlockTFThorns) {
				return true;
			}
		}
		return false;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction dirToNeighbor, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return !isValidPosition(state, world, pos) ? Blocks.AIR.getDefaultState() : state;
	}
}
