package twilightforest.structures.trollcave;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFCloudTree extends StructureTFComponentOld {

	public ComponentTFCloudTree(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFClTr, nbt);
	}

	public ComponentTFCloudTree(TFFeature feature, int index, int x, int y, int z) {
		super(TFTrollCavePieces.TFClTr, feature, index);

		this.setCoordBaseMode(Direction.SOUTH);

		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y >> 2) << 2;
		z = (z >> 2) << 2;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 20, 28, 20, Direction.SOUTH);

		// spawn list!
		this.spawnListIndex = 1;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// leaves
		this.fillWithBlocks(world, sbb, 0, 12, 0, 19, 19, 19, TFBlocks.giant_leaves.get().getDefaultState(), TFBlocks.giant_leaves.get().getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 20, 4, 15, 23, 15, TFBlocks.giant_leaves.get().getDefaultState(), TFBlocks.giant_leaves.get().getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 8, 24, 4, 11, 27, 15, TFBlocks.giant_leaves.get().getDefaultState(), TFBlocks.giant_leaves.get().getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 24, 8, 15, 27, 11, TFBlocks.giant_leaves.get().getDefaultState(), TFBlocks.giant_leaves.get().getDefaultState(), false);

		// trunk
		this.fillWithBlocks(world, sbb, 8, 0, 8, 11, 23, 11, TFBlocks.giant_log.get().getDefaultState(), TFBlocks.giant_log.get().getDefaultState(), false);

		// cloud base
		this.fillWithBlocks(world, sbb, 8, -4, 8, 11, -1, 11, TFBlocks.fluffy_cloud.get().getDefaultState(), TFBlocks.fluffy_cloud.get().getDefaultState(), false);

		return true;
	}
}
