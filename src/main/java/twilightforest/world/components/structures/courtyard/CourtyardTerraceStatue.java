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
import twilightforest.world.components.processors.StoneBricksVariants;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

public class CourtyardTerraceStatue extends TwilightTemplateStructurePiece {
    public CourtyardTerraceStatue(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(TFStructurePieceTypes.TFNCSt.get(), nbt, ctx, readSettings(nbt).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE));
    }

    public CourtyardTerraceStatue(int i, int x, int y, int z, Rotation rotation, StructureTemplateManager structureManager) {
        super(TFStructurePieceTypes.TFNCSt.get(), i, structureManager, TwilightForestMod.prefix("terrace_statue/terrace_fire"), makeSettings(rotation).addProcessor(CourtyardTerraceTemplateProcessor.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE), new BlockPos(x, y + 3, z));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureFeatureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        this.placePieceAdjusted(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos, -3);
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {

    }
}
