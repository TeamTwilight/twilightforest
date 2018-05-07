package twilightforest.world;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;
import twilightforest.enums.WoodVariant;

import java.util.List;
import java.util.Random;

public class TFGenMangroveTree extends TFTreeGenerator {

	private boolean checkForWater;
	private List<LeafBlob> leaves = Lists.newArrayList();

	public TFGenMangroveTree() {
		this(false);
	}

	public TFGenMangroveTree(boolean par1) {
		super(par1);

		this.checkForWater = !par1;

		treeState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.MANGROVE);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.MANGROVE).withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
		if (canGrowInto(worldIn.getBlockState(pos).getBlock()))
			super.setBlockAndNotifyAdequately(worldIn, pos, state);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// we only start over water
		if ((this.checkForWater && world.getBlockState(pos.down()).getBlock() != Blocks.WATER) || pos.getY() >= 128 - 18 - 1) {
			return false;
		}

		this.leaves.clear();

		//okay build a trunk!  Start 5 squares off the ground and go up maybe 6-9 squares
		buildBranch(world, random, pos, 5, 6 + random.nextInt(3), 0, 0, true);

		// make 0-3 branches
		int numBranches = random.nextInt(3);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, random, pos, 7 + b, 6 + random.nextInt(2), 0.3 * b + offset, 0.25, false);
		}

		// add the actual leaves
		for (LeafBlob blob : leaves) {
			makeLeafBlob(world, blob.pos, blob.size);
		}

		// make 3-5 roots
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextDouble();
		for (int i = 0; i < numRoots; i++) {
			double rTilt = 0.75 + (random.nextDouble() * 0.1);
			buildRoot(world, pos, 5, 12, 0.4 * i + offset, rTilt);
		}

		// add a firefly (torch) to the trunk
		addFirefly(world, pos, 5 + random.nextInt(5), random.nextDouble());


		return true;
	}

	private void makeLeafBlob(World world, BlockPos pos, int size) {
		TFGenerator.makeLeafCircle(this, world, pos.down(), size - 1, leafState, false);
		TFGenerator.makeLeafCircle(this, world, pos, size, leafState, false);
		TFGenerator.makeLeafCircle(this, world, pos.up(), size - 2, leafState, false);
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	private void buildBranch(World world, Random random, BlockPos pos, int height, double length, double angle, double tilt, boolean trunk) {
		BlockPos src = pos.up(height);
		BlockPos dest = TFGenerator.translate(src, length, angle, tilt);

		// variable size leaves
		int bSize = 2 + random.nextInt(3);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, bSize + 1)) {

			TFGenerator.drawBresehnam(this, world, src, dest, trunk ? treeState : branchState);

			// we only need these side blocks if the size is > 2
			if (bSize > 2) {
				setBlockAndNotifyAdequately(world, dest.east(), branchState);
				setBlockAndNotifyAdequately(world, dest.west(), branchState);
				setBlockAndNotifyAdequately(world, dest.south(), branchState);
				setBlockAndNotifyAdequately(world, dest.north(), branchState);
			}
			leaves.add(new LeafBlob(dest, bSize));
		}
	}

	/**
	 * Build a root.  (Which is really like a branch without the leaves)
	 */
	private void buildRoot(World world, BlockPos pos, int height, double length, double angle, double tilt) {
		BlockPos src = pos.up(height);
		BlockPos dest = TFGenerator.translate(src, length, angle, tilt);

		// only actually draw the root if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 1)) {
			BlockPos[] lineArray = TFGenerator.getBresehnamArrays(src, dest);
			boolean stillAboveGround = true;
			for (BlockPos coord : lineArray) {
				if (stillAboveGround && TFGenerator.hasAirAround(world, coord)) {
					this.setBlockAndNotifyAdequately(world, coord, branchState);
					this.setBlockAndNotifyAdequately(world, coord.down(), branchState);
				} else {
					this.placeRootBlock(world, coord, rootState);
					this.placeRootBlock(world, coord.down(), rootState);
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
