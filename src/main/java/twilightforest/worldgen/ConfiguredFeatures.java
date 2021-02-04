package twilightforest.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import twilightforest.TwilightForestMod;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;

public final class ConfiguredFeatures {
    // Base configurations
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TWILIGHT_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/twilight_oak"), Feature.TREE.withConfiguration(TreeConfigurations.TWILIGHT_OAK));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> FIREFLY_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/firefly_canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_FIREFLY));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DEAD_CANOPY_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dead_canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_DEAD));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MANGROVE_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mangrove_tree"), Feature.TREE.withConfiguration(TreeConfigurations.MANGROVE_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_tree"), Feature.TREE.withConfiguration(TreeConfigurations.DARKWOOD_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_LANTERN_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/darkwood_lantern_tree"), Feature.TREE.withConfiguration(TreeConfigurations.DARKWOOD_LANTERN_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> TIME_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/time_tree"), TFBiomeFeatures.TREE_OF_TIME.get().withConfiguration(TreeConfigurations.TIME_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TRANSFORM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/transform_tree"), Feature.TREE.withConfiguration(TreeConfigurations.TRANSFORM_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> MINING_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/mining_tree"), TFBiomeFeatures.MINERS_TREE.get().withConfiguration(TreeConfigurations.MINING_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> SORT_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/sort_tree"), Feature.TREE.withConfiguration(TreeConfigurations.SORT_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> DENSE_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.get().withConfiguration(TreeConfigurations.DENSE_OAK));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> HOLLOW_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.get().withConfiguration(TreeConfigurations.HOLLOW_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RAINBOW_OAK_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.RAINBOAK_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> BROWN_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/brown_canopy_mushroom"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_BROWN));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RED_CANOPY_MUSHROOM_TREE_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/base/red_canopy_mushroom"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_RED));

    // Compounded configurations
    public static final ConfiguredFeature<?, ?> WOOD_ROOTS_SPREAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("ore/wood_roots_spread"), TFBiomeFeatures.WOOD_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(30).square().func_242731_b(20));

    private static final List<ConfiguredFeature<? extends IFeatureConfig, ? extends Feature<?>>> ALL_REGULAR_TREES = ImmutableList.of(TWILIGHT_OAK_BASE, CANOPY_TREE_BASE, MANGROVE_TREE_BASE, DARKWOOD_TREE_BASE, /*FIXME DENSE_OAK_TREE,*/ HOLLOW_TREE_BASE);
    public static final ConfiguredFeature<?, ?> DEFAULT_TWILIGHT_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/placeholder_filler_trees"),
            Feature.RANDOM_SELECTOR
                    .withConfiguration(new MultipleRandomFeatureConfig(ALL_REGULAR_TREES.stream().map(configuredFeature -> configuredFeature.withChance(1f / (ALL_REGULAR_TREES.size() + 0.5f))).collect(ImmutableList.toImmutableList()), RAINBOW_OAK_TREE_BASE))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.5f, 1)))
    );
    // TODO Actually figure out how the hell placement logic works
    private static final ConfiguredPlacement<?> DEFAULT_TREE_PLACEMENT = Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(8, 0.4F, 4)).square();

    public static final ConfiguredFeature<?, ?> CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/canopy_trees"), CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> FIREFLY_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/firefly_canopy_trees"), FIREFLY_CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DEAD_CANOPY_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/dead_canopy_trees"), DEAD_CANOPY_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/rainbow_oak_trees"), RAINBOW_OAK_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> MANGROVE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mangrove_trees"), MANGROVE_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARKWOOD_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_trees"), DARKWOOD_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARKWOOD_LANTERN_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_lantern_trees"), DARKWOOD_LANTERN_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));

    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_sparse"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.withChance(0.15f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.withChance(0.05f)
            ), ConfiguredFeatures.CANOPY_TREE_BASE))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT) //TODO?
                    .square()
                    .func_242731_b(8)
    );

    public static final ConfiguredFeature<?, ?> CANOPY_MUSHROOMS_DENSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushroom/canopy_mushrooms_dense"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE_BASE.withChance(0.675f),
                    ConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE_BASE.withChance(0.225f)
            ), ConfiguredFeatures.CANOPY_TREE_BASE))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT) //TODO?
                    .square()
                    .func_242731_b(8)
    );
}
