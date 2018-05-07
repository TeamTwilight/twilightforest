package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFGenerator;

import java.util.List;
import java.util.Random;

import static net.minecraft.block.BlockLog.LOG_AXIS;


public class ComponentTFHollowTreeLargeBranch extends ComponentTFHollowTreeMedBranch {

	private static final int LEAF_DUNGEON_CHANCE = 8;
	public boolean hasLeafDungeon = false;

	public ComponentTFHollowTreeLargeBranch() {
		super();
	}

	protected ComponentTFHollowTreeLargeBranch(TFFeature feature, int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(feature, i, sx, sy, sz, length, angle, tilt, leafy);
	}

	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random rand) {
		int index = getComponentType();

		this.hasLeafDungeon = (rand.nextInt(LEAF_DUNGEON_CHANCE) == 0);

		if (this.hasLeafDungeon) {
			// go 1-2 blocks past the end of the branch
			BlockPos dpos = TFGenerator.translate(this.dest, 2, this.angle, this.tilt);
			makeLeafDungeon(list, rand, index + 1, dpos);
		}

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = rand.nextInt((int) (length / 6)) + (int) (length / 8);

		for (int i = 0; i <= numMedBranches; i++) {

			double outVar = (rand.nextDouble() * 0.3) + 0.3;
			double angleVar = rand.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = TFGenerator.translate(src, length * outVar, angle, tilt);

			makeMedBranch(list, rand, index + 2 + i, bsrc.getX(), bsrc.getY(), bsrc.getZ(), length * 0.6, angle + angleVar, tilt, leafy);
		}
	}

	public void makeLeafDungeon(List<StructureComponent> list, Random rand, int index, BlockPos pos) {
		ComponentTFHollowTreeLeafDungeon dungeon = new ComponentTFHollowTreeLeafDungeon(getFeatureType(), index, pos.getX(), pos.getY(), pos.getZ(), 4);
		list.add(dungeon);
		dungeon.buildComponent(this, list, rand);
	}

	public void makeMedBranch(List<StructureComponent> list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		ComponentTFHollowTreeMedBranch branch = new ComponentTFHollowTreeMedBranch(getFeatureType(), index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
		if (!branchIntersectsDungeon(branch, list))
		{
			list.add(branch);
			branch.buildComponent(this, list, rand);
		}
	}


	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb)
	{
		return this.addComponentParts(world, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb, boolean drawLeaves) {

		BlockPos rsrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rdest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves)
		{
			// main branch
			final IBlockState defaultState = TFBlocks.twilight_log.getDefaultState();
			drawBresehnam(world, sbb, rsrc.getX(), rsrc.getY(), rsrc.getZ(), rdest.getX(), rdest.getY(), rdest.getZ(), defaultState.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE));

			// reinforce it
			int reinforcements = 4;
			for (int i = 0; i <= reinforcements; i++)
			{
				int vx = (i & 2) == 0 ? 1 : 0;
				int vy = (i & 1) == 0 ? 1 : -1;
				int vz = (i & 2) == 0 ? 0 : 1;
				drawBresehnam(world, sbb, rsrc.getX() + vx, rsrc.getY() + vy, rsrc.getZ() + vz, rdest.getX(), rdest.getY(), rdest.getZ(), defaultState.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE));
			}
		}

		// make 1-2 small branches near the base
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
		int numSmallBranches = decoRNG.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {

			double outVar = (decoRNG.nextFloat() * 0.25F) + 0.25F;
			double angleVar = decoRNG.nextFloat() * 0.25F * ((i & 1) == 0 ? 1.0F : -1.0F);
			BlockPos bsrc = TFGenerator.translate(rsrc, length * outVar, angle, tilt);

			drawSmallBranch(world, sbb, bsrc.getX(), bsrc.getY(), bsrc.getZ(), Math.max(length * 0.3F, 2F), angle + angleVar, tilt, drawLeaves);
		}

		if (drawLeaves && !this.hasLeafDungeon) {
			// leaf blob at the end
			makeLeafBlob(world, sbb, rdest.getX(), rdest.getY(), rdest.getZ(), 3);
		}

		return true;
	}
}
