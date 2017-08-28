package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.structures.StructureTFComponent;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeMound extends StructureTFComponent {

	public ComponentTFMazeMound() {
		super();
		// TODO Auto-generated constructor stub
	}


	public static final int DIAMETER = 35;
	private int averageGroundLevel = -1;

	private ComponentTFMazeUpperEntrance mazeAbove;

	public ComponentTFMazeMound(int i, Random rand, int x, int y, int z) {
		super(i);
		this.setCoordBaseMode(EnumFacing.HORIZONTALS[rand.nextInt(4)]);

		this.boundingBox = new StructureBoundingBox(x, y, z, x + DIAMETER, y + 8, z + DIAMETER);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
		super.buildComponent(structurecomponent, list, random);

		// add aboveground maze entrance building
		mazeAbove = new ComponentTFMazeUpperEntrance(3, random, boundingBox.minX + 10, boundingBox.minY + 0, boundingBox.minZ + 10);
		list.add(mazeAbove);
		mazeAbove.buildComponent(this, list, random);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		if (this.averageGroundLevel < 0) {
			this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

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
					this.setBlockState(world, Blocks.GRASS.getDefaultState(), x, hheight, z, sbb);

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
	protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox) {
		int totalHeight = 0;
		int totalMeasures = 0;

		for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5) {
			for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6) {
				BlockPos pos = new BlockPos(var6, 64, var5);

				if (par2StructureBoundingBox.isVecInside(pos)) {
					final BlockPos topPos = par1World.getTopSolidOrLiquidBlock(pos);
					totalHeight += Math.max(topPos.getY(), par1World.provider.getAverageGroundLevel());
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
