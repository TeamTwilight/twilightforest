package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.type.*;

public class TFStructures {

	public static final ResourceKey<Structure> HEDGE_MAZE = registerKey("hedge_maze");
	public static final ResourceKey<Structure> QUEST_GROVE = registerKey("quest_grove");
	public static final ResourceKey<Structure> HOLLOW_HILL_SMALL = registerKey("small_hollow_hill");
	public static final ResourceKey<Structure> HOLLOW_HILL_MEDIUM = registerKey("medium_hollow_hill");
	public static final ResourceKey<Structure> HOLLOW_HILL_LARGE = registerKey("large_hollow_hill");
	public static final ResourceKey<Structure> NAGA_COURTYARD = registerKey("naga_courtyard");
	public static final ResourceKey<Structure> LICH_TOWER = registerKey("lich_tower");
	public static final ResourceKey<Structure> LABYRINTH = registerKey("labyrinth");
	public static final ResourceKey<Structure> HYDRA_LAIR = registerKey("hydra_lair");
	public static final ResourceKey<Structure> KNIGHT_STRONGHOLD = registerKey("knight_stronghold");
	public static final ResourceKey<Structure> DARK_TOWER = registerKey("dark_tower");
	public static final ResourceKey<Structure> YETI_CAVE = registerKey("yeti_cave");
	public static final ResourceKey<Structure> AURORA_PALACE = registerKey("aurora_palace");
	public static final ResourceKey<Structure> TROLL_CAVE = registerKey("troll_cave");
	public static final ResourceKey<Structure> GIANT_HOUSE = registerKey("giant_house");
	public static final ResourceKey<Structure> FINAL_CASTLE = registerKey("final_castle");

	public static final ResourceKey<Structure> MUSHROOM_TOWER = registerKey("mushroom_tower");
	public static final ResourceKey<Structure> QUEST_ISLAND = registerKey("quest_island");
	public static final ResourceKey<Structure> DRUID_GROVE = registerKey("druid_grove");
	public static final ResourceKey<Structure> FLOATING_RUINS = registerKey("floating_ruins");
	public static final ResourceKey<Structure> WORLD_TREE = registerKey("world_tree");

	public static ResourceKey<Structure> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE, TwilightForestMod.prefix(name));
	}

	@SuppressWarnings("deprecation")
	public static void bootstrap(BootstapContext<Structure> context) {
		context.register(HEDGE_MAZE, HedgeMazeStructure.buildStructureConfig(context));
		context.register(QUEST_GROVE, QuestGroveStructure.buildStructureConfig(context));
		context.register(HOLLOW_HILL_SMALL, HollowHillStructure.buildSmallHillConfig(context));
		context.register(HOLLOW_HILL_MEDIUM, HollowHillStructure.buildMediumHillConfig(context));
		context.register(HOLLOW_HILL_LARGE, HollowHillStructure.buildLargeHillConfig(context));
		context.register(NAGA_COURTYARD, NagaCourtyardStructure.buildStructureConfig(context));
		context.register(LICH_TOWER, LichTowerStructure.buildLichTowerConfig(context));
		context.register(LABYRINTH, LabyrinthStructure.buildLabyrinthConfig(context));
		context.register(HYDRA_LAIR, HydraLairStructure.buildHydraLairConfig(context));
		context.register(KNIGHT_STRONGHOLD, KnightStrongholdStructure.buildKnightStrongholdConfig(context));
		context.register(DARK_TOWER, DarkTowerStructure.buildDarkTowerConfig(context));
		context.register(YETI_CAVE, YetiCaveStructure.buildYetiCaveConfig(context));
		context.register(AURORA_PALACE, AuroraPalaceStructure.buildAuroraPalaceConfig(context));
		context.register(TROLL_CAVE, TrollCaveStructure.buildTrollCaveConfig(context));
		context.register(GIANT_HOUSE, GiantHouseStructure.buildGiantHouseConfig(context));
		context.register(FINAL_CASTLE, FinalCastleStructure.buildFinalCastleConfig(context));

		context.register(MUSHROOM_TOWER, MushroomTowerStructure.buildStructureConfig(context));
	}
}
