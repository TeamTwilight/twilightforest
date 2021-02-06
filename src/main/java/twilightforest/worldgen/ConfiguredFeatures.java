package twilightforest.worldgen;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.function.Supplier;

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
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> LARGE_RAINBOW_OAK_BASE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/base/large_rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.LARGE_RAINBOAK_TREE));
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
    public static final ConfiguredFeature<?, ?> LARGE_RAINBOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/large_rainbow_oak_trees"), LARGE_RAINBOW_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> MANGROVE_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/mangrove_trees"), MANGROVE_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARKWOOD_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_trees"), DARKWOOD_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> DARKWOOD_LANTERN_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/darkwood_lantern_trees"), DARKWOOD_LANTERN_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> TWILIGHT_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/twilight_oak_trees"), TWILIGHT_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.6F, 1))));
    public static final ConfiguredFeature<?, ?> SAVANNAH_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/savannah_oak_trees"), TWILIGHT_OAK_BASE.withPlacement(DEFAULT_TREE_PLACEMENT));
    public static final ConfiguredFeature<?, ?> HOLLOW_OAK_TREES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/hollow_oak_trees"), HOLLOW_TREE_BASE.withPlacement(DEFAULT_TREE_PLACEMENT).square());
    
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
    
    public static final ConfiguredFeature<?, ?> OAK_TREES_SPARSE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("tree/oak_trees_sparse"),
            Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    ConfiguredFeatures.HOLLOW_OAK_TREES.withChance(0.05f),
                    ConfiguredFeatures.SAVANNAH_OAK_TREES.withChance(0.35f)
            ), Feature.NO_OP.withConfiguration(NoFeatureConfig.field_236559_b_)))
                    .withPlacement(Features.Placements.BAMBOO_PLACEMENT) //TODO?
                    .square()
                    .func_242731_b(1)
    );
    
    //"structures" that arent actually structures
    public static final ConfiguredFeature<?, ?> DRUID_HUT = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("druid_hut"), TFBiomeFeatures.DRUID_HUT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> GRAVEYARD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("graveyard"), TFBiomeFeatures.GRAVEYARD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    public static final ConfiguredFeature<?, ?> WELL = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("well"), TFBiomeFeatures.WELL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));

    //all the fun little things you find around the dimension
    public static final ConfiguredFeature<?, ?> BIG_MUSHGLOOM = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("big_mushgloom"), TFBiomeFeatures.BIG_MUSHGLOOM.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> HOLLOW_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_log"), TFBiomeFeatures.FALLEN_HOLLOW_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> FALLEN_LEAVES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fallen_leaves"), TFBiomeFeatures.FALLEN_LEAVES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> SMALL_LOG = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("small_log"), TFBiomeFeatures.FALLEN_SMALL_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> FIRE_JET = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("fire_jet"), TFBiomeFeatures.FIRE_JET.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.FIRE_JET)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> FOUNDATION = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("foundation"), TFBiomeFeatures.FOUNDATION.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> GROVE_RUINS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grove_ruins"), TFBiomeFeatures.GROVE_RUINS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> HOLLOW_STUMP = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("hollow_stump"), TFBiomeFeatures.HOLLOW_STUMP.get().withConfiguration(TreeConfigurations.HOLLOW_TREE).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> HUGE_LILY_PAD = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_lily_pad"), TFBiomeFeatures.HUGE_LILY_PAD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> HUGE_WATER_LILY = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("huge_water_lily"), TFBiomeFeatures.HUGE_WATER_LILY.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("lamppost"), TFBiomeFeatures.LAMPPOSTS.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.FIREFLY_JAR)).withPlacement(Features.Placements.BAMBOO_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> MONOLITH = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("monolith"), TFBiomeFeatures.MONOLITH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> MUSHGLOOM_CLUSTER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mushgloom_cluster"), Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.MUSHGLOOM), SimpleBlockPlacer.PLACER)).tries(8).func_227317_b_().build()));
    public static final ConfiguredFeature<?, ?> MYCELIUM_BLOB = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("mycelium_blob"), TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.MYCELIUM, FeatureSpread.func_242253_a(4, 2), 3, ImmutableList.of(BlockConstants.GRASS_BLOCK))));
    public static final ConfiguredFeature<?, ?> OUTSIDE_STALAGMITE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("outside_stalagmite"), TFBiomeFeatures.OUTSIDE_STALAGMITE.get().withConfiguration(new CaveStalactiteConfig(BlockConstants.STONE, 3, 5, 10, false)).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> PLANT_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("plant_roots"), TFBiomeFeatures.PLANT_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> STONE_CIRCLE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("stone_circle"), TFBiomeFeatures.STONE_CIRCLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> THORNS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("thorns"), TFBiomeFeatures.THORNS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(124).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT.range(124)).range(124).func_242731_b(125).func_242732_c(125).range(124));
    public static final ConfiguredFeature<?, ?> TORCH_BERRIES = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("torch_berries"), TFBiomeFeatures.TORCH_BERRIES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> TROLL_ROOTS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("troll_roots"), TFBiomeFeatures.TROLL_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).range(25).square().func_242731_b(1));
    public static final ConfiguredFeature<?, ?> WEBS = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("webs"), TFBiomeFeatures.WEBS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(60));
    public static final ConfiguredFeature<?, ?> PUMPKIN_LAMPPOST = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("pumpkin_lamppost"), TFBiomeFeatures.LAMPPOSTS.get().withConfiguration(new BlockStateFeatureConfig(BlockConstants.JACK_O_LANTERN)).withPlacement(Features.Placements.BAMBOO_PLACEMENT).square().func_242731_b(1));

    //Selects a random feature to place down. This makes things more random and rare
    public static final ConfiguredFeature<?, ?> RANDOM_COMMON_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_common"), 
    		Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
                    SMALL_LOG.withChance(0.15f),
                    GROVE_RUINS.withChance(0.15F),
                    MONOLITH.withChance(0.15F),
                    OUTSIDE_STALAGMITE.withChance(0.15F),
                    STONE_CIRCLE.withChance(0.15F)
            ), Feature.NO_OP.withConfiguration(NoFeatureConfig.field_236559_b_)))
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .square()
                    .func_242731_b(1)
            );
    
    public static final ConfiguredFeature<?, ?> RANDOM_FALLEN_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_fallen"),
			Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
					FOUNDATION.withChance(0.25F),
					HOLLOW_LOG.withChance(0.25F), 
					HOLLOW_STUMP.withChance(0.25F)
			), SMALL_LOG))
					.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
					.square()
					.func_242731_b(1)
			);

	public static final ConfiguredFeature<?, ?> RANDOM_WATER_FEATURE = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("random_water"),
			Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(
					HUGE_LILY_PAD.withChance(0.40F),
					HUGE_WATER_LILY.withChance(0.40F)
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
	
    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_GRASS = ImmutableList.of(() -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.MAYAPPLE), SimpleBlockPlacer.PLACER)).tries(4).build());
    }, () -> { 
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FIDDLEHEAD), SimpleBlockPlacer.PLACER)).tries(4).build());
    }, () -> { 
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.PLACER)).tries(64).build());
    }, () -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_GRASS), DoublePlantBlockPlacer.PLACER)).tries(32).func_227317_b_().build());
    }, () -> { 
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FERN), SimpleBlockPlacer.PLACER)).tries(32).build());	
    }, () -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_FERN), DoublePlantBlockPlacer.PLACER)).tries(16).func_227317_b_().build());
    });
    
    public static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> OTHER_GRASS = ImmutableList.of(() -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.GRASS), SimpleBlockPlacer.PLACER)).tries(64).build());
    }, () -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_GRASS), DoublePlantBlockPlacer.PLACER)).tries(32).func_227317_b_().build());
    }, () -> { 
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.FERN), SimpleBlockPlacer.PLACER)).tries(32).build());	
    }, () -> {
    	return Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockConstants.TALL_FERN), DoublePlantBlockPlacer.PLACER)).tries(16).func_227317_b_().build());
    });
    
    public static final ConfiguredFeature<?, ?> GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(OTHER_GRASS)).func_242730_a(FeatureSpread.func_242253_a(-1, 4)).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(30).chance(5));
    public static final ConfiguredFeature<?, ?> FOREST_GRASS_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("forest_grass_placer"), Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(FOREST_GRASS)).func_242730_a(FeatureSpread.func_242253_a(-1, 4)).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).square().func_242731_b(30).chance(5));
    public static final ConfiguredFeature<?, ?> FLOWER_PLACER = TwilightFeatures.registerWorldFeature(TwilightForestMod.prefix("flower_placer"), Feature.FLOWER.withConfiguration(SMALL_FLOWER_CONFIG).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242731_b(25).chance(15));

    public static final SurfaceBuilderConfig DEADROCK_CONFIG = new SurfaceBuilderConfig(BlockConstants.DEADROCK, BlockConstants.DEADROCK, BlockConstants.DEADROCK);
    public static final SurfaceBuilderConfig HIGHLANDS_CONFIG = new SurfaceBuilderConfig(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.GRASS_BLOCK);
}