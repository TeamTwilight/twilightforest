package twilightforest.block;

import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import twilightforest.TFConfig;

public class BlockTFLeaves3 extends LeavesBlock {

	protected BlockTFLeaves3(Properties props) {
		super(props);
		this.setDefaultState(this.stateContainer.getBaseState().with(DISTANCE, Integer.valueOf(1)).with(PERSISTENT, Boolean.valueOf(true)));
	}

	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return TFConfig.COMMON_CONFIG.PERFORMANCE.leavesLightOpacity.get();
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 30;
	}
}
