package twilightforest.world;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFSwarmSpider;

import java.util.Random;

public class TFGenHollowTree extends TFGenerator {

	private static final int LEAF_DUNGEON_CHANCE = 8;

	protected IBlockState treeState = TFBlocks.twilight_log.getDefaultState();
	protected IBlockState branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
	protected IBlockState leafState = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false);
	protected IBlockState rootState = TFBlocks.root.getDefaultState();

	public TFGenHollowTree() {
		this(false);
	}

	public TFGenHollowTree(boolean par1) {
		super(par1);
	}

	public static boolean canGrowInto(Block blockType) {
		Material material = blockType.getDefaultState().getMaterial();
		return material == Material.AIR || material == Material.LEAVES || material == Material.WATER || material == Material.LAVA || blockType instanceof IGrowable || blockType instanceof BlockDirt || blockType instanceof BlockLog || blockType instanceof BlockBush || blockType instanceof BlockVine;
	}

	@Override
	protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
		if (canGrowInto(worldIn.getBlockState(pos).getBlock()))
			super.setBlockAndNotifyAdequately(worldIn, pos, state);
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {

		int height = random.nextInt(64) + 32;
		int diameter = random.nextInt(4) + 1;


		// do we have enough height?
		if (pos.getY() < 1 || pos.getY() + height + diameter > TFWorld.MAXHEIGHT) {
			return false;
		}

//		// are we going to hit something?
//		for (int dx = -diameter; dx <= diameter; dx++)
//		{
//			for (int dz = -diameter; dz <= diameter; dz++)
//			{
//				for (int dy = 1; dy <= height; dy++)
//				{
//					int whatsThere = world.getBlock(dx + x, dy + y, dz + z);
//					if(whatsThere != 0 && whatsThere != Blocks.LEAVES)
//					{
//						return false;
//					}
//				}
//			}
//		}
		// check the top too
		int crownRadius = diameter * 4 + 8;
		for (int dx = -crownRadius; dx <= crownRadius; dx++) {
			for (int dz = -crownRadius; dz <= crownRadius; dz++) {
				for (int dy = height - crownRadius; dy <= height + crownRadius; dy++) {
					Block whatsThere = world.getBlockState(pos.add(dx, dy, dz)).getBlock();
					if (whatsThere != Blocks.AIR && whatsThere != Blocks.LEAVES) {
						return false;
					}
				}
			}
		}


		// make a tree!

		// check if we're on dirt or grass
		Block j1 = world.getBlockState(pos.down()).getBlock();
		if (j1 != Blocks.GRASS && j1 != Blocks.DIRT) {
			return false;
		}

		// build the trunk
		buildTrunk(world, random, pos, diameter, height);

		// fireflies
		int numFireflies = random.nextInt(3 * diameter) + 5;
		for (int i = 0; i <= numFireflies; i++) {
			int fHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double fAngle = random.nextDouble();
			addFirefly(world, pos, diameter, fHeight, fAngle);
		}

		// cicadas
		numFireflies = random.nextInt(3 * diameter) + 5;
		for (int i = 0; i <= numFireflies; i++) {
			int fHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double fAngle = random.nextDouble();
			addCicada(world, pos, diameter, fHeight, fAngle);
		}

		// build the crown
		buildFullCrown(world, random, pos, diameter, height);


		// 3-5 couple branches on the way up...
		int numBranches = random.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double branchRotation = random.nextDouble();
			makeSmallBranch(world, random, pos, diameter, branchHeight, 4, branchRotation, 0.35D, true);
		}

		// 3-5 roots at the bottom
		buildBranchRing(world, random, pos, diameter, 3, 2, 6, 0, 0.75D, 0, 3, 5, 3, false);


		// several more taproots
		buildBranchRing(world, random, pos, diameter, 1, 2, 8, 0, 0.9D, 0, 3, 5, 3, false);

		return true;
	}

	/**
	 * Build the crown of the tree
	 *
	 * @param diameter
	 * @param height
	 */
	protected void buildFullCrown(World world, Random random, BlockPos pos, int diameter, int height) {
		int crownRadius = diameter * 4 + 4;
		int bvar = diameter + 2;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		buildBranchRing(world, random, pos, diameter, height - crownRadius, 0, crownRadius, 0, 0.35D, 0, bvar, bvar + 2, 2, true);

		// then, let's do 3-5 medium branches at the crown middle
		buildBranchRing(world, random, pos, diameter, height - (crownRadius / 2), 0, crownRadius, 0, 0.28D, 0, bvar, bvar + 2, 1, true);

		// finally, let's do 2-4 main branches at the crown top
		buildBranchRing(world, random, pos, diameter, height, 0, crownRadius, 0, 0.15D, 0, 2, 4, 2, true);

		// and extra finally, let's do 3-6 medium branches going straight up
		buildBranchRing(world, random, pos, diameter, height, 0, (crownRadius / 2), 0, 0.05D, 0, bvar, bvar + 2, 1, true);

		// this glass sphere approximates where we want our crown		
		//drawBlob(x, y + height, z, (byte)crownRadius, (byte)Blocks.GLASS, false);

	}

	/**
	 * Build the crown of the tree.  This builds a smaller crown, since the large ones were causing some performance issues
	 *
	 * @param height
	 */
	protected void buildWeakCrown(World world, Random random, BlockPos pos, int diameter, int height) {
		int crownRadius = 8;
		int bvar = 2;

		// 3-5 medium branches starting at the bottom of the crown
		buildBranchRing(world, random, pos, diameter, height - crownRadius, 0, crownRadius, 0, 0.35D, 0, bvar, bvar + 2, 1, true);

		// 3-5 medium branches at the crown middle
		buildBranchRing(world, random, pos, diameter, height - (crownRadius / 2), 0, crownRadius, 0, 0.28D, 0, bvar, bvar + 2, 1, true);

		// 2-4 medium branches at the crown top
		buildBranchRing(world, random, pos, diameter, height, 0, crownRadius, 0, 0.15D, 0, 2, 4, 1, true);

		// 3-6 medium branches going straight up
		buildBranchRing(world, random, pos, diameter, height, 0, (crownRadius / 2), 0, 0.05D, 0, bvar, bvar + 2, 1, true);
	}

	/**
	 * Build a ring of branches around the tree
	 * size 0 = small, 1 = med, 2 = large, 3 = root
	 */
	protected void buildBranchRing(World world, Random random, BlockPos pos, int diameter, int branchHeight, int heightVar, int length, int lengthVar, double tilt, double tiltVar, int minBranches, int maxBranches, int size, boolean leafy) {
		//let's do this!
		int numBranches = random.nextInt(maxBranches - minBranches) + minBranches;
		;
		double branchRotation = 1.0 / (numBranches + 1);
		double branchOffset = random.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;
			if (heightVar > 0) {
				dHeight = branchHeight - heightVar + random.nextInt(2 * heightVar);
			} else {
				dHeight = branchHeight;
			}

			if (size == 2) {
				makeLargeBranch(world, random, pos, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 1) {
				makeMedBranch(world, random, pos, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 3) {
				makeRoot(world, random, pos, diameter, dHeight, length, i * branchRotation + branchOffset, tilt);
			} else {
				makeSmallBranch(world, random, pos, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			}
		}
	}


	/**
	 * This function builds the hollow trunk of the tree
	 */
	protected void buildTrunk(World world, Random random, BlockPos pos, int diameter, int height) {

		int hollow = diameter / 2;


		// go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				for (int dy = -4; dy < 0; dy++) {
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					if (dist <= diameter) {
						BlockPos dPos = pos.add(dx, dy, dz);
						if (hasAirAround(world, dPos)) {
							this.setBlockAndNotifyAdequately(world, dPos, dist > hollow ? treeState : branchState);
						} else {
							this.setBlockAndNotifyAdequately(world, dPos, rootState);
						}
					}

				}
			}
		}

		// build the trunk upwards
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				for (int dy = 0; dy <= height; dy++) {
					BlockPos dPos = pos.add(dx, dy, dz);
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					// make a trunk!
					if (dist <= diameter && dist > hollow) {
						setBlockAndNotifyAdequately(world, dPos, treeState);
					}


					// fill it with lava!
					if (dist <= hollow) {
						// just kidding!
						//world.setBlock(dx + x, dy + y, dz + z, Blocks.LAVA);
					}

					// how about a ladder?  is that okay?
					if (dist == hollow && dx == hollow) {
//						putBlockAndMetadata(dx + x, dy + y, dz + z, Blocks.LADDER,  4, true);
						setBlockAndNotifyAdequately(world, dPos, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true));
					}
				}
			}
		}

	}

	/**
	 * Make a branch!
	 */
	protected void makeMedBranch(World world, Random random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy) {
		BlockPos src = translate(pos.up(branchHeight), diameter, angle, 0.5);
		makeMedBranch(world, random, src, length, angle, tilt, leafy);
	}

	/**
	 * Make a branch!
	 */
	protected void makeMedBranch(World world, Random random, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		BlockPos dest = translate(src, length, angle, tilt);

		drawBresehnam(this, world, src, dest, branchState);

		// with leaves!

		if (leafy) {
			/*
			int numLeafBalls = random.nextInt(2) + 1;
			for(int i = 0; i <= numLeafBalls; i++) {

				double slength = random.nextDouble() * 0.6 + 0.2;
				int[] bdst = translate(src[0], src[1], src[2], slength, angle, tilt);


				drawBlob(bdst[0], bdst[1], bdst[2], 2, leafBlock, false);		
			}
			*/

			// and a blob at the end
			drawLeafBlob(this, world, dest, 2, leafState);
		}


		// and several small branches

		int numShoots = random.nextInt(2) + 1;
		double angleInc, angleVar, outVar, tiltVar;

		angleInc = 0.8 / numShoots;

		for (int i = 0; i <= numShoots; i++) {

			angleVar = (angleInc * i) - 0.4;
			outVar = (random.nextDouble() * 0.8) + 0.2;
			tiltVar = (random.nextDouble() * 0.75) + 0.15;

			BlockPos bsrc = translate(src, length * outVar, angle, tilt);
			double slength = length * 0.4;

			makeSmallBranch(world, random, bsrc, slength, angle + angleVar, tilt * tiltVar, leafy);
		}
	}

	/**
	 * Make a small branch with a leaf blob at the end
	 */
	protected void makeSmallBranch(World world, Random random, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		BlockPos dest = translate(src, length, angle, tilt);

		drawBresehnam(this, world, src, dest, branchState);

		if (leafy) {
			byte leafRad = (byte) (random.nextInt(2) + 1);
			drawLeafBlob(this, world, dest, leafRad, leafState);
		}
	}

	/**
	 * Make a small branch at a certain height
	 */
	protected void makeSmallBranch(World world, Random random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy) {
		BlockPos src = translate(pos.up(branchHeight), diameter, angle, 0.5);
		makeSmallBranch(world, random, src, length, angle, tilt, leafy);
	}

	/**
	 * Make a root
	 */
	protected void makeRoot(World world, Random random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt) {
		BlockPos src = translate(pos.up(branchHeight), diameter, angle, 0.5);
		BlockPos dest = translate(src, length, angle, tilt);

		BlockPos[] lineArray = getBresehnamArrays(src, dest);
		boolean stillAboveGround = true;
		for (BlockPos coord : lineArray) {
			if (stillAboveGround && hasAirAround(world, coord)) {
				this.setBlockAndNotifyAdequately(world, coord, branchState);
				this.setBlockAndNotifyAdequately(world, coord.down(), branchState);
			} else {
				this.setBlockAndNotifyAdequately(world, coord, rootState);
				this.setBlockAndNotifyAdequately(world, coord.down(), rootState);
				stillAboveGround = false;
			}
		}
	}

	/**
	 * Make a large, branching "base" branch in a specific location.
	 * <p>
	 * The large branch will have 1-4 medium branches and several small branches too
	 */
	protected void makeLargeBranch(World world, Random random, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		BlockPos dest = translate(src, length, angle, tilt);

		// draw the main branch
		drawBresehnam(this, world, src, dest, branchState);

		// reinforce it
		//drawBresehnam(src[0], src[1] + 1, src[2], dest[0], dest[1], dest[2], treeBlock, true);
		int reinforcements = random.nextInt(3);
		for (int i = 0; i <= reinforcements; i++) {
			int vx = (i & 2) == 0 ? 1 : 0;
			int vy = (i & 1) == 0 ? 1 : -1;
			int vz = (i & 2) == 0 ? 0 : 1;
			drawBresehnam(this, world, src.add(vx, vy, vz), dest, branchState);
		}

		if (leafy) {
			// add a leaf blob at the end
			drawLeafBlob(this, world, dest.up(), 3, leafState);
		}

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = random.nextInt((int) (length / 6)) + random.nextInt(2) + 1;

		for (int i = 0; i <= numMedBranches; i++) {

			double outVar = (random.nextDouble() * 0.3) + 0.3;
			double angleVar = random.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = translate(src, length * outVar, angle, tilt);

			makeMedBranch(world, random, bsrc, length * 0.6, angle + angleVar, tilt, leafy);
		}

		// make 1-2 small ones near the base
		int numSmallBranches = random.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {

			double outVar = (random.nextDouble() * 0.25) + 0.25;
			double angleVar = random.nextDouble() * 0.25 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = translate(src, length * outVar, angle, tilt);

			makeSmallBranch(world, random, bsrc, Math.max(length * 0.3, 2), angle + angleVar, tilt, leafy);
		}

		if (random.nextInt(LEAF_DUNGEON_CHANCE) == 0) {
			makeLeafDungeon(world, random, dest.up());
		}

	}


	private void makeLeafDungeon(World world, Random random, BlockPos pos) {
		// make leaves
		drawLeafBlob(this, world, pos, 4, leafState);
		// wood support
		drawBlob(this, world, pos, 3, branchState);
		// air
		drawBlob(this, world, pos, 2, Blocks.AIR.getDefaultState());


		// spawner
		world.setBlockState(pos.up(), Blocks.MOB_SPAWNER.getDefaultState(), 2);
		TileEntityMobSpawner ms = (TileEntityMobSpawner) world.getTileEntity(pos.up());
		if (ms != null) {
			ms.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityTFSwarmSpider.class));
		}

		// treasure chests?
		makeLeafDungeonChest(world, random, pos);
	}

	private void makeLeafDungeonChest(World world, Random random, BlockPos pos) {
		pos = pos.offset(EnumFacing.HORIZONTALS[random.nextInt(4)]);
		TFTreasure.tree_cache.generateChest(world, pos.down(), false);
	}

	/**
	 * Make a large, branching "base" branch off of the tree
	 */
	protected void makeLargeBranch(World world, Random random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy) {
		BlockPos src = translate(pos.up(branchHeight), diameter, angle, 0.5);
		makeLargeBranch(world, random, src, length, angle, tilt, leafy);
	}

	/**
	 * Add a firefly at the specified height and angle.
	 */
	protected void addFirefly(World world, BlockPos pos, int diameter, int fHeight, double fAngle) {
		BlockPos src = translate(pos.up(fHeight), diameter + 1, fAngle, 0.5);

		fAngle = fAngle % 1.0;
		EnumFacing facing = EnumFacing.EAST;

		if (fAngle > 0.875 || fAngle <= 0.125) {
			facing = EnumFacing.SOUTH;
		} else if (fAngle > 0.125 && fAngle <= 0.375) {
			facing = EnumFacing.EAST;
		} else if (fAngle > 0.375 && fAngle <= 0.625) {
			facing = EnumFacing.NORTH;
		} else if (fAngle > 0.625 && fAngle <= 0.875) {
			facing = EnumFacing.WEST;
		}

		if (TFBlocks.firefly.canPlaceBlockAt(world, src)) {
			setBlockAndNotifyAdequately(world, src, TFBlocks.firefly.getDefaultState().withProperty(BlockDirectional.FACING, facing));
		}
	}

	protected void addCicada(World world, BlockPos pos, int diameter, int fHeight, double fAngle) {
		BlockPos src = translate(pos.up(fHeight), diameter + 1, fAngle, 0.5);

		fAngle = fAngle % 1.0;
		EnumFacing facing = EnumFacing.EAST;

		if (fAngle > 0.875 || fAngle <= 0.125) {
			facing = EnumFacing.SOUTH;
		} else if (fAngle > 0.125 && fAngle <= 0.375) {
			facing = EnumFacing.EAST;
		} else if (fAngle > 0.375 && fAngle <= 0.625) {
			facing = EnumFacing.NORTH;
		} else if (fAngle > 0.625 && fAngle <= 0.875) {
			facing = EnumFacing.WEST;
		}

		if (TFBlocks.cicada.canPlaceBlockAt(world, src)) {
			setBlockAndNotifyAdequately(world, src, TFBlocks.cicada.getDefaultState().withProperty(BlockDirectional.FACING, facing));
		}
	}


}