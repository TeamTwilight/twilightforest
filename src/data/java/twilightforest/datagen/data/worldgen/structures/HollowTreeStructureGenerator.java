package twilightforest.datagen.data.worldgen.structures;

import net.minecraft.core.HolderSet;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.world.components.structures.hollowtree.HollowTreePiece;
import twilightforest.world.components.structures.type.HollowTreeStructure;
import twilightforest.world.components.structures.util.DecorationClearance;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HollowTreeStructureGenerator {

    public static HollowTreeStructure buildStructureConfig(boolean allowInWater, HolderSet<Biome> biomes) {
        return new HollowTreeStructure(
            new Structure.StructureSettings(
                biomes,
                Arrays.stream(MobCategory.values()).collect(Collectors.<MobCategory, MobCategory, StructureSpawnOverride>toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder().build()))), // Landmarks have Controlled Mob spawning
                GenerationStep.Decoration.SURFACE_STRUCTURES,
                TerrainAdjustment.NONE
            ),
            new DecorationClearance.DecorationConfig(0.5f, false, true, true),
            HollowTreePiece.DEFAULT_HEIGHT,
            HollowTreePiece.DEFAULT_RADIUS,
            HollowTreePiece.DEFAULT_LOG,
            HollowTreePiece.DEFAULT_WOOD,
            HollowTreePiece.DEFAULT_ROOT,
            HollowTreePiece.DEFAULT_LEAVES,
            HollowTreePiece.DEFAULT_VINE,
            HollowTreePiece.DEFAULT_BUG,
            HollowTreePiece.DEFAULT_WOOD,
            HollowTreePiece.DEFAULT_DUNGEON_AIR,
            HollowTreePiece.DEFAULT_DUNGEON_LOOT_BLOCK,
            HollowTreePiece.DEFAULT_DUNGEON_LOOT_TABLE,
            HollowTreePiece.DEFAULT_DUNGEON_MONSTER,
            allowInWater
        );
    }

}
