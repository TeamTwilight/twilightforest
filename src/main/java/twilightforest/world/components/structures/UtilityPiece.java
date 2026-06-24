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
import twilightforest.init.TFStructurePieceTypes;

public class UtilityPiece extends StructurePiece {
	public final boolean allowFeatures;

	public UtilityPiece(int genDepth, BoundingBox boundingBox) {
		this(genDepth, boundingBox, true);
	}

	public UtilityPiece(int genDepth, BoundingBox boundingBox, boolean allowFeatures) {
		super(TFStructurePieceTypes.TFUtilityPiece.value(), genDepth, boundingBox);
		this.allowFeatures = allowFeatures;
	}

	public UtilityPiece(StructurePieceSerializationContext context, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TFUtilityPiece.value(), compoundTag);
		this.allowFeatures = compoundTag.getBooleanOr("allow_features", true);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putBoolean("allow_features", allowFeatures);
	}


	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {}
}
