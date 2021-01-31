package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFHollowTreeTrunk extends StructureTFTreeComponent {

	int radius;
	int height;
	int groundLevel = -1;

	enum BranchSize {SMALL, MEDIUM, LARGE, ROOT}

	public ComponentTFHollowTreeTrunk(TemplateManager manager, CompoundNBT nbt) {
		super(TFHollowTreePieces.TFHTTr, nbt);
	}

	public ComponentTFHollowTreeTrunk(Random rand, int index, int x, int y, int z) {
		super(TFHollowTreePieces.TFHTTr, TFFeature.NOTHING, index);

		height = rand.nextInt(64) + 32;
		radius = rand.nextInt(4) + 1;

		this.setCoordBaseMode(Direction.SOUTH);

		boundingBox = new MutableBoundingBox(x, y, z, (x + radius * 2) + 2, y + height, (z + radius * 2) + 2);
	}

	/**
	 * Save to NBT
	 * TODO: See super
	 */
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		super.writeStructureToNBT(tagCompound);
//
//		tagCompound.putInt("trunkRadius", this.radius);
//		tagCompound.putInt("trunkHeight", this.height);
//		tagCompound.putInt("trunkGroundLevel", this.groundLevel);
//	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);

		this.radius = tagCompound.getInt("trunkRadius");
		this.height = tagCompound.getInt("trunkHeight");
		this.groundLevel = tagCompound.getInt("trunkGroundLevel");
	}

	/**
	 * Add on the various bits and doo-dads we need to succeed
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random rand) {
		int index = getComponentType();

		// 3-5 couple branches on the way up...
		int numBranches = rand.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int) (height * rand.nextDouble() * 0.5) + (height / 10);
			double branchRotation = rand.nextDouble();

			makeSmallBranch(list, rand, index + i + 1, branchHeight, 4, branchRotation, 0.35D, true);
		}

		// build the crown
		buildFullCrown(list, rand, index + numBranches + 1);

		// roots
		// 3-5 roots at the bottom
		buildBranchRing(list, rand, index, 3, 2, 6, 0.75D, 0.1,3, 5, BranchSize.ROOT, false);

		// several more taproots
		buildBranchRing(list, rand, index, 1, 2, 8, 0.9D, 0.1,3, 5, BranchSize.ROOT, false);
	}

	/**
	 * Build the crown of the tree
	 */
	protected void buildFullCrown(List<StructurePiece> list, Random rand, int index) {
		int crownRadius = radius * 4 + 4;
		int minBranches = radius + 3;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		index += buildBranchRing(list, rand, index, height - crownRadius, 4, crownRadius, 0.35, 0.1, minBranches, minBranches + 2, BranchSize.LARGE, true);

		// then, let's do 3-5 medium branches at the crown middle
		index += buildBranchRing(list, rand, index, height - (crownRadius / 2), 4, (int)(crownRadius * 0.8), 0.25,0.2, minBranches, minBranches + 2, BranchSize.MEDIUM, true);

		// finally, let's do some medium branches going straight up
		index += buildBranchRing(list, rand, index, height - 2, 2, (crownRadius / 2), 0.05, 0.2, minBranches, minBranches + 2, BranchSize.MEDIUM, true);
	}

	/**
	 * Build a ring of branches around the tree
	 */
	protected int buildBranchRing(List<StructurePiece> list, Random rand, int index, int branchHeight, int heightVar, int length, double tilt, double tiltVar, int minBranches, int maxBranches, BranchSize size, boolean leafy) {
		//let's do this!
		int numBranches = rand.nextInt(maxBranches - minBranches + 1) + minBranches;
		double rotationPerBranch = 1.0 / numBranches;
		double branchOffset = rand.nextDouble();
		double branchTilt = tilt + rand.nextDouble() * tiltVar;

		for (int i = 0; i < numBranches; i++) {
			int dHeight = branchHeight - heightVar + (heightVar > 0 ? rand.nextInt(2 * heightVar) : 0);
			double branchRotation = (i * rotationPerBranch) + branchOffset;

			BlockPos pos = getBranchSrc(dHeight, branchRotation);
			StructureTFTreeComponent branch = branchFor(index, pos, length, branchRotation, branchTilt, leafy, size);
			if (!branchIntersectsDungeon(branch, list))
			{
				list.add(branch);
				branch.buildComponent(this, list, rand);
			} else if (size == BranchSize.LARGE || size == BranchSize.MEDIUM) {
				// try at half length?
				branch = branchFor(index, pos, length / 2, branchRotation, branchTilt, leafy, BranchSize.MEDIUM);
				if (!branchIntersectsDungeon(branch, list))
				{
					list.add(branch);
					branch.buildComponent(this, list, rand);
				}
			}
		}

		return numBranches;
	}

	public StructureTFTreeComponent branchFor(int index, BlockPos pos, int branchLength, double branchRotation, double branchAngle, boolean leafy, BranchSize size) {
		switch(size) {
			case LARGE:
				return new ComponentTFHollowTreeLargeBranch(getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), branchLength, branchRotation, branchAngle, leafy);
			case SMALL:
			default:
				return new ComponentTFHollowTreeSmallBranch(getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), branchLength, branchRotation, branchAngle, leafy);
			case MEDIUM:
				return new ComponentTFHollowTreeMedBranch(TFHollowTreePieces.TFHTMB, getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), branchLength, branchRotation, branchAngle, leafy);
			case ROOT:
				return new ComponentTFHollowTreeRoot(getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), branchLength, branchRotation, branchAngle, false);
		}
	}

	public void makeSmallBranch(List<StructurePiece> list, Random rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(getFeatureType(), index, bSrc.getX(), bSrc.getY(), bSrc.getZ(), branchLength, branchRotation, branchAngle, leafy);
		if (!branchIntersectsDungeon(branch, list))
		{
			list.add(branch);
			branch.buildComponent(this, list, rand);
		}
	}

	/**
	 * Where should we start this branch?
	 */
	private BlockPos getBranchSrc(int branchHeight, double branchRotation) {
		return FeatureUtil.translate(new BlockPos(boundingBox.minX + radius + 1, boundingBox.minY + branchHeight, boundingBox.minZ + radius + 1), radius, branchRotation, 0.5);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return this.addComponentParts(world, generator, random, sbb, false);
	}

	/**
	 * Generate the tree trunk
	 */
	@Override
	public boolean addComponentParts(ISeedReader world, ChunkGenerator generator, Random random, MutableBoundingBox sbb, boolean drawLeaves) {

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
						this.setBlockState(world, TFBlocks.oak_log.get().getDefaultState(), dx + 1, dy, dz + 1, sbb); // offset, since our BB is slightly larger than the trunk
					}
				}

				// fill to ground
				if (dist <= radius) {
					this.replaceAirAndLiquidDownwards(world, TFBlocks.oak_log.get().getDefaultState(), dx + 1, -1, dz + 1, sbb);
				}

				// add vines
				if (dist == hollow && dx == hollow + radius) {
					this.replaceAirAndLiquidDownwards(world, Blocks.VINE.getDefaultState().with(VineBlock.EAST, true), dx + 1, height, dz + 1, sbb);
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
	protected void addInsect(ISeedReader world, int fHeight, double fAngle, Random random, MutableBoundingBox sbb) {
		BlockPos bugSpot = FeatureUtil.translate(new BlockPos(this.radius + 1, fHeight, this.radius + 1), this.radius + 1, fAngle, 0.5);

		fAngle = fAngle % 1.0;
		Direction insectDirection = Direction.DOWN;

		if (fAngle > 0.875 || fAngle <= 0.125) {
			insectDirection = Direction.SOUTH;
		} else if (fAngle > 0.125 && fAngle <= 0.375) {
			insectDirection = Direction.EAST;
		} else if (fAngle > 0.375 && fAngle <= 0.625) {
			insectDirection = Direction.NORTH;
		} else if (fAngle > 0.625 && fAngle <= 0.875) {
			insectDirection = Direction.WEST;
		}

		final BlockState block = (random.nextBoolean() ? TFBlocks.firefly : TFBlocks.cicada).get().getDefaultState();
		addInsect(world, block.with(DirectionalBlock.FACING, insectDirection), bugSpot.getX(), bugSpot.getY(), bugSpot.getZ(), sbb);
	}

	/**
	 * Add an insect if we can at the position specified
	 */
	private void addInsect(ISeedReader world, BlockState blockState, int posX, int posY, int posZ, MutableBoundingBox sbb) {
		final BlockPos pos = getBlockPosWithOffset(posX, posY, posZ);
		BlockState whatsThere = world.getBlockState(pos);

		// don't overwrite wood or leaves
		if (sbb.isVecInside(pos) && whatsThere == AIR && blockState.getBlock().getDefaultState().isValidPosition(world, pos)) {
			world.setBlockState(pos, blockState, 2);
		}
	}
}
