package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.BiConsumer;

public class LargeWinterTreeFeature extends TFTreeFeature<TFTreeFeatureConfig> {

	public LargeWinterTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config) {
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
		if (!SnowTreeFeature.validTreePos(world, pos)) {
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

	private void makeLeaves(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			FeaturePlacers.placeCircleEven(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, pos.above(offGround + treeHeight - dy), radius, config.leavesProvider);
			this.makePineBranches(world, trunkPlacer, random, pos.above(offGround + treeHeight - dy), radius, config);
		}
	}

	private void makePineBranches(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int radius, TFTreeFeatureConfig config) {
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

	private void placeLogAt(BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, Direction.Axis axis, TFTreeFeatureConfig config) {
		trunkPlacer.accept(pos, config.trunkProvider.getState(rand, pos).setValue(RotatedPillarBlock.AXIS, axis));
	}

	private int leafRadius(int treeHeight, int dy, int functionType) {
		return switch (functionType) {
			case 1 -> (int) (4F * dy / treeHeight + (0.75F * dy % 3));
			case 99 -> (treeHeight - (dy / 2) - 1) % 4; // bad
			default -> (dy - 1) % 4;
		};
	}

	private void buildTrunk(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		for (int dy = 0; dy < treeHeight; dy++) {
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config.trunkProvider) && dy == 0)
				world.setBlock(pos.offset(0, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config.trunkProvider) && dy == 0)
				world.setBlock(pos.offset(0, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config.trunkProvider) && dy == 0)
				world.setBlock(pos.offset(1, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config.trunkProvider) && dy == 0)
				world.setBlock(pos.offset(1, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		}
	}
}
