package twilightforest.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.Direction;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.MegaPineFoliagePlacer;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.GiantTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import twilightforest.block.BlockTFFirefly;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.world.feature.config.TFTreeFeatureConfig;
import twilightforest.worldgen.treeplacers.*;

import java.util.OptionalInt;

public final class TreeConfigurations {
    private static final int canopyDistancing = 5;

    public static final BaseTreeFeatureConfig TWILIGHT_OAK = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.TOAK_LEAVES),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    public static final BaseTreeFeatureConfig SWAMPY_OAK = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.TOAK_LEAVES),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS, LeaveVineTreeDecorator.field_236871_b_))
            .build();

    private final static int LEAF_SHAG_FACTOR = 24;
    public static final BaseTreeFeatureConfig CANOPY_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.CANOPY_LOG),
            new SimpleBlockStateProvider(BlockConstants.CANOPY_LEAVES),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f, LEAF_SHAG_FACTOR),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new TwoLayerFeature(20, 0, canopyDistancing)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
            .setIgnoreVines()
            .build();

    public static final BaseTreeFeatureConfig CANOPY_TREE_FIREFLY = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.CANOPY_LOG),
            new SimpleBlockStateProvider(BlockConstants.CANOPY_LEAVES),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f, LEAF_SHAG_FACTOR),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new TwoLayerFeature(20, 0, canopyDistancing)
    )
            .setDecorators(ImmutableList.of(
                    TreeDecorators.LIVING_ROOTS,
                    TreeDecorators.FIREFLY,
                    new TrunkSideDecorator( // A few more Fireflies!
                            4,
                            0.5f,
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
            new SimpleBlockStateProvider(BlockConstants.CANOPY_LOG),
            new SimpleBlockStateProvider(BlockConstants.AIR),
            // TODO Make No-op foilage placer - dead tree
            new LeafSpheroidFoliagePlacer(0, 0, FeatureSpread.func_242252_a(0), 0, 0, 0, 0),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new TwoLayerFeature(20, 0, canopyDistancing)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
            .setIgnoreVines()
            .build();

    public static final BaseTreeFeatureConfig MANGROVE_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.MANGROVE_LOG),
            new SimpleBlockStateProvider(BlockConstants.MANGROVE_LEAVES),
            new LeafSpheroidFoliagePlacer(2.5f, 1.5f, FeatureSpread.func_242252_a(0), 2, 0, -0.25f, (int) (LEAF_SHAG_FACTOR * 0.666f)),
            new TrunkRiser(5, new BranchingTrunkPlacer(6, 4, 0, 1, new BranchesConfig(0, 3, 6, 2, 0.3, 0.25), false)),
            new TwoLayerFeature(1, 0, 1)
    )
            .setMaxWaterDepth(6)
            .setDecorators(ImmutableList.of(
                    TreeDecorators.FIREFLY,
                    new TreeRootsDecorator(3, 1, 12, new SimpleBlockStateProvider(BlockConstants.MANGROVE_WOOD), (new WeightedBlockStateProvider())
                            .addWeightedBlockstate(BlockConstants.ROOTS, 4)
                            .addWeightedBlockstate(TFBlocks.liveroot_block.get().getDefaultState(), 1)),
                    LeaveVineTreeDecorator.field_236871_b_
                    )
            )
            .build();

    private static final SimpleBlockStateProvider DARKWOOD_LEAVES_PROVIDER = new SimpleBlockStateProvider(BlockConstants.DARKWOOD_LEAVES);

    public static final BaseTreeFeatureConfig DARKWOOD_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.DARKWOOD_LOG),
            DARKWOOD_LEAVES_PROVIDER,
            new LeafSpheroidFoliagePlacer(4.5f, 2.25f, FeatureSpread.func_242252_a(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
            new BranchingTrunkPlacer(6, 2, 2, 4, new BranchesConfig(4, 0, 10, 4, 0.23, 0.23), false),
            new TwoLayerFeature(1, 0, 1)
    )
            //.setDecorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)) //TODO roots grow EVERYWHERE in strongholds, lets disable for now
            .setIgnoreVines()
            .build();

    public static final BaseTreeFeatureConfig DARKWOOD_LANTERN_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.DARKWOOD_LOG),
            DARKWOOD_LEAVES_PROVIDER,
            new LeafSpheroidFoliagePlacer(4.5f, 2.25f, FeatureSpread.func_242252_a(0), 1, 0, 0.45f,  (int) (LEAF_SHAG_FACTOR * 1.5f)),
            new BranchingTrunkPlacer(6, 2, 2, 4, new BranchesConfig(4, 0, 10, 4, 0.23, 0.23), false),
            new TwoLayerFeature(1, 0, 1)
    )
            .setDecorators(ImmutableList.of(
                    TreeDecorators.LIVING_ROOTS,
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

    //[VanillaCopy] of Features.MEGA_SPRUCE, just without all the nasty podzol so it fits better
    public static final BaseTreeFeatureConfig BIG_SPRUCE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.SPRUCE_LOG),
            new SimpleBlockStateProvider(BlockConstants.SPRUCE_LEAVES),
            new MegaPineFoliagePlacer(FeatureSpread.func_242252_a(0),
                    FeatureSpread.func_242252_a(0),
                    FeatureSpread.func_242253_a(13, 4)),
            new GiantTrunkPlacer(13, 2, 14),
            new TwoLayerFeature(1, 1, 2))
        .build();

    // Requires Hollowtree gen
    public static final TFTreeFeatureConfig TIME_TREE = new TFTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TIME_LOG),
            new SimpleBlockStateProvider(BlockConstants.TIME_LEAVES),
            new SimpleBlockStateProvider(BlockConstants.TIME_WOOD),
            new SimpleBlockStateProvider(BlockConstants.ROOTS)
    )
            .setSapling(TFBlocks.time_sapling.get())
            .build();

    public static final BaseTreeFeatureConfig TRANSFORM_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TRANSFORM_LOG),
            new SimpleBlockStateProvider(BlockConstants.TRANSFORM_LEAVES),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, FeatureSpread.func_242252_a(0), 1, 0, -0.25f, 0),
            new BranchingTrunkPlacer(6, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            new TwoLayerFeature(20, 0, canopyDistancing)
    )
            .setIgnoreVines()
            .setDecorators(ImmutableList.of(new TreeCorePlacer(3, new SimpleBlockStateProvider(TFBlocks.transformation_log_core.get().getDefaultState().with(BlockTFLog.AXIS, Direction.Axis.Y)))))
            .build();

    public static final TFTreeFeatureConfig MINING_TREE = new TFTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.MINING_LOG),
            new SimpleBlockStateProvider(BlockConstants.MINING_LEAVES),
            new SimpleBlockStateProvider(BlockConstants.MINING_WOOD),
            new SimpleBlockStateProvider(BlockConstants.ROOTS)
    )
            .setSapling(TFBlocks.mining_sapling.get())
            .build();

    public static final BaseTreeFeatureConfig SORT_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.SORT_LOG),
            new SimpleBlockStateProvider(BlockConstants.SORT_LEAVES),
            new LeafSpheroidFoliagePlacer(1.5f, 2.25f, FeatureSpread.func_242252_a(0), 1, 0, 0.5f, 0),
            new StraightTrunkPlacer(3, 0, 0),
            new TwoLayerFeature(1, 0, 1)
    )
            .setIgnoreVines()
            .setDecorators(ImmutableList.of(new TreeCorePlacer(1, new SimpleBlockStateProvider(TFBlocks.sorting_log_core.get().getDefaultState().with(BlockTFLog.AXIS, Direction.Axis.Y)))))
            .build();

    public static final TFTreeFeatureConfig DENSE_OAK = new TFTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.TOAK_LEAVES),
            new SimpleBlockStateProvider(BlockConstants.TOAK_WOOD),
            new SimpleBlockStateProvider(BlockConstants.ROOTS)
    )
            .setSapling(TFBlocks.oak_sapling.get())
            .build();

    public static final TFTreeFeatureConfig HOLLOW_TREE = new TFTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.TOAK_LEAVES),
            new SimpleBlockStateProvider(BlockConstants.TOAK_WOOD),
            new SimpleBlockStateProvider(BlockConstants.ROOTS)
    )
            .setSapling(TFBlocks.hollow_oak_sapling.get())
            .build();

    public static final BaseTreeFeatureConfig RAINBOAK_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.RAINBOW_LEAVES),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    public static final BaseTreeFeatureConfig LARGE_RAINBOAK_TREE =  new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.TOAK_LOG),
            new SimpleBlockStateProvider(BlockConstants.RAINBOW_LEAVES),
            new FancyFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(4), 4),
            new FancyTrunkPlacer(3, 11, 0),
            new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
    )
            .setDecorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    // TODO Smarter "foilage placer" for mushrooms that actually set the mushroom block states properly
    public static final BaseTreeFeatureConfig MUSHROOM_BROWN = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.MUSHROOM_STEM),
            new SimpleBlockStateProvider(BlockConstants.MUSHROOM_CAP_BROWN),
            new LeafSpheroidFoliagePlacer(4.25f, 0f, FeatureSpread.func_242252_a(1), 1, 0, 0f, 0),
            new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfig(3, 1, 9, 1, 0.3, 0.2), true),
            new TwoLayerFeature(11, 0, canopyDistancing)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.FIREFLY))
            .setIgnoreVines()
            .build();

    public static final BaseTreeFeatureConfig MUSHROOM_RED = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(BlockConstants.MUSHROOM_STEM),
            new SimpleBlockStateProvider(BlockConstants.MUSHROOM_CAP_RED),
            new LeafSpheroidFoliagePlacer(4.25f, 1.75f, FeatureSpread.func_242252_a(1), 0, 0, -0.45f, 0),
            new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfig(3, 1, 9, 1, 0.3, 0.2), true),
            new TwoLayerFeature(11, 0, canopyDistancing)
    )
            .setDecorators(ImmutableList.of(TreeDecorators.FIREFLY))
            .setIgnoreVines()
            .build();

}
