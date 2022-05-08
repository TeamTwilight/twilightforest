package twilightforest.world.registration.features;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.OutOfStructureFilter;

import java.util.List;

public class TFPlacedFeatures {

	public static final Holder<PlacedFeature> PLACED_LAKE_LAVA = register("lava_lake", TFConfiguredFeatures.LAKE_LAVA, tfFeatureCheckArea(OutOfStructureFilter.checkBoth(), 10).build());
	public static final Holder<PlacedFeature> PLACED_LAKE_WATER = register("water_lake", TFConfiguredFeatures.LAKE_WATER, tfFeatureCheckArea(OutOfStructureFilter.checkBoth(), 4).build());
	public static final Holder<PlacedFeature> PLACED_SIMPLE_WELL = register("simple_well", TFConfiguredFeatures.SIMPLE_WELL, ImmutableList.<PlacementModifier>builder().build());
	public static final Holder<PlacedFeature> PLACED_FANCY_WELL = register("fancy_well", TFConfiguredFeatures.FANCY_WELL, ImmutableList.<PlacementModifier>builder().build());
	public static final Holder<PlacedFeature> PLACED_DRUID_HUT = register("druid_hut", TFConfiguredFeatures.DRUID_HUT, tfFeatureCheckArea(OutOfStructureFilter.checkBoth(), 105).build());
	public static final Holder<PlacedFeature> PLACED_GRAVEYARD = register("graveyard", TFConfiguredFeatures.GRAVEYARD, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 75).build());
	public static final Holder<PlacedFeature> PLACED_BIG_MUSHGLOOM = register("big_mushgloom", TFConfiguredFeatures.BIG_MUSHGLOOM, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 1).build());
	public static final Holder<PlacedFeature> PLACED_FALLEN_LEAVES = register("fallen_leaves", TFConfiguredFeatures.FALLEN_LEAVES, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 1).build());
	public static final Holder<PlacedFeature> PLACED_FIDDLEHEAD = register("fiddlehead", TFConfiguredFeatures.FIDDLEHEAD, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_FIRE_JET = register("fire_jet", TFConfiguredFeatures.FIRE_JET, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_FOUNDATION = register("foundation", TFConfiguredFeatures.FOUNDATION, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 100).build());
	public static final Holder<PlacedFeature> PLACED_GROVE_RUINS = register("grove_ruins", TFConfiguredFeatures.GROVE_RUINS, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 105).build());
	public static final Holder<PlacedFeature> PLACED_HOLLOW_LOG = register("hollow_log", TFConfiguredFeatures.HOLLOW_LOG, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 100).build());
	public static final Holder<PlacedFeature> PLACED_HOLLOW_STUMP = register("hollow_stump", TFConfiguredFeatures.HOLLOW_STUMP, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 90).build());
	public static final Holder<PlacedFeature> PLACED_HUGE_LILY_PAD = register("huge_lily_pad", TFConfiguredFeatures.HUGE_LILY_PAD, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), CountPlacement.of(10), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_HUGE_WATER_LILY = register("huge_water_lily", TFConfiguredFeatures.HUGE_WATER_LILY, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_CICADA_LAMPPOST = register("cicada_lamppost", TFConfiguredFeatures.CICADA_LAMPPOST, ImmutableList.<PlacementModifier>builder().build());
	public static final Holder<PlacedFeature> PLACED_FIREFLY_LAMPPOST = register("firefly_lamppost", TFConfiguredFeatures.FIREFLY_LAMPPOST, ImmutableList.<PlacementModifier>builder().build());
	public static final Holder<PlacedFeature> PLACED_MONOLITH = register("monolith", TFConfiguredFeatures.MONOLITH, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 90).build());
	public static final Holder<PlacedFeature> PLACED_MUSHGLOOM_CLUSTER = register("mushgloom_cluster", TFConfiguredFeatures.MUSHGLOOM_CLUSTER, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(5), InSquarePlacement.spread(), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_MYCELIUM_BLOB = register("mycelium_blob", TFConfiguredFeatures.MYCELIUM_BLOB, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 3).build());
	public static final Holder<PlacedFeature> PLACED_OUTSIDE_STALAGMITE = register("outside_stalagmite", TFConfiguredFeatures.OUTSIDE_STALAGMITE, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 88).build());
	public static final Holder<PlacedFeature> PLACED_PLANT_ROOTS = register("plant_roots", TFConfiguredFeatures.PLANT_ROOTS, tfFeatureCheckArea(OutOfStructureFilter.checkUnderground(), 1, CountPlacement.of(4), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(10))).build());
	public static final Holder<PlacedFeature> PLACED_PUMPKIN_LAMPPOST = register("pumpkin_lamppost", TFConfiguredFeatures.PUMPKIN_LAMPPOST, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 1).build());
	public static final Holder<PlacedFeature> PLACED_SMOKER = register("smoker", TFConfiguredFeatures.SMOKER, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_STONE_CIRCLE = register("stone_circle", TFConfiguredFeatures.STONE_CIRCLE, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 105).build());
	public static final Holder<PlacedFeature> PLACED_THORNS = register("thorns", TFConfiguredFeatures.THORNS, ImmutableList.<PlacementModifier>builder().add(ChunkBlanketingModifier.addThorns(), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_TORCH_BERRIES = register("torch_berries", TFConfiguredFeatures.TORCH_BERRIES, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)), CountPlacement.of(8), OutOfStructureFilter.checkUnderground(), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_TROLL_ROOTS = register("troll_roots", TFConfiguredFeatures.TROLL_ROOTS, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)), CountPlacement.of(8), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_VANILLA_ROOTS = register("vanilla_roots", TFConfiguredFeatures.VANILLA_ROOTS, tfFeatureCheckArea(OutOfStructureFilter.checkUnderground(), 1, CountPlacement.of(16), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(10))).build());
	public static final Holder<PlacedFeature> PLACED_WEBS = register("webs", TFConfiguredFeatures.WEBS, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, InSquarePlacement.spread(), CountPlacement.of(60), BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_WOOD_ROOTS_SPREAD = register("wood_roots", TFConfiguredFeatures.WOOD_ROOTS_SPREAD, tfFeatureCheckArea(OutOfStructureFilter.checkUnderground(), 40, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0))).build());
	public static final Holder<PlacedFeature> PLACED_SNOW_UNDER_TREES = register("snow_under_trees", TFConfiguredFeatures.SNOW_UNDER_TREES, ImmutableList.<PlacementModifier>builder().add(BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_TF_OAK_FALLEN_LOG = register("tf_oak_fallen_log", TFConfiguredFeatures.TF_OAK_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_CANOPY_FALLEN_LOG = register("canopy_fallen_log", TFConfiguredFeatures.CANOPY_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_MANGROVE_FALLEN_LOG = register("mangrove_fallen_log", TFConfiguredFeatures.MANGROVE_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_OAK_FALLEN_LOG = register("oak_fallen_log", TFConfiguredFeatures.OAK_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_SPRUCE_FALLEN_LOG = register("spruce_fallen_log", TFConfiguredFeatures.SPRUCE_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_BIRCH_FALLEN_LOG = register("birch_fallen_log", TFConfiguredFeatures.BIRCH_FALLEN_LOG, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());
	public static final Holder<PlacedFeature> PLACED_SMALL_GRANITE = register("small_granite", TFConfiguredFeatures.SMALL_GRANITE, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), CountPlacement.of(5)).build());
	public static final Holder<PlacedFeature> PLACED_SMALL_DIORITE = register("small_diorite", TFConfiguredFeatures.SMALL_DIORITE, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), CountPlacement.of(5)).build());
	public static final Holder<PlacedFeature> PLACED_SMALL_ANDESITE = register("small_andesite", TFConfiguredFeatures.SMALL_ANDESITE, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), CountPlacement.of(5)).build());
	public static final Holder<PlacedFeature> PLACED_DARK_MUSHGLOOMS = register("dark_mushglooms", TFConfiguredFeatures.DARK_MUSHGLOOMS, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_DARK_PUMPKINS = register("dark_pumpkins", TFConfiguredFeatures.DARK_PUMPKINS, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(30), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_DARK_GRASS = register("dark_grass", TFConfiguredFeatures.DARK_GRASS, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_DARK_FERNS = register("dark_ferns", TFConfiguredFeatures.DARK_FERNS, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(4), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_DARK_MUSHROOMS = register("dark_mushrooms", TFConfiguredFeatures.DARK_MUSHROOMS, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());
	public static final Holder<PlacedFeature> PLACED_DARK_DEAD_BUSHES = register("dark_dead_bushes", TFConfiguredFeatures.DARK_DEAD_BUSHES, ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(15), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build());

	//Ores!
	public static final Holder<PlacedFeature> PLACED_LEGACY_COAL_ORE = register("legacy_coal_ore", TFConfiguredFeatures.LEGACY_COAL_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(127)), InSquarePlacement.spread(), CountPlacement.of(20)).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_IRON_ORE = register("legacy_iron_ore", TFConfiguredFeatures.LEGACY_IRON_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63)), InSquarePlacement.spread(), CountPlacement.of(20)).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_GOLD_ORE = register("legacy_gold_ore", TFConfiguredFeatures.LEGACY_GOLD_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(31)), InSquarePlacement.spread(), CountPlacement.of(2)).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_REDSTONE_ORE = register("legacy_redstone_ore", TFConfiguredFeatures.LEGACY_REDSTONE_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), InSquarePlacement.spread(), CountPlacement.of(8)).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_DIAMOND_ORE = register("legacy_diamond_ore", TFConfiguredFeatures.LEGACY_DIAMOND_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15))).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_LAPIS_ORE = register("legacy_lapis_ore", TFConfiguredFeatures.LEGACY_LAPIS_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(30)), InSquarePlacement.spread()).build());
	public static final Holder<PlacedFeature> PLACED_LEGACY_COPPER_ORE = register("legacy_copper_ore", TFConfiguredFeatures.LEGACY_COPPER_ORE, ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(96)), InSquarePlacement.spread(), CountPlacement.of(6)).build());

	// !!!!!!!!!!!!!!!!!!!!!!!!!! WARNING!!!! DUE TO CLASSLOAD ISSUES THESE ***MUST***, ABSOLUTELY MUST!!!! BE HERE, DO NOT FUCKING MOVE THIS !!!!!!!!!!!!!!!!!!!!!!!
	//random selector stuff
	public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> WELL_PLACER = TFConfiguredFeatures.register("well_placer", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(TFPlacedFeatures.PLACED_FANCY_WELL, 0.07F)), TFPlacedFeatures.PLACED_SIMPLE_WELL));
	public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> LAMPPOST_PLACER = TFConfiguredFeatures.register("lamppost_placer", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(TFPlacedFeatures.PLACED_CICADA_LAMPPOST, 0.1F)), TFPlacedFeatures.PLACED_FIREFLY_LAMPPOST));
	public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> DEFAULT_FALLEN_LOGS = TFConfiguredFeatures.register("default_fallen_logs", Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(TFPlacedFeatures.PLACED_BIRCH_FALLEN_LOG, 0.1F), new WeightedPlacedFeature(TFPlacedFeatures.PLACED_OAK_FALLEN_LOG, 0.2F), new WeightedPlacedFeature(TFPlacedFeatures.PLACED_CANOPY_FALLEN_LOG, 0.4F)), TFPlacedFeatures.PLACED_TF_OAK_FALLEN_LOG));
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	public static final Holder<PlacedFeature> PLACED_WELL_PLACER = register("well_placer", WELL_PLACER, tfFeatureCheckArea(OutOfStructureFilter.checkBoth(), 80).build());
	public static final Holder<PlacedFeature> PLACED_LAMPPOST_PLACER = register("lamppost_placer", LAMPPOST_PLACER, tfFeatureCheckArea(OutOfStructureFilter.checkSurface(), 2).build());
	public static final Holder<PlacedFeature> PLACED_DEFAULT_FALLEN_LOGS = register("default_fallen_logs", DEFAULT_FALLEN_LOGS, hollowLog(OutOfStructureFilter.checkSurface(), 40).build());

	public static final Holder<PlacedFeature> PLACED_GRASS_PLACER = register("grass_placer", TFConfiguredFeatures.GRASS_PLACER, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(30), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(3)).build());
	public static final Holder<PlacedFeature> PLACED_FOREST_GRASS_PLACER = register("forest_grass_placer", TFConfiguredFeatures.FOREST_GRASS_PLACER, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(30), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(3)).build());
	public static final Holder<PlacedFeature> PLACED_FLOWER_PLACER = register("flower_placer", TFConfiguredFeatures.FLOWER_PLACER, ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(25), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(15)).build());

	//Trees!
	public static final Holder<PlacedFeature> PLACED_DEAD_CANOPY_TREE = register("tree/dead_canopy_tree", TFConfiguredFeatures.DEAD_CANOPY_TREE, tfTreeCheckArea(PlacementUtils.countExtra(2, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_MANGROVE_TREE = register("tree/mangrove_tree", TFConfiguredFeatures.MANGROVE_TREE, List.of(PlacementUtils.countExtra(3, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(6), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, OutOfStructureFilter.checkSurface(), PlacementUtils.filteredByBlockSurvival(TFBlocks.MANGROVE_SAPLING.get()), BiomeFilter.biome()));
	public static final Holder<PlacedFeature> PLACED_TWILIGHT_OAK_TREE = register("tree/twilight_oak_tree", TFConfiguredFeatures.TWILIGHT_OAK_TREE, tfTreeCheckArea(PlacementUtils.countExtra(2, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_DENSE_TREE = register("tree/dense_tree", TFConfiguredFeatures.TWILIGHT_OAK_TREE, tfTreeCheckArea(PlacementUtils.countExtra(7, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_SAVANNAH_OAK_TREE = register("tree/savannah_oak_tree", TFConfiguredFeatures.TWILIGHT_OAK_TREE, tfTreeCheckArea(PlacementUtils.countExtra(0, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_SWAMPY_OAK_TREE = register("tree/swampy_oak_tree", TFConfiguredFeatures.SWAMPY_OAK_TREE, tfTreeCheckArea(PlacementUtils.countExtra(4, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_DARKWOOD_TREE = register("tree/darkwood_tree", TFConfiguredFeatures.DARKWOOD_TREE, List.of(PlacementUtils.countExtra(5, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, new OutOfStructureFilter(true, false, 16), PlacementUtils.filteredByBlockSurvival(TFBlocks.DARKWOOD_SAPLING.get()), BiomeFilter.biome()));
	public static final Holder<PlacedFeature> PLACED_SNOWY_SPRUCE_TREE = register("tree/snowy_spruce", TFConfiguredFeatures.SNOWY_SPRUCE_TREE, tfTreeCheckArea(Blocks.SPRUCE_SAPLING.defaultBlockState()));

	public static final Holder<PlacedFeature> PLACED_CANOPY_TREES = register("tree/selector/canopy_trees", TFConfiguredFeatures.CANOPY_TREES, tfTreeCheckArea(PlacementUtils.countExtra(2, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_DENSE_CANOPY_TREES = register("tree/selector/dense_canopy_trees", TFConfiguredFeatures.DENSE_CANOPY_TREES, tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_FIREFLY_FOREST_TREES = register("tree/selector/firefly_forest_trees", TFConfiguredFeatures.FIREFLY_FOREST_TREES, tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_DARK_FOREST_TREES = register("tree/selector/dark_forest_trees", TFConfiguredFeatures.DARK_FOREST_TREES, tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.DARKWOOD_SAPLING.get().defaultBlockState(), false));
	public static final Holder<PlacedFeature> PLACED_HIGHLANDS_TREES = register("tree/selector/highlands_trees", TFConfiguredFeatures.HIGHLANDS_TREES, tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), Blocks.SPRUCE_SAPLING.defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_ENCHANTED_FOREST_TREES = register("tree/selector/enchanted_forest_trees", TFConfiguredFeatures.ENCHANTED_FOREST_TREES, tfTreeCheckArea(PlacementUtils.countExtra(10, 0.1F, 1), TFBlocks.RAINBOW_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_SNOWY_FOREST_TREES = register("tree/selector/snowy_forest_trees", TFConfiguredFeatures.SNOWY_FOREST_TREES, tfTreeCheckArea(PlacementUtils.countExtra(10, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_VANILLA_TF_TREES = register("tree/selector/vanilla_trees", TFConfiguredFeatures.VANILLA_TF_TREES, tfTreeCheckArea(PlacementUtils.countExtra(10, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState()));
	public static final Holder<PlacedFeature> PLACED_VANILLA_TF_BIG_MUSH = register("tree/selector/vanilla_mushrooms", TFConfiguredFeatures.VANILLA_TF_BIG_MUSH, tfTreeCheckArea(TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));

	public static final Holder<PlacedFeature> PLACED_CANOPY_MUSHROOMS_SPARSE = register("mushroom/canopy_mushrooms_sparse", TFConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE, tfTreeCheckArea(PlacementUtils.countExtra(8, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));
	
	public static final Holder<PlacedFeature> PLACED_CANOPY_MUSHROOMS_DENSE = register("mushroom/canopy_mushrooms_dense", TFConfiguredFeatures.CANOPY_MUSHROOMS_DENSE, tfTreeCheckArea(PlacementUtils.countExtra(8, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState()));

	private static List<PlacementModifier> tfTreeCheckArea(BlockState sapling) {
		return List.of(InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, OutOfStructureFilter.checkSurface(), PlacementUtils.filteredByBlockSurvival(sapling.getBlock()), BiomeFilter.biome());
	}

	private static List<PlacementModifier> tfTreeCheckArea(PlacementModifier count, BlockState sapling) {
		return tfTreeCheckArea(count, sapling, true);
	}

	private static List<PlacementModifier> tfTreeCheckArea(PlacementModifier count, BlockState sapling, boolean checkSurvival) {
		ImmutableList.Builder<PlacementModifier> list = ImmutableList.builder();
		list.add(count, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, OutOfStructureFilter.checkSurface());
		if (checkSurvival) {
			list.add(PlacementUtils.filteredByBlockSurvival(sapling.getBlock()));
		}
		list.add(BiomeFilter.biome());
		return list.build();
	}

	private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(OutOfStructureFilter filter, int rarity) {
		return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
	}

	private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(OutOfStructureFilter filter, int rarity, PlacementModifier... extra) {
		return ImmutableList.<PlacementModifier>builder().add(extra).add(filter, RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	private static ImmutableList.Builder<PlacementModifier> hollowLog(OutOfStructureFilter filter, int rarity) {
		return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter);
	}

	public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> register(String name, Holder<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placements) {
		return BuiltinRegistries.registerExact(BuiltinRegistries.PLACED_FEATURE, TwilightForestMod.prefix(name).toString(), new PlacedFeature(Holder.hackyErase(feature), List.copyOf(placements)));
	}
}
