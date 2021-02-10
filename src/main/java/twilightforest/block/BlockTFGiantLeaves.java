package twilightforest.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import twilightforest.TFConfig;

public class BlockTFGiantLeaves extends BlockTFGiantBlock {

	public BlockTFGiantLeaves(Properties props) {
		super(props);
	}

	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return TFConfig.COMMON_CONFIG.PERFORMANCE.leavesLightOpacity.get();
	}
}
