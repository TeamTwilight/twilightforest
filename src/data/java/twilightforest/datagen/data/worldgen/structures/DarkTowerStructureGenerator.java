package twilightforest.datagen.data.worldgen.structures;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
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
import twilightforest.world.components.structures.type.DarkTowerStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DarkTowerStructureGenerator {

    public static DarkTowerStructure buildDarkTowerConfig(BootstrapContext<Structure> context) {
        return new DarkTowerStructure(
            ControlledSpawns.ControlledSpawningConfig.create(List.of(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GOLEM.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1), 5)
                .add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 2)
                .add(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1), 1)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GHASTLING.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_BROODLING.get(), 4, 4), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 1, 1), 10)
                .build(),
                // roof ghasts
                WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.CARMINITE_GHASTGUARD.get(), 1, 2), 10)
                .build()
            ), WeightedList.of(), WeightedList.<MobSpawnSettings.SpawnerData>builder()
                // aquarium squids (only in aquariums between y = 35 and y = 64. :/)
                .add(new MobSpawnSettings.SpawnerData(EntityType.SQUID, 4, 4), 10)
                .build()
            ),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_knights"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("darktower", 3), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(1, false, true, true)),
            true, Optional.of(TFMapDecorations.DARK_TOWER),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_DARK_TOWER_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.BEARD_THIN
            )
        );
    }

}
