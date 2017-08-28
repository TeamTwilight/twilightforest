package twilightforest.structures.minotaurmaze;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.MazestoneVariant;
import twilightforest.structures.StructureTFComponent;
import twilightforest.world.TFWorld;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeEntranceShaft extends StructureTFComponent {

	public ComponentTFMazeEntranceShaft() {
		super();
		// TODO Auto-generated constructor stub
	}


	private int averageGroundLevel = -1;

	public ComponentTFMazeEntranceShaft(int i, Random rand, int x, int y, int z) {
		super(i);
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


		this.fillWithBlocks(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSize(), 5, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK), AIR, true);
		this.fillWithAir(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSize(), 4);

		return true;
	}


	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox) {
		int var3 = 0;
		int var4 = 0;

		for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5) {
			for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6) {
				BlockPos pos = new BlockPos(var6, 64, var5);
				if (par2StructureBoundingBox.isVecInside(pos)) {
					final BlockPos topBlock = par1World.getTopSolidOrLiquidBlock(pos);
					var3 += Math.max(topBlock.getY(), par1World.provider.getAverageGroundLevel());
					++var4;
				}
			}
		}

		if (var4 == 0) {
			return -1;
		} else {
			return var3 / var4;
		}
	}
}
