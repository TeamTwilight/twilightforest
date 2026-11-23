package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TreeConfig;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */
public interface CanopyTreeFeature extends TFTreeFeature {

	@Override
	default boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		// Determine a height
		List<BlockPos> leaves = Lists.newArrayList();
		int treeHeight = config().minHeight();
		if (random.nextInt(config().chanceAddFiveFirst()) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(config().chanceAddFiveSecond()) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.CANOPY_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		leaves.clear();

		// Okay build a tree!  Go up to the height
		buildBranch(world, leaves, pos, trunkPlacer, 0, treeHeight, 0, 0, true, random);

		// Make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, leaves, pos, trunkPlacer, treeHeight - 10 + b, 9, 0.3 * b + offset, 0.2, false, random);
		}

		// Add the actual leaves
		if (config().hasLeaves())
			for (BlockPos leafPos : leaves) {
				makeLeafBlob(world, leavesPlacer, random, leafPos, config());
			}

		// Root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), config().trunkProvider());
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decorationPlacer, random, pos.below(), config().rootsProvider());
		}

		// Roots!
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, config().rootsProvider());
		}

		return true;
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	default void buildBranch(WorldGenLevel world, List<BlockPos> leaves, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, RandomSource treeRNG) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// Only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {

			if (trunk) {
				FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, config().trunkProvider(), treeRNG);
			} else {
				FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, config().branchProvider());
			}

			// Seems to help lighting to place this firefly now
			if (trunk) {
				// Add a firefly (torch) to the trunk
				FeaturePlacers.addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
			}

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.east(), config().branchProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.west(), config().branchProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.south(), config().branchProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.north(), config().branchProvider());

			// Save leaf position for later
			leaves.add(dest);
		}
	}

	static void makeLeafBlob(WorldGenLevel world, BiConsumer<BlockPos, BlockState> leafPlacer, RandomSource random, BlockPos leafPos, TreeConfig config) {
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos.below(), 3, config.leavesProvider());
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos, 4, config.leavesProvider());
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, random, leafPos.above(), 2, config.leavesProvider());
	}

	static void makeRoots(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, RootPlacer decoPlacer, RandomSource random, BlockPos pos, TreeConfig config) {
		// Root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), config.trunkProvider());
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, pos.below(), config.rootsProvider());
		}

		// Roots!
		int numRoots = 1 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decoPlacer, random, pos, offset, b, config.rootsProvider());
		}
	}
}
