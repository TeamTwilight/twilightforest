package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.LandmarkStructure;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NagaCourtyardStructure extends LandmarkStructure {
    public static final Codec<NagaCourtyardStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, NagaCourtyardStructure::new));

    public NagaCourtyardStructure(DecorationConfig decorationConfig, StructureSettings structureSettings) {
        super(decorationConfig, structureSettings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return TFLandmark.NAGA_COURTYARD.generateStub(context, this);
    }

    @Override
    public StructureType<?> type() {
        return TFStructureTypes.NAGA_COURTYARD.get();
    }

    public static NagaCourtyardStructure buildStructureConfig(BootstapContext<Structure> context) {
        return new NagaCourtyardStructure(
                new DecorationConfig(false, true, true),
                new StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_NAGA_COURTYARD_BIOMES),
                        Map.of(), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                )
        );
    }
}
