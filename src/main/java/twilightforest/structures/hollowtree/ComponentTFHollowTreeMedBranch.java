package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFGenerator;

import java.util.List;
import java.util.Random;

import static net.minecraft.block.BlockLog.LOG_AXIS;


public class ComponentTFHollowTreeMedBranch extends StructureTFTreeComponent {

	BlockPos src, dest;  // source and destination of branch, array of 3 ints representing x, y, z
	double length;
	double angle;
	double tilt;
	boolean leafy;


	public ComponentTFHollowTreeMedBranch() {
		super();
	}

	protected ComponentTFHollowTreeMedBranch(int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(i);

		this.src = new BlockPos(sx, sy, sz);
		this.dest = TFGenerator.translate(src, length, angle, tilt);

		this.length = length;
		this.angle = angle;
		this.tilt = tilt;
		this.leafy = leafy;

		this.setCoordBaseMode(EnumFacing.SOUTH);

		this.boundingBox = new StructureBoundingBox(src, dest);

		this.boundingBox.expandTo(makeExpandedBB(0.5F, length, angle, tilt));
		this.boundingBox.expandTo(makeExpandedBB(0.1F, length, 0.225, tilt));
		this.boundingBox.expandTo(makeExpandedBB(0.1F, length, -0.225, tilt));
	}

	private StructureBoundingBox makeExpandedBB(double outVar, double branchLength, double branchAngle, double branchTilt) {
		BlockPos branchSrc = TFGenerator.translate(src, this.length * outVar, this.angle, this.tilt);
		BlockPos branchDest = TFGenerator.translate(branchSrc, branchLength, branchAngle, branchTilt);

		return new StructureBoundingBox(branchSrc, branchDest);
	}


