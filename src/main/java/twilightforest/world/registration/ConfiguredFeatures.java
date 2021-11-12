package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.data.worldgen.Features;
import net.minecraft.sounds.Music;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.WaterDepthThresholdConfiguration;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.placements.StructureClearingConfig;

import java.util.function.Supplier;

import static twilightforest.world.registration.TwilightFeatures.PLACEMENT_NO_STRUCTURE;

public final class ConfiguredFeatures {
    // Base configurations
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> TWILIGHT_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/twilight_oak"), Feature.TREE.configured(TreeConfigurations.TWILIGHT_OAK));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> SWAMPY_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/swampy_oak"), Feature.TREE.configured(TreeConfigurations.SWAMPY_OAK));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> FIREFLY_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/firefly_canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE_FIREFLY));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DEAD_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dead_canopy_tree"), Feature.TREE.configured(TreeConfigurations.CANOPY_TREE_DEAD));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> MANGROVE_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mangrove_tree"), Feature.TREE.configured(TreeConfigurations.MANGROVE_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.get().configured(TreeConfigurations.DARKWOOD_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> HOMEGROWN_DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/homegrown_darkwood_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.get().configured(TreeConfigurations.HOMEGROWN_DARKWOOD_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> DARKWOOD_LANTERN_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_lantern_tree"), TFBiomeFeatures.DARK_CANOPY_TREE.get().configured(TreeConfigurations.DARKWOOD_LANTERN_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> TIME_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/time_tree"), TFBiomeFeatures.TREE_OF_TIME.get().configured(TreeConfigurations.TIME_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> TRANSFORM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/transform_tree"), Feature.TREE.configured(TreeConfigurations.TRANSFORM_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> MINING_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mining_tree"), TFBiomeFeatures.MINERS_TREE.get().configured(TreeConfigurations.MINING_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> SORT_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/sort_tree"), Feature.TREE.configured(TreeConfigurations.SORT_TREE));
    //public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> DENSE_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.get().configured(TreeConfigurations.DENSE_OAK));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> HOLLOW_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.get().configured(TreeConfigurations.HOLLOW_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> RAINBOW_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/rainbow_oak"), Feature.TREE.configured(TreeConfigurations.RAINBOAK_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> LARGE_RAINBOW_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/large_rainbow_oak"), Feature.TREE.configured(TreeConfigurations.LARGE_RAINBOAK_TREE));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> BROWN_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/brown_canopy_mushroom"), Feature.TREE.configured(TreeConfigurations.MUSHROOM_BROWN));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> RED_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/red_canopy_mushroom"), Feature.TREE.configured(TreeConfigurations.MUSHROOM_RED));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> MEGA_SPRUCE_NO_PODZOL_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mega_spruce_no_podzol"), TFBiomeFeatures.SNOW_TREE.get().configured(TreeConfigurations.BIG_SPRUCE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> LARGE_WINTER_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/large_winter_tree_base"), TFBiomeFeatures.LARGE_WINTER_TREE.get().configured(TreeConfigurations.LARGE_WINTER));

    private static final ConfiguredDecorator<?> DEFAULT_TREE_PLACEMENT = TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE.decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD);
    private static final ConfiguredDecorator<?> DEFAULT_TREE_PLACEMENT_SQUARED = TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE.decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED);
    //Spruce trees that can be placed on snow
    public static final ConfiguredFeature<TreeConfiguration, ?> SNOW_SPRUCE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce"), TFBiomeFeatures.SNOW_TREE.get().configured(Features.SPRUCE.config()));
    public static final ConfiguredFeature<?, ?> SNOW_SPRUCE_SNOWY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_spruce_snowy"), SNOW_SPRUCE.decorated(DEFAULT_TREE_PLACEMENT));
    //Dark Forest
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> OAK_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/oak_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.get().configured(Features.OAK.config()));
    public static final ConfiguredFeature<TreeConfiguration, ? extends Feature<?>> BIRCH_DARK_FOREST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/birch_dark_forest"), TFBiomeFeatures.DARK_CANOPY_TREE.get().configured(Features.BIRCH.config()));

    //Since we use some vanilla features, we have to register some of them as our own with our own placements
    public static final ConfiguredFeature<?, ?> VANILLA_TF_OAK = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_oak"), Feature.TREE.configured(Features.OAK.config()));
    public static final ConfiguredFeature<?, ?> VANILLA_TF_BIRCH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_birch"), Feature.TREE.configured(Features.BIRCH.config()));
    public static final ConfiguredFeature<?, ?> VANILLA_TF_BIG_MUSH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla/vanilla_tf_big_mush"), Feature.RANDOM_BOOLEAN_SELECTOR.configured(new RandomBooleanFeatureConfiguration(
            () -> Features.HUGE_RED_MUSHROOM,
            () -> Features.HUGE_BROWN_MUSHROOM
    )).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_SQUARE));

    //Trees!
    public static final ConfiguredFeature<?, ?> CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/canopy_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(CANOPY_TREE_BASE.weighted(0.6F)), TWILIGHT_OAK_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1))).count(4)));
    public static final ConfiguredFeature<?, ?> DENSE_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dense_canopy_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(CANOPY_TREE_BASE.weighted(0.7F)), TWILIGHT_OAK_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> DEAD_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dead_canopy_trees"), DEAD_CANOPY_TREE_BASE.decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> MANGROVE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mangrove_trees"), MANGROVE_TREE_BASE.decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR.decorated(FeatureDecorator.WATER_DEPTH_THRESHOLD.configured(new WaterDepthThresholdConfiguration(6))).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(3, 0.1F, 1))).squared()));
    public static final ConfiguredFeature<?, ?> TWILIGHT_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/twilight_oak_trees"), TWILIGHT_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> DENSE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dense_trees"), TWILIGHT_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(7, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> SAVANNAH_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/savannah_oak_trees"), TWILIGHT_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.6F, 1)))));
    public static final ConfiguredFeature<?, ?> SWAMPY_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/swampy_oak_trees"), SWAMPY_OAK_BASE.decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(4, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> FIREFLY_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/firefly_forest_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(CANOPY_TREE_BASE.weighted(0.33F), FIREFLY_CANOPY_TREE_BASE.weighted(0.45F)), TWILIGHT_OAK_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> DARK_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dark_forest_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(BIRCH_DARK_FOREST.weighted(0.35F), OAK_DARK_FOREST.weighted(0.35F)), DARKWOOD_TREE_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> DARKWOOD_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_trees"), DARKWOOD_TREE_BASE.decorated(PLACEMENT_NO_STRUCTURE.configured(new StructureClearingConfig(true, false, 16)).decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> HIGHLANDS_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/highlands_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(Features.SPRUCE.weighted(0.25F), Features.PINE.weighted(0.1F)), MEGA_SPRUCE_NO_PODZOL_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> ENCHANTED_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/enchanted_forest_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(VANILLA_TF_OAK.weighted(0.15F), VANILLA_TF_BIRCH.weighted(0.15F), LARGE_RAINBOW_OAK_BASE.weighted(0.1F)), RAINBOW_OAK_TREE_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> SNOWY_FOREST_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/snowy_forest_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(MEGA_SPRUCE_NO_PODZOL_BASE.weighted(0.1F), LARGE_WINTER_TREE_BASE.weighted(0.01F)), SNOW_SPRUCE_SNOWY)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1)))));
    public static final ConfiguredFeature<?, ?> VANILLA_TF_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/vanilla_tf_trees"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(VANILLA_TF_BIRCH.weighted(0.25F), VANILLA_TF_OAK.weighted(0.25F)), TWILIGHT_OAK_BASE)).decorated(DEFAULT_TREE_PLACEMENT_SQUARED.decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1)))));

    //"structures" that arent actually structures
    public static final ConfiguredFeature<?, ?> SIMPLE_WELL = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("simple_well"), TFBiomeFeatures.SIMPLE_WELL.get().configured(FeatureConfiguration.NONE));
    public static final ConfiguredFeature<?, ?> FANCY_WELL = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fancy_well"), TFBiomeFeatures.FANCY_WELL.get().configured(FeatureConfiguration.NONE));
    public static final ConfiguredFeature<?, ?> WELL_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("well_placer"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(FANCY_WELL.weighted(0.07F)), SIMPLE_WELL)).decorated(TwilightFeatures.OCCUPIES_STRUCTURE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(80));
    public static final ConfiguredFeature<?, ?> DRUID_HUT = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("druid_hut"), TFBiomeFeatures.DRUID_HUT.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_STRUCTURE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(105));
    public static final ConfiguredFeature<?, ?> GRAVEYARD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("graveyard"), TFBiomeFeatures.GRAVEYARD.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).rarity(90));

    //all the fun little things you find around the dimension
    public static final ConfiguredFeature<?, ?> BIG_MUSHGLOOM = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("big_mushgloom"), TFBiomeFeatures.BIG_MUSHGLOOM.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fallen_leaves"), TFBiomeFeatures.FALLEN_LEAVES.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> FIDDLEHEAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fiddlehead"), Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FIDDLEHEAD), SimpleBlockPlacer.INSTANCE)).tries(16).build()));
    public static final ConfiguredFeature<?, ?> FIRE_JET = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fire_jet"), TFBiomeFeatures.FIRE_JET.get().configured(new BlockStateConfiguration(BlockConstants.FIRE_JET)).decorated(Features.Decorators.HEIGHTMAP_SQUARE));
    public static final ConfiguredFeature<?, ?> FOUNDATION = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("foundation"), TFBiomeFeatures.FOUNDATION.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(100).squared());
    public static final ConfiguredFeature<?, ?> GROVE_RUINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grove_ruins"), TFBiomeFeatures.GROVE_RUINS.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(105).squared());
    public static final ConfiguredFeature<?, ?> HOLLOW_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_log"), TFBiomeFeatures.FALLEN_HOLLOW_LOG.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(100).squared());
    public static final ConfiguredFeature<?, ?> HOLLOW_STUMP = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_stump"), TFBiomeFeatures.HOLLOW_STUMP.get().configured(TreeConfigurations.HOLLOW_TREE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(100).squared());
    public static final ConfiguredFeature<?, ?> HUGE_LILY_PAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_lily_pad"), TFBiomeFeatures.HUGE_LILY_PAD.get().configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(20).squared().count(10));
    public static final ConfiguredFeature<?, ?> HUGE_WATER_LILY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_water_lily"), TFBiomeFeatures.HUGE_WATER_LILY.get().configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(50).squared().count(5));
    public static final ConfiguredFeature<?, ?> CICADA_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("cicada_lamppost"), TFBiomeFeatures.LAMPPOSTS.get().configured(new BlockStateConfiguration(BlockConstants.CICADA_JAR)));
    public static final ConfiguredFeature<?, ?> FIREFLY_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("firefly_lamppost"), TFBiomeFeatures.LAMPPOSTS.get().configured(new BlockStateConfiguration(BlockConstants.FIREFLY_JAR)));
    public static final ConfiguredFeature<?, ?> LAMPPOST_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("lamppost_placer"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(CICADA_LAMPPOST.weighted(0.1F)), FIREFLY_LAMPPOST)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(2).squared());
    public static final ConfiguredFeature<?, ?> MONOLITH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("monolith"), TFBiomeFeatures.MONOLITH.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().rarity(90));
    public static final ConfiguredFeature<?, ?> MUSHGLOOM_CLUSTER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushgloom_cluster"), Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.INSTANCE)).tries(8).build()));
    public static final ConfiguredFeature<?, ?> MYCELIUM_BLOB = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mycelium_blob"), TFBiomeFeatures.MYCELIUM_BLOB.get().configured(new DiskConfiguration(BlockConstants.MYCELIUM, UniformInt.of(4, 6), 3, ImmutableList.of(BlockConstants.GRASS_BLOCK))).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> OUTSIDE_STALAGMITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("outside_stalagmite"), TFBiomeFeatures.CAVE_STALACTITE.get().configured(new SpikeConfig(new SimpleStateProvider(BlockConstants.STONE), UniformInt.of(5, 10), ConstantInt.of(0), false)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(88).squared());
    public static final ConfiguredFeature<?, ?> PLANT_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("plant_roots"), TFBiomeFeatures.UNDERGROUND_PLANTS.get().configured(new BlockStateConfiguration(BlockConstants.ROOT_STRAND)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4));
    public static final ConfiguredFeature<?, ?> PUMPKIN_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("pumpkin_lamppost"), TFBiomeFeatures.LAMPPOSTS.get().configured(new BlockStateConfiguration(BlockConstants.JACK_O_LANTERN)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared());
    public static final ConfiguredFeature<?, ?> SMOKER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("smoker"), TFBiomeFeatures.FIRE_JET.get().configured(new BlockStateConfiguration(BlockConstants.SMOKER)).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));
    public static final ConfiguredFeature<?, ?> STONE_CIRCLE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("stone_circle"), TFBiomeFeatures.STONE_CIRCLE.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(105).squared());
    public static final ConfiguredFeature<?, ?> THORNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("thorns"), TFBiomeFeatures.THORNS.get().configured(new ThornsConfig(7, 3, 3, 50)).decorated(TwilightFeatures.CONFIGURED_THORNLANDS_BLANKETING));
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("torch_berries"), TFBiomeFeatures.UNDERGROUND_PLANTS.get().configured(new BlockStateConfiguration(BlockConstants.TORCHBERRIES)).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4).decorated(TwilightFeatures.OCCUPIES_UNDERGROUND_CLEARANCE));
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("troll_roots"), TFBiomeFeatures.TROLL_VINES.get().configured(new BlockStateConfiguration(BlockConstants.TROLLVIDR)).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(4).countRandom(8));
    public static final ConfiguredFeature<?, ?> VANILLA_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("vanilla_roots"), TFBiomeFeatures.UNDERGROUND_PLANTS.get().configured(new BlockStateConfiguration(BlockConstants.HANGING_ROOTS)).decorated(Features.Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).range(Features.Decorators.RANGE_BOTTOM_TO_60).count(16).decorated(TwilightFeatures.OCCUPIES_UNDERGROUND_CLEARANCE));
    public static final ConfiguredFeature<?, ?> WEBS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("webs"), TFBiomeFeatures.WEBS.get().configured(FeatureConfiguration.NONE).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(60));
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("ore/wood_roots_spread"), TFBiomeFeatures.WOOD_ROOTS.get().configured(FeatureConfiguration.NONE).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).range(Features.Decorators.RANGE_BOTTOM_TO_60).rarity(30).squared().count(20));
    public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("snow_under_trees"), TFBiomeFeatures.SNOW_UNDER_TREES.get().configured(FeatureConfiguration.NONE));
    //fallen logs!
    public static final ConfiguredFeature<?, ?> TF_OAK_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tf_oak_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.TF_OAK_LOG, BlockConstants.HOLLOW_TF_OAK_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> CANOPY_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.CANOPY_LOG, BlockConstants.HOLLOW_CANOPY_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> MANGROVE_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mangrove_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.MANGROVE_LOG, BlockConstants.HOLLOW_MANGROVE_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> OAK_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("oak_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.OAK_LOG, BlockConstants.HOLLOW_OAK_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> SPRUCE_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("spruce_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.SPRUCE_LOG, BlockConstants.HOLLOW_SPRUCE_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> BIRCH_FALLEN_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("birch_fallen_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().configured(new HollowLogConfig(BlockConstants.BIRCH_LOG, BlockConstants.HOLLOW_BIRCH_LOG)).decorated(TwilightFeatures.OCCUPIES_SURFACE_CLEARANCE).decorated(Features.Decorators.HEIGHTMAP_OCEAN_FLOOR).rarity(40).squared());
    public static final ConfiguredFeature<?, ?> DEFAULT_FALLEN_LOGS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("default_fallen_logs"), Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(BIRCH_FALLEN_LOG.weighted(0.1F), OAK_FALLEN_LOG.weighted(0.2F), CANOPY_FALLEN_LOG.weighted(0.4F)), TF_OAK_FALLEN_LOG)));
    //TODO remove me when you figure out why removing me causes a datapack failure
    public static final ConfiguredFeature<?, ?> SMALL_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_log"), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE));
    //smol stone veins
    public static final ConfiguredFeature<?, ?> SMALL_GRANITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_granite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.GRANITE.defaultBlockState(), 16)).rarity(60).squared().count(5));
    public static final ConfiguredFeature<?, ?> SMALL_DIORITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_diorite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.DIORITE.defaultBlockState(), 16)).rarity(60).squared().count(5));
    public static final ConfiguredFeature<?, ?> SMALL_ANDESITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_andesite"), Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blocks.ANDESITE.defaultBlockState(), 16)).rarity(60).squared().count(5));
    //Dark Forest needs special placements, so here we go
    public static final ConfiguredFeature<?, ?> DARK_MUSHGLOOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushglooms"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(10));
    public static final ConfiguredFeature<?, ?> DARK_PUMPKINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_pumpkins"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.PUMPKIN), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(16).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(30));
    public static final ConfiguredFeature<?, ?> DARK_GRASS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_grass"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(512).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).count(4));
    public static final ConfiguredFeature<?, ?> DARK_FERNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_ferns"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(256).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).count(4));
    public static final ConfiguredFeature<?, ?> DARK_MUSHROOMS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_mushrooms"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.BROWN_MUSHROOM), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(64).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE));
    public static final ConfiguredFeature<?, ?> DARK_DEAD_BUSHES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dark_dead_bushes"), TFBiomeFeatures.DARK_FOREST_PLACER.get().configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.DEAD_BUSH), SimpleBlockPlacer.INSTANCE)).whitelist(ImmutableSet.of(BlockConstants.GRASS_BLOCK.getBlock())).tries(32).noProjection().build()).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).rarity(15));

    //tree and mushroom placements
    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_sparse"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.weighted(0.15f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.weighted(0.05f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .decorated(DEFAULT_TREE_PLACEMENT_SQUARED)
                    .count(8)
    );

    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_DENSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_dense"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.weighted(0.675f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.weighted(0.225f)
            ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .decorated(DEFAULT_TREE_PLACEMENT_SQUARED)
                    .count(8)
    );

    //why does it only place hollow trees this way?
    public static final ConfiguredFeature<?, ?> HOLLOW_TREE_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_placer"),
            Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(
                            ConfiguredFeatures.HOLLOW_TREE_BASE.weighted(0.04f)
                    ), Feature.NO_OP.configured(NoneFeatureConfiguration.INSTANCE)))
                    .decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD)
                    .decorated(PLACEMENT_NO_STRUCTURE.configured(new StructureClearingConfig(true, false, 16)))
                    .decorated(TwilightFeatures.CONFIGURED_CHUNK_CENTERER)
                    .count(1)
    );

    //ground decoration
    public static final RandomPatchConfiguration SMALL_FLOWER_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
            .add(BlockConstants.POPPY, 1)
            .add(BlockConstants.DANDELION, 1)
            .add(BlockConstants.RED_TULIP, 1)
            .add(BlockConstants.ORANGE_TULIP, 1)
            .add(BlockConstants.PINK_TULIP, 1)
            .add(BlockConstants.WHITE_TULIP, 1)
            .add(BlockConstants.CORNFLOWER, 1)
            .add(BlockConstants.LILY, 1)
            .add(BlockConstants.ORCHID, 1)
            .add(BlockConstants.ALLIUM, 1)
            .add(BlockConstants.AZURE, 1)
            .add(BlockConstants.OXEYE, 1).build())),
            SimpleBlockPlacer.INSTANCE))
            .tries(128)
            .build();

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.MAYAPPLE), SimpleBlockPlacer.INSTANCE)).tries(4).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_GRASS), DoublePlantPlacer.INSTANCE)).tries(32).noProjection().build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_FERN), DoublePlantPlacer.INSTANCE)).tries(16).noProjection().build()));

    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> OTHER_GRASS = ImmutableList.of(
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.INSTANCE)).tries(64).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_GRASS), DoublePlantPlacer.INSTANCE)).tries(32).noProjection().build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.FERN), SimpleBlockPlacer.INSTANCE)).tries(32).build()),
            () -> Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockConstants.TALL_FERN), DoublePlantPlacer.INSTANCE)).tries(16).noProjection().build()));

    public static final ConfiguredFeature<?, ?> GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(OTHER_GRASS)).count(UniformInt.of(1, 4)).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(30).rarity(3));
    public static final ConfiguredFeature<?, ?> FOREST_GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("forest_grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.configured(new SimpleRandomFeatureConfiguration(FOREST_GRASS)).count(UniformInt.of(1, 4)).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(30).rarity(3));
    public static final ConfiguredFeature<?, ?> FLOWER_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("flower_placer"), Feature.FLOWER.configured(SMALL_FLOWER_CONFIG).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_WORLD_SURFACE).squared().count(25).rarity(15));

    //music!
    public static final Music TFMUSICTYPE = new Music(TFSounds.MUSIC, 1200, 12000, true);
}