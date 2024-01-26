package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

public class CourtyardPathPiece extends TwilightTemplateStructurePiece {
    public CourtyardPathPiece(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(TFStructurePieceTypes.TFNCPa.get(), nbt, ctx, readSettings(nbt).addProcessor(NagastoneVariants.INSTANCE));
    }

    public CourtyardPathPiece(int i, int x, int y, int z, StructureTemplateManager structureManager) {
        super(TFStructurePieceTypes.TFNCPa.get(), i, structureManager, TwilightForestMod.prefix("courtyard/pathway"), makeSettings(Rotation.NONE).addProcessor(NagastoneVariants.INSTANCE), new BlockPos(x, y + 1, z));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureFeatureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        this.placePieceAdjusted(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos, -1);
    }


    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {

    }
}
