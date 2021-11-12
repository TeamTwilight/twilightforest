package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.util.FeatureLogic;
import twilightforest.util.FeaturePlacers;
import twilightforest.util.FeatureUtil;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class TFGenCanopyOak extends TFGenCanopyTree {

	private final List<BlockPos> leaves = Lists.newArrayList();

	public TFGenCanopyOak(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, Random random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, BiConsumer<BlockPos, BlockState> decorationPlacer, TFTreeFeatureConfig config) {
		// determine a height
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

		// check if we're on dirt or grass
		BlockState state = world.getBlockState(pos.below());
		if (!state.getBlock().canSustainPlant(state, world, pos.below(), Direction.UP, config.getSapling(random, pos))) {
			return false;
		}

		this.leaves.clear();

		//okay build a tree!  Go up to the height
		buildTrunk(world, trunkPlacer, random, pos, treeHeight, config);

		// make 12 - 20 branches
		int numBranches = 12 + random.nextInt(9);
		float bangle = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			float btilt = 0.15F + (random.nextFloat() * 0.35F);
			buildBranch(world, pos, trunkPlacer, treeHeight - 10 + (b / 2), 5, bangle, btilt, false, random, config);

			bangle += (random.nextFloat() * 0.4F);
			if (bangle > 1.0F) {
				bangle -= 1.0F;
			}
		}

		// add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, leavesPlacer, random, leafPos, config);
		}

		TFGenCanopyOak.makeRoots(world, trunkPlacer, decorationPlacer, random, pos, config);
		TFGenCanopyOak.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east(), config);
		TFGenCanopyOak.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.south(), config);
		TFGenCanopyOak.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east().south(), config);

		return true;
	}

	private void makeLeafBlob(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> leafPlacer, Random rand, BlockPos leafPos, TFTreeFeatureConfig config) {
		FeaturePlacers.placeSpheroid(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos, 2.5f, 2.5f, config.leavesProvider);
	}

	private static void makeRoots(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> decoPlacer, Random random, BlockPos pos, TFTreeFeatureConfig config) {
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

	private void buildTrunk(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, Random rand, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		for (int dy = 0; dy < treeHeight; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config.trunkProvider);
		}

		this.leaves.add(pos.offset(0, treeHeight, 0));
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	@Override
	void buildBranch(LevelAccessor world, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG, TFTreeFeatureConfig config) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// constrain branch spread
		int limit = 5;
		if ((dest.getX() - pos.getX()) < -limit) {
			dest = new BlockPos(pos.getX() - limit, dest.getY(), dest.getZ());
		}
		if ((dest.getX() - pos.getX()) > limit) {
			dest = new BlockPos(pos.getX() + limit, dest.getY(), dest.getZ());
		}
		if ((dest.getZ() - pos.getZ()) < -limit) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() - limit);
		}
		if ((dest.getZ() - pos.getZ()) > limit) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() + limit);
		}

		if (trunk) {
			FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, config.trunkProvider, treeRNG);
		} else {
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, config.branchProvider);
		}

		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.east(), config.branchProvider);
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.west(), config.branchProvider);
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.north(), config.branchProvider);
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.south(), config.branchProvider);

		this.leaves.add(dest);
	}
}
