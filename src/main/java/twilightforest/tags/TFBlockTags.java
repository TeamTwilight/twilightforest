package twilightforest.tags;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;

public class TFBlockTags {

	public static final TagKey<Block> MAZESTONE = create("mazestone");
	public static final TagKey<Block> TOWERWOOD = create("towerwood");
	public static final TagKey<Block> CLOUDS = create("clouds");
	public static final TagKey<Block> DEADROCK = create("deadrock");
	public static final TagKey<Block> CASTLE_BLOCKS = create("castle_blocks");

	public static final TagKey<Block> TF_LOGS = create("logs");
	public static final TagKey<Block> TWILIGHT_OAK_LOGS = create("twilight_oak_logs");
	public static final TagKey<Block> CANOPY_LOGS = create("canopy_logs");
	public static final TagKey<Block> MANGROVE_LOGS = create("mangrove_logs");
	public static final TagKey<Block> DARKWOOD_LOGS = create("darkwood_logs");
	public static final TagKey<Block> TIME_LOGS = create("timewood_logs");
	public static final TagKey<Block> TRANSFORMATION_LOGS = create("transwood_logs");
	public static final TagKey<Block> MINING_LOGS = create("mining_logs");
	public static final TagKey<Block> SORTING_LOGS = create("sortwood_logs");

	public static final TagKey<Block> HOLLOW_LOGS = create("hollow_logs");
	public static final TagKey<Block> HOLLOW_LOGS_HORIZONTAL = create("hollow_logs_horizontal");
	public static final TagKey<Block> HOLLOW_LOGS_VERTICAL = create("hollow_logs_vertical");
	public static final TagKey<Block> HOLLOW_LOGS_CLIMBABLE = create("hollow_logs_climbable");

	public static final TagKey<Block> BANISTERS = create("banisters");
	public static final TagKey<Block> TF_CHESTS = create("chests");

	public static final TagKey<Block> PORTAL_EDGE = create("portal/edge");
	public static final TagKey<Block> PORTAL_POOL = create("portal/fluid");
	public static final TagKey<Block> PORTAL_DECO = create("portal/decoration");
	public static final TagKey<Block> GENERATED_PORTAL_DECO = create("portal/generated_decoration");

	public static final TagKey<Block> DARK_TOWER_ALLOWED_POTS = create("dark_tower_allowed_pots");
	public static final TagKey<Block> TROPHIES = create("trophies");
	public static final TagKey<Block> TROPHY_PEDESTAL_ACTIVATION_BLOCKS = create("trophy_pedestal_activation_blocks");
	public static final TagKey<Block> FIRE_JET_FUEL = create("fire_jet_fuel");
	public static final TagKey<Block> ICE_BOMB_REPLACEABLES = create("ice_bomb_replaceables");
	public static final TagKey<Block> MAZEBREAKER_ACCELERATED = create("mazebreaker_accelerated_mining");

	public static final TagKey<Block> COMMON_PROTECTIONS = create("common_protections");
	public static final TagKey<Block> ANNIHILATION_INCLUSIONS = create("annihilation_inclusions");
	public static final TagKey<Block> ANTIBUILDER_IGNORES = create("antibuilder_ignores");
	public static final TagKey<Block> CARMINITE_REACTOR_IMMUNE = create("carminite_reactor_immune");
	public static final TagKey<Block> CARMINITE_REACTOR_ORES = create("carminite_reactor_ores");
	public static final TagKey<Block> STRUCTURE_BANNED_INTERACTIONS = create("structure_banned_interactions");
	public static final TagKey<Block> PROGRESSION_ALLOW_BREAKING = create("progression_allow_breaking");
	public static final TagKey<Block> CANNOT_TROLL_CAVE_HOLLOW = create("cannot_troll_cave_hollow");

	public static final TagKey<Block> WORLDGEN_REPLACEABLES = create("worldgen_replaceables");
	public static final TagKey<Block> ROOT_TRACE_SKIP = create("tree_roots_skip");
	public static final TagKey<Block> SUPPORTS_STALAGMITES = create("supports_stalagmites");
	public static final TagKey<Block> CARVER_REPLACEABLES = create("carver_replaceables");
	public static final TagKey<Block> PLANTS_HANG_ON = create("plants_hang_on");
	public static final TagKey<Block> OREBERRY_BUSHES_SURVIVE = create("oreberry_bushes_survive");
	public static final TagKey<Block> TF_BERRY_BUSHES_SURVIVE = create("tf_berry_bushes_survive");
	public static final TagKey<Block> TF_BERRY_BUSHES_REPLACE = create("tf_berry_bushes_replace");
	public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_SURVIVE = create("dark_tower_berry_bushes_survive");
	public static final TagKey<Block> DARK_TOWER_BERRY_BUSHES_DIE = create("dark_tower_berry_bushes_die");
	public static final TagKey<Block> HUGE_MUSHGLOOM_PLACEABLE = create("huge_mushgloom_placeable");

