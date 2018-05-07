package twilightforest.world;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.enums.WoodVariant;

import java.util.Random;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */
public class TFGenDarkCanopyTree extends TFTreeGenerator {


	public TFGenDarkCanopyTree() {
		this(false);
	}

	public TFGenDarkCanopyTree(boolean par1) {
		super(par1);
		treeState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.DARK);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.dark_leaves.getDefaultState();
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// if we are given leaves as a starting position, seek dirt or grass underneath
		boolean foundDirt = false;
		Material materialUnder;
		for (int dy = pos.getY(); dy >= TFWorld.SEALEVEL; dy--) {
			materialUnder = world.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ())).getMaterial();
			if (materialUnder == Material.GRASS || materialUnder == Material.GROUND) {
				// yes!
				foundDirt = true;
				pos = new BlockPos(pos.getX(), dy, pos.getZ());
				break;
			} else if (materialUnder == Material.ROCK || materialUnder == Material.SAND) {
				// nope
				break;
			}
		}

		if (!foundDirt) {
			return false;
		}

		// do not grow next to another tree
		for (EnumFacing e : EnumFacing.HORIZONTALS) {
			if (world.getBlockState(pos.offset(e)).getMaterial() == Material.WOOD)
				return false;
		}

		// determine a height
		int treeHeight = 6 + random.nextInt(5);

		//okay build a tree!  trunk here
		TFGenerator.drawBresehnam(this, world, pos, pos.up(treeHeight), treeState);
		leafAround(world, pos.up(treeHeight));

		// make 4 branches
		int numBranches = 4;
		double offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, treeHeight - 3 - numBranches + (b / 2), 10 + random.nextInt(4), 0.23 * b + offset, 0.23, random);
		}

		// root bulb
		if (TFGenerator.hasAirAround(world, pos.down())) {
			this.setBlockAndNotifyAdequately(world, pos.down(), treeState);
		} else {
			this.setBlockAndNotifyAdequately(world, pos.down(), rootState);
		}

		// roots!
		int numRoots = 3 + random.nextInt(2);
		offset = random.nextDouble();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, pos, offset, b);
		}


		return true;
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	private void buildBranch(World world, BlockPos pos, int height, double length, double angle, double tilt, Random random) {
		BlockPos src = pos.up(height);
		BlockPos dest = TFGenerator.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 6)) {
			TFGenerator.drawBresehnam(this, world, src, dest, branchState);
			leafAround(world, dest);
		}
	}

	/**
	 * Make our leaf pattern
	 */
	private void leafAround(World world, BlockPos pos) {
		int leafSize = 4;

		// only leaf if there are no leaves by where we are thinking of leafing
		if (TFGenerator.hasAirAround(world, pos)) {
			TFGenerator.makeLeafCircle(this, world, pos.down(), leafSize, leafState, false);
			TFGenerator.makeLeafCircle(this, world, pos, leafSize + 1, leafState, false);
			TFGenerator.makeLeafCircle(this, world, pos.up(), leafSize, leafState, false);
			TFGenerator.makeLeafCircle(this, world, pos.up(2), leafSize - 2, leafState, false);
		}
	}

}
