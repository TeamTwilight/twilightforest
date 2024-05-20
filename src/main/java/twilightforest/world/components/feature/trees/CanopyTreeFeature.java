package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.util.FeatureLogic;
import twilightforest.util.FeaturePlacers;
import twilightforest.util.FeatureUtil;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */
public class CanopyTreeFeature extends TFTreeFeature<TFTreeFeatureConfig> {

	public CanopyTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config) {
		// determine a height
		List<BlockPos> leaves = Lists.newArrayList();
		int treeHeight = config.minHeight;
		if (random.nextInt(config.chanceAddFiveFirst) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(config.chanceAddFiveSecond) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		BlockState state = world.getBlockState(pos.below());
		if (!state.getBlock().canSustainPlant(state, world, pos.below(), Direction.UP, TFBlocks.CANOPY_SAPLING.get())) {
			return false;
		}

		leaves.clear();

		//okay build a tree!  Go up to the height
		buildBranch(world, leaves, pos, trunkPlacer, 0, treeHeight, 0, 0, true, random, config);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, leaves, pos, trunkPlacer, treeHeight - 10 + b, 9, 0.3 * b + offset, 0.2, false, random, config);
		}

		// add the actual leaves
		if (config.hasLeaves)
			for (BlockPos leafPos : leaves) {
				makeLeafBlob(world, leavesPlacer, random, leafPos, config);
			}

		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), config.trunkProvider);
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decorationPlacer, random, pos.below(), config.rootsProvider);
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, config.rootsProvider);
		}

		return true;
	}

	private void makeLeafBlob(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> leafPlacer, RandomSource random, BlockPos leafPos, TFTreeFeatureConfig config) {
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos.below(), 3, config.leavesProvider);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos, 4, config.leavesProvider);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos.above(), 2, config.leavesProvider);
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	void buildBranch(LevelAccessor world, List<BlockPos> leaves, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, RandomSource treeRNG, TFTreeFeatureConfig config) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {

			if (trunk) {
				FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, config.trunkProvider, treeRNG);
			} else {
				FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, config.branchProvider);
			}

			// seems to help lighting to place this firefly now
			if (trunk) {
				// add a firefly (torch) to the trunk
				FeaturePlacers.addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
			}

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.east(), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.west(), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.south(), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.north(), config.branchProvider);
//			setBlockAndNotifyAdequately(world, dest.east(), branchState);
//			setBlockAndNotifyAdequately(world, dest.west(), branchState);
//			setBlockAndNotifyAdequately(world, dest.south(), branchState);
//			setBlockAndNotifyAdequately(world, dest.north(), branchState);

			// save leaf position for later
			leaves.add(dest);
		}
	}

	protected static void makeRoots(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, RootPlacer decoPlacer, RandomSource random, BlockPos pos, TFTreeFeatureConfig config) {
		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), config.trunkProvider);
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, pos.below(), config.rootsProvider);
		}

		// roots!
		int numRoots = 1 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decoPlacer, random, pos, offset, b, config.rootsProvider);
		}
	}
}
