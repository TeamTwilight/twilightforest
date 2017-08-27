package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.block.TFBlocks;

import java.util.Random;

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

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb)
	{
		return this.addComponentParts(world, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb, boolean drawLeaves) {

		BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves)
		{
			IBlockState log = TFBlocks.log.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
			drawBresehnam(world, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
		} else {
			int leafRad = random.nextInt(2) + 1;
			makeLeafBlob(world, sbb, rDest.getX(), rDest.getY(), rDest.getZ(), leafRad);
		}
		return true;
	}


}
