package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponent;
import twilightforest.world.TFGenerator;

import java.util.List;
import java.util.Random;

import static twilightforest.block.TFBlockProperties.FACING;


public class ComponentTFHollowTreeTrunk extends StructureTFComponent {

	int radius;
	int height;
	int groundLevel = -1;

	public ComponentTFHollowTreeTrunk() {
		super();
	}

	public ComponentTFHollowTreeTrunk(World world, Random rand, int index, int x, int y, int z) {
		super(index);

		height = rand.nextInt(64) + 32;
		radius = rand.nextInt(4) + 1;

		this.setCoordBaseMode(EnumFacing.SOUTH);


		boundingBox = new StructureBoundingBox(x, y, z, (x + radius * 2) + 2, y + height, (z + radius * 2) + 2);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("trunkRadius", this.radius);
		par1NBTTagCompound.setInteger("trunkHeight", this.height);
		par1NBTTagCompound.setInteger("trunkGroundLevel", this.groundLevel);

	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);

		this.radius = par1NBTTagCompound.getInteger("trunkRadius");
		this.height = par1NBTTagCompound.getInteger("trunkHeight");
		this.groundLevel = par1NBTTagCompound.getInteger("trunkGroundLevel");
	}

	/**
	 * Add on the various bits and doo-dads we need to succeed
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List list, Random rand) {
		int index = getComponentType();

		// 3-5 couple branches on the way up...
		int numBranches = rand.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int) (height * rand.nextDouble() * 0.9) + (height / 10);
			double branchRotation = rand.nextDouble();

			makeSmallBranch(list, rand, index + i + 1, branchHeight, 4, branchRotation, 0.35D, true);
		}

		// build the crown
		buildFullCrown(list, rand, index + numBranches + 1);

		// roots
		// 3-5 roots at the bottom
		buildBranchRing(list, rand, index, 3, 2, 6, 0, 0.75D, 0, 3, 5, 3, false);


		// several more taproots
		buildBranchRing(list, rand, index, 1, 2, 8, 0, 0.9D, 0, 3, 5, 3, false);

	}

	/**
	 * Build the crown of the tree
	 */
	protected void buildFullCrown(List list, Random rand, int index) {
		int crownRadius = radius * 4 + 4;
		int bvar = radius + 2;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		index += buildBranchRing(list, rand, index, height - crownRadius, 0, crownRadius, 0, 0.35D, 0, bvar, bvar + 2, 2, true);

		// then, let's do 3-5 medium branches at the crown middle
		index += buildBranchRing(list, rand, index, height - (crownRadius / 2), 0, crownRadius, 0, 0.28D, 0, bvar, bvar + 2, 1, true);

		// finally, let's do 2-4 main branches at the crown top
		index += buildBranchRing(list, rand, index, height, 0, crownRadius, 0, 0.15D, 0, 2, 4, 2, true);

		// and extra finally, let's do 3-6 medium branches going straight up
		index += buildBranchRing(list, rand, index, height, 0, (crownRadius / 2), 0, 0.05D, 0, bvar, bvar + 2, 1, true);
	}

	/**
	 * Build a ring of branches around the tree
	 * size 0 = small, 1 = med, 2 = large, 3 = root
	 */
	protected int buildBranchRing(List list, Random rand, int index, int branchHeight, int heightVar, int length, int lengthVar, double tilt, double tiltVar, int minBranches, int maxBranches, int size, boolean leafy) {
		//let's do this!
		int numBranches = rand.nextInt(maxBranches - minBranches + 1) + minBranches;
		double branchRotation = 1.0 / numBranches;
		double branchOffset = rand.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;
			if (heightVar > 0) {
				dHeight = branchHeight - heightVar + rand.nextInt(2 * heightVar);
			} else {
				dHeight = branchHeight;
			}

			if (size == 2) {
				makeLargeBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 1) {
				makeMedBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 3) {
				makeRoot(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt);
			} else {
				makeSmallBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			}
		}

		return numBranches;
	}


	public void makeSmallBranch(List list, Random rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(index, bSrc.getX(), bSrc.getY(), bSrc.getZ(), branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}

	public void makeMedBranch(List list, Random rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		ComponentTFHollowTreeMedBranch branch = new ComponentTFHollowTreeMedBranch(index, bSrc.getX(), bSrc.getY(), bSrc.getZ(), branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}

	public void makeLargeBranch(List list, Random rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		ComponentTFHollowTreeMedBranch branch = new ComponentTFHollowTreeLargeBranch(index, bSrc.getX(), bSrc.getY(), bSrc.getZ(), branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}


	public void makeRoot(List list, Random rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		ComponentTFHollowTreeRoot branch = new ComponentTFHollowTreeRoot(index, bSrc.getX(), bSrc.getY(), bSrc.getZ(), branchLength, branchRotation, branchAngle, false);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}

	/**
	 * Where should we start this branch?
	 */
	private BlockPos getBranchSrc(int branchHeight, double branchRotation) {
		return TFGenerator.translate(new BlockPos(boundingBox.minX + radius + 1, boundingBox.minY + branchHeight, boundingBox.minZ + radius + 1), radius, branchRotation, 0.5);
	}

	/**
	 * Generate the tree trunk
	 */
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {

		// offset bounding box to average ground level
		if (this.groundLevel < 0) {
			this.groundLevel = this.getAverageGroundLevel(world, sbb);

			if (this.groundLevel < 0) {
				return true;
			}

			this.height = this.boundingBox.maxY - this.groundLevel;
			this.boundingBox.minY = this.groundLevel;
		}

		int hollow = radius / 2;

		for (int dx = 0; dx <= 2 * radius; dx++) {
			for (int dz = 0; dz <= 2 * radius; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx - radius);
				int az = Math.abs(dz - radius);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

				for (int dy = 0; dy <= height; dy++) {
					// fill the body of the trunk
					if (dist <= radius && dist > hollow) {
						this.setBlockState(world, TFBlocks.log.getDefaultState(), dx + 1, dy, dz + 1, sbb); // offset, since our BB is slightly larger than the trunk
					}
				}

				// fill to ground
				if (dist <= radius) {
					this.replaceAirAndLiquidDownwards(world, TFBlocks.log.getDefaultState(), dx + 1, -1, dz + 1, sbb);
				}

				// add vines
				if (dist == hollow && dx == hollow + radius) {
					this.replaceAirAndLiquidDownwards(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.EAST, true), dx + 1, height, dz + 1, sbb);
				}
			}
		}

		// fireflies & cicadas
		int numInsects = random.nextInt(3 * radius) + random.nextInt(3 * radius) + 10;
		for (int i = 0; i <= numInsects; i++) {
			int fHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double fAngle = random.nextDouble();
			addInsect(world, fHeight, fAngle, random, sbb);
		}

		return true;
	}

	/**
	 * Add a random insect
	 */
	protected void addInsect(World world, int fHeight, double fAngle, Random random, StructureBoundingBox sbb) {
		BlockPos bugSpot = TFGenerator.translate(new BlockPos(this.radius + 1, fHeight, this.radius + 1), this.radius + 1, fAngle, 0.5);

		fAngle = fAngle % 1.0;
		EnumFacing insectDirection = EnumFacing.DOWN;

		if (fAngle > 0.875 || fAngle <= 0.125) {
			insectDirection = EnumFacing.SOUTH;
		} else if (fAngle > 0.125 && fAngle <= 0.375) {
			insectDirection = EnumFacing.EAST;
		} else if (fAngle > 0.375 && fAngle <= 0.625) {
			insectDirection = EnumFacing.NORTH;
		} else if (fAngle > 0.625 && fAngle <= 0.875) {
			insectDirection = EnumFacing.WEST;
		}

		final IBlockState block = (random.nextBoolean() ? TFBlocks.firefly : TFBlocks.cicada).getDefaultState();
		addInsect(world, block.withProperty(FACING, insectDirection), bugSpot.getX(), bugSpot.getY(), bugSpot.getZ(), sbb);
	}

	/**
	 * Add an insect if we can at the position specified
	 */
	private void addInsect(World world, IBlockState blockState, int posX, int posY, int posZ, StructureBoundingBox sbb) {
		final BlockPos posWithOffset = getBlockPosWithOffset(posX, posY, posZ);

		if (sbb.isVecInside(posWithOffset) && blockState != null && blockState.getBlock().canPlaceBlockAt(world, posWithOffset)) {
			world.setBlockState(posWithOffset, blockState, 2);
		}
	}

}
