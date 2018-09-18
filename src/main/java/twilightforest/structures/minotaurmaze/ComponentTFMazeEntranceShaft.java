package twilightforest.structures.minotaurmaze;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.world.TFWorld;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeEntranceShaft extends StructureTFComponentOld {

	public ComponentTFMazeEntranceShaft() {
		super();
	}


	private int averageGroundLevel = -1;

	public ComponentTFMazeEntranceShaft(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(feature, i);
		this.setCoordBaseMode(EnumFacing.HORIZONTALS[rand.nextInt(4)]);

		this.boundingBox = new StructureBoundingBox(x, y, z, x + 6 - 1, y + 14, z + 6 - 1);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
		;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

			if (this.averageGroundLevel < 0) {
				return true;
			}

			this.boundingBox.maxY = this.averageGroundLevel;
			this.boundingBox.minY = TFWorld.SEALEVEL - 10;
		}


		this.fillWithBlocks(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSize(), 5, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK), AIR, true);
		this.fillWithAir(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSize(), 4);

		return true;
	}


	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(World world, StructureBoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ; ++z) {
			for (int x = this.boundingBox.minX; x <= this.boundingBox.maxX; ++x) {
				BlockPos pos = new BlockPos(x, 64, z);
				if (boundingBox.isVecInside(pos)) {
					final BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
					yTotal += Math.max(topBlock.getY(), world.provider.getAverageGroundLevel());
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
