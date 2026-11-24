package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import tamaized.beanification.Autowired;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.util.RootPlacer;
import twilightforest.util.features.HollowTreeUtil;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * A stump from a hollow tree
 *
 * @author Ben
 */
public record HollowStumpFeature(
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

	public static final MapCodec<HollowStumpFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(HollowStumpFeature::trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(HollowStumpFeature::leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(HollowStumpFeature::branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(HollowStumpFeature::rootsProvider),
		Codec.INT.fieldOf("minimum_size").forGetter(HollowStumpFeature::minHeight),
		Codec.INT.fieldOf("add_first_five_chance").forGetter(HollowStumpFeature::chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").forGetter(HollowStumpFeature::chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").forGetter(HollowStumpFeature::hasLeaves),
		Codec.BOOL.fieldOf("check_water").forGetter(HollowStumpFeature::checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(HollowStumpFeature::decorators)
	).apply(instance, HollowStumpFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		int radius = random.nextInt(2) + 2;

		if (!FeatureUtil.isAreaSuitable(world, pos.offset(-radius, 0, -radius), 2 * radius, 6, 2 * radius)) {
			return false;
		}

		this.buildSmallTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius);

		// Start with roots first, so they don't fail placement because they intersect the trunk shell first
		// 3-5 roots at the bottom
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 3, 2, 6, 0.75D, 3, 5, HollowTreeUtil.BranchType.ROOT, false, branchProvider(), leavesProvider(), rootsProvider());

		// Several more taproots
		hollowTreeUtil.buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 8, 0.9D, 3, 5, HollowTreeUtil.BranchType.ROOT, false, branchProvider(), leavesProvider(), rootsProvider());

		return true;
	}

	private void buildSmallTrunk(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, RootPlacer decoPlacer, RandomSource random, BlockPos pos, int diameter) {
		int hollow = diameter >> 1;

		// Go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				for (int dy = -4; dy < 0; dy++) {
					// Determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					if (dist <= diameter) {
						BlockPos dPos = pos.offset(dx, dy, dz);
						if (FeatureLogic.hasEmptyNeighborExceptBelow(world, dPos)) {
							BlockStateProvider trunkProvider = dist > hollow ? trunkProvider().value() : branchProvider().value();
							trunkPlacer.accept(dPos, trunkProvider.getState(world, random, dPos));
						} else {
							FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, dPos, rootsProvider().value());
						}
					}
				}
			}
		}

		// Build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				int height = 2 + random.nextInt(3) + random.nextInt(2);

				for (int dy = 0; dy <= height; dy++) {
					// Determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = Math.max(ax, az) + (Math.min(ax, az) >> 1);

					// Make a trunk!
					if (dist <= diameter && dist > hollow) {
						FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.offset(dx, dy, dz), trunkProvider().value());
					}
				}
			}
		}
	}
}

