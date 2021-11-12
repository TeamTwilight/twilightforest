package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class MazeRoomSpawnerChestsComponent extends MazeRoomComponent {

	public MazeRoomSpawnerChestsComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRSC, nbt);
	}

	public MazeRoomSpawnerChestsComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRSC, feature, i, rand, x, y, z);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// 4 pillar enclosures
		placePillarEnclosure(world, sbb, 3, 3);
		placePillarEnclosure(world, sbb, 10, 3);
		placePillarEnclosure(world, sbb, 3, 10);
		placePillarEnclosure(world, sbb, 10, 10);

		// spawner
		setSpawner(world, 4, 2, 4, sbb, TFEntities.MINOTAUR);

		// treasure
		this.placeTreasureAtCurrentPosition(world, 4, 2, 11, TFTreasure.LABYRINTH_ROOM, sbb);

		// treasure
		this.placeTreasureAtCurrentPosition(world, 11, 2, 4, TFTreasure.LABYRINTH_ROOM, sbb);

		// trap
		placeBlock(world, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), 11, 1, 11, sbb);
		placeBlock(world, Blocks.TNT.defaultBlockState(), 10, 0, 11, sbb);
		placeBlock(world, Blocks.TNT.defaultBlockState(), 11, 0, 10, sbb);
		placeBlock(world, Blocks.TNT.defaultBlockState(), 11, 0, 12, sbb);
		placeBlock(world, Blocks.TNT.defaultBlockState(), 12, 0, 11, sbb);

		return true;
	}

	private void placePillarEnclosure(WorldGenLevel world, BoundingBox sbb,
									  int dx, int dz) {
		for (int y = 1; y < 5; y++) {
			final BlockState chiselledMazeBlock = TFBlocks.CUT_MAZESTONE.get().defaultBlockState();
			placeBlock(world, chiselledMazeBlock, dx, y, dz, sbb);
			placeBlock(world, chiselledMazeBlock, dx + 2, y, dz, sbb);
			placeBlock(world, chiselledMazeBlock, dx, y, dz + 2, sbb);
			placeBlock(world, chiselledMazeBlock, dx + 2, y, dz + 2, sbb);
		}
		placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), dx + 1, 1, dz + 1, sbb);
		placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), dx + 1, 4, dz + 1, sbb);

		final BlockState defaultState = Blocks.OAK_STAIRS.defaultBlockState();


		placeBlock(world, getStairState(defaultState, Direction.NORTH, false), dx + 1, 1, dz, sbb);
		placeBlock(world, getStairState(defaultState, Direction.WEST, false), dx, 1, dz + 1, sbb);
		placeBlock(world, getStairState(defaultState, Direction.EAST, false), dx + 2, 1, dz + 1, sbb);
		placeBlock(world, getStairState(defaultState, Direction.SOUTH, false), dx + 1, 1, dz + 2, sbb);

		placeBlock(world, getStairState(defaultState, Direction.NORTH, true), dx + 1, 4, dz, sbb);
		placeBlock(world, getStairState(defaultState, Direction.WEST, true), dx, 4, dz + 1, sbb);
		placeBlock(world, getStairState(defaultState, Direction.EAST, true), dx + 2, 4, dz + 1, sbb);
		placeBlock(world, getStairState(defaultState, Direction.SOUTH, true), dx + 1, 4, dz + 2, sbb);

		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 1, 2, dz, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx, 2, dz + 1, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 2, 2, dz + 1, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 1, 2, dz + 2, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 1, 3, dz, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx, 3, dz + 1, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 2, 3, dz + 1, sbb);
		placeBlock(world, Blocks.IRON_BARS.defaultBlockState(), dx + 1, 3, dz + 2, sbb);
	}
}
