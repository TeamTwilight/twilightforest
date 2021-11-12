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
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class MazeDeadEndChestComponent extends MazeDeadEndComponent {

	public MazeDeadEndChestComponent(ServerLevel level, CompoundTag nbt) {
		this(MinotaurMazePieces.TFMMDEC, nbt);
	}

	public MazeDeadEndChestComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public MazeDeadEndChestComponent(StructurePieceType type, TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(type, feature, i, x, y, z, rotation);
		this.setOrientation(rotation);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		//super.addComponentParts(world, rand, sbb, chunkPosIn);

		// dais
		this.placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), 2, 1, 4, sbb);
		this.placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), 3, 1, 4, sbb);
		this.placeBlock(world, getStairState(Blocks.OAK_STAIRS.defaultBlockState(), Direction.NORTH, false), 2, 1, 3, sbb);
		this.placeBlock(world, getStairState(Blocks.OAK_STAIRS.defaultBlockState(), Direction.NORTH, false), 3, 1, 3, sbb);

		// chest
		this.setDoubleLootChest(world, 2, 2, 4, 3, 2, 4, Direction.SOUTH, TFTreasure.LABYRINTH_DEAD_END, sbb, false);

//		// torches
//		this.setBlockState(world, Blocks.TORCH, 0, 1, 3, 4, sbb);
//		this.setBlockState(world, Blocks.TORCH, 0, 4, 3, 4, sbb);

		// doorway w/ bars
		this.generateBox(world, sbb, 1, 1, 0, 4, 3, 1, TFBlocks.CUT_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 1, 4, 0, 4, 4, 1, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 2, 1, 0, 3, 3, 1, Blocks.IRON_BARS.defaultBlockState(), AIR, false);

		return true;
	}
}
