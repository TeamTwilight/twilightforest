package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFStructurePieceTypes;

public class TerraformingPiece extends StructurePiece {
	public TerraformingPiece(int genDepth, BoundingBox boundingBox) {
		super(TFStructurePieceTypes.TFTerraformingPiece.value(), genDepth, boundingBox);
	}

	public TerraformingPiece(StructurePieceSerializationContext context, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TFTerraformingPiece.value(), compoundTag);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}


	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {}
}
