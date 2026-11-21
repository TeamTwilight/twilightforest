package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.TwilightForestMod;

public final class TFFeatures {

	//vanilla features with custom placement code
	public static final ResourceKey<Feature> LAKE_LAVA = registerKey("lava_lake");
	public static final ResourceKey<Feature> LAKE_WATER = registerKey("water_lake");
	public static final ResourceKey<Feature> LAKE_FROZEN = registerKey("water_frozen");

	//"structures" that arent actually structures
	public static final ResourceKey<Feature> SIMPLE_WELL = registerKey("simple_well");
	public static final ResourceKey<Feature> FANCY_WELL = registerKey("fancy_well");
	public static final ResourceKey<Feature> DRUID_HUT = registerKey("druid_hut");
	public static final ResourceKey<Feature> GRAVEYARD = registerKey("graveyard");

	//all the fun little things you find around the dimension
	public static final ResourceKey<Feature> BIG_MUSHGLOOM = registerKey("mushroom/big_mushgloom");
	public static final ResourceKey<Feature> DENSE_FERNS = registerKey("dense_ferns");
	public static final ResourceKey<Feature> DENSE_LARGE_FERNS = registerKey("dense_large_ferns");
	public static final ResourceKey<Feature> FALLEN_LEAVES = registerKey("fallen_leaves");
	public static final ResourceKey<Feature> MAYAPPLE = registerKey("mayapple");
	public static final ResourceKey<Feature> FIDDLEHEAD = registerKey("fiddlehead");
	public static final ResourceKey<Feature> FIRE_JET = registerKey("fire_jet");
	public static final ResourceKey<Feature> FOUNDATION = registerKey("foundation");
	public static final ResourceKey<Feature> GROVE_RUINS = registerKey("grove_ruins");
	public static final ResourceKey<Feature> HOLLOW_LOG = registerKey("hollow_log");
	public static final ResourceKey<Feature> HOLLOW_STUMP = registerKey("hollow_stump");
	public static final ResourceKey<Feature> HUGE_LILY_PAD = registerKey("huge_lily_pad");
	public static final ResourceKey<Feature> HUGE_WATER_LILY = registerKey("huge_water_lily");
	public static final ResourceKey<Feature> CICADA_LAMPPOST = registerKey("cicada_lamppost");
	public static final ResourceKey<Feature> FIREFLY_LAMPPOST = registerKey("firefly_lamppost");
	public static final ResourceKey<Feature> MONOLITH = registerKey("monolith");
	public static final ResourceKey<Feature> MUSHGLOOM_CLUSTER = registerKey("mushgloom_cluster");
	public static final ResourceKey<Feature> MYCELIUM_BLOB = registerKey("mycelium_blob");
	public static final ResourceKey<Feature> OUTSIDE_STALAGMITE = registerKey("outside_stalagmite");
	public static final ResourceKey<Feature> PLANT_ROOTS = registerKey("plant_roots");
	public static final ResourceKey<Feature> IRON_OREBERRIES = registerKey("iron_oreberries");
	public static final ResourceKey<Feature> GOLD_OREBERRIES = registerKey("gold_oreberries");
	public static final ResourceKey<Feature> COPPER_OREBERRIES = registerKey("copper_oreberries");
	public static final ResourceKey<Feature> ESSENCE_OREBERRIES = registerKey("essence_oreberries");
	public static final ResourceKey<Feature> PUMPKIN_LAMPPOST = registerKey("pumpkin_lamppost");
	public static final ResourceKey<Feature> RASPBERRY_BUSHES = registerKey("raspberry_bushes");
	public static final ResourceKey<Feature> BLUEBERRY_BUSHES = registerKey("blueberry_bushes");
	public static final ResourceKey<Feature> BLACKBERRY_BUSHES = registerKey("blackberry_bushes");
	public static final ResourceKey<Feature> MALOBERRY_BUSHES = registerKey("maloberry_bushes");
	public static final ResourceKey<Feature> SMOKER = registerKey("smoker");
	public static final ResourceKey<Feature> STONE_CIRCLE = registerKey("stone_circle");
	public static final ResourceKey<Feature> THORNS = registerKey("thorns");
	public static final ResourceKey<Feature> TORCH_BERRIES = registerKey("torch_berries");
	public static final ResourceKey<Feature> TROLL_ROOTS = registerKey("troll_roots");
	public static final ResourceKey<Feature> TROLL_BIG_MUSHGLOOMS = registerKey("troll_big_mushglooms");
	public static final ResourceKey<Feature> TROLL_HUGE_RED_MUSHROOMS = registerKey("troll_huge_red_mushrooms");
	public static final ResourceKey<Feature> TROLL_HUGE_BROWN_MUSHROOMS = registerKey("troll_huge_brown_mushrooms");
	public static final ResourceKey<Feature> TROLL_MUSHGLOOMS = registerKey("troll_mushglooms");
	public static final ResourceKey<Feature> VANILLA_ROOTS = registerKey("vanilla_roots");
	public static final ResourceKey<Feature> WEBS = registerKey("webs");
	public static final ResourceKey<Feature> WOOD_ROOTS_SPREAD = registerKey("ore/wood_roots_spread");
	public static final ResourceKey<Feature> SNOW_UNDER_TREES = registerKey("snow_under_trees");
	public static final ResourceKey<Feature> ENCHANTED_FOREST_VINES = registerKey("enchanted_forest_vines");

