package twilightforest.datagen.data.worldgen.structures;

import net.minecraft.core.HolderSet;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.init.TFBlocks;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.type.FallenTrunkStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FallenTrunkStructureGenerator {

    public static FallenTrunkStructure buildStructureConfig(HolderSet<Biome> biomes) {
        return new FallenTrunkStructure(
            new Structure.StructureSettings(
                biomes,
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, _ -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.NONE
            ),
            UniformInt.of(17, 24), UniformInt.of(22, 28), BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()), TFLootTables.FALLEN_TRUNK_LOOT
        );
    }

}
