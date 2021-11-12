package twilightforest.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class UnripeTorchClusterBlock extends TrollRootBlock {
	private static final int RIPEN_THRESHHOLD = 6;

	protected UnripeTorchClusterBlock(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (world.getMaxLocalRawBrightness(pos) >= RIPEN_THRESHHOLD) {
			// ripen!
			world.setBlockAndUpdate(pos, TFBlocks.TROLLBER.get().defaultBlockState());
		}
	}
}
