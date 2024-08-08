package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

public class RootPlacer {
	private final BiConsumer<BlockPos, BlockState> rootPlacer;
	private final int rootPenetrability;
	public RootPlacer(BiConsumer<BlockPos, BlockState> placer, int penetrability) {
		rootPlacer = placer;
		rootPenetrability = penetrability;
	}

	public BiConsumer<BlockPos, BlockState> getPlacer() {
		return rootPlacer;
	}

	public int getRootPenetrability() {
		return rootPenetrability;
	}
}
