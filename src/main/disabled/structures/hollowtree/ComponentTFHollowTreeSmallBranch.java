package twilightforest.structures.hollowtree;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class ComponentTFHollowTreeSmallBranch extends ComponentTFHollowTreeMedBranch {

	public ComponentTFHollowTreeSmallBranch(TemplateManager manager, CompoundNBT nbt) {
		super(TFHollowTreePieces.TFHTSB, nbt);
	}

	protected ComponentTFHollowTreeSmallBranch(TFFeature feature, int i, int sx, int sy, int sz, double length, double angle, double tilt, boolean leafy) {
		super(TFHollowTreePieces.TFHTSB, feature, i, sx, sy, sz, length, angle, tilt, leafy);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return this.addComponentParts(world, generator, random, sbb, false);
	}

	@Override
	public boolean addComponentParts(ISeedReader world, ChunkGenerator generator, Random random, MutableBoundingBox sbb, boolean drawLeaves) {

		BlockPos rSrc = src.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);
		BlockPos rDest = dest.add(-boundingBox.minX, -boundingBox.minY, -boundingBox.minZ);

		if (!drawLeaves)
		{
			BlockState log = TFBlocks.oak_wood.get().getDefaultState();
			drawBresehnam(world, sbb, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), log);
		} else {
			int leafRad = random.nextInt(2) + 1;
			makeLeafBlob(world, sbb, rDest.getX(), rDest.getY(), rDest.getZ(), leafRad);
		}
		return true;
	}
}
