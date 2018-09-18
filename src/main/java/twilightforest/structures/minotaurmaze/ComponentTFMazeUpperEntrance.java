package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
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

import java.util.List;
import java.util.Random;

public class ComponentTFMazeUpperEntrance extends StructureTFComponentOld {

	public ComponentTFMazeUpperEntrance() {
		super();
	}


	public ComponentTFMazeUpperEntrance(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(feature, i);
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
		this.generateMaybeBox(world, sbb, rand, 0.7F, 0, 5, 0, 15, 5, 15, TFBlocks.maze_stone.getDefaultState(), AIR, true, 0);

		this.fillWithBlocks(world, sbb, 0, 0, 0, 15, 0, 15, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.MOSAIC), AIR, false);
		this.fillWithBlocks(world, sbb, 0, 1, 0, 15, 1, 15, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK), AIR, true);
		this.fillWithBlocks(world, sbb, 0, 4, 0, 15, 4, 15, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, true);
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
		this.fillWithBlocks(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, false);
		this.fillWithBlocks(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, false);
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
	protected int getAverageGroundLevel(World world, StructureBoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ; ++z) {
			for (int x = this.boundingBox.minX; x <= this.boundingBox.maxX; ++x) {
				BlockPos pos = new BlockPos(x, 64, z);

				if (boundingBox.isVecInside(pos)) {
					final BlockPos topPos = world.getTopSolidOrLiquidBlock(pos);
					yTotal += Math.max(topPos.getY(), world.provider.getAverageGroundLevel());
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
