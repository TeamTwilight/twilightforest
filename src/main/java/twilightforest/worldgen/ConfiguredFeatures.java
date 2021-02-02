package twilightforest.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import twilightforest.TwilightForestMod;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;

public final class ConfiguredFeatures {
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TWILIGHT_OAK = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("twilight_oak"), Feature.TREE.withConfiguration(TreeConfigurations.TWILIGHT_OAK));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_FIREFLY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_tree_firefly"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_FIREFLY));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_DEAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_tree_dead"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_DEAD));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MANGROVE_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mangrove_tree"), Feature.TREE.withConfiguration(TreeConfigurations.MANGROVE_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("darkwood_tree"), Feature.TREE.withConfiguration(TreeConfigurations.DARKWOOD_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> TIME_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("time_tree"), TFBiomeFeatures.TREE_OF_TIME.get().withConfiguration(TreeConfigurations.TIME_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TRANSFORM_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("transform_tree"), Feature.TREE.withConfiguration(TreeConfigurations.TRANSFORM_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> MINING_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mining_tree"), TFBiomeFeatures.MINERS_TREE.get().withConfiguration(TreeConfigurations.MINING_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> SORT_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("sort_tree"), Feature.TREE.withConfiguration(TreeConfigurations.SORT_TREE));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> DENSE_OAK_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.get().withConfiguration(TreeConfigurations.DENSE_OAK));
    public static final ConfiguredFeature<TFTreeFeatureConfig, ? extends Feature<?>> HOLLOW_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.get().withConfiguration(TreeConfigurations.HOLLOW_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RAINBOAK_TREE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.RAINBOAK_TREE));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MUSHROOM_BROWN = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_mushroom_brown"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_BROWN));
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MUSHROOM_RED = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("canopy_mushroom_red"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_RED));

    private static final List<ConfiguredFeature<? extends IFeatureConfig, ? extends Feature<?>>> trees = ImmutableList.of(TWILIGHT_OAK, CANOPY_TREE, MANGROVE_TREE, DARKWOOD_TREE, /*FIXME DENSE_OAK_TREE,*/ HOLLOW_TREE);
    public static final ConfiguredFeature<?, ?> DEFAULT_TWILIGHT_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("placeholder_tree_filler"),
            Feature.RANDOM_SELECTOR
                    .withConfiguration(new MultipleRandomFeatureConfig(trees.stream().map(configuredFeature -> configuredFeature.withChance(1f / (trees.size() + 0.5f))).collect(ImmutableList.toImmutableList()), RAINBOAK_TREE))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(1, 0.5f, 1)))
    );
}
