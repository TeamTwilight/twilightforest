package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.Random;

public class CourtyardPathPiece extends TwilightTemplateStructurePiece {
    public CourtyardPathPiece(ServerLevel level, CompoundTag nbt) {
        super(NagaCourtyardPieces.TFNCPa, nbt, level, readSettings(nbt));
    }

    public CourtyardPathPiece(int i, int x, int y, int z, StructureManager structureManager) {
        super(NagaCourtyardPieces.TFNCPa, i, structureManager, TwilightForestMod.prefix("courtyard/pathway"), makeSettings(Rotation.NONE).addProcessor(NagastoneVariants.INSTANCE), new BlockPos(x, y + 1, z));
    }

    @Override
    public boolean postProcess(WorldGenLevel level, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        return this.placePieceAdjusted(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos, -1);
    }


    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    public NoiseEffect getNoiseEffect() {
        return NoiseEffect.BEARD;
    }
}
