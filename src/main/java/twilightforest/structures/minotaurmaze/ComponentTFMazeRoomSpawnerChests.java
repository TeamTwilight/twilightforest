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
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class ComponentTFMazeRoomSpawnerChests extends ComponentTFMazeRoom {

	public ComponentTFMazeRoomSpawnerChests(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMRSC, nbt);
	}

	public ComponentTFMazeRoomSpawnerChests(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(TFMinotaurMazePieces.TFMMRSC, feature, i, rand, x, y, z);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// 4 pillar enclosures
		placePillarEnclosure(world, sbb, 3, 3);
		placePillarEnclosure(world, sbb, 10, 3);
		placePillarEnclosure(world, sbb, 3, 10);
		placePillarEnclosure(world, sbb, 10, 10);

		// spawner
		setSpawner(world, 4, 2, 4, sbb, TFEntities.minotaur);

		// treasure
		this.placeTreasureAtCurrentPosition(world, 4, 2, 11, TFTreasure.labyrinth_room, sbb);

		// treasure
		this.placeTreasureAtCurrentPosition(world, 11, 2, 4, TFTreasure.labyrinth_room, sbb);

		// trap
		setBlockState(world, Blocks.OAK_PRESSURE_PLATE.getDefaultState(), 11, 1, 11, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 10, 0, 11, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 11, 0, 10, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 11, 0, 12, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 12, 0, 11, sbb);

		return true;
	}

	private void placePillarEnclosure(ISeedReader world, MutableBoundingBox sbb,
									  int dx, int dz) {
		for (int y = 1; y < 5; y++) {
			final BlockState chiselledMazeBlock = TFBlocks.maze_stone_chiseled.get().getDefaultState();
			setBlockState(world, chiselledMazeBlock, dx, y, dz, sbb);
			setBlockState(world, chiselledMazeBlock, dx + 2, y, dz, sbb);
			setBlockState(world, chiselledMazeBlock, dx, y, dz + 2, sbb);
			setBlockState(world, chiselledMazeBlock, dx + 2, y, dz + 2, sbb);
		}
		setBlockState(world, Blocks.OAK_PLANKS.getDefaultState(), dx + 1, 1, dz + 1, sbb);
		setBlockState(world, Blocks.OAK_PLANKS.getDefaultState(), dx + 1, 4, dz + 1, sbb);

		final BlockState defaultState = Blocks.OAK_STAIRS.getDefaultState();


		setBlockState(world, getStairState(defaultState, Direction.NORTH, false), dx + 1, 1, dz, sbb);
		setBlockState(world, getStairState(defaultState, Direction.WEST, false), dx, 1, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, Direction.EAST, false), dx + 2, 1, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, Direction.SOUTH, false), dx + 1, 1, dz + 2, sbb);

		setBlockState(world, getStairState(defaultState, Direction.NORTH, true), dx + 1, 4, dz, sbb);
		setBlockState(world, getStairState(defaultState, Direction.WEST, true), dx, 4, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, Direction.EAST, true), dx + 2, 4, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, Direction.SOUTH, true), dx + 1, 4, dz + 2, sbb);

		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 2, dz, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx, 2, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 2, 2, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 2, dz + 2, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 3, dz, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx, 3, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 2, 3, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 3, dz + 2, sbb);
	}
}
