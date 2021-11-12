package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
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
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class MazeRoomVaultComponent extends MazeRoomComponent {

	public MazeRoomVaultComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRV, nbt);
	}

	public MazeRoomVaultComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRV, feature, i, rand, x, y, z);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// fill room with bricks
		generateBox(world, sbb, 0, 1, 0, 15, 4, 15, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		generateBox(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, false);

		// 4x4 room in the middle
		generateAirBox(world, sbb, 6, 2, 6, 9, 3, 9);

		// pressure plates, sand & tnt
		generateBox(world, sbb, 6, 2, 5, 9, 2, 5, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 6, 2, 10, 9, 2, 10, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 5, 2, 6, 5, 2, 9, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 10, 2, 6, 10, 2, 9, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), AIR, false);

		// unfair sand
		generateBox(world, sbb, 6, 4, 5, 9, 4, 5, Blocks.SAND.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 6, 4, 10, 9, 4, 10, Blocks.SAND.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 5, 4, 6, 5, 4, 9, Blocks.SAND.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 10, 4, 6, 10, 4, 9, Blocks.SAND.defaultBlockState(), AIR, false);

		generateBox(world, sbb, 6, 0, 5, 9, 0, 5, Blocks.TNT.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 6, 0, 10, 9, 0, 10, Blocks.TNT.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 5, 0, 6, 5, 0, 9, Blocks.TNT.defaultBlockState(), AIR, false);
		generateBox(world, sbb, 10, 0, 6, 10, 0, 9, Blocks.TNT.defaultBlockState(), AIR, false);

		// LEWTZ!
		this.setDoubleLootChest(world, 7, 2, 6, 8, 2, 6, Direction.SOUTH, TFTreasure.LABYRINTH_VAULT, sbb, false);
		this.setDoubleLootChest(world, 8, 2, 9, 7, 2, 9, Direction.NORTH, TFTreasure.LABYRINTH_VAULT, sbb, false);
		this.setDoubleLootChest(world, 6, 2, 8, 6, 2, 7, Direction.EAST, TFTreasure.LABYRINTH_VAULT, sbb, false);
		this.setDoubleLootChest(world, 9, 2, 7, 9, 2, 8, Direction.WEST, TFTreasure.LABYRINTH_VAULT, sbb, false);

		// mazebreaker!

		return true;
	}
}
