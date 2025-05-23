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
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.world.components.structures.TFStructureComponentOld;

public class MazeUpperEntranceComponent extends TFStructureComponentOld {

	public MazeUpperEntranceComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMUE.get(), nbt);
	}

	public MazeUpperEntranceComponent(int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMUE.get(), i, x, y, z);
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
		FeaturePlacers.replaceBlocksDome(world, this.boundingBox.getCenter().atY(this.getWorldY(-1)), 16, 0.8f, sbb, this.boundingBox.inflatedBy(8), Blocks.STONE, Blocks.DIRT.defaultBlockState());

		// ceiling
		this.generateMaybeBox(world, sbb, rand, 0.7F, 0, 5, 0, 15, 5, 15, TFBlocks.MAZESTONE.get().defaultBlockState(), AIR, true, false);

		this.generateBox(world, sbb, 0, 0, 0, 15, 0, 15, TFBlocks.MAZESTONE_MOSAIC.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 0, 1, 0, 15, 1, 15, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, true);
		this.generateBox(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, true);
		this.generateBox(world, sbb, 0, 4, 0, 15, 4, 15, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, true);
		this.generateMaybeBox(world, sbb, rand, 0.2F, 0, 0, 0, 15, 5, 15, Blocks.GRAVEL.defaultBlockState(), AIR, true, false);

		// doorways
		generateBox(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 7, 1, 0, 8, 3, 0);
		generateBox(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 7, 1, 15, 8, 3, 15);
		generateBox(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 0, 1, 7, 0, 3, 8);
		generateBox(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 15, 1, 7, 15, 3, 8);

		// random holes
		this.generateAirBox(world, sbb, 1, 1, 1, 14, 4, 14);

		// entrance pit
		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.defaultBlockState(), AIR, false, false);
//		this.fillWithBlocks(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS, 0, AIR, false);

		this.generateAirBox(world, sbb, 6, 0, 6, 9, 4, 9);
	}
}
