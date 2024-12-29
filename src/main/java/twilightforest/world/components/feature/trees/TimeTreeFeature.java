package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.util.RootPlacer;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.BiConsumer;

public class TimeTreeFeature extends HollowTreeFeature {
	public TimeTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config) {
		final int height = 8;
		final int radius = 1;

		// do we have enough height?
		if (world.isOutsideBuildHeight(pos.getY() + 1)
			|| world.isOutsideBuildHeight(pos.getY() + height + radius)
			|| FeatureUtil.isAnyMatchInArea(pos.subtract(new Vec3i(1, 4, 1)), 3, 4, 3, blockPos -> world.getBlockState(blockPos).is(BlockTags.FEATURES_CANNOT_REPLACE))
			|| FeatureUtil.isAnyMatchInArea(pos.subtract(new Vec3i(1, 0, 1)), 3, 16, 3, blockPos -> !FeaturePlacers.validTreePos(world, blockPos) || blockPos == pos)) {
				return false;
 		}

		// check if we're on dirt or grass
		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.TIME_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		// Start with roots first, so they don't fail placement because they intersect the trunk shell first
		// 3-5 roots at the bottom
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 0, 12, 0.75D, 3, 5, 3, false, config);

		// several more taproots
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 18, 0.9D, 3, 5, 3, false, config);

		// make a tree!

		// build the trunk
		buildTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius, height, config);

		// build the crown
		buildTinyCrown(world, trunkPlacer, leavesPlacer, random, pos, radius, height, config);

		// add clock block
		BlockPos corePos = pos.offset(-1, 2, 0);
		if (world.getBlockState(corePos).canBeReplaced()) {
			world.setBlock(corePos, TFBlocks.TIME_LOG_CORE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), Block.UPDATE_ALL);
			world.scheduleTick(corePos, TFBlocks.TIME_LOG_CORE.get(), 20);
		}

		return true;
	}

	/**
	 * Build the crown of the tree. This builds a smaller crown, since the large
	 * ones were causing some performance issues
	 */
	protected void buildTinyCrown(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius
		, int height, TFTreeFeatureConfig config) {
		final int crownRadius = 4;
		final int bvar = 1;

		// 3-5 medium branches starting at the bottom of the crown
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - crownRadius, 0, crownRadius, 0.35D, bvar, bvar + 2, 1, true, config);

		// 3-5 medium branches at the crown middle
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - (crownRadius >> 1), 0, crownRadius, 0.28D, bvar, bvar + 2, 1, true, config);

		// 2-4 medium branches at the crown top
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius, 0.15D, 2, 4, 0, true, config);

		// 3-6 medium branches going straight up
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius >> 1, 0.05D, bvar, bvar + 2, 0, true, config);
	}
}
