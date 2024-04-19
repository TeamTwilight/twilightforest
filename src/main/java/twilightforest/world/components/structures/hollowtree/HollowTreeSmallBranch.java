package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;

public class HollowTreeSmallBranch extends HollowTreeMedBranch {
	protected HollowTreeSmallBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		super(TFStructurePieceTypes.TFHTSB.value(), i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy, wood, leaves);
	}

	public HollowTreeSmallBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTSB.value(), context, tag);
	}

	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource rand) {
		// No-op
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		this.drawBresehnam(level, writeableBounds, this.src, this.dest, this.wood, decoRNG);

		// with leaves!
		if (this.leafy) {
			int leafRad = decoRNG.nextInt(2) + 1;
			int sx = this.dest.getX() - this.boundingBox.minX();
			int sy = this.dest.getY() - this.boundingBox.minY();
			int sz = this.dest.getZ() - this.boundingBox.minZ();
			this.drawBlockBlob(level, writeableBounds, sx, sy, sz, leafRad, decoRNG, this.leaves, false, false, true);
		}
	}
}