	//fallen logs!
	public static final ResourceKey<Feature> TF_OAK_FALLEN_LOG = registerKey("tf_oak_fallen_log");
	public static final ResourceKey<Feature> CANOPY_FALLEN_LOG = registerKey("canopy_fallen_log");
	public static final ResourceKey<Feature> MANGROVE_FALLEN_LOG = registerKey("mangrove_fallen_log");
	public static final ResourceKey<Feature> OAK_FALLEN_LOG = registerKey("oak_fallen_log");
	public static final ResourceKey<Feature> SPRUCE_FALLEN_LOG = registerKey("spruce_fallen_log");
	public static final ResourceKey<Feature> BIRCH_FALLEN_LOG = registerKey("birch_fallen_log");

	//smol stone veins
	public static final ResourceKey<Feature> SMALL_GRANITE = registerKey("small_granite");
	public static final ResourceKey<Feature> SMALL_DIORITE = registerKey("small_diorite");
	public static final ResourceKey<Feature> SMALL_ANDESITE = registerKey("small_andesite");

	//Ores! Lets keep pre 1.18 ore rates :)
	public static final ResourceKey<Feature> LEGACY_COAL_ORE = registerKey("legacy_coal_ore");
	public static final ResourceKey<Feature> LEGACY_IRON_ORE = registerKey("legacy_iron_ore");
	public static final ResourceKey<Feature> LEGACY_GOLD_ORE = registerKey("legacy_gold_ore");
	public static final ResourceKey<Feature> LEGACY_REDSTONE_ORE = registerKey("legacy_redstone_ore");
	public static final ResourceKey<Feature> LEGACY_DIAMOND_ORE = registerKey("legacy_diamond_ore");
	public static final ResourceKey<Feature> LEGACY_LAPIS_ORE = registerKey("legacy_lapis_ore");
	public static final ResourceKey<Feature> LEGACY_COPPER_ORE = registerKey("legacy_copper_ore");

	//Dark Forest needs special placements, so here we go
	public static final ResourceKey<Feature> DARK_PUMPKINS = registerKey("dark_pumpkins");
	public static final ResourceKey<Feature> DARK_GRASS = registerKey("dark_grass");
	public static final ResourceKey<Feature> DARK_FERNS = registerKey("dark_ferns");
	public static final ResourceKey<Feature> DARK_MUSHGLOOMS = registerKey("dark_mushglooms");
	public static final ResourceKey<Feature> DARK_BROWN_MUSHROOMS = registerKey("dark_brown_mushrooms");
	public static final ResourceKey<Feature> DARK_RED_MUSHROOMS = registerKey("dark_red_mushrooms");
	public static final ResourceKey<Feature> DARK_DEAD_BUSHES = registerKey("dark_dead_bushes");

	//troll caves special stuff
	public static final ResourceKey<Feature> UBEROUS_SOIL_PATCH_BIG = registerKey("uberous_soil_patch_big");
	public static final ResourceKey<Feature> UBEROUS_SOIL_PATCH_SMALL = registerKey("uberous_soil_patch_small");
	public static final ResourceKey<Feature> TROLL_CAVE_MYCELIUM = registerKey("troll_cave_mycelium");
	public static final ResourceKey<Feature> TROLL_CAVE_DIRT = registerKey("troll_cave_dirt");

