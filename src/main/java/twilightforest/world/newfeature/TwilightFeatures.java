package twilightforest.world.newfeature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFFirefly;
import twilightforest.block.TFBlocks;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.List;

public final class TwilightFeatures {
    public static final TrunkPlacerType<BranchingTrunkPlacer> TRUNK_BRANCHING = registerTrunk(TwilightForestMod.prefix("branching_trunk_placer"), BranchingTrunkPlacer.CODEC);
    public static final TrunkPlacerType<TrunkRiser> TRUNK_RISER = registerTrunk(TwilightForestMod.prefix("trunk_mover_upper"), TrunkRiser.CODEC);
    public static final TrunkPlacerType<HollowTrunkPlacer> HOLLOW_TRUNK = registerTrunk(TwilightForestMod.prefix("hollow_trunk_placer"), HollowTrunkPlacer.CODEC);

    public static final FoliagePlacerType<LeafSpheroidFoliagePlacer> FOLIAGE_SPHEROID = registerFoliage(TwilightForestMod.prefix("spheroid_foliage_placer"), LeafSpheroidFoliagePlacer.CODEC);

    public static final TreeDecoratorType<TrunkSideDecorator> TRUNKSIDE_DECORATOR = registerTreeFeature(TwilightForestMod.prefix("trunkside_decorator"), TrunkSideDecorator.CODEC);
    public static final TreeDecoratorType<TreeRootsDecorator> TREE_ROOTS = registerTreeFeature(TwilightForestMod.prefix("tree_roots"), TreeRootsDecorator.CODEC);
    public static final TreeDecoratorType<DangleFromTreeDecorator> DANGLING_DECORATOR = registerTreeFeature(TwilightForestMod.prefix("dangle_from_tree_decorator"), DangleFromTreeDecorator.CODEC);

    public static final class BlockStates {
        //Blockstates
        public static final BlockState WATER = net.minecraft.block.Blocks.WATER.getDefaultState();
        public static final BlockState LAVA = net.minecraft.block.Blocks.LAVA.getDefaultState();
        public static final BlockState SPRUCE_LOG = net.minecraft.block.Blocks.SPRUCE_LOG.getDefaultState();
        public static final BlockState SPRUCE_WOOD = net.minecraft.block.Blocks.SPRUCE_WOOD.getDefaultState();
        public static final BlockState SPRUCE_LEAVES = net.minecraft.block.Blocks.SPRUCE_LEAVES.getDefaultState();
        public static final BlockState OAK_LOG = TFBlocks.oak_log.get().getDefaultState();
        public static final BlockState OAK_WOOD = TFBlocks.oak_wood.get().getDefaultState();
        public static final BlockState OAK_LEAVES = TFBlocks.oak_leaves.get().getDefaultState();
        public static final BlockState CANOPY_LOG = TFBlocks.canopy_log.get().getDefaultState();
        public static final BlockState CANOPY_WOOD = TFBlocks.canopy_wood.get().getDefaultState();
        public static final BlockState CANOPY_LEAVES = TFBlocks.canopy_leaves.get().getDefaultState();
        public static final BlockState MANGROVE_LOG = TFBlocks.mangrove_log.get().getDefaultState();
        public static final BlockState MANGROVE_WOOD = TFBlocks.mangrove_wood.get().getDefaultState();
        public static final BlockState MANGROVE_LEAVES = TFBlocks.mangrove_leaves.get().getDefaultState();
        public static final BlockState DARKWOOD_LOG = TFBlocks.dark_log.get().getDefaultState();
        public static final BlockState DARKWOOD_WOOD = TFBlocks.dark_wood.get().getDefaultState();
        public static final BlockState DARKWOOD_LEAVES = TFBlocks.dark_leaves.get().getDefaultState();
        public static final BlockState TIME_LOG = TFBlocks.time_log.get().getDefaultState();
        public static final BlockState TIME_WOOD = TFBlocks.time_wood.get().getDefaultState();
        public static final BlockState TIME_LEAVES = TFBlocks.time_leaves.get().getDefaultState();
        public static final BlockState TRANSFORM_LOG = TFBlocks.transformation_log.get().getDefaultState();
        public static final BlockState TRANSFORM_WOOD = TFBlocks.transformation_wood.get().getDefaultState();
        public static final BlockState TRANSFORM_LEAVES = TFBlocks.transformation_leaves.get().getDefaultState();
        public static final BlockState MINING_LOG = TFBlocks.mining_log.get().getDefaultState();
        public static final BlockState MINING_WOOD = TFBlocks.mining_wood.get().getDefaultState();
        public static final BlockState MINING_LEAVES = TFBlocks.mining_leaves.get().getDefaultState();
        public static final BlockState SORT_LOG = TFBlocks.sorting_log.get().getDefaultState();
        public static final BlockState SORT_WOOD = TFBlocks.sorting_wood.get().getDefaultState();
        public static final BlockState SORT_LEAVES = TFBlocks.sorting_leaves.get().getDefaultState();
        public static final BlockState RAINBOW_LEAVES = TFBlocks.rainboak_leaves.get().getDefaultState();
        public static final BlockState ROOTS = TFBlocks.root.get().getDefaultState();
        public static final BlockState GRASS = net.minecraft.block.Blocks.GRASS.getDefaultState();
        public static final BlockState FERN = net.minecraft.block.Blocks.FERN.getDefaultState();
        public static final BlockState MAYAPPLE = TFBlocks.mayapple.get().getDefaultState();
        public static final BlockState FIDDLEHEAD = TFBlocks.fiddlehead.get().getDefaultState();
        public static final BlockState MUSHGLOOM = TFBlocks.mushgloom.get().getDefaultState();
        public static final BlockState DEAD_BUSH = net.minecraft.block.Blocks.DEAD_BUSH.getDefaultState();
        public static final BlockState FOREST_GRASS = net.minecraft.block.Blocks.GRASS.getDefaultState();
        public static final BlockState FIRE_JET = TFBlocks.fire_jet.get().getDefaultState();
        public static final BlockState SMOKER = TFBlocks.smoker.get().getDefaultState();
        public static final BlockState AIR = Blocks.AIR.getDefaultState();
        public static final BlockState MUSHROOM_STEM      = Blocks.MUSHROOM_STEM       .getDefaultState();//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.DOWN, false).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
        public static final BlockState MUSHROOM_CAP_RED   = Blocks.RED_MUSHROOM_BLOCK  .getDefaultState().with(HugeMushroomBlock.DOWN, false);//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
        public static final BlockState MUSHROOM_CAP_BROWN = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().with(HugeMushroomBlock.DOWN, false);//.with(HugeMushroomBlock.UP, true).with(HugeMushroomBlock.NORTH, true).with(HugeMushroomBlock.SOUTH, true).with(HugeMushroomBlock.WEST, true).with(HugeMushroomBlock.EAST, true);
    }


