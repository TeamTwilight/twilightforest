package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.FeaturePlacers;
import twilightforest.util.FeatureUtil;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.BiConsumer;

/**
 * A stump from a hollow tree
 *
 * @author Ben
 */
public class HollowStumpFeature extends HollowTreeFeature {

	public HollowStumpFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, BiConsumer<BlockPos, BlockState> decorationPlacer, TFTreeFeatureConfig config) {
		int radius = random.nextInt(2) + 2;

		if (!FeatureUtil.isAreaSuitable(world, pos.offset(-radius, 0, -radius), 2 * radius, 6, 2 * radius)) {
			return false;
		}

		// Start with roots first, so they don't fail placement because they intersect the trunk shell first
		// 3-5 roots at the bottom
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 3, 2, 6, 0.75D, 3, 5, 3, false, config);

		// several more taproots
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 8, 0.9D, 3, 5, 3, false, config);

		buildTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius, 6, config);

		return true;
	}

	@Override
	protected void buildTrunk(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> decoPlacer, RandomSource  random, BlockPos pos, int diameter, int maxheight, TFTreeFeatureConfig config) {
		int hollow = diameter >> 1;

		// go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				for (int dy = -4; dy < 0; dy++) {
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					if (dist <= diameter) {
						BlockPos dPos = pos.offset(dx, dy, dz);
						if (FeatureUtil.hasAirAround(world, dPos)) {
							FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, dPos, config.trunkProvider);
						} else {
							FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, dPos, config.rootsProvider);
						}
					}
				}
			}
		}

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				int height = 2 + random.nextInt(3) + random.nextInt(2);

				for (int dy = 0; dy <= height; dy++) {
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = Math.max(ax, az) + (Math.min(ax, az) >> 1);

					// make a trunk!
					if (dist <= diameter && dist > hollow) {
						FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, pos.offset(dx, dy, dz), config.trunkProvider);
					}
				}
			}
		}
	}
}