	/**
	 * Save to NBT
	 */
	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setInteger("srcPosX", this.src.getX());
		par1NBTTagCompound.setInteger("srcPosY", this.src.getY());
		par1NBTTagCompound.setInteger("srcPosZ", this.src.getZ());
		par1NBTTagCompound.setInteger("destPosX", this.dest.getX());
		par1NBTTagCompound.setInteger("destPosY", this.dest.getY());
		par1NBTTagCompound.setInteger("destPosZ", this.dest.getZ());
		par1NBTTagCompound.setDouble("branchLength", this.length);
		par1NBTTagCompound.setDouble("branchAngle", this.angle);
		par1NBTTagCompound.setDouble("branchTilt", this.tilt);
		par1NBTTagCompound.setBoolean("branchLeafy", this.leafy);

	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);


		this.src = new BlockPos(par1NBTTagCompound.getInteger("srcPosX"), par1NBTTagCompound.getInteger("srcPosY"), par1NBTTagCompound.getInteger("srcPosZ"));
		this.dest = new BlockPos(par1NBTTagCompound.getInteger("destPosX"), par1NBTTagCompound.getInteger("destPosY"), par1NBTTagCompound.getInteger("destPosZ"));

		this.length = par1NBTTagCompound.getDouble("branchLength");
		this.angle = par1NBTTagCompound.getDouble("branchAngle");
		this.tilt = par1NBTTagCompound.getDouble("branchTilt");
		this.leafy = par1NBTTagCompound.getBoolean("branchLeafy");
	}

	public void makeSmallBranch(List<StructureComponent> list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb)
	{
		return this.addComponentParts(world, random, sbb, false);
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb, boolean drawLeaves) {

		BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves)
		{
			IBlockState log = TFBlocks.log.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
			drawBresehnam(world, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
			drawBresehnam(world, sbb, rSrc.getX(), rSrc.getY() + 1, rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
		}

		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// and several small branches
		int numShoots = Math.min(decoRNG.nextInt(3) + 1, (int) (length / 5));
		double angleInc, angleVar, outVar;

		angleInc = 0.8 / numShoots;

		for (int i = 0; i < numShoots; i++) {

			angleVar = (angleInc * i) - 0.4;
			outVar = (decoRNG.nextDouble() * 0.8) + 0.2;

			BlockPos bSrc = TFGenerator.translate(rSrc, length * outVar, angle, tilt);

			drawSmallBranch(world, sbb, bSrc.getX(), bSrc.getY(), bSrc.getZ(), Math.max(length * 0.3F, 2F), angle + angleVar, tilt, drawLeaves);
		}

		// leaves, if we're doing that right now
		if (drawLeaves) {
			int numLeafBalls = Math.min(decoRNG.nextInt(3) + 1, (int) (length / 5));
			for (int i = 0; i < numLeafBalls; i++) {

				double slength = (decoRNG.nextFloat() * 0.6F + 0.2F) * length;
				BlockPos bdst = TFGenerator.translate(rSrc, slength, angle, tilt);

				makeLeafBlob(world, sbb, bdst.getX(), bdst.getY(), bdst.getZ(), decoRNG.nextBoolean() ? 2 : 3);
			}
			makeLeafBlob(world, sbb, rDest.getX(), rDest.getY(), rDest.getZ(), 3);

		}

		return true;
	}


	/**
	 * Draws a line
	 */
	protected void drawBresehnam(World world, StructureBoundingBox sbb, int x1, int y1, int z1, int x2, int y2, int z2, IBlockState blockState) {
		BlockPos lineCoords[] = TFGenerator.getBresehnamArrays(x1, y1, z1, x2, y2, z2);

		for (BlockPos coords : lineCoords) {
			this.setBlockState(world, blockState, coords.getX(), coords.getY(), coords.getZ(), sbb);
		}
	}

	/**
	 * Make a leaf blob
	 */
	protected void makeLeafBlob(World world, StructureBoundingBox sbb, int sx, int sy, int sz, int radius) {
		// then trace out a quadrant
		for (int dx = 0; dx <= radius; dx++) {
			for (int dy = 0; dy <= radius; dy++) {
				for (int dz = 0; dz <= radius; dz++) {
					// determine how far we are from the center.
					int dist = 0;

					if (dx >= dy && dx >= dz) {
						dist = (int) (dx + ((Math.max(dy, dz) * 0.5F) + (Math.min(dy, dz) * 0.25F)));
					} else if (dy >= dx && dy >= dz) {
						dist = (int) (dy + ((Math.max(dx, dz) * 0.5F) + (Math.min(dx, dz) * 0.25F)));
					} else {
						dist = (int) (dz + ((Math.max(dx, dy) * 0.5F) + (Math.min(dx, dy) * 0.25F)));
					}


					// if we're inside the blob, fill it
					if (dist <= radius) {
						// do eight at a time for easiness!
						final IBlockState leaves = TFBlocks.leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false);
						placeLeafBlock(world, leaves, sx + dx, sy + dy, sz + dz, sbb);
						placeLeafBlock(world, leaves, sx + dx, sy + dy, sz - dz, sbb);
						placeLeafBlock(world, leaves, sx - dx, sy + dy, sz + dz, sbb);
						placeLeafBlock(world, leaves, sx - dx, sy + dy, sz - dz, sbb);
						placeLeafBlock(world, leaves, sx + dx, sy - dy, sz + dz, sbb);
						placeLeafBlock(world, leaves, sx + dx, sy - dy, sz - dz, sbb);
						placeLeafBlock(world, leaves, sx - dx, sy - dy, sz + dz, sbb);
						placeLeafBlock(world, leaves, sx - dx, sy - dy, sz - dz, sbb);

					}
				}
			}
		}
	}

	/**
	 * This is like the small branch component, but we're just drawing it directly into the world
	 */
	protected void drawSmallBranch(World world, StructureBoundingBox sbb, int sx, int sy, int sz, double branchLength, double branchAngle, double branchTilt, boolean drawLeaves) {
		// draw a line
		BlockPos branchDest = TFGenerator.translate(new BlockPos(sx, sy, sz), branchLength, branchAngle, branchTilt);

		if (!drawLeaves)
		{
			IBlockState log = TFBlocks.log.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
			drawBresehnam(world, sbb, sx, sy, sz, branchDest.getX(), branchDest.getY(), branchDest.getZ(), log);
		}
		else
		{
			makeLeafBlob(world, sbb, branchDest.getX(), branchDest.getY(), branchDest.getZ(), 2);
		}
	}


}
