package twilightforest.world.feature;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class TFGenMangroveTree<T extends TFTreeFeatureConfig> extends TFTreeGenerator<T> {

	//private boolean checkForWater;
	private List<LeafBlob> leaves = Lists.newArrayList();

//	public TFGenMangroveTree() {
//		this(false);
//	}
//
//	public TFGenMangroveTree(boolean notify) {
//		super(notify);
//
//		this.checkForWater = !notify;
//
//		treeState = TFBlocks.twilight_log.getDefaultState().with(BlockTFLog.VARIANT, WoodVariant.MANGROVE);
//		branchState = treeState.with(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
//		leafState = TFBlocks.twilight_leaves.getDefaultState().with(BlockTFLeaves.VARIANT, LeavesVariant.MANGROVE).with(LeavesBlock.CHECK_DECAY, false);
//		rootState = TFBlocks.root.getDefaultState();
//	}

	public TFGenMangroveTree(Function<Dynamic<?>, T> config) {
		super(config);
	}

//	@Override
//	protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, BlockState state) {
//		if (canGrowInto(worldIn.getBlockState(pos).getBlock()))
//			worldIn.setBlockState(pos, state);
//	}

	@Override
	protected boolean generate(IWorldGenerationReader worldIn, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, T config) {
		World world = (World)worldIn;
		// we only start over water
		if (pos.getY() >= 128 - 18 - 1 || (config.checkWater && world.getBlockState(pos.down()).getBlock() != Blocks.WATER)) {
			return false;
		}

		this.leaves.clear();

		//okay build a trunk!  Start 5 squares off the ground and go up maybe 6-9 squares
		buildBranch(world, random, pos, trunk, branch, 5, 6 + random.nextInt(3), 0, 0, mbb, config, true);

		// make 0-3 branches
		int numBranches = random.nextInt(3);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, random, pos, trunk, branch, 7 + b, 6 + random.nextInt(2), 0.3 * b + offset, 0.25, mbb, config, false);
		}

		// add the actual leaves
		for (LeafBlob blob : this.leaves) {
			makeLeafBlob(world, random, blob.pos, leaves, blob.size, config);
		}

		// make 3-5 roots
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextDouble();
		for (int i = 0; i < numRoots; i++) {
			double rTilt = 0.75 + (random.nextDouble() * 0.1);
			buildRoot(world, random, pos, branch, root, 5, 12, 0.4 * i + offset, rTilt, mbb, config);
		}

		// add a firefly (torch) to the trunk
		addFirefly(world, pos, 5 + random.nextInt(5), random.nextDouble());

		return true;
	}

	private void makeLeafBlob(World world, Random random, BlockPos pos, Set<BlockPos> leaves, int size, T config) {
		FeatureUtil.makeLeafCircle(world, pos.down(), size - 1, config.leavesProvider.getBlockState(random, pos.down()), leaves, false);
		FeatureUtil.makeLeafCircle(world, pos, size, config.leavesProvider.getBlockState(random, pos), leaves, false);
		FeatureUtil.makeLeafCircle(world, pos.up(), size - 2, config.leavesProvider.getBlockState(random, pos.up()), leaves, false);
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	private void buildBranch(World world, Random random, BlockPos pos, Set<BlockPos> log, Set<BlockPos> branch, int height, double length, double angle, double tilt, MutableBoundingBox mbb, T config, boolean trunk) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

		// variable size leaves
		int bSize = 2 + random.nextInt(3);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, bSize + 1)) {

			if (trunk) {
				FeatureUtil.drawBresehnamTree(world, src, dest, config.trunkProvider.getBlockState(random, src), log);
			} else {
				FeatureUtil.drawBresehnamBranch(this, world, random, src, dest, branch, mbb, config);
			}

			// we only need these side blocks if the size is > 2
			if (bSize > 2) {
				this.setBranchBlockState(world, random, dest.east(), branch, mbb, config);
				this.setBranchBlockState(world, random, dest.west(), branch, mbb, config);
				this.setBranchBlockState(world, random, dest.south(), branch, mbb, config);
				this.setBranchBlockState(world, random, dest.north(), branch, mbb, config);
//				setBlockAndNotifyAdequately(world, dest.east(), branchState);
//				setBlockAndNotifyAdequately(world, dest.west(), branchState);
//				setBlockAndNotifyAdequately(world, dest.south(), branchState);
//				setBlockAndNotifyAdequately(world, dest.north(), branchState);
			}
			leaves.add(new LeafBlob(dest, bSize));
		}
	}

	/**
	 * Build a root.  (Which is really like a branch without the leaves)
	 */
	private void buildRoot(World world, Random rand, BlockPos pos, Set<BlockPos> branch, Set<BlockPos> root, int height, double length, double angle, double tilt, MutableBoundingBox mbb, T config) {
		BlockPos src = pos.up(height);
		BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

		// only actually draw the root if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 1)) {
			BlockPos[] lineArray = FeatureUtil.getBresehnamArrays(src, dest);
			boolean stillAboveGround = true;
			for (BlockPos coord : lineArray) {
				if (stillAboveGround && FeatureUtil.hasAirAround(world, coord)) {
					this.setBranchBlockState(world, rand, coord, branch, mbb, config);
					this.setBranchBlockState(world, rand, coord.down(), branch, mbb, config);
//					this.setBlockAndNotifyAdequately(world, coord, branchState);
//					this.setBlockAndNotifyAdequately(world, coord.down(), branchState);
				} else {
					this.setRootsBlockState(world, rand, coord, root, mbb, config);
					this.setRootsBlockState(world, rand, coord.down(), root, mbb, config);
//					this.placeRootBlock(world, coord, rootState);
//					this.placeRootBlock(world, coord.down(), rootState);
					stillAboveGround = false;
				}
			}
		}
	}

	private class LeafBlob {
		BlockPos pos;
		int size;

		public LeafBlob(BlockPos pos, int size) {
			this.pos = pos;
			this.size = size;
		}
	}
}
