package twilightforest.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

/**
 * ChunkPrimer that stores states directly.
 */
public class DirectChunkPrimer extends ChunkPrimer {

	private static final IBlockState DEFAULT_STATE = Blocks.AIR.getDefaultState();

	private final IBlockState[] states = new IBlockState[65536];

	@Override
	public IBlockState getBlockState(int x, int y, int z) {
		IBlockState state = states[getBlockIndex(x, y, z)];
		return state == null ? DEFAULT_STATE : state;
	}

	@Override
	public void setBlockState(int x, int y, int z, IBlockState state) {
		states[getBlockIndex(x, y, z)] = state;
	}

	private static int getBlockIndex(int x, int y, int z) {
		return x << 12 | z << 8 | y;
	}
}
