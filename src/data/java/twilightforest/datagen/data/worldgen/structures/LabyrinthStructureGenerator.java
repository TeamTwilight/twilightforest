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
import twilightforest.world.components.structures.type.LabyrinthStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LabyrinthStructureGenerator {

    public static LabyrinthStructure buildLabyrinthConfig(BootstrapContext<Structure> context) {
        return new LabyrinthStructure(
            ControlledSpawns.ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.MINOTAUR.get(), 2, 3), 20)
                .add(new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.MAZE_SLIME.get(), 2, 4), 10)
                .add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2), 1)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.FIRE_BEETLE.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.PINCH_BEETLE.get(), 1, 1), 10)
                .build()
            ),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("labyrinth", 5), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(3, true, false, false)),
            true, Optional.of(TFMapDecorations.LABYRINTH),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_LABYRINTH_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                TerrainAdjustment.BURY
            )
        );
    }

}
