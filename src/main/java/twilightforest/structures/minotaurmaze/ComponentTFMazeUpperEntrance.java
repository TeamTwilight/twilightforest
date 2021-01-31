package twilightforest.structures.minotaurmaze;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeUpperEntrance extends StructureTFComponentOld {

	public ComponentTFMazeUpperEntrance(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMUE, nbt);
	}

	public ComponentTFMazeUpperEntrance(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(TFMinotaurMazePieces.TFMMUE, feature, i);
		this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));

		this.boundingBox = new MutableBoundingBox(x, y, z, x + 15, y + 4, z + 15);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// ceiling
		this.generateMaybeBox(world, sbb, rand, 0.7F, 0, 5, 0, 15, 5, 15, TFBlocks.maze_stone.get().getDefaultState(), AIR, true, false);

		this.fillWithBlocks(world, sbb, 0, 0, 0, 15, 0, 15, TFBlocks.maze_stone_mosaic.get().getDefaultState(), AIR, false);
		this.fillWithBlocks(world, sbb, 0, 1, 0, 15, 1, 15, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.maze_stone_brick.get().getDefaultState(), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 4, 0, 15, 4, 15, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, true);
		this.generateMaybeBox(world, sbb, rand, 0.2F, 0, 0, 0, 15, 5, 15, Blocks.GRAVEL.getDefaultState(), AIR, true, false);

		// doorways
		fillWithBlocks(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		fillWithAir(world, sbb, 7, 1, 0, 8, 3, 0);
		fillWithBlocks(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		fillWithAir(world, sbb, 7, 1, 15, 8, 3, 15);
		fillWithBlocks(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		fillWithAir(world, sbb, 0, 1, 7, 0, 3, 8);
		fillWithBlocks(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		fillWithAir(world, sbb, 15, 1, 7, 15, 3, 8);

		// random holes
//		this.randomlyRareFillWithBlocks(world, sbb, 0, 1, 0, 15, 4, 15, 0, false);
//		this.randomlyRareFillWithBlocks(world, sbb, 0, 3, 0, 15, 4, 15, 0, true);
//		this.randomlyRareFillWithBlocks(world, sbb, 0, 4, 0, 15, 4, 15, 0, true);
		this.fillWithAir(world, sbb, 1, 1, 1, 14, 4, 14);

		// entrance pit
		this.fillWithBlocks(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, false);
		this.fillWithBlocks(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, false);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.getDefaultState(), AIR, false, false);
//		this.fillWithBlocks(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS, 0, AIR, false);

		this.fillWithAir(world, sbb, 6, 0, 6, 9, 4, 9);

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(ISeedReader world, MutableBoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ; ++z) {
			for (int x = this.boundingBox.minX; x <= this.boundingBox.maxX; ++x) {
				BlockPos pos = new BlockPos(x, 64, z);

				if (boundingBox.isVecInside(pos)) {
					final BlockPos topPos = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
					yTotal += Math.max(topPos.getY(), ((ServerWorld) world).getChunkProvider().getChunkGenerator().getGroundHeight());
					++count;
				}
			}
		}

		if (count == 0) {
			return -1;
		} else {
			return yTotal / count;
		}
	}
}
