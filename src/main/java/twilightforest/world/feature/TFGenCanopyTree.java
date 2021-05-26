package twilightforest.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import twilightforest.util.FeatureUtil;
import twilightforest.world.TFGenerationSettings;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */
public class TFGenCanopyTree extends TFTreeGenerator<TFTreeFeatureConfig> {

//	protected int minHeight = 20;

	private List<BlockPos> leaves = Lists.newArrayList();

	public TFGenCanopyTree(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(IWorld world, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		// determine a height
		int treeHeight = config.minHeight;
		if (random.nextInt(config.chanceAddFiveFirst) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(config.chanceAddFiveSecond) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (pos.getY() >= TFGenerationSettings.MAXHEIGHT - treeHeight) {
			return false;
		}

		BlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), Direction.UP, config.getSapling(random, pos))) {
			return false;
		}

		this.leaves.clear();

		//okay build a tree!  Go up to the height
		buildBranch(world, pos, trunk, branch, 0, treeHeight, 0, 0, true, random, mbb, config);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, trunk, branch, treeHeight - 10 + b, 9, 0.3 * b + offset, 0.2, false, random, mbb, config);
		}

		// add the actual leaves
		if (config.hasLeaves)
			for (BlockPos leafPos : this.leaves) {
				makeLeafBlob(world, random, leafPos, leaves, config);
			}

		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.down())) {
			this.setLogBlockState(world, random, pos.down(), trunk, mbb, config);
		} else {
			this.setRootsBlockState(world, random, pos.down(), root, mbb, config);
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, random, pos, root, offset, b, mbb, config);
		}

		return true;
	}

	private void makeLeafBlob(IWorld world, Random random, BlockPos leafPos, Set<BlockPos> setLeaves, TFTreeFeatureConfig config) {
		FeatureUtil.makeLeafCircle(world, leafPos.down(), 3, config.leavesProvider.getBlockState(random, leafPos.down()), setLeaves, true);
		FeatureUtil.makeLeafCircle(world, leafPos, 4, config.leavesProvider.getBlockState(random, leafPos), setLeaves, true);
		FeatureUtil.makeLeafCircle(world, leafPos.up(), 2, config.leavesProvider.getBlockState(random, leafPos.up()), setLeaves, true);
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	void buildBranch(IWorld world, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> branchpos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {

			if (trunk) {
				// Oooouch! This is EXTREMELY expensive for a straight trunk! D:
				FeatureUtil.drawBresenhamTree(world, src, dest, config.trunkProvider.getBlockState(treeRNG, src), logpos);
			} else {
				FeatureUtil.drawBresenhamBranch(this, world, treeRNG, src, dest, branchpos, mbb, config);
			}

			// seems to help lighting to place this firefly now
			if (trunk) {
				// add a firefly (torch) to the trunk
				addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
			}

			this.setBranchBlockState(world, treeRNG, dest.east(), branchpos, mbb, config);
			this.setBranchBlockState(world, treeRNG, dest.west(), branchpos, mbb, config);
			this.setBranchBlockState(world, treeRNG, dest.south(), branchpos, mbb, config);
			this.setBranchBlockState(world, treeRNG, dest.north(), branchpos, mbb, config);
//			setBlockAndNotifyAdequately(world, dest.east(), branchState);
//			setBlockAndNotifyAdequately(world, dest.west(), branchState);
//			setBlockAndNotifyAdequately(world, dest.south(), branchState);
//			setBlockAndNotifyAdequately(world, dest.north(), branchState);

			// save leaf position for later
			this.leaves.add(dest);
		}
	}
}
