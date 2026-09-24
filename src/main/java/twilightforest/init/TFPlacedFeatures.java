package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.*;
import twilightforest.TwilightForestMod;

public class TFPlacedFeatures {

	public static final ResourceKey<PlacedFeature> PLACED_LAKE_LAVA = registerKey("lava_lake");
	public static final ResourceKey<PlacedFeature> PLACED_LAKE_WATER = registerKey("water_lake");
	public static final ResourceKey<PlacedFeature> PLACED_LAKE_FROZEN = registerKey("frozen_lake");
	public static final ResourceKey<PlacedFeature> PLACED_DRUID_HUT = registerKey("druid_hut");
	public static final ResourceKey<PlacedFeature> PLACED_DENSE_FERNS = registerKey("dense_ferns");
	public static final ResourceKey<PlacedFeature> PLACED_DENSE_LARGE_FERNS = registerKey("dense_large_ferns");
	public static final ResourceKey<PlacedFeature> PLACED_DENSE_LAKE_WATER = registerKey("dense_water_lake");
	public static final ResourceKey<PlacedFeature> PLACED_GRAVEYARD = registerKey("graveyard");
	public static final ResourceKey<PlacedFeature> PLACED_BIG_MUSHGLOOM = registerKey("big_mushgloom");
	public static final ResourceKey<PlacedFeature> PLACED_FALLEN_LEAVES = registerKey("fallen_leaves");
	public static final ResourceKey<PlacedFeature> PLACED_FIDDLEHEAD = registerKey("fiddlehead");
	public static final ResourceKey<PlacedFeature> PLACED_FIRE_JET = registerKey("fire_jet");
	public static final ResourceKey<PlacedFeature> PLACED_FOUNDATION = registerKey("foundation");
	public static final ResourceKey<PlacedFeature> PLACED_GROVE_RUINS = registerKey("grove_ruins");
	public static final ResourceKey<PlacedFeature> PLACED_HOLLOW_LOG = registerKey("hollow_log");
	public static final ResourceKey<PlacedFeature> PLACED_HOLLOW_STUMP = registerKey("hollow_stump");
	public static final ResourceKey<PlacedFeature> PLACED_HUGE_LILY_PAD = registerKey("huge_lily_pad");
	public static final ResourceKey<PlacedFeature> PLACED_HUGE_WATER_LILY = registerKey("huge_water_lily");
	public static final ResourceKey<PlacedFeature> PLACED_MAYAPPLE = registerKey("mayapple");
	public static final ResourceKey<PlacedFeature> PLACED_MONOLITH = registerKey("monolith");
	public static final ResourceKey<PlacedFeature> PLACED_MUSHGLOOM_CLUSTER = registerKey("mushgloom_cluster");
	public static final ResourceKey<PlacedFeature> PLACED_SPARSE_MUSHGLOOMS = registerKey("sparse_mushglooms");
	public static final ResourceKey<PlacedFeature> PLACED_MYCELIUM_BLOB = registerKey("mycelium_blob");
	public static final ResourceKey<PlacedFeature> PLACED_OUTSIDE_STALAGMITE = registerKey("outside_stalagmite");
	public static final ResourceKey<PlacedFeature> PLACED_PLANT_ROOTS = registerKey("plant_roots");
	public static final ResourceKey<PlacedFeature> PLACED_IRON_OREBERRIES = registerKey("iron_oreberries");
	public static final ResourceKey<PlacedFeature> PLACED_GOLD_OREBERRIES = registerKey("gold_oreberries");
	public static final ResourceKey<PlacedFeature> PLACED_COPPER_OREBERRIES = registerKey("copper_oreberries");
	public static final ResourceKey<PlacedFeature> PLACED_IRON_OREBERRIES_ENCHANTED_FOREST = registerKey("iron_oreberries_enchanted_forest");
	public static final ResourceKey<PlacedFeature> PLACED_GOLD_OREBERRIES_ENCHANTED_FOREST = registerKey("gold_oreberries_enchanted_forest");
	public static final ResourceKey<PlacedFeature> PLACED_COPPER_OREBERRIES_ENCHANTED_FOREST = registerKey("copper_oreberries_enchanted_forest");
	public static final ResourceKey<PlacedFeature> PLACED_ESSENCE_OREBERRIES_ENCHANTED_FOREST = registerKey("essence_oreberries_enchanted_forest");
	public static final ResourceKey<PlacedFeature> PLACED_RASPBERRY_BUSHES = registerKey("raspberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_SWAMP_RASPBERRY_BUSHES = registerKey("swamp_raspberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_BLUEBERRY_BUSHES = registerKey("blueberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_SNOWY_BLUEBERRY_BUSHES = registerKey("snowy_blueberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_BLACKBERRY_BUSHES = registerKey("blackberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_SWAMP_BLACKBERRY_BUSHES = registerKey("swamp_blackberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_MALOBERRY_BUSHES = registerKey("maloberry_bushes");
	public static final ResourceKey<PlacedFeature> PLACED_PUMPKIN_LAMPPOST = registerKey("pumpkin_lamppost");
	public static final ResourceKey<PlacedFeature> PLACED_SMOKER = registerKey("smoker");
	public static final ResourceKey<PlacedFeature> PLACED_STONE_CIRCLE = registerKey("stone_circle");
	public static final ResourceKey<PlacedFeature> PLACED_THORNS = registerKey("thorns");
	public static final ResourceKey<PlacedFeature> PLACED_TORCH_BERRIES = registerKey("torch_berries");
	public static final ResourceKey<PlacedFeature> PLACED_TROLL_ROOTS = registerKey("troll_roots");
	public static final ResourceKey<PlacedFeature> PLACED_TROLL_MUSHGLOOMS = registerKey("troll_mushglooms");
	public static final ResourceKey<PlacedFeature> PLACED_VANILLA_ROOTS = registerKey("vanilla_roots");
	public static final ResourceKey<PlacedFeature> PLACED_WEBS = registerKey("webs");
	public static final ResourceKey<PlacedFeature> PLACED_WOOD_ROOTS_SPREAD = registerKey("wood_roots");
	public static final ResourceKey<PlacedFeature> PLACED_SNOW_UNDER_TREES = registerKey("snow_under_trees");
	public static final ResourceKey<PlacedFeature> PLACED_ENCHANTED_FOREST_VINES = registerKey("enchanted_forest_vines");
	public static final ResourceKey<PlacedFeature> PLACED_TF_OAK_FALLEN_LOG = registerKey("tf_oak_fallen_log");
	public static final ResourceKey<PlacedFeature> PLACED_CANOPY_FALLEN_LOG = registerKey("canopy_fallen_log");
	public static final ResourceKey<PlacedFeature> PLACED_MANGROVE_FALLEN_LOG = registerKey("mangrove_fallen_log");
	public static final ResourceKey<PlacedFeature> PLACED_SPRUCE_FALLEN_LOG = registerKey("spruce_fallen_log");
	public static final ResourceKey<PlacedFeature> PLACED_SMALL_GRANITE = registerKey("small_granite");
	public static final ResourceKey<PlacedFeature> PLACED_SMALL_DIORITE = registerKey("small_diorite");
	public static final ResourceKey<PlacedFeature> PLACED_SMALL_ANDESITE = registerKey("small_andesite");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_MUSHGLOOMS = registerKey("dark_mushglooms");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_PUMPKINS = registerKey("dark_pumpkins");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_GRASS = registerKey("dark_grass");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_FERNS = registerKey("dark_ferns");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_BROWN_MUSHROOMS = registerKey("dark_brown_mushrooms");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_RED_MUSHROOMS = registerKey("dark_red_mushrooms");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_DEAD_BUSHES = registerKey("dark_dead_bushes");

	//Ores!
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_COAL_ORE = registerKey("legacy_coal_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_IRON_ORE = registerKey("legacy_iron_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_GOLD_ORE = registerKey("legacy_gold_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_REDSTONE_ORE = registerKey("legacy_redstone_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_DIAMOND_ORE = registerKey("legacy_diamond_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_LAPIS_ORE = registerKey("legacy_lapis_ore");
	public static final ResourceKey<PlacedFeature> PLACED_LEGACY_COPPER_ORE = registerKey("legacy_copper_ore");

	public static final ResourceKey<PlacedFeature> PLACED_WELL_PLACER = registerKey("well_placer");
	public static final ResourceKey<PlacedFeature> PLACED_LAMPPOST_PLACER = registerKey("lamppost_placer");
	public static final ResourceKey<PlacedFeature> PLACED_DEFAULT_FALLEN_LOGS = registerKey("default_fallen_logs");

	public static final ResourceKey<PlacedFeature> PLACED_FLOWER_PLACER = registerKey("flower_placer");
	public static final ResourceKey<PlacedFeature> PLACED_FLOWER_PLACER_ALT = registerKey("flower_placer_alt");

	//Trees!
	public static final ResourceKey<PlacedFeature> PLACED_DEAD_CANOPY_TREE = registerKey("tree/dead_canopy_tree");
	public static final ResourceKey<PlacedFeature> PLACED_MEGA_CANOPY_TREE = registerKey("tree/mega_canopy_tree");
	public static final ResourceKey<PlacedFeature> PLACED_MANGROVE_TREE = registerKey("tree/mangrove_tree");
	public static final ResourceKey<PlacedFeature> PLACED_TWILIGHT_OAK_TREE = registerKey("tree/twilight_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_LARGE_TWILIGHT_OAK_TREE = registerKey("tree/large_twilight_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_FOREST_MEGA_OAK_TREE = registerKey("tree/forest_mega_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_SAVANNAH_OAK_TREE = registerKey("tree/savannah_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_SAVANNAH_MEGA_OAK_TREE = registerKey("tree/savannah_mega_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_SWAMPY_OAK_TREE = registerKey("tree/swampy_oak_tree");
	public static final ResourceKey<PlacedFeature> PLACED_DARKWOOD_TREE = registerKey("tree/darkwood_tree");
	public static final ResourceKey<PlacedFeature> PLACED_OAK_BUSH = registerKey("tree/oak_bush");
	public static final ResourceKey<PlacedFeature> PLACED_OAK_BUSH_DENSE = registerKey("tree/oak_bush_dense");

	public static final ResourceKey<PlacedFeature> PLACED_CANOPY_TREES = registerKey("tree/selector/canopy_trees");
	public static final ResourceKey<PlacedFeature> PLACED_DENSE_CANOPY_TREES = registerKey("tree/selector/dense_canopy_trees");
	public static final ResourceKey<PlacedFeature> PLACED_FIREFLY_FOREST_TREES = registerKey("tree/selector/firefly_forest_trees");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_FOREST_TREES = registerKey("tree/selector/dark_forest_trees");
	public static final ResourceKey<PlacedFeature> PLACED_DARK_FOREST_TREE_MIX = registerKey("tree/selector/dark_forest_tree_mix");
	public static final ResourceKey<PlacedFeature> PLACED_HIGHLANDS_TREES = registerKey("tree/selector/highlands_trees");
	public static final ResourceKey<PlacedFeature> PLACED_ENCHANTED_FOREST_TREES = registerKey("tree/selector/enchanted_forest_trees");
	public static final ResourceKey<PlacedFeature> PLACED_SNOWY_FOREST_TREES = registerKey("tree/selector/snowy_forest_trees");
	public static final ResourceKey<PlacedFeature> PLACED_VANILLA_TF_TREES = registerKey("tree/selector/vanilla_trees");
	public static final ResourceKey<PlacedFeature> PLACED_VANILLA_TF_BIG_MUSH = registerKey("tree/selector/vanilla_mushrooms");

	public static final ResourceKey<PlacedFeature> PLACED_CANOPY_MUSHROOMS_SPARSE = registerKey("mushroom/canopy_mushrooms_sparse");
	public static final ResourceKey<PlacedFeature> PLACED_CANOPY_MUSHROOMS_DENSE = registerKey("mushroom/canopy_mushrooms_dense");

	// Twilight variants of grass placers (Vanilla-copied), configured to avoid the Lich Tower
	// public static final ResourceKey<PlacedFeature> PATCH_GRASS_PLAIN = registerKey("patch_grass_plain");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_FOREST = registerKey("patch_grass_forest");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_BADLANDS = registerKey("patch_grass_badlands");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_SAVANNA = registerKey("patch_grass_savanna");
	// public static final ResourceKey<PlacedFeature> PATCH_GRASS_NORMAL = registerKey("patch_grass_normal");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA_2 = registerKey("patch_grass_taiga_2");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_TAIGA = registerKey("patch_grass_taiga");
	public static final ResourceKey<PlacedFeature> PATCH_GRASS_JUNGLE = registerKey("patch_grass_jungle");
	public static final ResourceKey<PlacedFeature> PATCH_TALL_GRASS = PlacementUtils.createKey("patch_tall_grass");
	public static final ResourceKey<PlacedFeature> PATCH_LARGE_FERN = PlacementUtils.createKey("patch_large_fern");
	public static final ResourceKey<PlacedFeature> FLOWER_FOREST_FLOWERS = PlacementUtils.createKey("flower_forest_flowers");

	public static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, TwilightForestMod.prefix(name));
	}

}
