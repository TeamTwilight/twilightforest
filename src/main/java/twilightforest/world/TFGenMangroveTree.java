package twilightforest.world;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlockProperties;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.LeavesVariant;
import twilightforest.block.enums.WoodVariant;

import java.util.Random;

public class TFGenMangroveTree extends TFTreeGenerator {

	private boolean checkForWater;

	public TFGenMangroveTree() {
		this(false);
	}

	public TFGenMangroveTree(boolean par1) {
		super(par1);

		this.checkForWater = !par1;

		treeState = TFBlocks.log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.MANGROVE);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.MANGROVE).withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// we only start over water
		if ((this.checkForWater && world.getBlockState(pos.down()).getBlock() != Blocks.WATER) || pos.getY() >= 128 - 18 - 1) {
			return false;
		}

		//okay build a trunk!  Start 5 squares off the ground and go up maybe 6-9 squares
		buildBranch(world, random, pos, 5, 6 + random.nextInt(3), 0, 0, true);

		// make 0-3 branches
		int numBranches = random.nextInt(3);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, random, pos, 7 + b, 6 + random.nextInt(2), 0.3 * b + offset, 0.25, false);
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
			// leaves!
			TFGenerator.makeLeafCircle(this, world, dest.down(), bSize - 1, leafState, false);
			TFGenerator.makeLeafCircle(this, world, dest, bSize, leafState, false);
			TFGenerator.makeLeafCircle(this, world, dest.up(), bSize - 2, leafState, false);
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
			BlockPos[] lineArray = TFGenerator.getBresehnamArrayCoords(src, dest);
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

	/**
	 * Add a firefly at the specified height and angle.
	 *
	 * @param height how far up the tree
	 * @param angle  from 0 - 1 rotation around the tree
	 */
	protected void addFirefly(World world, BlockPos pos, int height, double angle) {
		int iAngle = (int) (angle * 4.0);
		if (iAngle == 0) {
			setBlockAndNotifyAdequately(world, pos.add(1, height, 0), TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.EAST));
		} else if (iAngle == 1) {
			setBlockAndNotifyAdequately(world, pos.add(-1, height, 0), TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.WEST));
		} else if (iAngle == 2) {
			setBlockAndNotifyAdequately(world, pos.add(0, height, 1), TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.SOUTH));
		} else if (iAngle == 3) {
			setBlockAndNotifyAdequately(world, pos.add(0, height, -1), TFBlocks.firefly.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.NORTH));
		}
	}
}
