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
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.type.AuroraPalaceStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuroraPalaceStructureGenerator {

    public static AuroraPalaceStructure buildAuroraPalaceConfig(BootstrapContext<Structure> context) {
        return new AuroraPalaceStructure(
            ControlledSpawns.ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.SNOW_GUARDIAN.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.STABLE_ICE_CORE.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.UNSTABLE_ICE_CORE.get(), 1, 2), 5)
                .build()
            ),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_yeti"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("icetower", 3), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(2, false, true, false)),
            true, Optional.of(TFMapDecorations.AURORA_PALACE),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_AURORA_PALACE_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.of()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.NONE
            )
        );
    }

}
