package twilightforest.structures.minotaurmaze;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class ComponentTFMazeDeadEndTrappedChest extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndTrappedChest() {
		super();
	}

	public ComponentTFMazeDeadEndTrappedChest(TFFeature feature, int i, int x, int y, int z, EnumFacing rotation) {
		super(feature, i, x, y, z, rotation);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		//super.addComponentParts(world, rand, sbb);

		// dais
		this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 2, 1, 4, sbb);
		this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 3, 1, 4, sbb);
		this.setBlockState(world, getStairState(Blocks.OAK_STAIRS.getDefaultState(), EnumFacing.NORTH, rotation, false), 2, 1, 3, sbb);
		this.setBlockState(world, getStairState(Blocks.OAK_STAIRS.getDefaultState(), EnumFacing.NORTH, rotation, false), 3, 1, 3, sbb);

		// chest
		this.setBlockState(world, Blocks.TRAPPED_CHEST.getDefaultState(), 2, 2, 4, sbb);
		this.placeTreasureAtCurrentPosition(world, rand, 3, 2, 4, TFTreasure.labyrinth_deadend, true, sbb);

//		// torches
//		this.setBlockState(world, Blocks.TORCH, 0, 1, 3, 4, sbb);
//		this.setBlockState(world, Blocks.TORCH, 0, 4, 3, 4, sbb);

		// doorway w/ bars
		this.fillWithBlocks(world, sbb, 1, 1, 0, 4, 3, 1, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CHISELED), AIR, false);
		this.fillWithBlocks(world, sbb, 1, 4, 0, 4, 4, 1, TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.DECORATIVE), AIR, false);
		this.fillWithBlocks(world, sbb, 2, 1, 0, 3, 3, 1, Blocks.IRON_BARS.getDefaultState(), AIR, false);

		// TNT!
		IBlockState tnt = Blocks.TNT.getDefaultState();
		this.setBlockState(world, tnt, 2,  0, 3, sbb);
		this.setBlockState(world, tnt, 3,  0, 3, sbb);
		this.setBlockState(world, tnt, 2,  0, 4, sbb);
		this.setBlockState(world, tnt, 3,  0, 4, sbb);

		return true;
	}

}
