package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;

public class MazeRoomComponent extends TFStructureComponentOld {

	public MazeRoomComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFMMR.get(), nbt);
	}

	public MazeRoomComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public MazeRoomComponent(StructurePieceType type, int i, RandomSource rand, int x, int y, int z) {
		super(type, i, x, y, z);
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));

		this.boundingBox = new BoundingBox(x, y, z, x + 15, y + 4, z + 15);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		// NO-OP
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// floor border
		generateBox(world, sbb, 1, 0, 1, 14, 0, 14, TFBlocks.MAZESTONE_BORDER.get().defaultBlockState(), AIR, true);
		generateBox(world, sbb, 2, 0, 2, 13, 0, 13, TFBlocks.MAZESTONE_MOSAIC.get().defaultBlockState(), AIR, true);

		// doorways
		if (this.getBlock(world, 7, 1, 0, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
			generateAirBox(world, sbb, 7, 1, 0, 8, 3, 0);
		}

		if (this.getBlock(world, 7, 1, 15, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
			generateAirBox(world, sbb, 7, 1, 15, 8, 3, 15);
		}

		if (this.getBlock(world, 0, 1, 7, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
			generateAirBox(world, sbb, 0, 1, 7, 0, 3, 8);
		}

		if (this.getBlock(world, 15, 1, 7, sbb).getBlock() == Blocks.AIR) {
			generateBox(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
			generateAirBox(world, sbb, 15, 1, 7, 15, 3, 8);
		}
	}
}
