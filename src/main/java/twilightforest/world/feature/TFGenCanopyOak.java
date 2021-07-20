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

public class TFGenCanopyOak extends TFGenCanopyTree {

	private final List<BlockPos> leaves = Lists.newArrayList();

	public TFGenCanopyOak(Codec<TFTreeFeatureConfig> config) {
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

		// check if we're on dirt or grass
		BlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), Direction.UP, config.getSapling(random, pos))) {
			return false;
		}

		this.leaves.clear();

		//okay build a tree!  Go up to the height
		buildTrunk(world, random, pos, trunk, treeHeight, mbb, config);

		// make 12 - 20 branches
		int numBranches = 12 + random.nextInt(9);
		float bangle = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			float btilt = 0.15F + (random.nextFloat() * 0.35F);
			buildBranch(world, pos, trunk, branch, treeHeight - 10 + (b / 2), 5, bangle, btilt, false, random, mbb, config);

			bangle += (random.nextFloat() * 0.4F);
			if (bangle > 1.0F) {
				bangle -= 1.0F;
			}
		}

		// add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, random, leafPos, leaves, config);
		}

		makeRoots(world, random, pos, trunk, root, mbb, config);
		makeRoots(world, random, pos.east(), trunk, root, mbb, config);
		makeRoots(world, random, pos.south(), trunk, root, mbb, config);
		makeRoots(world, random, pos.east().south(), trunk, root, mbb, config);

		return true;
	}

	private void makeLeafBlob(IWorld world, Random rand, BlockPos leafPos, Set<BlockPos> setLeaves, TFTreeFeatureConfig config) {
		FeatureUtil.drawLeafBlob(world, leafPos, 2, config.leavesProvider.getBlockState(rand, leafPos), setLeaves);
	}

	private void makeRoots(IWorld world, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> root, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.down())) {
			this.setLogBlockState(world, random, pos.down(), trunk, mbb, config);
		} else {
			this.setRootsBlockState(world, random, pos.down(), root, mbb, config);
		}

		// roots!
		int numRoots = 1 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, random, pos, root, offset, b, mbb, config);
		}
	}

	private void buildTrunk(IWorld world, Random rand, BlockPos pos, Set<BlockPos> trunk, int treeHeight, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		for (int dy = 0; dy < treeHeight; dy++) {
			this.setLogBlockState(world, rand, pos.add(0, dy, 0), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(1, dy, 0), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(0, dy, 1), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(1, dy, 1), trunk, mbb, config);
		}

		this.leaves.add(pos.add(0, treeHeight, 0));
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	@Override
	void buildBranch(IWorld world, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> branchpos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

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
			FeatureUtil.drawBresenhamTree(world, src, dest, config.trunkProvider.getBlockState(treeRNG, src), logpos);
		} else {
			FeatureUtil.drawBresenhamBranch(this, world, treeRNG, src, dest, branchpos, mbb, config);
		}

		this.setBranchBlockState(world, treeRNG, dest.east(), branchpos, mbb, config);
		this.setBranchBlockState(world, treeRNG, dest.west(), branchpos, mbb, config);
		this.setBranchBlockState(world, treeRNG, dest.north(), branchpos, mbb, config);
		this.setBranchBlockState(world, treeRNG, dest.south(), branchpos, mbb, config);

		this.leaves.add(dest);
	}
}
