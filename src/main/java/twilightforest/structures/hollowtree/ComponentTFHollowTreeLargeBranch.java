package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
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

	protected ComponentTFHollowTreeLargeBranch(int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(i, sx, sy, sz, length, angle, tilt, leafy);
	}

	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List list, Random rand) {
		int index = getComponentType();

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

//		// make 1-2 small ones near the base
//		int numSmallBranches = rand.nextInt(2) + 1;
//		for(int i = 0; i <= numSmallBranches; i++) {
//			
//			double outVar = (rand.nextDouble() * 0.25) + 0.25;
//			double angleVar = rand.nextDouble() * 0.25 * ((i & 1) == 0 ? 1.0 : -1.0);
//			BlockPos bsrc = TFGenerator.translateCoords(src.posX, src.posY, src.posZ, length * outVar, angle, tilt);
//			
//			makeSmallBranch(list, rand, index + numMedBranches + 1 + i, bsrc.posX, bsrc.posY, bsrc.posZ, Math.max(length * 0.3, 2), angle + angleVar, tilt, leafy);
//		}

		this.hasLeafDungeon = (rand.nextInt(LEAF_DUNGEON_CHANCE) == 0);

		if (this.hasLeafDungeon) {
			makeLeafDungeon(list, rand, index + 1, dest.getX(), dest.getY(), dest.getZ());
		}

	}

	public void makeLeafDungeon(List list, Random rand, int index, int x, int y, int z) {
		ComponentTFHollowTreeLeafDungeon dungeon = new ComponentTFHollowTreeLeafDungeon(index, x, y, z, 4);
		list.add(dungeon);
		dungeon.buildComponent(this, list, rand);
	}

	public void makeMedBranch(List list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		ComponentTFHollowTreeMedBranch branch = new ComponentTFHollowTreeMedBranch(index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
		list.add(branch);
		branch.buildComponent(this, list, rand);
	}


//	public void makeSmallBranch(List list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
//        ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
//        list.add(branch);
//        branch.buildComponent(this, list, rand);
//	}


	/**
	 * Draw this branch
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		BlockPos rsrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rdest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		// main branch
		final IBlockState defaultState = TFBlocks.log.getDefaultState();
		drawBresehnam(world, sbb, rsrc.getX(), rsrc.getY(), rsrc.getZ(), rdest.getX(), rdest.getY(), rdest.getZ(), defaultState.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE));

		// reinforce it
		int reinforcements = 4;
		for (int i = 0; i <= reinforcements; i++) {
			int vx = (i & 2) == 0 ? 1 : 0;
			int vy = (i & 1) == 0 ? 1 : -1;
			int vz = (i & 2) == 0 ? 0 : 1;
			drawBresehnam(world, sbb, rsrc.getX() + vx, rsrc.getY() + vy, rsrc.getZ() + vz, rdest.getX(), rdest.getY(), rdest.getZ(), defaultState.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE));
		}

		// make 1-2 small branches near the base
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));
		int numSmallBranches = decoRNG.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {

			double outVar = (decoRNG.nextFloat() * 0.25F) + 0.25F;
			double angleVar = decoRNG.nextFloat() * 0.25F * ((i & 1) == 0 ? 1.0F : -1.0F);
			BlockPos bsrc = TFGenerator.translate(rsrc, length * outVar, angle, tilt);

			drawSmallBranch(world, sbb, bsrc.getX(), bsrc.getY(), bsrc.getZ(), Math.max(length * 0.3F, 2F), angle + angleVar, tilt, leafy);
		}

		if (leafy && !this.hasLeafDungeon) {
			// leaf blob at the end
			makeLeafBlob(world, sbb, rdest.getX(), rdest.getY(), rdest.getZ(), 3);
		}

		return true;
	}
}
