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

public class MazeRoomExitComponent extends MazeRoomComponent {

	public MazeRoomExitComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMRE, nbt);
	}

	public MazeRoomExitComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMRE, feature, i, rand, x, y, z);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// shaft down
		this.generateBox(world, sbb, 5, -5, 5, 10, 0, 10, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateAirBox(world, sbb, 6, -5, 6, 9, 4, 9);

		return true;
	}
}
