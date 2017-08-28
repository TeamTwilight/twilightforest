package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;


/**
 * Makes large mushrooms with flat mushroom tops that provide a canopy for the forest
 *
 * @author Ben
 */
public class TFGenCanopyMushroom extends TFTreeGenerator {

	public TFGenCanopyMushroom() {
		this(false);
	}

	public TFGenCanopyMushroom(boolean par1) {
		super(par1);
		treeState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
		branchState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM);
		leafState = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		// determine a height
		int treeHeight = 12;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(5);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(5);
			}
		}

		// check if we're on dirt or grass
		Block blockUnder = world.getBlockState(pos.down()).getBlock();
		if (blockUnder != Blocks.GRASS && blockUnder != Blocks.DIRT && blockUnder != Blocks.MYCELIUM || pos.getY() >= 256 - treeHeight - 1) {
			return false;
		}


		this.treeState = random.nextInt(3) == 0 ? Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM) : Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
		this.leafState = treeState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.CENTER);

		//okay build a tree!  Go up to the height
		buildBranch(world, pos, 0, treeHeight, 0, 0, true, random);

		// make 3-4 branches
		int numBranches = 3 + random.nextInt(2);
		double offset = random.nextDouble();
		for (int b = 0; b < numBranches; b++) {
			buildBranch(world, pos, treeHeight - 5 + b, 9, 0.3 * b + offset, 0.2, false, random);
		}

		return true;
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 *
	 * @param height
	 * @param length
	 * @param angle
	 * @param tilt
	 */
	private void buildBranch(World world, BlockPos pos, int height, double length, double angle, double tilt, boolean trunk, Random treeRNG) {
		BlockPos src = pos.up(height);
		BlockPos dest = TFGenerator.translate(src, length, angle, tilt);

		// only actually draw the branch if it's not going to load new chunks
		if (world.isAreaLoaded(dest, 5)) {
			if (src.getX() != dest.getX() || src.getZ() != dest.getZ()) {
				// branch
				TFGenerator.drawBresehnam(this, world, src, new BlockPos(dest.getX(), src.getY(), dest.getZ()), branchState);
				TFGenerator.drawBresehnam(this, world, new BlockPos(dest.getX(), src.getY() + 1, dest.getZ()), dest.down(), treeState);
			} else {
				// trunk
				TFGenerator.drawBresehnam(this, world, src, dest.down(), treeState);
			}

			if (trunk) {
				// add a firefly (torch) to the trunk
				addFirefly(world, pos, 3 + treeRNG.nextInt(7), treeRNG.nextDouble());
			}

			drawMushroomCircle(world, dest, 4, leafState);
		}
	}

	/**
	 * Draw a flat blob (a circle?) of mushroom pieces
	 * <p>
	 * This assumes that the baseState you've passed in is the center variant
	 */
	private void drawMushroomCircle(World world, BlockPos pos, int rad, IBlockState baseState) {
		// trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			for (byte dz = 0; dz <= rad; dz++) {
				int dist = (int) (Math.max(dx, dz) + (Math.min(dx, dz) * 0.5));

				//hack!  I keep getting failing leaves at a certain position.
				if (dx == 3 && dz == 3) {
					dist = 6;
				}

				// if we're inside the blob, fill it
				if (dx == 0) {
					// two!
					if (dz < rad) {
						setBlockAndNotifyAdequately(world, pos.add(0, 0, dz), baseState);
						setBlockAndNotifyAdequately(world, pos.add(0, 0, -dz), baseState);
					} else {
						setBlockAndNotifyAdequately(world, pos.add(0, 0, dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH));
						setBlockAndNotifyAdequately(world, pos.add(0, 0, -dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH));
					}
				} else if (dz == 0) {
					// two!
					if (dx < rad) {
						setBlockAndNotifyAdequately(world, pos.add(dx, 0, 0), baseState);
						setBlockAndNotifyAdequately(world, pos.add(-dx, 0, 0), baseState);
					} else {
						setBlockAndNotifyAdequately(world, pos.add(dx, 0, 0), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST));
						setBlockAndNotifyAdequately(world, pos.add(-dx, 0, 0), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST));
					}
				} else if (dist < rad) {
					// do four at a time for easiness!
					setBlockAndNotifyAdequately(world, pos.add(dx, 0, dz), baseState);
					setBlockAndNotifyAdequately(world, pos.add(dx, 0, -dz), baseState);
					setBlockAndNotifyAdequately(world, pos.add(-dx, 0, dz), baseState);
					setBlockAndNotifyAdequately(world, pos.add(-dx, 0, -dz), baseState);
				} else if (dist == rad) {
					// do four at a time for easiness!
					setBlockAndNotifyAdequately(world, pos.add(dx, 0, dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST));
					setBlockAndNotifyAdequately(world, pos.add(dx, 0, -dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST));
					setBlockAndNotifyAdequately(world, pos.add(-dx, 0, dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST));
					setBlockAndNotifyAdequately(world, pos.add(-dx, 0, -dz), baseState.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST));
				}
			}
		}
	}
}
