package twilightforest.worldgen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BushFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.function.Supplier;

public final class ConfiguredFeatures {
    // Base configurations
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TWILIGHT_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/twilight_oak"), Feature.TREE.withConfiguration(TreeConfigurations.TWILIGHT_OAK));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> SWAMPY_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/swampy_oak"), Feature.TREE.withConfiguration(TreeConfigurations.SWAMPY_OAK));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> FIREFLY_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/firefly_canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_FIREFLY));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DEAD_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dead_canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_DEAD));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MANGROVE_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mangrove_tree"), Feature.TREE.withConfiguration(TreeConfigurations.MANGROVE_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration(TreeConfigurations.DARKWOOD_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_LANTERN_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_lantern_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration(TreeConfigurations.DARKWOOD_LANTERN_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> TIME_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/time_tree"), TFBiomeFeatures.TREE_OF_TIME.get().withConfiguration(TreeConfigurations.TIME_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TRANSFORM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/transform_tree"), Feature.TREE.withConfiguration(TreeConfigurations.TRANSFORM_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> MINING_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mining_tree"), TFBiomeFeatures.MINERS_TREE.get().withConfiguration(TreeConfigurations.MINING_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> SORT_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/sort_tree"), Feature.TREE.withConfiguration(TreeConfigurations.SORT_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> DENSE_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.get().withConfiguration(TreeConfigurations.DENSE_OAK));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> HOLLOW_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.get().withConfiguration(TreeConfigurations.HOLLOW_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RAINBOW_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.RAINBOAK_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> LARGE_RAINBOW_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/large_rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.LARGE_RAINBOAK_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> BROWN_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/brown_canopy_mushroom"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_BROWN));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RED_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/red_canopy_mushroom"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_RED));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MEGA_SPRUCE_NO_PODZOL_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mega_spruce_no_podzol"), TFBiomeFeatures.SNOW_TREE.get().withConfiguration(TreeConfigurations.BIG_SPRUCE));
    private static final ConfiguredPlacement<?> DEFAULT_TREE_PLACEMENT = TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE_WITHHEIGHTMAP.square();/*.withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(8, 0.4F, 4)));*/ // FIXME: Execution order = Outer -> Inner (Last -> First); COUNT_EXTRA was first meaning it did nothing, so now properly placing it last causes mayhem. Reenable this and fix all the random values to match 1.12 again
    //Spruce trees that can be placed on snow
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> SNOW_SPRUCE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce"), TFBiomeFeatures.SNOW_TREE.get().withConfiguration(Features.SPRUCE.getConfig()));
    public static final ConfiguredFeature<?, ?> SNOW_SPRUCE_SNOWY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce_snowy"), SNOW_SPRUCE.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.1F, 1))));
    //Dark Forest
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> OAK_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/oak_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration(Features.OAK.getConfig()));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> BIRCH_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/birch_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration(Features.BIRCH.getConfig()));
    //fun, can't inherit the config so just have to copy-paste it :)
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> BUSH_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/bush_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.JUNGLE_LOG), new SimpleBlockStateProvider(BlockConstants.OAK_LEAVES), new BushFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(1), 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayerFeature(0, 0, 0))).build()));

    //Trees!
    public static final ConfiguredFeature<?, ?> CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/canopy_trees"), CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> FIREFLY_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/firefly_canopy_trees"), FIREFLY_CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DEAD_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dead_canopy_trees"), DEAD_CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/rainbow_oak_trees"), RAINBOW_OAK_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> LARGE_RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/large_rainbow_oak_trees"), LARGE_RAINBOW_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> MANGROVE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mangrove_trees"), MANGROVE_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARKWOOD_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_trees"), DARKWOOD_TREE_BASE.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(DEFAULT_TREE_PLACEMENT).func_242731_b(2).func_242732_c(2).square());
    public static final ConfiguredFeature<?, ?> DARKWOOD_LANTERN_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_lantern_trees"), DARKWOOD_LANTERN_TREE_BASE.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(DEFAULT_TREE_PLACEMENT).func_242731_b(4).func_242732_c(2).square());
    public static final ConfiguredFeature<?, ?> TWILIGHT_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/twilight_oak_trees"), TWILIGHT_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.6F, 1))));
    public static final ConfiguredFeature<?, ?> SAVANNAH_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/savannah_oak_trees"), TWILIGHT_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> SWAMPY_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/swampy_oak_trees"), SWAMPY_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.6F, 1))));
    public static final ConfiguredFeature<?, ?> HOLLOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_oak_trees"), HOLLOW_TREE_BASE.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
    public static final ConfiguredFeature<?, ?> MEGA_SPRUCE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mega_spruce_no_podzol"), MEGA_SPRUCE_NO_PODZOL_BASE.withPlacement(DEFAULT_TREE_PLACEMENT).square().func_242732_c(4));
    public static final ConfiguredFeature<?, ?> OAK_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/oak_dark_forest_trees"), OAK_DARK_FOREST.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.6F, 1))).square());
    public static final ConfiguredFeature<?, ?> BIRCH_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/birch_dark_forest_trees"), BIRCH_DARK_FOREST.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.6F, 1))).square());
    public static final ConfiguredFeature<?, ?> BUSH_DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/bush_dark_forest_trees"), BUSH_DARK_FOREST.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.6F, 1))).square());

    //"structures" that arent actually structures
    public static final ConfiguredFeature<?, ?> WELL = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("well"), TFBiomeFeatures.WELL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DRUID_HUT = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("druid_hut"), TFBiomeFeatures.DRUID_HUT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
    public static final ConfiguredFeature<?, ?> GRAVEYARD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("graveyard"), TFBiomeFeatures.GRAVEYARD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).chance(95));

    //all the fun little things you find around the dimension
    public static final ConfiguredFeature<?, ?> BIG_MUSHGLOOM = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("big_mushgloom"), TFBiomeFeatures.BIG_MUSHGLOOM.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fallen_leaves"), TFBiomeFeatures.FALLEN_LEAVES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> FIDDLEHEAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fiddlehead"), Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FIDDLEHEAD), SimpleBlockPlacer.PLACER)).tries(16).build()));
    public static final ConfiguredFeature<?, ?> FIRE_JET = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fire_jet"), TFBiomeFeatures.FIRE_JET.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.FIRE_JET)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
    public static final ConfiguredFeature<?, ?> FOUNDATION = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("foundation"), TFBiomeFeatures.FOUNDATION.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> GROVE_RUINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grove_ruins"), TFBiomeFeatures.GROVE_RUINS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> HOLLOW_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_log"), TFBiomeFeatures.FALLEN_HOLLOW_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> HOLLOW_STUMP = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_stump"), TFBiomeFeatures.HOLLOW_STUMP.get().withConfiguration(TreeConfigurations.HOLLOW_TREE).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> HUGE_LILY_PAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_lily_pad"), TFBiomeFeatures.HUGE_LILY_PAD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> HUGE_WATER_LILY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_water_lily"), TFBiomeFeatures.HUGE_WATER_LILY.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("lamppost"), TFBiomeFeatures.LAMPPOSTS.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.FIREFLY_JAR)).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.BAMBOO_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> MONOLITH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("monolith"), TFBiomeFeatures.MONOLITH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> MUSHGLOOM_CLUSTER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushgloom_cluster"), Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.PLACER)).tries(8).build()));
    public static final ConfiguredFeature<?, ?> MYCELIUM_BLOB = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mycelium_blob"), TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.MYCELIUM, FeatureSpread.func_242253_a(4, 2), 3, ImmutableList.of(BlockConstants.GRASS_BLOCK))).withPlacement(Features.Placements.PATCH_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> OUTSIDE_STALAGMITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("outside_stalagmite"), TFBiomeFeatures.OUTSIDE_STALAGMITE.get().withConfiguration(new CaveStalactiteConfig(BlockConstants.STONE, 3, 5, 10, false)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> PLANT_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("plant_roots"), TFBiomeFeatures.PLANT_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(4));
    public static final ConfiguredFeature<?, ?> PUMPKIN_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("pumpkin_lamppost"), TFBiomeFeatures.LAMPPOSTS.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.JACK_O_LANTERN)).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.BAMBOO_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> SMALL_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square());
    public static final ConfiguredFeature<?, ?> SMOKER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("smoker"), TFBiomeFeatures.FIRE_JET.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.SMOKER)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT));
    public static final ConfiguredFeature<?, ?> STONE_CIRCLE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("stone_circle"), TFBiomeFeatures.STONE_CIRCLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> THORNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("thorns"), TFBiomeFeatures.THORNS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(TwilightFeatures.CONFIGURED_PLACEMENT_NOTFSTRUCTURE).range(95).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(50).func_242732_c(85));
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("torch_berries"), TFBiomeFeatures.TORCH_BERRIES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(4));
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("troll_roots"), TFBiomeFeatures.TROLL_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(4).func_242732_c(8));
    public static final ConfiguredFeature<?, ?> WEBS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("webs"), TFBiomeFeatures.WEBS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(60));
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("ore/wood_roots_spread"), TFBiomeFeatures.WOOD_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(30).square().func_242731_b(20));
    public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_under_trees"), TFBiomeFeatures.SNOW_UNDER_TREES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    //smol stone veins
    public static final ConfiguredFeature<?, ?> SMALL_GRANITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_granite"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Blocks.GRANITE.getDefaultState(), 16)).range(60).square().func_242731_b(5));
    public static final ConfiguredFeature<?, ?> SMALL_DIORITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_diorite"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Blocks.DIORITE.getDefaultState(), 16)).range(60).square().func_242731_b(5));
    public static final ConfiguredFeature<?, ?> SMALL_ANDESITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_andesite"), Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Blocks.ANDESITE.getDefaultState(), 16)).range(60).square().func_242731_b(5));
    //Dark Forest needs special placements, so here we go
    public static final ConfiguredFeature<?, ?> DARK_MUSHGLOOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushglooms"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(10));
    public static final ConfiguredFeature<?, ?> DARK_PUMPKINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_pumpkins"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.PUMPKIN), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(16).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(30));
    public static final ConfiguredFeature<?, ?> DARK_GRASS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_grass"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(128).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(4));
    public static final ConfiguredFeature<?, ?> DARK_FERNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_ferns"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FERN), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(128).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(4));
    public static final ConfiguredFeature<?, ?> DARK_MUSHROOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushrooms"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.BROWN_MUSHROOM), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARK_DEAD_BUSHES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_dead_bushes"), TFBiomeFeatures.DARK_FOREST_PLACER.get().withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.DEAD_BUSH), SimpleBlockPlacer.PLACER)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(32).func_227317_b_().build()).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(15));

    //tree and mushroom placements
    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_sparse"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.withChance(0.15f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.withChance(0.05f)
            ), ConfiguredFeatures.CANOPY_TREE_BASE))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT)
                    .square()
                    .func_242731_b(8)
    );

    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_DENSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_dense"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.withChance(0.675f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.withChance(0.225f)
            ), ConfiguredFeatures.CANOPY_TREE_BASE))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT)
                    .square()
                    .func_242731_b(8)
    );

    public static final ConfiguredFeature<?, ?> OAK_TREES_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/oak_trees_sparse"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.HOLLOW_OAK_TREES.withChance(0.05f),
                    ConfiguredFeatures.SAVANNAH_OAK_TREES.withChance(0.35f)
            ), Feature.NO_OP.withConfiguration(NoFeatureConfig.field_236559_b_)))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT)
                    .square()
                    .func_242731_b(1)
    );
    //why does it only place hollow trees this way?
    public static final ConfiguredFeature<?, ?> HOLLOW_TREE_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_placer"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.HOLLOW_OAK_TREES.withChance(0.01f),
                    ConfiguredFeatures.HOLLOW_OAK_TREES.withChance(0.01f)
            ), Feature.NO_OP.withConfiguration(NoFeatureConfig.field_236559_b_)))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .func_242731_b(1)
    );

    //selects a random feature to place down. This makes things more random and rare
    //List first
    private static TFConfig.Common.Dimension.WorldGenWeights weights = TFConfig.COMMON_CONFIG.DIMENSION.worldGenWeights;
    private static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> COMMON_FEATURES = ImmutableList.of(
            () -> DRUID_HUT.chance(weights.druidHutWeight.get() / 4), //make this a higher chance because theyre SUPER rare otherwise
            () -> WELL.chance(weights.wellWeight.get() / 2),
            () -> GROVE_RUINS.chance(weights.groveRuinsWeight.get() / 2),
            () -> MONOLITH.chance(weights.monolithWeight.get() / 2),
            () -> OUTSIDE_STALAGMITE.chance(weights.stalagmiteWeight.get() / 2),
            () -> STONE_CIRCLE.chance(weights.stoneCircleWeight.get() / 2),
            () -> FOUNDATION.chance(weights.foundationWeight.get() / 2),
            () -> HOLLOW_LOG.chance(weights.fallenHollowLogWeight.get() / 2),
            () -> HOLLOW_STUMP.chance(weights.hollowStumpWeight.get() / 2),
            () -> SMALL_LOG.chance(weights.fallenSmallLogWeight.get() / 2));

    public static final ConfiguredFeature<?, ?> RANDOM_COMMON_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_common"),
            Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(COMMON_FEATURES))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .square()
    );

    public static final ConfiguredFeature<?, ?> RANDOM_FALLEN_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_fallen"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    FOUNDATION.withChance(0.05F),
                    HOLLOW_LOG.withChance(0.05F),
                    HOLLOW_STUMP.withChance(0.05F),
                    SMALL_LOG.withChance(0.1F)
            ), Feature.NO_OP.withConfiguration(NoFeatureConfig.field_236559_b_)))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .square()
                    .func_242731_b(1)
    );

    public static final ConfiguredFeature<?, ?> RANDOM_WATER_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_water"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    HUGE_LILY_PAD.withChance(0.50F),
                    HUGE_WATER_LILY.withChance(0.20F)
            ), Features.PATCH_WATERLILLY))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .square()
                    .func_242731_b(5)
    );

    //ground decoration
    public static final BlockClusterFeatureConfig SMALL_FLOWER_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider())
            .addWeightedBlockstate(BlockConstants.POPPY, 1)
            .addWeightedBlockstate(BlockConstants.DANDELION, 1)
            .addWeightedBlockstate(BlockConstants.RED_TULIP, 1)
            .addWeightedBlockstate(BlockConstants.ORANGE_TULIP, 1)
            .addWeightedBlockstate(BlockConstants.PINK_TULIP, 1)
            .addWeightedBlockstate(BlockConstants.WHITE_TULIP, 1)
            .addWeightedBlockstate(BlockConstants.CORNFLOWER, 1)
            .addWeightedBlockstate(BlockConstants.LILY, 1)
            .addWeightedBlockstate(BlockConstants.ORCHID, 1)
            .addWeightedBlockstate(BlockConstants.ALLIUM, 1)
            .addWeightedBlockstate(BlockConstants.AZURE, 1)
            .addWeightedBlockstate(BlockConstants.OXEYE, 1),
            SimpleBlockPlacer.PLACER))
            .tries(128)
            .build();

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.MAYAPPLE), SimpleBlockPlacer.PLACER)).tries(4).build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.PLACER)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_GRASS), DoublePlantBlockPlacer.PLACER)).tries(32).func_227317_b_().build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FERN), SimpleBlockPlacer.PLACER)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_FERN), DoublePlantBlockPlacer.PLACER)).tries(16).func_227317_b_().build()));

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> OTHER_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.PLACER)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_GRASS), DoublePlantBlockPlacer.PLACER)).tries(32).func_227317_b_().build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FERN), SimpleBlockPlacer.PLACER)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_FERN), DoublePlantBlockPlacer.PLACER)).tries(16).func_227317_b_().build()));

    public static final ConfiguredFeature<?, ?> GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(OTHER_GRASS)).func_242730_a(FeatureSpread.func_242253_a(-1, 4)).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(30).chance(5));
    public static final ConfiguredFeature<?, ?> FOREST_GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("forest_grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(FOREST_GRASS)).func_242730_a(FeatureSpread.func_242253_a(-1, 4)).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(30).chance(5));
    public static final ConfiguredFeature<?, ?> FLOWER_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("flower_placer"), Feature.FLOWER.withConfiguration(SMALL_FLOWER_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(25).chance(15));

    //music!
    public static final BackgroundMusicSelector TFMUSICTYPE = new BackgroundMusicSelector(TFSounds.MUSIC, 1200, 12000, true);
}