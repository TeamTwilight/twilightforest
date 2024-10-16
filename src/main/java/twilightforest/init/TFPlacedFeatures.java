package twilightforest.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.placements.AvoidLandmarkModifier;
import twilightforest.world.components.placements.ChunkBlanketingModifier;
import twilightforest.world.components.placements.ChunkCenterModifier;

import java.util.List;

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

	private static List<PlacementModifier> tfTreeCheckArea(BlockState sapling) {
		return List.of(InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(sapling.getBlock()), BiomeFilter.biome());
	}

	private static List<PlacementModifier> tfTreeCheckArea(PlacementModifier count, BlockState sapling) {
		return ImmutableList.of(count, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(sapling.getBlock()), BiomeFilter.biome());
	}

	private static List<PlacementModifier> darkForestTreeCheck(PlacementModifier count) {
		return ImmutableList.of(count, InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, new AvoidLandmarkModifier(true, false, 10), BiomeFilter.biome());
	}

	private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(AvoidLandmarkModifier filter, int rarity) {
		return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
	}

	private static ImmutableList.Builder<PlacementModifier> tfFeatureCheckArea(AvoidLandmarkModifier filter, int rarity, PlacementModifier... extra) {
		return ImmutableList.<PlacementModifier>builder().add(extra).add(filter, RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	private static ImmutableList.Builder<PlacementModifier> hollowLog(AvoidLandmarkModifier filter) {
		return ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, filter, BiomeFilter.biome());
	}

	public static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

		context.register(PLACED_LAKE_LAVA, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_LAVA), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 10).build()));
		context.register(PLACED_LAKE_WATER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_WATER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 32).build()));
		context.register(PLACED_LAKE_FROZEN, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_FROZEN), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 4).build()));
		context.register(PLACED_DRUID_HUT, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DRUID_HUT), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 105).build()));
		context.register(PLACED_DENSE_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_FERNS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.countExtra(3, 0.5F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DENSE_LARGE_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_LARGE_FERNS), ImmutableList.<PlacementModifier>builder().add(CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DENSE_LAKE_WATER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAKE_WATER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 4).build()));
		context.register(PLACED_GRAVEYARD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GRAVEYARD), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 70).build()));
		context.register(PLACED_BIG_MUSHGLOOM, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.BIG_MUSHGLOOM), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 1).build()));
		context.register(PLACED_FALLEN_LEAVES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FALLEN_LEAVES), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 1).build()));
		context.register(PLACED_FIDDLEHEAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIDDLEHEAD), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_FIRE_JET, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIRE_JET), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_FOUNDATION, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FOUNDATION), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 90).build()));
		context.register(PLACED_GROVE_RUINS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.GROVE_RUINS), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 110).build()));
		context.register(PLACED_HOLLOW_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HOLLOW_LOG), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 85).build()));
		context.register(PLACED_HOLLOW_STUMP, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HOLLOW_STUMP), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 80).build()));
		context.register(PLACED_HUGE_LILY_PAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HUGE_LILY_PAD), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), CountPlacement.of(10), BiomeFilter.biome()).build()));
		context.register(PLACED_HUGE_WATER_LILY, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HUGE_WATER_LILY), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
		context.register(PLACED_MAYAPPLE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MAYAPPLE), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_MONOLITH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MONOLITH), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 90).build()));
		context.register(PLACED_MUSHGLOOM_CLUSTER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MUSHGLOOM_CLUSTER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_SPARSE_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MUSHGLOOM_CLUSTER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_MYCELIUM_BLOB, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MYCELIUM_BLOB), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 3).build()));
		context.register(PLACED_OUTSIDE_STALAGMITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OUTSIDE_STALAGMITE), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 77).build()));
		context.register(PLACED_PLANT_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.PLANT_ROOTS), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 1, CountPlacement.of(4), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(10))).build()));
		context.register(PLACED_PUMPKIN_LAMPPOST, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.PUMPKIN_LAMPPOST), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 10).build()));
		context.register(PLACED_SMOKER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMOKER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_STONE_CIRCLE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.STONE_CIRCLE), tfFeatureCheckArea(AvoidLandmarkModifier.checkSurface(), 105).build()));
		context.register(PLACED_THORNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.THORNS), ImmutableList.<PlacementModifier>builder().add(ChunkBlanketingModifier.addThorns(HolderSet.direct(biomes.getOrThrow(TFBiomes.THORNLANDS)))).build()));
		context.register(PLACED_TORCH_BERRIES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TORCH_BERRIES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(60)), CountPlacement.of(8), AvoidLandmarkModifier.checkUnderground(), BiomeFilter.biome()).build()));
		context.register(PLACED_TROLL_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TROLL_ROOTS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountOnEveryLayerPlacement.of(12), BiomeFilter.biome()).build()));
		context.register(PLACED_TROLL_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TROLL_MUSHGLOOMS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), CountPlacement.of(3), AvoidLandmarkModifier.checkUnderground(), BiomeFilter.biome()).build()));
		context.register(PLACED_VANILLA_ROOTS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_ROOTS), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 1, CountPlacement.of(16), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0)), PlacementUtils.filteredByBlockSurvival(TFBlocks.TORCHBERRY_PLANT.get())).build()));
		context.register(PLACED_WEBS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WEBS), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(60), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_WOOD_ROOTS_SPREAD, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WOOD_ROOTS_SPREAD), tfFeatureCheckArea(AvoidLandmarkModifier.checkUnderground(), 40, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0))).build()));

		context.register(PLACED_SNOW_UNDER_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SNOW_UNDER_TREES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_TF_OAK_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TF_OAK_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
		context.register(PLACED_CANOPY_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
		context.register(PLACED_MANGROVE_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MANGROVE_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
		context.register(PLACED_SPRUCE_FALLEN_LOG, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SPRUCE_FALLEN_LOG), hollowLog(AvoidLandmarkModifier.checkSurface()).build()));
		context.register(PLACED_SMALL_GRANITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_GRANITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
		context.register(PLACED_SMALL_DIORITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_DIORITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));
		context.register(PLACED_SMALL_ANDESITE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SMALL_ANDESITE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)), RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), CountPlacement.of(5), BiomeFilter.biome()).build()));

		context.register(PLACED_DARK_PUMPKINS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_PUMPKINS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_GRASS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_GRASS), ImmutableList.<PlacementModifier>builder().add(CountPlacement.of(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_FERNS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_FERNS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_MUSHGLOOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_MUSHGLOOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_BROWN_MUSHROOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_BROWN_MUSHROOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_RED_MUSHROOMS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_RED_MUSHROOMS), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));
		context.register(PLACED_DARK_DEAD_BUSHES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_DEAD_BUSHES), ImmutableList.<PlacementModifier>builder().add(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()).build()));

		context.register(PLACED_ENCHANTED_FOREST_VINES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.ENCHANTED_FOREST_VINES), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP).build()));

		context.register(PLACED_LEGACY_COAL_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_COAL_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(127)), InSquarePlacement.spread(), CountPlacement.of(20), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_IRON_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_IRON_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(63)), InSquarePlacement.spread(), CountPlacement.of(20), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_GOLD_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_GOLD_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(31)), InSquarePlacement.spread(), CountPlacement.of(2), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_REDSTONE_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_REDSTONE_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), InSquarePlacement.spread(), CountPlacement.of(8), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_DIAMOND_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_DIAMOND_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)), InSquarePlacement.spread(), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_LAPIS_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_LAPIS_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(30)), InSquarePlacement.spread(), CountPlacement.of(2), BiomeFilter.biome()).build()));
		context.register(PLACED_LEGACY_COPPER_ORE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LEGACY_COPPER_ORE), ImmutableList.<PlacementModifier>builder().add(HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(96)), InSquarePlacement.spread(), CountPlacement.of(6), BiomeFilter.biome()).build()));

		context.register(PLACED_WELL_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.WELL_PLACER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 80).build()));
		context.register(PLACED_LAMPPOST_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LAMPPOST_PLACER), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 2).build()));
		context.register(PLACED_DEFAULT_FALLEN_LOGS, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DEFAULT_FALLEN_LOGS), tfFeatureCheckArea(AvoidLandmarkModifier.checkBoth(), 40).build()));

		context.register(PLACED_FLOWER_PLACER, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FLOWER_PLACER), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(3), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(2), BiomeFilter.biome()).build()));
		context.register(PLACED_FLOWER_PLACER_ALT, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FLOWER_PLACER_ALT), ImmutableList.<PlacementModifier>builder().add(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, CountPlacement.of(3), InSquarePlacement.spread(), RarityFilter.onAverageOnceEvery(2), BiomeFilter.biome()).build()));

		context.register(PLACED_DEAD_CANOPY_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DEAD_CANOPY_TREE), tfTreeCheckArea(PlacementUtils.countExtra(2, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
		context.register(PLACED_MEGA_CANOPY_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MEGA_CANOPY_TREE), ImmutableList.of(ChunkCenterModifier.center(), RarityFilter.onAverageOnceEvery(20), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(TFBlocks.CANOPY_SAPLING.get().defaultBlockState().getBlock()), BiomeFilter.biome())));
		context.register(PLACED_MANGROVE_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.MANGROVE_TREE), List.of(PlacementUtils.countExtra(3, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(6), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), PlacementUtils.filteredByBlockSurvival(TFBlocks.MANGROVE_SAPLING.get()), BiomeFilter.biome())));
		context.register(PLACED_TWILIGHT_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_LARGE_TWILIGHT_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_FOREST_MEGA_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FOREST_MEGA_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(7, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_SAVANNAH_MEGA_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(0, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_SAVANNAH_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(1, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_SWAMPY_OAK_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SWAMPY_OAK_TREE), tfTreeCheckArea(PlacementUtils.countExtra(4, 0.1F, 1), TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_OAK_BUSH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OAK_BUSH), tfTreeCheckArea(PlacementUtils.countExtra(1, 1 / 3F, 1), Blocks.OAK_SAPLING.defaultBlockState())));  // 0.33F raise exception
		context.register(PLACED_OAK_BUSH_DENSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.OAK_BUSH), tfTreeCheckArea(PlacementUtils.countExtra(2, 1 / 3F, 2), Blocks.OAK_SAPLING.defaultBlockState())));  // 0.33F raise exception
		context.register(PLACED_DARKWOOD_TREE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARKWOOD_TREE), List.of(PlacementUtils.countExtra(5, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, new AvoidLandmarkModifier(true, false, 16), PlacementUtils.filteredByBlockSurvival(TFBlocks.DARKWOOD_SAPLING.get()), BiomeFilter.biome())));

		context.register(PLACED_CANOPY_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_TREES), tfTreeCheckArea(TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
		context.register(PLACED_DENSE_CANOPY_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DENSE_CANOPY_TREES), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
		context.register(PLACED_FIREFLY_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.FIREFLY_FOREST_TREES), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
		context.register(PLACED_DARK_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARKWOOD_TREE), darkForestTreeCheck(PlacementUtils.countExtra(8, 0.1F, 1))));
		context.register(PLACED_DARK_FOREST_TREE_MIX, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.DARK_FOREST_TREES), darkForestTreeCheck(PlacementUtils.countExtra(3, 0.1F, 1))));
		context.register(PLACED_HIGHLANDS_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.HIGHLANDS_TREES), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), Blocks.SPRUCE_SAPLING.defaultBlockState())));
		context.register(PLACED_ENCHANTED_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.ENCHANTED_FOREST_TREES), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.RAINBOW_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_SNOWY_FOREST_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.SNOWY_FOREST_TREES), List.of(PlacementUtils.countExtra(10, 0.1F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, AvoidLandmarkModifier.checkSurface(), EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.POWDER_SNOW)), 8), BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW)), BiomeFilter.biome())));
		context.register(PLACED_VANILLA_TF_TREES, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_TF_TREES), tfTreeCheckArea(TFBlocks.TWILIGHT_OAK_SAPLING.get().defaultBlockState())));
		context.register(PLACED_VANILLA_TF_BIG_MUSH, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.VANILLA_TF_BIG_MUSH), tfTreeCheckArea(TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));

		context.register(PLACED_CANOPY_MUSHROOMS_SPARSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE), tfTreeCheckArea(PlacementUtils.countExtra(3, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
		context.register(PLACED_CANOPY_MUSHROOMS_DENSE, new PlacedFeature(features.getOrThrow(TFConfiguredFeatures.CANOPY_MUSHROOMS_DENSE), tfTreeCheckArea(PlacementUtils.countExtra(5, 0.1F, 1), TFBlocks.CANOPY_SAPLING.get().defaultBlockState())));
	}
}
