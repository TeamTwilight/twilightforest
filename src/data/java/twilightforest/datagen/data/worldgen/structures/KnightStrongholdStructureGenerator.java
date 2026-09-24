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
import twilightforest.world.components.structures.type.KnightStrongholdStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KnightStrongholdStructureGenerator {

    public static KnightStrongholdStructure buildKnightStrongholdConfig(BootstrapContext<Structure> context) {
        return new KnightStrongholdStructure(
            ControlledSpawns.ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.BLOCKCHAIN_GOBLIN.get(), 1, 2), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.LOWER_GOBLIN_KNIGHT.get(), 1, 2), 5)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.HELMET_CRAB.get(), 2, 4), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.SLIME_BEETLE.get(), 2, 3), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.REDCAP_SAPPER.get(), 1, 2), 2)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 2, 4), 10)
                .add(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 2), 5)
                .add(new MobSpawnSettings.SpawnerData(EntityType.SLIME, 4, 4), 5)
                .build()
            ),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_trophy_pedestal"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("tfstronghold", 4), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(0, true, false, false, false)),
            true, Optional.of(TFMapDecorations.KNIGHT_STRONGHOLD),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_KNIGHT_STRONGHOLD_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                TerrainAdjustment.BURY
            )
        );
    }

}
