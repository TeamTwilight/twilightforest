package twilightforest.datagen.data.worldgen.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.init.TFMapDecorations;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.type.NagaCourtyardStructure;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class NagaCourtyardStructureGenerator {

    public static NagaCourtyardStructure buildStructureConfig(BootstrapContext<Structure> context) {
        return new NagaCourtyardStructure(
            Optional.of(new DecorationClearance.DecorationConfig(3, false, true, true)),
            true, Optional.of(TFMapDecorations.NAGA_COURTYARD),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_NAGA_COURTYARD_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, _ -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.BEARD_THIN
            )
        );
    }

}
