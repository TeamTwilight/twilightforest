package twilightforest.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.palette.UpgradeData;
import net.minecraft.world.chunk.ChunkPrimer;

/**
 * ChunkPrimer that stores states directly.
 */
public class DirectChunkPrimer extends ChunkPrimer {

	private static final BlockState DEFAULT_STATE = Blocks.AIR.getDefaultState();

	private final BlockState[] states = new BlockState[65536];

	public DirectChunkPrimer(ChunkPos pos) {
		super(pos, UpgradeData.EMPTY);
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		BlockState state = states[getBlockIndex(pos)];
		return state == null ? DEFAULT_STATE : state;
	}

	@Override
	public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
		states[getBlockIndex(pos)] = state;
		return state;
	}

	private static int getBlockIndex(BlockPos pos) {
		return pos.getX() << 12 | pos.getZ() << 8 | pos.getY();
	}
}
