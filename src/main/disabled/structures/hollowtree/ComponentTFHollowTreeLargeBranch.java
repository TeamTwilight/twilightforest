package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
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

public class ComponentTFHollowTreeLargeBranch extends ComponentTFHollowTreeMedBranch {

	private static final int LEAF_DUNGEON_CHANCE = 8;
	public boolean hasLeafDungeon = false;

	public ComponentTFHollowTreeLargeBranch(TemplateManager manager, CompoundNBT nbt) {
		super(TFHollowTreePieces.TFHTLB, nbt);
	}

	protected ComponentTFHollowTreeLargeBranch(TFFeature feature, int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(TFHollowTreePieces.TFHTLB, feature, i, sx, sy, sz, length, angle, tilt, leafy);
	}

	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random rand) {
		int index = getComponentType();

		this.hasLeafDungeon = (rand.nextInt(LEAF_DUNGEON_CHANCE) == 0);

		if (this.hasLeafDungeon) {
			// go 1-2 blocks past the end of the branch
			BlockPos dpos = FeatureUtil.translate(this.dest, 2, this.angle, this.tilt);
			makeLeafDungeon(list, rand, index + 1, dpos);
		}

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = rand.nextInt((int) (length / 6)) + (int) (length / 8);

		for (int i = 0; i <= numMedBranches; i++) {

			double outVar = (rand.nextDouble() * 0.3) + 0.3;
			double angleVar = rand.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = FeatureUtil.translate(src, length * outVar, angle, tilt);

			makeMedBranch(list, rand, index + 2 + i, bsrc.getX(), bsrc.getY(), bsrc.getZ(), length * 0.6, angle + angleVar, tilt, leafy);
		}
	}

	public void makeLeafDungeon(List<StructurePiece> list, Random rand, int index, BlockPos pos) {
		ComponentTFHollowTreeLeafDungeon dungeon = new ComponentTFHollowTreeLeafDungeon(getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), 4);
		list.add(dungeon);
		dungeon.buildComponent(this, list, rand);
	}

	public void makeMedBranch(List<StructurePiece> list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		ComponentTFHollowTreeMedBranch branch = new ComponentTFHollowTreeMedBranch(TFHollowTreePieces.TFHTMB, getFeatureType(), index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
		if (!branchIntersectsDungeon(branch, list))
		{
			list.add(branch);
			branch.buildComponent(this, list, rand);
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader seed, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return this.addComponentParts(seed, generator, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(ISeedReader world, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, boolean drawLeaves) {
		BlockPos rsrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rdest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves) {
			// main branch
			final BlockState defaultState = TFBlocks.oak_wood.get().getDefaultState();
			drawBresehnam(world, sbb, rsrc.getX(), rsrc.getY(), rsrc.getZ(), rdest.getX(), rdest.getY(), rdest.getZ(), defaultState);

			// reinforce it
			int reinforcements = 4;
			for (int i = 0; i <= reinforcements; i++) {
				int vx = (i & 2) == 0 ? 1 : 0;
				int vy = (i & 1) == 0 ? 1 : -1;
				int vz = (i & 2) == 0 ? 0 : 1;
				drawBresehnam(world, sbb, rsrc.getX() + vx, rsrc.getY() + vy, rsrc.getZ() + vz, rdest.getX(), rdest.getY(), rdest.getZ(), defaultState);
			}
		}

		// make 1-2 small branches near the base
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
		int numSmallBranches = decoRNG.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {

			double outVar = (decoRNG.nextFloat() * 0.25F) + 0.25F;
			double angleVar = decoRNG.nextFloat() * 0.25F * ((i & 1) == 0 ? 1.0F : -1.0F);
			BlockPos bsrc = FeatureUtil.translate(rsrc, length * outVar, angle, tilt);

			drawSmallBranch(world, sbb, bsrc.getX(), bsrc.getY(), bsrc.getZ(), Math.max(length * 0.3F, 2F), angle + angleVar, tilt, drawLeaves);
		}

		if (drawLeaves && !this.hasLeafDungeon) {
			// leaf blob at the end
			makeLeafBlob(world, sbb, rdest.getX(), rdest.getY(), rdest.getZ(), 3);
		}

		return true;
	}
}
