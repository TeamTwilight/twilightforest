package twilightforest.structures.hollowtree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.TFBlocks;

import static net.minecraft.block.BlockLog.LOG_AXIS;


public class ComponentTFHollowTreeSmallBranch extends
		ComponentTFHollowTreeMedBranch {

	public ComponentTFHollowTreeSmallBranch() {
		super();
	}

	protected ComponentTFHollowTreeSmallBranch(int i, int sx, int sy, int sz,
			double length, double angle, double tilt, boolean leafy) {
		super(i, sx, sy, sz, length, angle, tilt, leafy);
	}
	
	/**
	 * Add a leaf ball to the end
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List list, Random rand) {
//		int index = getComponentType();
//		
//		if (leafy) {
//			int leafRad = rand.nextInt(2) + 1;
//			ComponentTFLeafSphere leafBlob = new ComponentTFLeafSphere(index + 1, dest.posX, dest.posY, dest.posZ, leafRad);
//	        list.add(leafBlob);
//	        leafBlob.buildComponent(this, list, rand); // doesn't really need to be here for leaves.
//		}

	}
	
	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {

		BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		IBlockState log = TFBlocks.log.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);

		drawBresehnam(world, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
		
		// with leaves!
		if (leafy) {
			int leafRad = random.nextInt(2) + 1;
			makeLeafBlob(world, sbb, rDest.getX(), rDest.getY(), rDest.getZ(), leafRad);

		}


		return true;
	}


}
