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
import twilightforest.world.components.structures.type.LichTowerStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LichTowerStructureGenerator {

    @SuppressWarnings("unchecked")
    public static LichTowerStructure buildLichTowerConfig(BootstrapContext<Structure> context) {
        WeightedList<MobSpawnSettings.SpawnerData> yardSpawns = WeightedList.<MobSpawnSettings.SpawnerData>builder()
            .add(new MobSpawnSettings.SpawnerData(TFEntities.RISING_ZOMBIE.value(), 1, 2), 2)
            .build();
        WeightedList<MobSpawnSettings.SpawnerData> interiorSpawns = WeightedList.<MobSpawnSettings.SpawnerData>builder()
            .add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2), 10)
            .add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2), 10)
            .add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1), 1)
            .add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 1)
            .add(new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.value(), 2, 3), 10)
            .add(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1), 1)
            .build();
        ControlledSpawns.ControlledSpawningConfig monsters = ControlledSpawns.ControlledSpawningConfig.justMonsters(
            yardSpawns,
            interiorSpawns
        );
        return new LichTowerStructure(
            monsters,
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(0, false, true, false, true)),
            true, Optional.of(TFMapDecorations.LICH_TOWER),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_LICH_TOWER_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.BEARD_THIN
            )
        );
    }

}
