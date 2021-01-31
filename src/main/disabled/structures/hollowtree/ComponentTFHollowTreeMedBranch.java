package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFHollowTreeMedBranch extends StructureTFTreeComponent {

	BlockPos src, dest;  // source and destination of branch, array of 3 ints representing x, y, z
	double length;
	double angle;
	double tilt;
	boolean leafy;

	public ComponentTFHollowTreeMedBranch(TemplateManager manager, CompoundNBT nbt) {
		super(TFHollowTreePieces.TFHTMB, nbt);
	}

	public ComponentTFHollowTreeMedBranch(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	protected ComponentTFHollowTreeMedBranch(IStructurePieceType type, TFFeature feature, int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(type, feature, i);

		this.src = new BlockPos(sx, sy, sz);
		this.dest = FeatureUtil.translate(src, length, angle, tilt);

		this.length = length;
		this.angle = angle;
		this.tilt = tilt;
		this.leafy = leafy;

		this.setCoordBaseMode(Direction.SOUTH);

		this.boundingBox = new MutableBoundingBox(src, dest);

		this.boundingBox.expandTo(makeExpandedBB(0.5F, length, angle, tilt));
		this.boundingBox.expandTo(makeExpandedBB(0.1F, length, 0.225, tilt));
		this.boundingBox.expandTo(makeExpandedBB(0.1F, length, -0.225, tilt));
	}

	private MutableBoundingBox makeExpandedBB(double outVar, double branchLength, double branchAngle, double branchTilt) {
		BlockPos branchSrc = FeatureUtil.translate(src, this.length * outVar, this.angle, this.tilt);
		BlockPos branchDest = FeatureUtil.translate(branchSrc, branchLength, branchAngle, branchTilt);

		return new MutableBoundingBox(branchSrc, branchDest);
	}

	/**
	 * Save to NBT
	 * TODO: See super
	 */
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		super.writeStructureToNBT(tagCompound);
//
//		tagCompound.putInt("srcPosX", this.src.getX());
//		tagCompound.putInt("srcPosY", this.src.getY());
//		tagCompound.putInt("srcPosZ", this.src.getZ());
//		tagCompound.putInt("destPosX", this.dest.getX());
//		tagCompound.putInt("destPosY", this.dest.getY());
//		tagCompound.putInt("destPosZ", this.dest.getZ());
//		tagCompound.putDouble("branchLength", this.length);
//		tagCompound.putDouble("branchAngle", this.angle);
//		tagCompound.putDouble("branchTilt", this.tilt);
//		tagCompound.putBoolean("branchLeafy", this.leafy);
//	}

	/**
	 * Load from NBT
	 */
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);

		this.src = new BlockPos(tagCompound.getInt("srcPosX"), tagCompound.getInt("srcPosY"), tagCompound.getInt("srcPosZ"));
		this.dest = new BlockPos(tagCompound.getInt("destPosX"), tagCompound.getInt("destPosY"), tagCompound.getInt("destPosZ"));

		this.length = tagCompound.getDouble("branchLength");
		this.angle = tagCompound.getDouble("branchAngle");
		this.tilt = tagCompound.getDouble("branchTilt");
		this.leafy = tagCompound.getBoolean("branchLeafy");
	}

	public void makeSmallBranch(List<StructurePiece> list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(getFeatureType(), index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}

	@Override
	public boolean func_230383_a_(ISeedReader seed, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return this.addComponentParts(seed, generator, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(ISeedReader seed, ChunkGenerator generator, Random random, MutableBoundingBox sbb, boolean drawLeaves) {
		BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves) {
			BlockState log = TFBlocks.oak_wood.get().getDefaultState();
			drawBresehnam(seed, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
			drawBresehnam(seed, sbb, rSrc.getX(), rSrc.getY() + 1, rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
		}

		Random decoRNG = new Random(seed.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// and several small branches
		int numShoots = Math.min(decoRNG.nextInt(3) + 1, (int) (length / 5));
		double angleInc, angleVar, outVar;

		angleInc = 0.8 / numShoots;

		for (int i = 0; i < numShoots; i++) {

			angleVar = (angleInc * i) - 0.4;
			outVar = (decoRNG.nextDouble() * 0.8) + 0.2;

			BlockPos bSrc = FeatureUtil.translate(rSrc, length * outVar, angle, tilt);

			drawSmallBranch(seed, sbb, bSrc.getX(), bSrc.getY(), bSrc.getZ(), Math.max(length * 0.3F, 2F), angle + angleVar, tilt, drawLeaves);
		}

		// leaves, if we're doing that right now
		if (drawLeaves) {
			int numLeafBalls = Math.min(decoRNG.nextInt(3) + 1, (int) (length / 5));
			for (int i = 0; i < numLeafBalls; i++) {

				double slength = (decoRNG.nextFloat() * 0.6F + 0.2F) * length;
				BlockPos bdst = FeatureUtil.translate(rSrc, slength, angle, tilt);

				makeLeafBlob(seed, sbb, bdst.getX(), bdst.getY(), bdst.getZ(), decoRNG.nextBoolean() ? 2 : 3);
			}

			makeLeafBlob(seed, sbb, rDest.getX(), rDest.getY(), rDest.getZ(), 3);
		}

		return true;
	}

	/**
	 * Draws a line
	 */
	protected void drawBresehnam(ISeedReader world, MutableBoundingBox sbb, int x1, int y1, int z1, int x2, int y2, int z2, BlockState blockState) {
		BlockPos lineCoords[] = FeatureUtil.getBresenhamArrays(x1, y1, z1, x2, y2, z2);

		for (BlockPos coords : lineCoords) {
			this.setBlockState(world, blockState, coords.getX(), coords.getY(), coords.getZ(), sbb);
		}
	}

	/**
	 * Make a leaf blob
	 */
	protected void makeLeafBlob(ISeedReader world, MutableBoundingBox sbb, int sx, int sy, int sz, int radius) {
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
						final BlockState leaves = TFBlocks.oak_leaves.get().getDefaultState();
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
	protected void drawSmallBranch(ISeedReader world, MutableBoundingBox sbb, int sx, int sy, int sz, double branchLength, double branchAngle, double branchTilt, boolean drawLeaves) {
		// draw a line
		BlockPos branchDest = FeatureUtil.translate(new BlockPos(sx, sy, sz), branchLength, branchAngle, branchTilt);

		if (!drawLeaves) {
			BlockState log = TFBlocks.oak_wood.get().getDefaultState();
			drawBresehnam(world, sbb, sx, sy, sz, branchDest.getX(), branchDest.getY(), branchDest.getZ(), log);
		} else {
			makeLeafBlob(world, sbb, branchDest.getX(), branchDest.getY(), branchDest.getZ(), 2);
		}
	}
}
