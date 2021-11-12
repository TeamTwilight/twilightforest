package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;
import twilightforest.world.components.feature.trees.growers.SnowTreePlacer;

import java.util.Random;
import java.util.function.BiConsumer;

public class TFGenLargeWinter extends TFTreeGenerator<TFTreeFeatureConfig> {

	public TFGenLargeWinter(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, Random random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, BiConsumer<BlockPos, BlockState> decorationPlacer, TFTreeFeatureConfig config) {
		// determine a height
		int treeHeight = 35;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(10);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(10);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		// check if we're on a valid block
		if (!SnowTreePlacer.isBlockUnderValid(world, pos.below())) {
			return false;
		}

		//okay build a tree!  Go up to the height
		buildTrunk(world, trunkPlacer, random, pos, treeHeight, config);

		// make leaves
		makeLeaves(world, trunkPlacer, leavesPlacer, random, pos, treeHeight, config);

		// roots!
		int numRoots = 4 + random.nextInt(3);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, config.rootsProvider);
		}

		return true;
	}

	private void makeLeaves(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, Random random, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			FeaturePlacers.placeCircleEven(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, pos.above(offGround + treeHeight - dy), radius, config.leavesProvider);
			this.makePineBranches(world, trunkPlacer, random, pos.above(offGround + treeHeight - dy), radius, config);
		}
	}

	private void makePineBranches(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, Random rand, BlockPos pos, int radius, TFTreeFeatureConfig config) {
		int branchLength = radius > 4 ? radius - 1 : radius - 2;

		switch (pos.getY() % 2) {
			case 0:
				// branches
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(trunkPlacer, rand, pos.offset(-i, 0, 0), Direction.Axis.X, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(0, 0, i + 1), Direction.Axis.Z, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(i + 1, 0, 1), Direction.Axis.X, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(1, 0, -i), Direction.Axis.Z, config);
				}
				break;
			case 1:
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(trunkPlacer, rand, pos.offset(-1, 0, 1), Direction.Axis.X, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(1, 0, i + 1), Direction.Axis.Z, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(i + 1, 0, 0), Direction.Axis.X, config);
					this.placeLogAt(trunkPlacer, rand, pos.offset(0, 0, -i), Direction.Axis.Z, config);
				}
				break;
		}
	}

	private void placeLogAt(BiConsumer<BlockPos, BlockState> trunkPlacer, Random rand, BlockPos pos, Direction.Axis axis, TFTreeFeatureConfig config) {
		trunkPlacer.accept(pos, config.trunkProvider.getState(rand, pos).setValue(RotatedPillarBlock.AXIS, axis));
	}

	private int leafRadius(int treeHeight, int dy, int functionType) {
		switch (functionType) {
			case 0:
			default:
				return (dy - 1) % 4;
			case 1:
				return (int) (4F * dy / treeHeight + (0.75F * dy % 3));
			case 99:
				return (treeHeight - (dy / 2) - 1) % 4; // bad
		}
	}

	private void buildTrunk(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, Random rand, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		for (int dy = 0; dy < treeHeight; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config.trunkProvider);
		}
	}
}