	//Trees!
	public static final ResourceKey<Feature> TWILIGHT_OAK_TREE = registerKey("tree/twilight_oak_tree");
	public static final ResourceKey<Feature> LARGE_TWILIGHT_OAK_TREE = registerKey("tree/large_twilight_oak_tree");
	public static final ResourceKey<Feature> SWAMPY_OAK_TREE = registerKey("tree/swampy_oak_tree");
	public static final ResourceKey<Feature> CANOPY_TREE = registerKey("tree/canopy_tree");
	public static final ResourceKey<Feature> MEGA_CANOPY_TREE = registerKey("tree/mega_canopy_tree");
	public static final ResourceKey<Feature> FIREFLY_CANOPY_TREE = registerKey("tree/firefly_canopy_tree");
	public static final ResourceKey<Feature> DEAD_CANOPY_TREE = registerKey("tree/dead_canopy_tree");
	public static final ResourceKey<Feature> MANGROVE_TREE = registerKey("tree/mangrove_tree");
	public static final ResourceKey<Feature> DARKWOOD_TREE = registerKey("tree/darkwood_tree");
	public static final ResourceKey<Feature> HOMEGROWN_DARKWOOD_TREE = registerKey("tree/homegrown_darkwood_tree");
	public static final ResourceKey<Feature> DARKWOOD_LANTERN_TREE = registerKey("tree/darkwood_lantern_tree");
	public static final ResourceKey<Feature> TIME_TREE = registerKey("tree/time_tree");
	public static final ResourceKey<Feature> TRANSFORMATION_TREE = registerKey("tree/transformation_tree");
	public static final ResourceKey<Feature> MINING_TREE = registerKey("tree/mining_tree");
	public static final ResourceKey<Feature> SORTING_TREE = registerKey("tree/sorting_tree");
	public static final ResourceKey<Feature> FOREST_MEGA_OAK_TREE = registerKey("tree/forest_mega_oak_tree");
	public static final ResourceKey<Feature> SAVANNAH_MEGA_OAK_TREE = registerKey("tree/savannah_mega_oak_tree");
	public static final ResourceKey<Feature> RAINBOW_OAK_TREE = registerKey("tree/rainbow_oak");
	public static final ResourceKey<Feature> LARGE_RAINBOW_OAK_TREE = registerKey("tree/large_rainbow_oak");
	public static final ResourceKey<Feature> BROWN_CANOPY_MUSHROOM_TREE = registerKey("mushroom/brown_canopy_mushroom");
	public static final ResourceKey<Feature> RED_CANOPY_MUSHROOM_TREE = registerKey("mushroom/red_canopy_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_VANILLA_MUSHROOM = registerKey("mushroom/canopy_red_vanilla_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_SMOOTH_MUSHROOM = registerKey("mushroom/canopy_red_smooth_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_SPHEROID_MUSHROOM = registerKey("mushroom/canopy_red_spheroid_mushroom");
	public static final ResourceKey<Feature> CANOPY_RED_FLAT_MUSHROOM = registerKey("mushroom/canopy_red_flat_mushroom");
	public static final ResourceKey<Feature> MEGA_SPRUCE_TREE = registerKey("tree/mega_spruce_tree");
	public static final ResourceKey<Feature> LARGE_WINTER_TREE = registerKey("tree/large_winter_tree");
	public static final ResourceKey<Feature> SNOWY_SPRUCE_TREE = registerKey("tree/snowy_spruce_tree");
	public static final ResourceKey<Feature> DARK_FOREST_OAK_TREE = registerKey("tree/dark_forest_oak_tree");
	public static final ResourceKey<Feature> DARK_FOREST_BIRCH_TREE = registerKey("tree/dark_forest_birch_tree");
	public static final ResourceKey<Feature> DARK_OAK_BUSH = registerKey("tree/dark_oak_bush");
	public static final ResourceKey<Feature> VANILLA_OAK_TREE = registerKey("tree/vanilla_oak_tree");
	public static final ResourceKey<Feature> VANILLA_BIRCH_TREE = registerKey("tree/vanilla_birch_tree");
	public static final ResourceKey<Feature> SMALLER_JUNGLE_TREE = registerKey("tree/smaller_jungle_tree");
	public static final ResourceKey<Feature> OAK_BUSH = registerKey("tree/oak_bush");
	public static final ResourceKey<Feature> DUMMY_TREE = registerKey("tree/dummy");

	//random selectors
	public static final ResourceKey<Feature> CANOPY_TREES = registerKey("tree/selector/canopy_trees");
	public static final ResourceKey<Feature> DENSE_CANOPY_TREES = registerKey("tree/selector/dense_canopy_trees");
	public static final ResourceKey<Feature> FIREFLY_FOREST_TREES = registerKey("tree/selector/firefly_forest_trees");
	public static final ResourceKey<Feature> DARK_FOREST_TREES = registerKey("tree/selector/dark_forest_trees");
	public static final ResourceKey<Feature> HIGHLANDS_TREES = registerKey("tree/selector/highlands_trees");
	public static final ResourceKey<Feature> ENCHANTED_FOREST_TREES = registerKey("tree/selector/enchanted_forest_trees");
	public static final ResourceKey<Feature> SNOWY_FOREST_TREES = registerKey("tree/selector/snowy_forest_trees");
	public static final ResourceKey<Feature> VANILLA_TF_TREES = registerKey("tree/selector/vanilla_trees");
	public static final ResourceKey<Feature> VANILLA_TF_BIG_MUSH = registerKey("tree/selector/vanilla/vanilla_mushrooms");

	public static final ResourceKey<Feature> WELL_PLACER = TFFeatures.registerKey("well_placer");
	public static final ResourceKey<Feature> LAMPPOST_PLACER = TFFeatures.registerKey("lamppost_placer");
	public static final ResourceKey<Feature> DEFAULT_FALLEN_LOGS = TFFeatures.registerKey("default_fallen_logs");

	//super funky tree placement lists
	public static final ResourceKey<Feature> CANOPY_MUSHROOMS_SPARSE = registerKey("mushroom/canopy_mushrooms_sparse");
	public static final ResourceKey<Feature> CANOPY_MUSHROOMS_DENSE = registerKey("mushroom/canopy_mushrooms_dense");

	//ground decoration
	public static final ResourceKey<Feature> FLOWER_PLACER = registerKey("flower_placer");
	public static final ResourceKey<Feature> FLOWER_PLACER_ALT = registerKey("flower_placer_alt");

	public static ResourceKey<Feature> registerKey(String name) {
		return ResourceKey.create(Registries.FEATURE, TwilightForestMod.prefix(name));
	}

}
