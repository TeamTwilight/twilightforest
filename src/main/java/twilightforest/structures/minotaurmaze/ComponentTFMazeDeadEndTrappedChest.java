package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class ComponentTFMazeDeadEndTrappedChest extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndTrappedChest(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMDETrC, nbt);
	}

	public ComponentTFMazeDeadEndTrappedChest(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(TFMinotaurMazePieces.TFMMDETrC, feature, i, x, y, z, rotation);
		this.setCoordBaseMode(rotation);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		//super.addComponentParts(world, rand, sbb, chunkPosIn);

		// dais
		this.setBlockState(world, Blocks.OAK_PLANKS.getDefaultState(), 2, 1, 4, sbb);
		this.setBlockState(world, Blocks.OAK_PLANKS.getDefaultState(), 3, 1, 4, sbb);
		this.setBlockState(world, getStairState(Blocks.OAK_STAIRS.getDefaultState(), Direction.NORTH, false), 2, 1, 3, sbb);
		this.setBlockState(world, getStairState(Blocks.OAK_STAIRS.getDefaultState(), Direction.NORTH, false), 3, 1, 3, sbb);

		// chest
		this.setDoubleLootChest(world, 2, 2, 4,3, 2, 4, Direction.SOUTH, TFTreasure.labyrinth_deadend, sbb, true);

//		// torches
//		this.setBlockState(world, Blocks.TORCH, 0, 1, 3, 4, sbb);
//		this.setBlockState(world, Blocks.TORCH, 0, 4, 3, 4, sbb);

		// doorway w/ bars
		this.fillWithBlocks(world, sbb, 1, 1, 0, 4, 3, 1, TFBlocks.maze_stone_chiseled.get().getDefaultState(), AIR, false);
		this.fillWithBlocks(world, sbb, 1, 4, 0, 4, 4, 1, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, false);
		this.fillWithBlocks(world, sbb, 2, 1, 0, 3, 3, 1, Blocks.IRON_BARS.getDefaultState(), AIR, false);

		// TNT!
		BlockState tnt = Blocks.TNT.getDefaultState();
		this.setBlockState(world, tnt, 2,  0, 3, sbb);
		this.setBlockState(world, tnt, 3,  0, 3, sbb);
		this.setBlockState(world, tnt, 2,  0, 4, sbb);
		this.setBlockState(world, tnt, 3,  0, 4, sbb);

		return true;
	}
}
