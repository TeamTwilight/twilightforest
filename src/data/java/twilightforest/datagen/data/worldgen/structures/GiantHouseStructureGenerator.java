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
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.type.GiantHouseStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GiantHouseStructureGenerator {

    public static GiantHouseStructure buildGiantHouseConfig(BootstrapContext<Structure> context) {
        return new GiantHouseStructure(
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_merge"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("trollcave", 3), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(1, true, true, false)),
            false, Optional.empty(),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_GIANT_HOUSE_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                TerrainAdjustment.NONE
            ),
            ControlledSpawns.ControlledSpawningConfig.create(List.of(WeightedList.<MobSpawnSettings.SpawnerData>builder() // cloud monsters
                .add(new MobSpawnSettings.SpawnerData(TFEntities.GIANT_MINER.get(), 1, 1), 10)
                .add(new MobSpawnSettings.SpawnerData(TFEntities.ARMORED_GIANT.get(), 1, 1), 10)
                .build()
            ), WeightedList.of(), WeightedList.of())
        );
    }

}
