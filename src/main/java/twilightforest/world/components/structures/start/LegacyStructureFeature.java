package twilightforest.world.components.structures.start;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

@Deprecated
public class LegacyStructureFeature extends TwilightStructureFeature<NoneFeatureConfiguration> {
    public final TFFeature feature;

    public LegacyStructureFeature(@Deprecated TFFeature feature) {
        super(NoneFeatureConfiguration.CODEC, configContext -> feature.generatePieces(configContext.chunkGenerator(), configContext.structureManager(), configContext.chunkPos(), configContext.heightAccessor(), new Random()).map(piece -> (structurePiecesBuilder, context) -> structurePiecesBuilder.addPiece(piece)));
        this.feature = feature;
    }

    //Temporally fixed Generation step error
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}
