package twilightforest.world;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
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

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */
public class TFGenCanopyTree extends TFTreeGenerator {

	protected int minHeight = 20;
	protected int chanceAddFirstFive = 3;
	protected int chanceAddSecondFive = 8;

	public TFGenCanopyTree() {
		this(false);
	}

	public TFGenCanopyTree(boolean par1) {
		super(par1);
		treeState = TFBlocks.log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.CANOPY);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.CANOPY).withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		int treeHeight;

		// check if we're on dirt or grass
		Material materialUnder = world.getBlockState(pos.down()).getMaterial();
		if ((materialUnder != Material.GRASS && materialUnder != Material.GROUND) || pos.getY() >= TFWorld.MAXHEIGHT - 12) {
			return false;
		}


		// determine a height
		treeHeight = minHeight;
		if (random.nextInt(chanceAddFirstFive) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(chanceAddSecondFive) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		//okay build a tree!  Go up to the height
		buildBranch(world, pos, 0, treeHeight, 0, 0, true, random);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, treeHeight - 10 + b, 9, 0.3 * b + offset, 0.2, false, random);
		}

		// root bulb
		if (TFGenerator.hasAirAround(world, pos.down())) {
			this.setBlockAndNotifyAdequately(world, pos.down(), treeState);
		} else {
			this.setBlockAndNotifyAdequately(world, pos.down(), rootState);
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, pos, offset, b);
		}


		return true;
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	void buildBranch(World world, BlockPos pos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG) {
		BlockPos src = pos.up(height);
		BlockPos dest = TFGenerator.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {

			TFGenerator.drawBresehnam(this, world, src, dest, trunk ? treeState : branchState);

			// seems to help lighting to place this firefly now
			if (trunk) {
				// add a firefly (torch) to the trunk
				addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
			}

			setBlockAndNotifyAdequately(world, dest.east(), branchState);
			setBlockAndNotifyAdequately(world, dest.west(), branchState);
			setBlockAndNotifyAdequately(world, dest.south(), branchState);
			setBlockAndNotifyAdequately(world, dest.north(), branchState);

			TFGenerator.makeLeafCircle(this, world, dest.down(), 3, leafState, true);
			TFGenerator.makeLeafCircle(this, world, dest, 4, leafState, true);
			TFGenerator.makeLeafCircle(this, world, dest.up(), 2, leafState, true);
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
