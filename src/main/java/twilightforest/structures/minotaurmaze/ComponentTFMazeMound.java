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
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeMound extends StructureTFComponentOld {

	public ComponentTFMazeMound(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMMound, nbt);
	}

	public static final int DIAMETER = 35;
	private int averageGroundLevel = -1;

	private ComponentTFMazeUpperEntrance mazeAbove;

	public ComponentTFMazeMound(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(TFMinotaurMazePieces.TFMMMound, feature, i);
		this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));

		this.boundingBox = new MutableBoundingBox(x, y, z, x + DIAMETER, y + 8, z + DIAMETER);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		super.buildComponent(structurecomponent, list, random);

		// add aboveground maze entrance building
		mazeAbove = new ComponentTFMazeUpperEntrance(getFeatureType(), 3, random, boundingBox.minX + 10, boundingBox.minY, boundingBox.minZ + 10);
		list.add(mazeAbove);
		mazeAbove.buildComponent(this, list, random);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, generator, sbb);

			if (this.averageGroundLevel < 0) {
				return true;
			}

			int offset = this.averageGroundLevel - this.boundingBox.maxY + 8 - 1;

			this.boundingBox.offset(0, offset, 0);

			if (this.mazeAbove != null) {
				mazeAbove.getBoundingBox().offset(0, offset, 0);
			}
		}

		//this.fillWithBlocks(world, sbb, 0, 0, 0, 25, 8, 25, Blocks.DIRT, 0, false);

		for (int x = 0; x < DIAMETER; x++) {
			for (int z = 0; z < DIAMETER; z++) {
				int cx = x - DIAMETER / 2;
				int cz = z - DIAMETER / 2;

				int dist = (int) Math.sqrt(cx * cx + cz * cz);
				int hheight = (int) (Math.cos((double) dist / DIAMETER * Math.PI) * (DIAMETER / 3));

				// leave a hole in the middle
				if (!(cx <= 2 && cx >= -1 && cz <= 2 && cz >= -1) && ((!(cx <= 2 && cx >= -1) && !(cz <= 2 && cz >= -1)) || hheight > 6)) {
					this.setBlockState(world, Blocks.GRASS_BLOCK.getDefaultState(), x, hheight, z, sbb);

					// only fill to the bottom when we're not in the entrances
					if (!(cx <= 2 && cx >= -1) && !(cz <= 2 && cz >= -1)) {
						this.setBlockState(world, Blocks.DIRT.getDefaultState(), x, hheight - 1, z, sbb);
					} else if (hheight > 6) {
						this.fillWithBlocks(world, sbb, x, 6, z, x, hheight - 1, z, Blocks.DIRT.getDefaultState(), AIR, false);
					}
				}
			}
		}

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(ISeedReader world, ChunkGenerator generator, MutableBoundingBox boundingBox) {
		int totalHeight = 0;
		int totalMeasures = 0;

		for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ; ++z) {
			for (int x = this.boundingBox.minX; x <= this.boundingBox.maxX; ++x) {
				BlockPos pos = new BlockPos(x, 64, z);

				if (boundingBox.isVecInside(pos)) {
					final BlockPos topPos = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
					totalHeight += Math.max(topPos.getY(), generator.getGroundHeight());
					++totalMeasures;
				}
			}
		}

		if (totalMeasures == 0) {
			return -1;
		} else {
			return totalHeight / totalMeasures;
		}
	}
}
