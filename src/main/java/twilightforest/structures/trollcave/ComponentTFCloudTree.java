package twilightforest.structures.trollcave;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;


public class ComponentTFCloudTree extends StructureTFComponentOld {

	public ComponentTFCloudTree() {
	}

	public ComponentTFCloudTree(int index, int x, int y, int z) {
		this.setCoordBaseMode(EnumFacing.SOUTH);

		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y >> 2) << 2;
		z = (z >> 2) << 2;

		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 20, 28, 20, EnumFacing.SOUTH);

		// spawn list!
		this.spawnListIndex = 1;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		// leaves
		this.fillWithBlocks(world, sbb, 0, 12, 0, 19, 19, 19, TFBlocks.giant_leaves.getDefaultState(), TFBlocks.giant_leaves.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 20, 4, 15, 23, 15, TFBlocks.giant_leaves.getDefaultState(), TFBlocks.giant_leaves.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 8, 24, 4, 11, 27, 15, TFBlocks.giant_leaves.getDefaultState(), TFBlocks.giant_leaves.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 24, 8, 15, 27, 11, TFBlocks.giant_leaves.getDefaultState(), TFBlocks.giant_leaves.getDefaultState(), false);

		// trunk
		this.fillWithBlocks(world, sbb, 8, 0, 8, 11, 23, 11, TFBlocks.giant_log.getDefaultState(), TFBlocks.giant_log.getDefaultState(), false);

		// cloud base
		this.fillWithBlocks(world, sbb, 8, -4, 8, 11, -1, 11, TFBlocks.fluffy_cloud.getDefaultState(), TFBlocks.fluffy_cloud.getDefaultState(), false);

		return true;
	}

}
