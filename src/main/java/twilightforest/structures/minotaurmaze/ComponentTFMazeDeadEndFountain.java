package twilightforest.structures.minotaurmaze;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;

import java.util.Random;

public class ComponentTFMazeDeadEndFountain extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndFountain() {
		super();
	}

	public ComponentTFMazeDeadEndFountain(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// normal doorway
		super.addComponentParts(world, rand, sbb);

		// back wall brick
		this.fillWithBlocks(world, sbb, 1, 1, 4, 4, 4, 4, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.BRICK), AIR, false);

		// water
		this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 2, 3, 4, sbb);
		this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 3, 3, 4, sbb);

		// receptacle
		this.setBlockState(world, AIR, 2, 0, 3, sbb);
		this.setBlockState(world, AIR, 3, 0, 3, sbb);

		return true;
	}
}
