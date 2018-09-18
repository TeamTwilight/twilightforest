package twilightforest.world.feature;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;
import twilightforest.enums.WoodVariant;
import twilightforest.world.TFWorld;

import java.util.List;
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

	private List<BlockPos> leaves = Lists.newArrayList();

	public TFGenCanopyTree() {
		this(false);
	}

	public TFGenCanopyTree(boolean notify) {
		super(notify);
		treeState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.CANOPY);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.CANOPY).withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// determine a height
		int treeHeight = minHeight;
		if (random.nextInt(chanceAddFirstFive) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(chanceAddSecondFive) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		if (pos.getY() >= TFWorld.MAXHEIGHT - treeHeight) {
			return false;
		}

		// check if we're on dirt or grass
		IBlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), EnumFacing.UP, source)) {
			return false;
		}

		leaves.clear();

		//okay build a tree!  Go up to the height
		buildBranch(world, pos, 0, treeHeight, 0, 0, true, random);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, treeHeight - 10 + b, 9, 0.3 * b + offset, 0.2, false, random);
		}

		// add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, leafPos);
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

	private void makeLeafBlob(World world, BlockPos leafPos)
	{
		TFGenerator.makeLeafCircle(this, world, leafPos.down(), 3, leafState, true);
		TFGenerator.makeLeafCircle(this, world, leafPos, 4, leafState, true);
		TFGenerator.makeLeafCircle(this, world, leafPos.up(), 2, leafState, true);
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

			// save leaf position for later
			this.leaves.add(dest);
		}
	}
}
