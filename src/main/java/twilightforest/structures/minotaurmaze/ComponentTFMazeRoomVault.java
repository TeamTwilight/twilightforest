package twilightforest.structures.minotaurmaze;

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

public class ComponentTFMazeRoomVault extends ComponentTFMazeRoom {

	public ComponentTFMazeRoomVault(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMRV, nbt);
	}

	public ComponentTFMazeRoomVault(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(TFMinotaurMazePieces.TFMMRV, feature, i, rand, x, y, z);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// fill room with bricks
		fillWithBlocks(world, sbb, 0, 1, 0, 15, 4, 15, TFBlocks.maze_stone_decorative.get().getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.maze_stone_brick.get().getDefaultState(), AIR, false);

		// 4x4 room in the middle
		fillWithAir(world, sbb, 6, 2, 6, 9, 3, 9);

		// pressure plates, sand & tnt
		fillWithBlocks(world, sbb, 6, 2, 5, 9, 2, 5, Blocks.OAK_PRESSURE_PLATE.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 6, 2, 10, 9, 2, 10, Blocks.OAK_PRESSURE_PLATE.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 5, 2, 6, 5, 2, 9, Blocks.OAK_PRESSURE_PLATE.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 10, 2, 6, 10, 2, 9, Blocks.OAK_PRESSURE_PLATE.getDefaultState(), AIR, false);

		// unfair sand
		fillWithBlocks(world, sbb, 6, 4, 5, 9, 4, 5, Blocks.SAND.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 6, 4, 10, 9, 4, 10, Blocks.SAND.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 5, 4, 6, 5, 4, 9, Blocks.SAND.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 10, 4, 6, 10, 4, 9, Blocks.SAND.getDefaultState(), AIR, false);

		fillWithBlocks(world, sbb, 6, 0, 5, 9, 0, 5, Blocks.TNT.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 6, 0, 10, 9, 0, 10, Blocks.TNT.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 5, 0, 6, 5, 0, 9, Blocks.TNT.getDefaultState(), AIR, false);
		fillWithBlocks(world, sbb, 10, 0, 6, 10, 0, 9, Blocks.TNT.getDefaultState(), AIR, false);

		// LEWTZ!
		this.setDoubleLootChest(world, 7, 2, 6, 8, 2, 6, Direction.SOUTH, TFTreasure.labyrinth_vault, sbb, false);
		this.setDoubleLootChest(world, 8, 2, 9, 7, 2, 9, Direction.NORTH, TFTreasure.labyrinth_vault, sbb, false);
		this.setDoubleLootChest(world, 6, 2, 8, 6, 2, 7, Direction.EAST, TFTreasure.labyrinth_vault, sbb, false);
		this.setDoubleLootChest(world, 9, 2, 7, 9, 2, 8, Direction.WEST, TFTreasure.labyrinth_vault, sbb, false);

		// mazebreaker!

		return true;
	}
}
