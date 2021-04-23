package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class BlockTFTrollRoot extends Block {

	protected static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9));

	protected BlockTFTrollRoot(Properties props) {
		super(props);
	}

	public static boolean canPlaceRootBelow(IWorldReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		return block.isIn(BlockTags.BASE_STONE_OVERWORLD) || block == TFBlocks.trollvidr.get() || block == TFBlocks.trollber.get() || block == TFBlocks.unripe_trollber.get();
	}

	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return canPlaceRootBelow(world, pos.up());
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction dirToNeighbor, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (dirToNeighbor == Direction.UP) {
			return isValidPosition(state, world, pos) ? state : Blocks.AIR.getDefaultState();
		}
		return state;
	}
}
