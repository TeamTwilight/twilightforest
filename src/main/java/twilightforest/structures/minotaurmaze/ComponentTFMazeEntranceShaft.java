package twilightforest.structures.minotaurmaze;

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
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.world.TFGenerationSettings;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeEntranceShaft extends StructureTFComponentOld {

	public ComponentTFMazeEntranceShaft(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMES, nbt);
	}

	private int averageGroundLevel = -1;

	public ComponentTFMazeEntranceShaft(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(TFMinotaurMazePieces.TFMMES, feature, i);
		this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));

		this.boundingBox = new MutableBoundingBox(x, y, z, x + 6 - 1, y + 14, z + 6 - 1);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		// NO-OP
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, generator, sbb);

			if (this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.maxY = this.averageGroundLevel;
			this.boundingBox.minY = TFGenerationSettings.SEALEVEL - 10;
		}

		this.fillWithBlocks(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSize(), 5, TFBlocks.maze_stone_brick.get().getDefaultState(), AIR, true);
		this.fillWithAir(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSize(), 4);

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(ISeedReader world, ChunkGenerator generator, MutableBoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ; ++z) {
			for (int x = this.boundingBox.minX; x <= this.boundingBox.maxX; ++x) {
				BlockPos pos = new BlockPos(x, 64, z);
				if (boundingBox.isVecInside(pos)) {
					final BlockPos topBlock = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
					yTotal += Math.max(topBlock.getY(), generator.getGroundHeight());
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
