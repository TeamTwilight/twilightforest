package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;

/**
 * A stump from a hollow tree
 *
 * @author Ben
 */
public class TFGenHollowStump extends TFGenHollowTree {

	public TFGenHollowStump(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(IWorld world, Random rand, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		int radius = rand.nextInt(2) + 2;

		if (!FeatureUtil.isAreaSuitable(world, rand, pos.add(-radius, 0, -radius), 2 * radius, 6, 2 * radius)) {
			return false;
		}

		buildTrunk(world, rand, pos, trunk, branch, root, radius, 6, mbb, config);

		// 3-5 roots at the bottom
		buildBranchRing(world, rand, pos, leaves, branch, radius, 3, 2, 6, 0.75D, 3, 5, 3, false, mbb, config);

		// several more taproots
		buildBranchRing(world, rand, pos, leaves, branch, radius, 1, 2, 8, 0.9D, 3, 5, 3, false, mbb, config);

		return true;
	}

	@Override
	protected void buildTrunk(IWorld world, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> branch, Set<BlockPos> root, int diameter, int maxHeight, MutableBoundingBox mbb, TFTreeFeatureConfig config) {

		int hollow = diameter / 2;

		// go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				for (int dy = -4; dy < 0; dy++) {
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					if (dist <= diameter) {
						BlockPos dPos = pos.add(dx, dy, dz);
						if (FeatureUtil.hasAirAround(world, dPos)) {
							if (dist > hollow) {
								this.setLogBlockState(world, random, dPos, trunk, mbb, config);
							} else {
								this.setLogBlockState(world, random, dPos, branch, mbb, config);
							}
						} else {
							this.setRootsBlockState(world, random, dPos, root, mbb, config);
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
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					// make a trunk!
					if (dist <= diameter && dist > hollow) {
						this.setLogBlockState(world, random, pos.add(dx, dy, dz), trunk, mbb, config);
					}
				}
			}
		}
	}
}

