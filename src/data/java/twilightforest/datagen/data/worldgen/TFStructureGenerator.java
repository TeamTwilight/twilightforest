package twilightforest.datagen.data.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.datagen.data.worldgen.structures.*;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFStructures;
import twilightforest.tags.TFBiomeTags;

public class TFStructureGenerator {

    public static void bootstrap(BootstrapContext<Structure> context) {
        context.register(TFStructures.FALLEN_TRUNK, FallenTrunkStructureGenerator.buildStructureConfig(context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_HOLLOW_TREE_BIOMES)));
        context.register(TFStructures.HOLLOW_TREE, HollowTreeStructureGenerator.buildStructureConfig(false, context.lookup(Registries.BIOME).getOrThrow(TFBiomeTags.VALID_HOLLOW_TREE_BIOMES)));
        context.register(TFStructures.SWAMP_HOLLOW_TREE, HollowTreeStructureGenerator.buildStructureConfig(true, HolderSet.direct(context.lookup(Registries.BIOME).getOrThrow(TFBiomes.SWAMP))));
        context.register(TFStructures.CAMP, CampStructureGenerator.buildStructureConfig(context));
        context.register(TFStructures.HEDGE_MAZE, HedgeMazeStructureGenerator.buildStructureConfig(context));
        context.register(TFStructures.QUEST_GROVE, QuestGroveStructureGenerator.buildStructureConfig(context));
        context.register(TFStructures.HOLLOW_HILL_SMALL, HollowHillStructureGenerator.buildSmallHillConfig(context));
        context.register(TFStructures.HOLLOW_HILL_MEDIUM, HollowHillStructureGenerator.buildMediumHillConfig(context));
        context.register(TFStructures.HOLLOW_HILL_LARGE, HollowHillStructureGenerator.buildLargeHillConfig(context));
        context.register(TFStructures.NAGA_COURTYARD, NagaCourtyardStructureGenerator.buildStructureConfig(context));
        context.register(TFStructures.LICH_TOWER, LichTowerStructureGenerator.buildLichTowerConfig(context));
        context.register(TFStructures.LABYRINTH, LabyrinthStructureGenerator.buildLabyrinthConfig(context));
        context.register(TFStructures.HYDRA_LAIR, HydraLairStructureGenerator.buildHydraLairConfig(context));
        context.register(TFStructures.KNIGHT_STRONGHOLD, KnightStrongholdStructureGenerator.buildKnightStrongholdConfig(context));
        context.register(TFStructures.DARK_TOWER, DarkTowerStructureGenerator.buildDarkTowerConfig(context));
        context.register(TFStructures.YETI_CAVE, YetiCaveStructureGenerator.buildYetiCaveConfig(context));
        context.register(TFStructures.AURORA_PALACE, AuroraPalaceStructureGenerator.buildAuroraPalaceConfig(context));
        context.register(TFStructures.TROLL_CAVE, TrollCaveStructureGenerator.buildTrollCaveConfig(context));
        context.register(TFStructures.GIANT_HOUSE, GiantHouseStructureGenerator.buildGiantHouseConfig(context));
        context.register(TFStructures.FINAL_CASTLE, FinalCastleStructureGenerator.buildFinalCastleConfig(context));

        context.register(TFStructures.MUSHROOM_TOWER, MushroomTowerStructureGenerator.buildStructureConfig(context));
    }

}