    public static final class Decorators {
        public static final TreeRootsDecorator LIVING_ROOTS = new TreeRootsDecorator(3, 1, 5, (new WeightedBlockStateProvider())
                .addWeightedBlockstate(BlockStates.ROOTS, 6)
                .addWeightedBlockstate(TFBlocks.liveroot_block.get().getDefaultState(), 1));

        public static final TrunkSideDecorator FIREFLY = new TrunkSideDecorator(1, 1.0f, new SimpleBlockStateProvider(TFBlocks.firefly.get().getDefaultState().with(BlockTFFirefly.FACING, Direction.NORTH)));
    }

    public static final class TreeConfigurations {
        private static final int canopyDistancing = 5;

        public static final BaseTreeFeatureConfig TWILIGHT_OAK = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.OAK_LOG),
                new SimpleBlockStateProvider(BlockStates.OAK_LEAVES),
                new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                new StraightTrunkPlacer(4, 2, 0),
                new TwoLayerFeature(1, 0, 1)
        )
                .func_236703_a_(ImmutableList.of(Decorators.LIVING_ROOTS))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig CANOPY_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.CANOPY_LOG),
                new SimpleBlockStateProvider(BlockStates.CANOPY_LEAVES),
                new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f),
                new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfiguration(3, 1, 10, 1, 0.3, 0.2), false),
                new TwoLayerFeature(20, 0, canopyDistancing)
        )
                .func_236703_a_(ImmutableList.of(Decorators.FIREFLY, Decorators.LIVING_ROOTS))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig CANOPY_TREE_FIREFLY = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.CANOPY_LOG),
                new SimpleBlockStateProvider(BlockStates.CANOPY_LEAVES),
                new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f),
                new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfiguration(3, 1, 10, 1, 0.3, 0.2), false),
                new TwoLayerFeature(20, 0, canopyDistancing)
        )
                .func_236703_a_(ImmutableList.of(
                        Decorators.LIVING_ROOTS,
                        Decorators.FIREFLY,
                        new TrunkSideDecorator( // A few more Fireflies!
                                3,
                                0.4f,
                                new SimpleBlockStateProvider(TFBlocks.firefly.get().getDefaultState().with(BlockTFFirefly.FACING, Direction.NORTH))
                        ),
                        new DangleFromTreeDecorator(
                                1,
                                2,
                                2,
                                5,
                                15,
                                new SimpleBlockStateProvider(TFBlocks.canopy_fence.get().getDefaultState()),
                                new SimpleBlockStateProvider(TFBlocks.firefly_jar.get().getDefaultState())
                        ),
                        new DangleFromTreeDecorator(
                                0,
                                1,
                                2,
                                5,
                                15,
                                new SimpleBlockStateProvider(Blocks.CHAIN.getDefaultState()),
                                new SimpleBlockStateProvider(TFBlocks.firefly_jar.get().getDefaultState())
                        )
                ))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig CANOPY_TREE_DEAD = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.CANOPY_LOG),
                new SimpleBlockStateProvider(BlockStates.AIR),
                new LeafSpheroidFoliagePlacer(0, 0, FeatureSpread.func_242252_a(0), 0, 0, 0),
                new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfiguration(3, 1, 10, 1, 0.3, 0.2), false),
                new TwoLayerFeature(20, 0, canopyDistancing)
        )
                .func_236703_a_(ImmutableList.of(Decorators.FIREFLY, Decorators.LIVING_ROOTS))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig MANGROVE_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.MANGROVE_LOG),
                new SimpleBlockStateProvider(BlockStates.MANGROVE_LEAVES),
                new LeafSpheroidFoliagePlacer(2.5f, 1.5f, FeatureSpread.func_242252_a(0), 2, 0, -0.25f),
                new TrunkRiser(5, new BranchingTrunkPlacer(6, 4, 0, 1, new BranchesConfiguration(0, 3, 6, 2, 0.3, 0.25), false)),
                new TwoLayerFeature(1, 0, 1)
        )
                .func_236703_a_(ImmutableList.of(
                        Decorators.FIREFLY,
                        new TreeRootsDecorator(3, 1, 12, new SimpleBlockStateProvider(BlockStates.MANGROVE_WOOD), (new WeightedBlockStateProvider())
                                .addWeightedBlockstate(BlockStates.ROOTS, 4)
                                .addWeightedBlockstate(TFBlocks.liveroot_block.get().getDefaultState(), 1))
                        )
                )
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig DARKWOOD_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.DARKWOOD_LOG),
                new SimpleBlockStateProvider(BlockStates.DARKWOOD_LEAVES),
                new LeafSpheroidFoliagePlacer(4.5f, 2.25f, FeatureSpread.func_242252_a(0), 1, 0, 0.45f),
                new BranchingTrunkPlacer(6, 3, 3, 5, new BranchesConfiguration(4, 0, 10, 4, 0.23, 0.23), false),
                new TwoLayerFeature(1, 0, 1)
        )
                .func_236703_a_(ImmutableList.of(
                        Decorators.LIVING_ROOTS,
                        new DangleFromTreeDecorator(
                                0,
                                1,
                                2,
                                4,
                                2,
                                new SimpleBlockStateProvider(Blocks.CHAIN.getDefaultState()),
                                new SimpleBlockStateProvider(Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true))
                        )
                ))
                .setIgnoreVines()
                .build();

        // Requires Hollowtree gen
        public static final TFTreeFeatureConfig TIME_TREE = new TFTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.TIME_LOG),
                new SimpleBlockStateProvider(BlockStates.TIME_LEAVES),
                new SimpleBlockStateProvider(BlockStates.TIME_WOOD),
                new SimpleBlockStateProvider(BlockStates.ROOTS)
        )
                .setSapling(TFBlocks.time_sapling.get())
                .build();

        public static final BaseTreeFeatureConfig TRANSFORM_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.TRANSFORM_LOG),
                new SimpleBlockStateProvider(BlockStates.TRANSFORM_LEAVES),
                new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f),
                new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfiguration(3, 1, 10, 1, 0.3, 0.2), false),
                new TwoLayerFeature(20, 0, canopyDistancing)
        )
                .setIgnoreVines()
                .build();

        public static final TFTreeFeatureConfig MINING_TREE = new TFTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.MINING_LOG),
                new SimpleBlockStateProvider(BlockStates.MINING_LEAVES),
                new SimpleBlockStateProvider(BlockStates.MINING_WOOD),
                new SimpleBlockStateProvider(BlockStates.ROOTS)
        )
                .setSapling(TFBlocks.mining_sapling.get())
                .build();

        public static final BaseTreeFeatureConfig SORT_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.SORT_LOG),
                new SimpleBlockStateProvider(BlockStates.SORT_LEAVES),
                new LeafSpheroidFoliagePlacer(1.5f, 2.25f, FeatureSpread.func_242252_a(0), 1, 0, 0.5f),
                new StraightTrunkPlacer(3, 0, 0),
                new TwoLayerFeature(1, 0, 1)
        )
                .setIgnoreVines()
                .build();

        public static final TFTreeFeatureConfig DENSE_OAK = new TFTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.OAK_LOG),
                new SimpleBlockStateProvider(BlockStates.OAK_LEAVES),
                new SimpleBlockStateProvider(BlockStates.OAK_WOOD),
                new SimpleBlockStateProvider(BlockStates.ROOTS)
        )
                .setSapling(TFBlocks.oak_sapling.get())
                .build();

        public static final TFTreeFeatureConfig HOLLOW_TREE = new TFTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.OAK_LOG),
                new SimpleBlockStateProvider(BlockStates.OAK_LEAVES),
                new SimpleBlockStateProvider(BlockStates.OAK_WOOD),
                new SimpleBlockStateProvider(BlockStates.ROOTS)
        )
                .setSapling(TFBlocks.hollow_oak_sapling.get())
                .build();

        public static final BaseTreeFeatureConfig WINTER_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.SPRUCE_LOG),
                new SimpleBlockStateProvider(BlockStates.SPRUCE_LEAVES),
                new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, 0f),
                new BranchingTrunkPlacer(20, 5, 5, 3, new BranchesConfiguration(3, 1, 9, 1, 0.3, 0.2), false),
                new TwoLayerFeature(1, 0, 1)
        )
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig RAINBOAK_TREE = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.OAK_LOG),
                new SimpleBlockStateProvider(BlockStates.RAINBOW_LEAVES),
                new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                new StraightTrunkPlacer(4, 2, 0),
                new TwoLayerFeature(1, 0, 1)
        )
                .func_236703_a_(ImmutableList.of(Decorators.LIVING_ROOTS))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig MUSHROOM_BROWN = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.MUSHROOM_STEM),
                new SimpleBlockStateProvider(BlockStates.MUSHROOM_CAP_BROWN),
                new LeafSpheroidFoliagePlacer(4.25f, 0f, FeatureSpread.func_242252_a(1), 1, 0, 0f),
                new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfiguration(3, 1, 9, 1, 0.3, 0.2), true),
                new TwoLayerFeature(11, 0, canopyDistancing)
        )
                .func_236703_a_(ImmutableList.of(Decorators.FIREFLY))
                .setIgnoreVines()
                .build();

        public static final BaseTreeFeatureConfig MUSHROOM_RED = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(BlockStates.MUSHROOM_STEM),
                new SimpleBlockStateProvider(BlockStates.MUSHROOM_CAP_RED),
                new LeafSpheroidFoliagePlacer(4.25f, 1.75f, FeatureSpread.func_242252_a(1), 0, 0, -0.45f),
                new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfiguration(3, 1, 9, 1, 0.3, 0.2), true),
                new TwoLayerFeature(11, 0, canopyDistancing)
        )
                .func_236703_a_(ImmutableList.of(Decorators.FIREFLY))
                .setIgnoreVines()
                .build();

        public static final BlockClusterFeatureConfig MUSHGLOOM_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockStates.MUSHGLOOM), new SimpleBlockPlacer())).tries(32).build();
    }

    public static final class ConfiguredFeatures {
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TWILIGHT_OAK = registerWorldFeature(TwilightForestMod.prefix("twilight_oak"), Feature.TREE.withConfiguration(TreeConfigurations.TWILIGHT_OAK));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE = registerWorldFeature(TwilightForestMod.prefix("canopy_tree"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_FIREFLY = registerWorldFeature(TwilightForestMod.prefix("canopy_tree_firefly"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_FIREFLY));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> CANOPY_TREE_DEAD = registerWorldFeature(TwilightForestMod.prefix("canopy_tree_dead"), Feature.TREE.withConfiguration(TreeConfigurations.CANOPY_TREE_DEAD));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MANGROVE_TREE = registerWorldFeature(TwilightForestMod.prefix("mangrove_tree"), Feature.TREE.withConfiguration(TreeConfigurations.MANGROVE_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> DARKWOOD_TREE = registerWorldFeature(TwilightForestMod.prefix("darkwood_tree"), Feature.TREE.withConfiguration(TreeConfigurations.DARKWOOD_TREE));
        public static final ConfiguredFeature<TFTreeFeatureConfig  , ? extends Feature<?>> TIME_TREE = registerWorldFeature(TwilightForestMod.prefix("time_tree"), TFBiomeFeatures.TREE_OF_TIME.get().withConfiguration(TreeConfigurations.TIME_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> TRANSFORM_TREE = registerWorldFeature(TwilightForestMod.prefix("transform_tree"), Feature.TREE.withConfiguration(TreeConfigurations.TRANSFORM_TREE));
        public static final ConfiguredFeature<TFTreeFeatureConfig  , ? extends Feature<?>> MINING_TREE = registerWorldFeature(TwilightForestMod.prefix("mining_tree"), TFBiomeFeatures.MINERS_TREE.get().withConfiguration(TreeConfigurations.MINING_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> SORT_TREE = registerWorldFeature(TwilightForestMod.prefix("sort_tree"), Feature.TREE.withConfiguration(TreeConfigurations.SORT_TREE));
        public static final ConfiguredFeature<TFTreeFeatureConfig  , ? extends Feature<?>> DENSE_OAK_TREE = registerWorldFeature(TwilightForestMod.prefix("dense_oak_tree"), TFBiomeFeatures.CANOPY_OAK.get().withConfiguration(TreeConfigurations.DENSE_OAK));
        public static final ConfiguredFeature<TFTreeFeatureConfig  , ? extends Feature<?>> HOLLOW_TREE = registerWorldFeature(TwilightForestMod.prefix("hollow_tree"), TFBiomeFeatures.HOLLOW_TREE.get().withConfiguration(TreeConfigurations.HOLLOW_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> RAINBOAK_TREE = registerWorldFeature(TwilightForestMod.prefix("rainbow_oak"), Feature.TREE.withConfiguration(TreeConfigurations.RAINBOAK_TREE));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MUSHROOM_BROWN = registerWorldFeature(TwilightForestMod.prefix("canopy_mushroom_brown"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_BROWN));
        public static final ConfiguredFeature<BaseTreeFeatureConfig, ? extends Feature<?>> MUSHROOM_RED = registerWorldFeature(TwilightForestMod.prefix("canopy_mushroom_red"), Feature.TREE.withConfiguration(TreeConfigurations.MUSHROOM_RED));

        private static final List<ConfiguredFeature<? extends IFeatureConfig, ? extends Feature<?>>> trees = ImmutableList.of(TWILIGHT_OAK, CANOPY_TREE, MANGROVE_TREE, DARKWOOD_TREE, /*FIXME DENSE_OAK_TREE,*/ HOLLOW_TREE);
        public static final ConfiguredFeature<?, ?> DEFAULT_TWILIGHT_TREES = registerWorldFeature(TwilightForestMod.prefix("placeholder_tree_filler"),
                Feature.RANDOM_SELECTOR
                        .withConfiguration(new MultipleRandomFeatureConfig(trees.stream().map(configuredFeature -> configuredFeature.withChance(1f / (trees.size() + 0.5f))).collect(ImmutableList.toImmutableList()), RAINBOAK_TREE))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .withPlacement(Placement.field_242902_f.configure(new AtSurfaceWithExtraConfig(1, 0.5f, 1)))
        );
    }

    private static <P extends FoliagePlacer> FoliagePlacerType<P> registerFoliage(ResourceLocation name, Codec<P> codec) {
        return Registry.register(Registry.FOLIAGE_PLACER_TYPE, name, new FoliagePlacerType<>(codec));
    }

    private static <P extends AbstractTrunkPlacer> TrunkPlacerType<P> registerTrunk(ResourceLocation name, Codec<P> codec) {
        // TRUNK_REPLACER is wrong, it only places, not replacing
        return Registry.register(Registry.TRUNK_REPLACER, name, new TrunkPlacerType<>(codec));
    }

    private static <P extends TreeDecorator> TreeDecoratorType<P> registerTreeFeature(ResourceLocation name, Codec<P> codec) {
        // TRUNK_REPLACER is wrong, it only places, not replacing
        return Registry.register(Registry.TREE_DECORATOR_TYPE, name, new TreeDecoratorType<>(codec));
    }

    protected static <FC extends IFeatureConfig, F extends Feature<FC>> ConfiguredFeature<FC, F> registerWorldFeature(ResourceLocation rl, ConfiguredFeature<FC, F> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, rl, feature);
    }

    private static <C extends IFeatureConfig, F extends Feature<C>> F registerFeatureCodec(String key, F value) {
        return Registry.register(Registry.FEATURE, key, value);
    }
}
