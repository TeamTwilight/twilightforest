package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenLargeWinter extends TFTreeGenerator {

	public TFGenLargeWinter() {
		this(false);
	}

	public TFGenLargeWinter(boolean par1) {
		super(par1);
		treeState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
		branchState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// determine a height
		int treeHeight = 35;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(10);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(10);
			}
		}

		// check if we're on dirt or grass
		Block blockUnder = world.getBlockState(pos.down()).getBlock();
		if (blockUnder != Blocks.GRASS && blockUnder != Blocks.DIRT || pos.getY() >= TFWorld.MAXHEIGHT - treeHeight) {
			return false;
		}


		//okay build a tree!  Go up to the height
		buildTrunk(world, pos, treeHeight);

		// make leaves
		makeLeaves(world, pos, treeHeight);

		// roots!
		int numRoots = 4 + random.nextInt(3);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, pos, offset, b);
		}

		return true;
	}

	private void makeLeaves(World world, BlockPos pos, int treeHeight) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			TFGenerator.makeLeafCircle2(this, world, pos.up(offGround + treeHeight - dy), radius, leafState, false);
			this.makePineBranches(world, pos.up(offGround + treeHeight - dy), radius);
		}
	}

	private void makePineBranches(World world, BlockPos pos, int radius) {
		int branchLength = radius > 4 ? radius - 1 : radius - 2;

		switch (pos.getY() % 2) {
			case 0:
				// branches
				for (int i = 1; i <= branchLength; i++) {
					this.setBlockAndNotifyAdequately(world, pos.add(-i, 0, 0), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X));
					this.setBlockAndNotifyAdequately(world, pos.add(0, 0, i + 1), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z));
					this.setBlockAndNotifyAdequately(world, pos.add(i + 1, 0, 1), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X));
					this.setBlockAndNotifyAdequately(world, pos.add(1, 0, -i), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z));
				}
				break;
			case 1:
				for (int i = 1; i <= branchLength; i++) {
					this.setBlockAndNotifyAdequately(world, pos.add(-1, 0, 1), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X));
					this.setBlockAndNotifyAdequately(world, pos.add(1, 0, i + 1), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z));
					this.setBlockAndNotifyAdequately(world, pos.add(i + 1, 0, 0), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X));
					this.setBlockAndNotifyAdequately(world, pos.add(0, 0, -i), branchState.withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z));
				}
				break;
		}
	}

	private int leafRadius(int treeHeight, int dy, int functionType) {
		switch (functionType) {
			case 0:
			default:
				return (dy - 1) % 4;
			case 1:
				return (int) (4F * (float) dy / (float) treeHeight + (0.75F * dy % 3));
			case 99:
				return (treeHeight - (dy / 2) - 1) % 4; // bad
		}
	}

	private void buildTrunk(World world, BlockPos pos, int treeHeight) {
		for (int dy = 0; dy < treeHeight; dy++) {
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 0), treeState);
			this.setBlockAndNotifyAdequately(world, pos.add(1, dy, 0), treeState);
			this.setBlockAndNotifyAdequately(world, pos.add(0, dy, 1), treeState);
			this.setBlockAndNotifyAdequately(world, pos.add(1, dy, 1), treeState);
		}
	}

}
