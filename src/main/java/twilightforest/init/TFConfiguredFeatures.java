package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TwilightForestMod;

import java.util.List;

public final class TFConfiguredFeatures {

	//vanilla features with custom placement code
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_LAVA = registerKey("lava_lake");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_WATER = registerKey("water_lake");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_FROZEN = registerKey("water_frozen");

	//"structures" that arent actually structures
	public static final ResourceKey<ConfiguredFeature<?, ?>> SIMPLE_WELL = registerKey("simple_well");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_WELL = registerKey("fancy_well");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DRUID_HUT = registerKey("druid_hut");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GRAVEYARD = registerKey("graveyard");

	//all the fun little things you find around the dimension
	public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_MUSHGLOOM = registerKey("mushroom/big_mushgloom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_FERNS = registerKey("dense_ferns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_LARGE_FERNS = registerKey("dense_large_ferns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_LEAVES = registerKey("fallen_leaves");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MAYAPPLE = registerKey("mayapple");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIDDLEHEAD = registerKey("fiddlehead");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_JET = registerKey("fire_jet");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOUNDATION = registerKey("foundation");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GROVE_RUINS = registerKey("grove_ruins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOW_LOG = registerKey("hollow_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOW_STUMP = registerKey("hollow_stump");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_LILY_PAD = registerKey("huge_lily_pad");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_WATER_LILY = registerKey("huge_water_lily");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CICADA_LAMPPOST = registerKey("cicada_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_LAMPPOST = registerKey("firefly_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MONOLITH = registerKey("monolith");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHGLOOM_CLUSTER = registerKey("mushgloom_cluster");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYCELIUM_BLOB = registerKey("mycelium_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OUTSIDE_STALAGMITE = registerKey("outside_stalagmite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PLANT_ROOTS = registerKey("plant_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> IRON_OREBERRIES = registerKey("iron_oreberries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GOLD_OREBERRIES = registerKey("gold_oreberries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COPPER_OREBERRIES = registerKey("copper_oreberries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ESSENCE_OREBERRIES = registerKey("essence_oreberries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PUMPKIN_LAMPPOST = registerKey("pumpkin_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RASPBERRY_BUSHES = registerKey("raspberry_bushes");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLUEBERRY_BUSHES = registerKey("blueberry_bushes");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLACKBERRY_BUSHES = registerKey("blackberry_bushes");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MALOBERRY_BUSHES = registerKey("maloberry_bushes");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMOKER = registerKey("smoker");
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_CIRCLE = registerKey("stone_circle");
	public static final ResourceKey<ConfiguredFeature<?, ?>> THORNS = registerKey("thorns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TORCH_BERRIES = registerKey("torch_berries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_ROOTS = registerKey("troll_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_BIG_MUSHGLOOMS = registerKey("troll_big_mushglooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_HUGE_RED_MUSHROOMS = registerKey("troll_huge_red_mushrooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_HUGE_BROWN_MUSHROOMS = registerKey("troll_huge_brown_mushrooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_MUSHGLOOMS = registerKey("troll_mushglooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_ROOTS = registerKey("vanilla_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WEBS = registerKey("webs");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOOD_ROOTS_SPREAD = registerKey("ore/wood_roots_spread");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_UNDER_TREES = registerKey("snow_under_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ENCHANTED_FOREST_VINES = registerKey("enchanted_forest_vines");

	//fallen logs!
	public static final ResourceKey<ConfiguredFeature<?, ?>> TF_OAK_FALLEN_LOG = registerKey("tf_oak_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_FALLEN_LOG = registerKey("canopy_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_FALLEN_LOG = registerKey("mangrove_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_FALLEN_LOG = registerKey("oak_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE_FALLEN_LOG = registerKey("spruce_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_FALLEN_LOG = registerKey("birch_fallen_log");

	//smol stone veins
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_GRANITE = registerKey("small_granite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_DIORITE = registerKey("small_diorite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_ANDESITE = registerKey("small_andesite");

	//Ores! Lets keep pre 1.18 ore rates :)
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_COAL_ORE = registerKey("legacy_coal_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_IRON_ORE = registerKey("legacy_iron_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_GOLD_ORE = registerKey("legacy_gold_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_REDSTONE_ORE = registerKey("legacy_redstone_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_DIAMOND_ORE = registerKey("legacy_diamond_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_LAPIS_ORE = registerKey("legacy_lapis_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_COPPER_ORE = registerKey("legacy_copper_ore");

	//Dark Forest needs special placements, so here we go
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_PUMPKINS = registerKey("dark_pumpkins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_GRASS = registerKey("dark_grass");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FERNS = registerKey("dark_ferns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_MUSHGLOOMS = registerKey("dark_mushglooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_BROWN_MUSHROOMS = registerKey("dark_brown_mushrooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_RED_MUSHROOMS = registerKey("dark_red_mushrooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_DEAD_BUSHES = registerKey("dark_dead_bushes");

	//troll caves special stuff
	public static final ResourceKey<ConfiguredFeature<?, ?>> UBEROUS_SOIL_PATCH_BIG = registerKey("uberous_soil_patch_big");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UBEROUS_SOIL_PATCH_SMALL = registerKey("uberous_soil_patch_small");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_CAVE_MYCELIUM = registerKey("troll_cave_mycelium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_CAVE_DIRT = registerKey("troll_cave_dirt");

	//Trees!
	public static final ResourceKey<ConfiguredFeature<?, ?>> TWILIGHT_OAK_TREE = registerKey("tree/twilight_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_TWILIGHT_OAK_TREE = registerKey("tree/large_twilight_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SWAMPY_OAK_TREE = registerKey("tree/swampy_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_TREE = registerKey("tree/canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_CANOPY_TREE = registerKey("tree/mega_canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_CANOPY_TREE = registerKey("tree/firefly_canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_CANOPY_TREE = registerKey("tree/dead_canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_TREE = registerKey("tree/mangrove_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_TREE = registerKey("tree/darkwood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOMEGROWN_DARKWOOD_TREE = registerKey("tree/homegrown_darkwood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_LANTERN_TREE = registerKey("tree/darkwood_lantern_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TIME_TREE = registerKey("tree/time_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TRANSFORMATION_TREE = registerKey("tree/transformation_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MINING_TREE = registerKey("tree/mining_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SORTING_TREE = registerKey("tree/sorting_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_MEGA_OAK_TREE = registerKey("tree/forest_mega_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SAVANNAH_MEGA_OAK_TREE = registerKey("tree/savannah_mega_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_OAK_TREE = registerKey("tree/rainbow_oak");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_RAINBOW_OAK_TREE = registerKey("tree/large_rainbow_oak");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_CANOPY_MUSHROOM_TREE = registerKey("mushroom/brown_canopy_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_CANOPY_MUSHROOM_TREE = registerKey("mushroom/red_canopy_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_RED_VANILLA_MUSHROOM = registerKey("mushroom/canopy_red_vanilla_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_RED_SMOOTH_MUSHROOM = registerKey("mushroom/canopy_red_smooth_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_RED_SPHEROID_MUSHROOM = registerKey("mushroom/canopy_red_spheroid_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_RED_FLAT_MUSHROOM = registerKey("mushroom/canopy_red_flat_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_SPRUCE_TREE = registerKey("tree/mega_spruce_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_WINTER_TREE = registerKey("tree/large_winter_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWY_SPRUCE_TREE = registerKey("tree/snowy_spruce_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_OAK_TREE = registerKey("tree/dark_forest_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_BIRCH_TREE = registerKey("tree/dark_forest_birch_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_BUSH = registerKey("tree/dark_oak_bush");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_OAK_TREE = registerKey("tree/vanilla_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_BIRCH_TREE = registerKey("tree/vanilla_birch_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALLER_JUNGLE_TREE = registerKey("tree/smaller_jungle_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BUSH = registerKey("tree/oak_bush");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DUMMY_TREE = registerKey("tree/dummy");

	//random selectors
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_TREES = registerKey("tree/selector/canopy_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_CANOPY_TREES = registerKey("tree/selector/dense_canopy_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_FOREST_TREES = registerKey("tree/selector/firefly_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_TREES = registerKey("tree/selector/dark_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HIGHLANDS_TREES = registerKey("tree/selector/highlands_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ENCHANTED_FOREST_TREES = registerKey("tree/selector/enchanted_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWY_FOREST_TREES = registerKey("tree/selector/snowy_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_TF_TREES = registerKey("tree/selector/vanilla_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_TF_BIG_MUSH = registerKey("tree/selector/vanilla/vanilla_mushrooms");

	public static final ResourceKey<ConfiguredFeature<?, ?>> WELL_PLACER = TFConfiguredFeatures.registerKey("well_placer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAMPPOST_PLACER = TFConfiguredFeatures.registerKey("lamppost_placer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEFAULT_FALLEN_LOGS = TFConfiguredFeatures.registerKey("default_fallen_logs");

	//super funky tree placement lists
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_MUSHROOMS_SPARSE = registerKey("mushroom/canopy_mushrooms_sparse");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_MUSHROOMS_DENSE = registerKey("mushroom/canopy_mushrooms_dense");

	//ground decoration
	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLACER = registerKey("flower_placer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLACER_ALT = registerKey("flower_placer_alt");

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, TwilightForestMod.prefix(name));
	}

}