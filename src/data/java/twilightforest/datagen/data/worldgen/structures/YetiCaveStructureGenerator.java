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
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFMapDecorations;
import twilightforest.init.custom.StructureSpeleothemConfigs;
import twilightforest.tags.TFBiomeTags;
import twilightforest.world.components.structures.type.YetiCaveStructure;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class YetiCaveStructureGenerator {

    public static YetiCaveStructure buildYetiCaveConfig(BootstrapContext<Structure> context) {
        return new YetiCaveStructure(
            ControlledSpawns.ControlledSpawningConfig.firstIndexMonsters(WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(TFEntities.YETI.get(), 1, 2), 5)
                .build()
            ),
            new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_lich"))),
            Optional.of(new StructureHints.HintConfig(StructureHints.HintConfig.book("yeticave", 3), TFEntities.KOBOLD.get())),
            Optional.of(new DecorationClearance.DecorationConfig(2, true, false, false)),
            false, Optional.of(TFMapDecorations.YETI_LAIR),
            new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_YETI_CAVE_BIOMES),
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.NONE
            ),
            context.lookup(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS).getOrThrow(StructureSpeleothemConfigs.YETI_CAVE)
        );
    }

}
