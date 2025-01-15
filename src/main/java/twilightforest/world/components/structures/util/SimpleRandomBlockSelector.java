package twilightforest.world.components.structures.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.util.WorldUtil;

import java.util.List;

public class SimpleRandomBlockSelector extends StructurePiece.BlockSelector {
	protected final List<Pair<BlockState, Float>> blockStatesAndWeights;
	protected final List<BlockState> blockStates;
	public SimpleRandomBlockSelector(List<Pair<BlockState, Float>> blockStatesAndWeights) {
		this.blockStatesAndWeights = blockStatesAndWeights;
		this.blockStates = blockStatesAndWeights.stream().map(Pair::getFirst).toList();
	}

	@Override
	public void next(RandomSource random, int x, int y, int z, boolean wall) {
		next(random, wall);
	}

	public void next(RandomSource random, boolean wall) {
		next = wall ? WorldUtil.getRandomElementWithWeights(blockStatesAndWeights, random) : Blocks.AIR.defaultBlockState();
	}

	public List<BlockState> getStates() {
		return blockStates;
	}
}
