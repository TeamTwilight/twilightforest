package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockTFHugeWaterLily extends LilyPadBlock {

	private static final VoxelShape AABB = makeCuboidShape(1.6, 1.6, 1.6, 14.4, 14.4, 14.4);

	protected BlockTFHugeWaterLily(Properties props) {
		super(props);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}
}
