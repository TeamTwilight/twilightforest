package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.MazestoneVariant;
import twilightforest.structures.StructureTFComponent;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeUpperEntrance extends StructureTFComponent {

	public ComponentTFMazeUpperEntrance() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ComponentTFMazeUpperEntrance(int i, Random rand, int x, int y, int z) {
		super(i);
		this.setCoordBaseMode(EnumFacing.HORIZONTALS[rand.nextInt(4)]);

		this.boundingBox = new StructureBoundingBox(x, y, z, x + 15, y + 4, z + 15);
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

		// ceiling
		this.generateMaybeBox(world, sbb, rand, 0.7F, 0, 5, 0, 15, 5, 15, TFBlocks.mazestone.getDefaultState(), AIR, true, 0);

		this.fillWithBlocks(world, sbb, 0, 0, 0, 15, 0, 15, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.MOSAIC), AIR, false);
		this.fillWithBlocks(world, sbb, 0, 1, 0, 15, 1, 15, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 4, 0, 15, 4, 15, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, true);
		this.generateMaybeBox(world, sbb, rand, 0.2F, 0, 0, 0, 15, 5, 15, Blocks.GRAVEL.getDefaultState(), AIR, true, 0);


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
		this.fillWithBlocks(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, false);
		this.fillWithBlocks(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.mazestone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, false);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.getDefaultState(), AIR, false, 0);
//		this.fillWithBlocks(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS, 0, AIR, false);


		this.fillWithAir(world, sbb, 6, 0, 6, 9, 4, 9);

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
					final BlockPos topPos = par1World.getTopSolidOrLiquidBlock(pos);
					var3 += Math.max(topPos.getY(), par1World.provider.getAverageGroundLevel());
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
