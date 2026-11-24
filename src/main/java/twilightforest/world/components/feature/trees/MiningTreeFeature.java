package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.util.RootPlacer;

import java.util.List;
import java.util.function.BiConsumer;

public record MiningTreeFeature(
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

	public static final MapCodec<MiningTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(MiningTreeFeature::trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(MiningTreeFeature::leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(MiningTreeFeature::branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(MiningTreeFeature::rootsProvider),
		Codec.INT.fieldOf("minimum_size").forGetter(MiningTreeFeature::minHeight),
		Codec.INT.fieldOf("add_first_five_chance").forGetter(MiningTreeFeature::chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").forGetter(MiningTreeFeature::chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").forGetter(MiningTreeFeature::hasLeaves),
		Codec.BOOL.fieldOf("check_water").forGetter(MiningTreeFeature::checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(MiningTreeFeature::decorators)
	).apply(instance, MiningTreeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		if (world.isOutsideBuildHeight(pos.getY() + 12)) {
			return false;
		}

		// check soil
		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.MINING_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		// 9 block high trunk
		for (int dy = 0; dy <= 9; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.above(dy), trunkProvider().value());
		}

		// branches with leaf blocks
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, 1), true, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, 2), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 8, 3), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 7, 4), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 6, 5), false, branchProvider(), leavesProvider());

		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, -1), true, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 9, -2), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 8, -3), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 7, -4), false, branchProvider(), leavesProvider());
		putBranchWithLeaves(world, trunkPlacer, leavesPlacer, random, pos.offset(0, 6, -5), false, branchProvider(), leavesProvider());

		// place minewood core
		BlockState stateAtCorePos = world.getBlockState(pos.above());
		if (stateAtCorePos.is(BlockTags.LOGS) || stateAtCorePos.canBeReplaced()) {
			world.setBlock(pos.above(), TFBlocks.MINING_LOG_CORE.get().defaultBlockState(), Block.UPDATE_ALL);
			world.scheduleTick(pos.above(), TFBlocks.MINING_LOG_CORE.get(), 20);
		}

		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.below())) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.below(), trunkProvider().value());
		} else {
			FeaturePlacers.placeIfValidRootPos(world, decorationPlacer, random, pos.below(), rootsProvider().value());
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, rootsProvider().value());
		}

		return true;
	}

	private static void putBranchWithLeaves(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource rand, BlockPos pos, boolean bushy, Holder<BlockStateProvider> branchProvider, Holder<BlockStateProvider> leavesProvider) {
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos, branchProvider.value());

		for (int lx = -1; lx <= 1; lx++) {
			for (int ly = -1; ly <= 1; ly++) {
				for (int lz = -1; lz <= 1; lz++) {
					if (!bushy && Math.abs(ly) > 0 && Math.abs(lx) > 0) {
						continue;
					}
					FeaturePlacers.placeProvidedBlock(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, pos.offset(lx, ly, lz), leavesProvider.value(), rand);
				}
			}
		}
	}
}
