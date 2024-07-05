package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.util.FeaturePlacers;
import twilightforest.util.FeatureUtil;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.BiConsumer;

public class MiningTreeFeature extends TFTreeFeature<TFTreeFeatureConfig> {

	public MiningTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, BiConsumer<BlockPos, BlockState> decorationPlacer, TFTreeFeatureConfig config) {
		if (world.isOutsideBuildHeight(pos.getY() + 12)) {
			return false;
		}

		// check soil
		BlockState state = world.getBlockState(pos.below());
		if (!state.getBlock().canSustainPlant(state, world, pos.below(), Direction.UP, TFBlocks.MINING_SAPLING.get())) {
			return false;
		}

		// 9 block high trunk
		for (int dy = 0; dy <= 9; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.above(dy), config.trunkProvider);
		}

		// branches with leaf blocks
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, 1), true, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, 2), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 8, 3), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 7, 4), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 6, 5), false, config);

		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, -1), true, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, -2), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 8, -3), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 7, -4), false, config);
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 6, -5), false, config);

		// place minewood core
		world.setBlock(pos.above(), TFBlocks.MINING_LOG_CORE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 3);
		world.scheduleTick(pos.above(), TFBlocks.MINING_LOG_CORE.get(), 20);

		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), config.trunkProvider);
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decorationPlacer, random, pos.below(), config.rootsProvider);
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, config.rootsProvider);
		}

		return true;
	}

	protected static void putBranchWithLeaves(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource rand, BlockPos pos, boolean bushy, TFTreeFeatureConfig config) {
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos, config.branchProvider);

		for (int lx = -1; lx <= 1; lx++) {
			for (int ly = -1; ly <= 1; ly++) {
				for (int lz = -1; lz <= 1; lz++) {
					if (!bushy && Math.abs(ly) > 0 && Math.abs(lx) > 0) {
						continue;
					}
					FeaturePlacers.placeProvidedBlock(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, pos.offset(lx, ly, lz), config.leavesProvider, rand);
				}
			}
		}
	}
}
