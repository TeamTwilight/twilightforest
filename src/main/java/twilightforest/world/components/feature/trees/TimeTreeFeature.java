package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import tamaized.beanification.Autowired;
import twilightforest.init.TFBlocks;
import twilightforest.util.RootPlacer;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.util.features.HollowTreeUtil;

import java.util.List;
import java.util.function.BiConsumer;

public record TimeTreeFeature(
	Holder<BlockStateProvider> trunkProvider,
	Holder<BlockStateProvider> leavesProvider,
	Holder<BlockStateProvider> branchProvider,
	Holder<BlockStateProvider> rootsProvider,
	int minHeight,
	int chanceAddFiveFirst,
	int chanceAddFiveSecond,
	boolean hasLeaves,
	boolean checkWater,
	List<TreeDecorator> decorators
) implements TFTreeFeature {

	@Autowired
	private static HollowTreeUtil hollowTreeUtil;

	public static final MapCodec<TimeTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(TimeTreeFeature::trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(TimeTreeFeature::leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(TimeTreeFeature::branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(TimeTreeFeature::rootsProvider),
		Codec.INT.fieldOf("minimum_size").forGetter(TimeTreeFeature::minHeight),
		Codec.INT.fieldOf("add_first_five_chance").forGetter(TimeTreeFeature::chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").forGetter(TimeTreeFeature::chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").forGetter(TimeTreeFeature::hasLeaves),
		Codec.BOOL.fieldOf("check_water").forGetter(TimeTreeFeature::checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(TimeTreeFeature::decorators)
	).apply(instance, TimeTreeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		final int height = 8;
		final int radius = 1;

		// Do we have enough height?
		if (world.isOutsideBuildHeight(pos.getY() + 1)
			|| world.isOutsideBuildHeight(pos.getY() + height + radius)
			|| FeatureUtil.isAnyMatchInArea(pos.subtract(new Vec3i(1, 4, 1)), 3, 4, 3, blockPos -> world.getBlockState(blockPos).is(BlockTags.FEATURES_CANNOT_REPLACE))
			|| FeatureUtil.isAnyMatchInArea(pos.subtract(new Vec3i(1, 0, 1)), 3, 16, 3, blockPos -> !FeaturePlacers.validTreePos(world, blockPos) || blockPos == pos)) {
				return false;
 		}

		// Check if we're on dirt or grass
		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.TIME_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		// Start with roots first, so they don't fail placement because they intersect the trunk shell first
		// 3-5 roots at the bottom
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 0, 12, 0.75D, 3, 5, HollowTreeUtil.BranchType.ROOT, false, branchProvider(), leavesProvider(), rootsProvider());

		// Several more taproots
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 18, 0.9D, 3, 5, HollowTreeUtil.BranchType.ROOT, false, branchProvider(), leavesProvider(), rootsProvider());

		// Make a tree!

		// Build the trunk
		hollowTreeUtil.buildTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius, height, trunkProvider(), branchProvider(), rootsProvider());

		// Build the crown
		buildTinyCrown(world, trunkPlacer, leavesPlacer, random, pos, radius, height);

		// Add clock block
		BlockPos corePos = pos.offset(-1, 2, 0);
		BlockState stateAtCorePos = world.getBlockState(pos.above());
		if (stateAtCorePos.is(BlockTags.LOGS) || stateAtCorePos.canBeReplaced()) {
			world.setBlock(corePos, TFBlocks.TIME_LOG_CORE.get().defaultBlockState(), Block.UPDATE_ALL);
			world.scheduleTick(corePos, TFBlocks.TIME_LOG_CORE.get(), 20);
		}

		return true;
	}

	/**
	 * Build the crown of the tree. This builds a smaller crown, since the large
	 * ones were causing some performance issues
	 */
	private void buildTinyCrown(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius, int height) {
		final int crownRadius = 4;
		final int bvar = 1;

		// 3-5 medium branches starting at the bottom of the crown
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - crownRadius, 0, crownRadius, 0.35D, bvar, bvar + 2, HollowTreeUtil.BranchType.MEDIUM, true, branchProvider(), leavesProvider(), rootsProvider());

		// 3-5 medium branches at the crown middle
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - (crownRadius >> 1), 0, crownRadius, 0.28D, bvar, bvar + 2, HollowTreeUtil.BranchType.MEDIUM, true, branchProvider(), leavesProvider(), rootsProvider());

		// 2-4 small branches at the crown top
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius, 0.15D, 2, 4, HollowTreeUtil.BranchType.SMALL, true, branchProvider(), leavesProvider(), rootsProvider());

		// 3-6 small branches going straight up
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius >> 1, 0.05D, bvar, bvar + 2, HollowTreeUtil.BranchType.SMALL, true, branchProvider(), leavesProvider(), rootsProvider());
	}
}
