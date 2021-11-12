package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
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

public class MazeRoomBossComponent extends MazeRoomComponent {

	public MazeRoomBossComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRB, nbt);
	}

	public MazeRoomBossComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRB, feature, i, rand, x, y, z);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// doorways
		if (this.getBlock(world, 7, 1, 0, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		}

		if (this.getBlock(world, 7, 1, 15, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		}

		if (this.getBlock(world, 0, 1, 7, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		}

		if (this.getBlock(world, 15, 1, 7, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		}

		// mycelium / small mushrooms on floor
		for (int x = 1; x < 14; x++) {
			for (int z = 1; z < 14; z++) {
				// calculate distance from middle
				int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));
				boolean mycelium = rand.nextInt(dist + 1) > 0;
				boolean mushroom = rand.nextInt(dist) > 0;
				boolean mushRed = rand.nextBoolean();

				// make part of the floor mycelium
				if (mycelium) {
					this.placeBlock(world, Blocks.MYCELIUM.defaultBlockState(), x, 0, z, sbb);
				}
				// add small mushrooms all over
				if (mushroom) {
					this.placeBlock(world, (mushRed ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM).defaultBlockState(), x, 1, z, sbb);
				}
			}
		}

		// mushroom chest shelves in corner
		final BlockState redMushroom = Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();
		final BlockState brownMushroom = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();

		generateBox(world, sbb, 1, 1, 1, 3, 1, 3, redMushroom, AIR, false);
		generateBox(world, sbb, 1, 2, 1, 1, 3, 4, redMushroom, AIR, false);
		generateBox(world, sbb, 2, 2, 1, 4, 3, 1, redMushroom, AIR, false);
		generateBox(world, sbb, 1, 4, 1, 3, 4, 3, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, 3, 2, 3, TFTreasure.LABYRINTH_ROOM, sbb);

		generateBox(world, sbb, 12, 1, 12, 14, 1, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 14, 2, 11, 14, 3, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 11, 2, 14, 14, 3, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 12, 4, 12, 14, 4, 14, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, 12, 2, 12, TFTreasure.LABYRINTH_ROOM, sbb);

		generateBox(world, sbb, 1, 1, 12, 3, 1, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 1, 2, 11, 1, 3, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 2, 2, 14, 4, 3, 14, redMushroom, AIR, false);
		generateBox(world, sbb, 1, 4, 12, 3, 4, 14, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, 3, 2, 12, TFTreasure.LABYRINTH_ROOM, sbb);

		generateBox(world, sbb, 12, 1, 1, 14, 1, 3, brownMushroom, AIR, false);
		generateBox(world, sbb, 11, 2, 1, 14, 3, 1, brownMushroom, AIR, false);
		generateBox(world, sbb, 14, 2, 2, 14, 3, 4, brownMushroom, AIR, false);
		generateBox(world, sbb, 12, 4, 1, 14, 4, 3, brownMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, 12, 2, 3, TFTreasure.LABYRINTH_ROOM, sbb);

		// a few more ceilingshrooms
		generateBox(world, sbb, 5, 4, 5, 7, 5, 7, brownMushroom, AIR, false);
		generateBox(world, sbb, 8, 4, 8, 10, 5, 10, redMushroom, AIR, false);

		// the moo-cen-mino-shrom-taur!
		final BlockState taurSpawner = TFBlocks.MINOSHROOM_BOSS_SPAWNER.get().defaultBlockState();
		setBlockStateRotated(world, taurSpawner, 7, 2, 7, Rotation.NONE, sbb);

		return true;
	}
}
