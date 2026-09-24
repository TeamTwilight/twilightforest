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
import twilightforest.world.components.structures.type.FinalCastleStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinalCastleStructureGenerator {

    public static FinalCastleStructure buildFinalCastleConfig(BootstrapContext<Structure> context) {
        return new FinalCastleStructure( // TODO Re-enable mob spawns when proper castle mobs are created
            ControlledSpawns.ControlledSpawningConfig.create(List.of(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                // plain parts of the castle, like the tower maze
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 2), 10)
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 1, 1), 10)
                //.add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1), 10)
                .build()
            , WeightedList.<MobSpawnSettings.SpawnerData>builder()
                // internal castle
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 2), 10)
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.HARBINGER_CUBE.get(), 1, 1), 10)
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT.get(), 1, 1), 10)
                .build()
            , WeightedList.<MobSpawnSettings.SpawnerData>builder()
                // dungeons
                //.add(new MobSpawnSettings.SpawnerData(TFEntities.ADHERENT.get(), 1, 1), 10)
                .build()
            , WeightedList.<MobSpawnSettings.SpawnerData>builder()
                // forge
                //.add(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 1, 1), 10)
                .build()
            ), WeightedList.of(), WeightedList.of()),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_troll"))),
            // TODO: change this when we make a book for the castle
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.defaultBook(), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(4, false, true, false)),
            true, Optional.of(TFMapDecorations.FINAL_CASTLE),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_FINAL_CASTLE_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.BEARD_BOX
            )
        );
    }

}
