package twilightforest.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTFUnripeTorchCluster extends BlockTFTrollRoot {
	private static final int RIPEN_THRESHHOLD = 6;

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);

		if (world.getLight(pos) >= RIPEN_THRESHHOLD) {
			// ripen!
			world.setBlockState(pos, TFBlocks.trollber.getDefaultState());
		}
	}
}