	public static final TagKey<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = create("ore_magnet/ore_safe_replace_block");
	public static final TagKey<Block> ORE_MAGNET_IGNORE = create("ore_magnet/ignored_ores");

	public static final TagKey<Block> ROOT_GROUND = makeCommonTag("ore_bearing_ground/root");
	public static final TagKey<Block> ROOT_ORES = makeCommonTag("ores_in_ground/root");

	public static final TagKey<Block> TIME_CORE_EXCLUDED = create("time_core_excluded");
	public static final TagKey<Block> MINING_CORE_EXCLUDED = create("mining_tree_excluded");
	public static final TagKey<Block> ORE_METER_TARGETABLE = create("ore_meter_targetable");

	public static final TagKey<Block> PENGUINS_SPAWNABLE_ON = create("penguins_spawnable_on");
	public static final TagKey<Block> GIANTS_SPAWNABLE_ON = create("giants_spawnable_on");
	public static final TagKey<Block> DRUID_PROJECTILE_REPLACEABLE = create("druid_projectile_replaceable");

	public static final TagKey<Block> STORAGE_BLOCKS_ARCTIC_FUR = makeCommonTag("storage_blocks/arctic_fur");
	public static final TagKey<Block> STORAGE_BLOCKS_CARMINITE = makeCommonTag("storage_blocks/carminite");
	public static final TagKey<Block> STORAGE_BLOCKS_FIERY = makeCommonTag("storage_blocks/fiery");
	public static final TagKey<Block> STORAGE_BLOCKS_IRONWOOD = makeCommonTag("storage_blocks/ironwood");
	public static final TagKey<Block> STORAGE_BLOCKS_KNIGHTMETAL = makeCommonTag("storage_blocks/knightmetal");
	public static final TagKey<Block> STORAGE_BLOCKS_STEELEAF = makeCommonTag("storage_blocks/steeleaf");

	public static final TagKey<Block> INCORRECT_FOR_IRONWOOD_TOOL = create("incorrect_for_ironwood_tool");
	public static final TagKey<Block> INCORRECT_FOR_FIERY_TOOL = create("incorrect_for_fiery_tool");
	public static final TagKey<Block> INCORRECT_FOR_STEELEAF_TOOL = create("incorrect_for_steeleaf_tool");
	public static final TagKey<Block> INCORRECT_FOR_KNIGHTMETAL_TOOL = create("incorrect_for_knightmetal_tool");
	public static final TagKey<Block> INCORRECT_FOR_GIANT_TOOL = create("incorrect_for_giant_tool");
	public static final TagKey<Block> INCORRECT_FOR_ICE_TOOL = create("incorrect_for_ice_tool");
	public static final TagKey<Block> INCORRECT_FOR_GLASS_TOOL = create("incorrect_for_glass_tool");

	public static final TagKey<Block> MINEABLE_WITH_BLOCK_AND_CHAIN = create("mineable_with_block_and_chain");
	public static final TagKey<Block> BLOCK_AND_CHAIN_NEVER_BREAKS = create("block_and_chain_never_breaks");

	public static final TagKey<Block> SMALL_LAKES_DONT_REPLACE = create("small_lakes_dont_replace");
	public static final TagKey<Block> DRYING_RACKS = create("drying_racks");

	public static final TagKey<Block> AC_FERROMAGNETIC_BLOCKS = create("alexscaves", "ferromagnetic_blocks");
	public static final TagKey<Block> AC_GLOOMOTH_LIGHT_SOURCES = create("alexscaves", "gloomoth_light_sources");
	public static final TagKey<Block> AC_UNDERZEALOT_LIGHT_SOURCES = create("alexscaves", "underzealot_light_sources");

	public static final TagKey<Block> ARTIFACTS_CAMPSITE_CHESTS = create("artifacts", "campsite_chests");

	public static final TagKey<Block> FD_COMPOST_ACTIVATORS = create("farmersdelight", "compost_activators");
	public static final TagKey<Block> FD_HEAT_SOURCES = create("farmersdelight", "heat_sources");

	private static TagKey<Block> create(String tagName) {
		return BlockTags.create(TwilightForestMod.prefix(tagName));
	}

	public static TagKey<Block> makeCommonTag(String tagName) {
		return create("c", tagName);
	}

	private static TagKey<Block> create(String modid, String tagName) {
		return BlockTags.create(Identifier.fromNamespaceAndPath(modid, tagName));
	}
}
