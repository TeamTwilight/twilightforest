package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import twilightforest.TwilightForestMod;

public class TFStructureSets {

	public static final ResourceKey<StructureSet> FALLEN_TRUNK = registerKey("fallen_trunk");
	public static final ResourceKey<StructureSet> HOLLOW_TREE = registerKey("hollow_tree");
	public static final ResourceKey<StructureSet> CAMP = registerKey("camp");
	public static final ResourceKey<StructureSet> HEDGE_MAZE = registerKey("hedge_maze");
	public static final ResourceKey<StructureSet> QUEST_GROVE = registerKey("quest_grove");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_SMALL = registerKey("small_hollow_hill");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_MEDIUM = registerKey("medium_hollow_hill");
	public static final ResourceKey<StructureSet> HOLLOW_HILL_LARGE = registerKey("large_hollow_hill");
	public static final ResourceKey<StructureSet> NAGA_COURTYARD = registerKey("naga_courtyard");
	public static final ResourceKey<StructureSet> LICH_TOWER = registerKey("lich_tower");
	public static final ResourceKey<StructureSet> LABYRINTH = registerKey("labyrinth");
	public static final ResourceKey<StructureSet> HYDRA_LAIR = registerKey("hydra_lair");
	public static final ResourceKey<StructureSet> KNIGHT_STRONGHOLD = registerKey("knight_stronghold");
	public static final ResourceKey<StructureSet> DARK_TOWER = registerKey("dark_tower");
	public static final ResourceKey<StructureSet> YETI_CAVE = registerKey("yeti_cave");
	public static final ResourceKey<StructureSet> AURORA_PALACE = registerKey("aurora_palace");
	public static final ResourceKey<StructureSet> TROLL_CAVE = registerKey("troll_cave");
	public static final ResourceKey<StructureSet> GIANT_HOUSE = registerKey("giant_house");
	public static final ResourceKey<StructureSet> FINAL_CASTLE = registerKey("final_castle");

	public static final ResourceKey<StructureSet> MUSHROOM_TOWER = registerKey("mushroom_tower");
	public static final ResourceKey<StructureSet> QUEST_ISLAND = registerKey("quest_island");
	public static final ResourceKey<StructureSet> DRUID_GROVE = registerKey("druid_grove");
	public static final ResourceKey<StructureSet> FLOATING_RUINS = registerKey("floating_ruins");
	public static final ResourceKey<StructureSet> WORLD_TREE = registerKey("world_tree");

	private static ResourceKey<StructureSet> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE_SET, TwilightForestMod.prefix(name));
	}

}
