package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class MazeRoomCollapseComponent extends MazeRoomComponent {

	public MazeRoomCollapseComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRC, nbt);
	}

	public MazeRoomCollapseComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRC, feature, i, rand, x, y, z);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		//
		for (int x = 1; x < 14; x++) {
			for (int z = 1; z < 14; z++) {
				// calculate distance from middle
				int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));
				int gravel = rand.nextInt(dist);
				int root = rand.nextInt(dist);

				if (gravel > 0) {
					gravel++; // get it out of the floor
					this.generateBox(world, sbb, x, 1, z, x, gravel, z, Blocks.GRAVEL.defaultBlockState(), AIR, false);
					this.generateAirBox(world, sbb, x, gravel, z, x, gravel + 5, z);
				} else if (root > 0) {
					this.generateBox(world, sbb, x, 5, z, x, 5 + root, z, Blocks.DIRT.defaultBlockState(), AIR, true);
					this.generateBox(world, sbb, x, 5 - rand.nextInt(5), z, x, 5, z, TFBlocks.ROOT_STRAND.get().defaultBlockState(), AIR, false);
				} else if (rand.nextInt(dist + 1) > 0) {
					// remove ceiling
					this.generateAirBox(world, sbb, x, 5, z, x, 5, z);
				}
			}
		}

		return true;
	}
}
