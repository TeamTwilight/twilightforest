package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import twilightforest.block.FireflyBlock;
import twilightforest.block.TFBlocks;
import twilightforest.block.TFLogBlock;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;
import twilightforest.world.components.feature.trees.treeplacers.*;

import java.util.OptionalInt;

public class TreeConfigurations {
    private static final int canopyDistancing = 5;

    static SimpleWeightedRandomList.Builder<BlockState> createBlockList() {
        return SimpleWeightedRandomList.builder();
    }

    public static final TreeConfiguration TWILIGHT_OAK = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            new StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    public static final TreeConfiguration SWAMPY_OAK = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            new StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS, LeaveVineDecorator.INSTANCE))
            .build();

    private final static int LEAF_SHAG_FACTOR = 24;
    public static final TreeConfiguration CANOPY_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, LEAF_SHAG_FACTOR),
            new TwoLayersFeatureSize(20, 0, canopyDistancing)
    )
            .decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
            .ignoreVines()
            .build();

    public static final TreeConfiguration CANOPY_TREE_FIREFLY = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, LEAF_SHAG_FACTOR),
            new TwoLayersFeatureSize(20, 1, canopyDistancing)
    )
            .decorators(ImmutableList.of(
                    TreeDecorators.LIVING_ROOTS,
                    TreeDecorators.FIREFLY,
                    new TrunkSideDecorator( // A few more Fireflies!
                            4,
                            0.5f,
                            BlockStateProvider.simple(TFBlocks.FIREFLY.get().defaultBlockState().setValue(FireflyBlock.FACING, Direction.NORTH))
                    ),
                    new DangleFromTreeDecorator(
                            1,
                            1,
                            2,
                            5,
                            15,
                            new WeightedStateProvider(createBlockList()
                                    .add(TFBlocks.CANOPY_FENCE.get().defaultBlockState(), 3)
                                    .add(Blocks.CHAIN.defaultBlockState(), 1)),
                            new WeightedStateProvider(createBlockList()
                                    .add(TFBlocks.FIREFLY_JAR.get().defaultBlockState(), 10)
                                    .add(TFBlocks.CICADA_JAR.get().defaultBlockState(), 1))
                    )
            ))
            .ignoreVines()
            .build();

    public static final TreeConfiguration CANOPY_TREE_DEAD = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
            new BranchingTrunkPlacer(20, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            BlockStateProvider.simple(Blocks.AIR),
            // TODO Make No-op foilage placer - dead tree
            new LeafSpheroidFoliagePlacer(0, 0, ConstantInt.of(0), 0, 0, 0, 0),
            new TwoLayersFeatureSize(20, 0, canopyDistancing)
    )
            .decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
            .ignoreVines()
            .build();

    public static final TreeConfiguration MANGROVE_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.MANGROVE_LOG.get()),
            new TrunkRiser(5, new BranchingTrunkPlacer(6, 4, 0, 1, new BranchesConfig(0, 3, 6, 2, 0.3, 0.25), false)),
            BlockStateProvider.simple(TFBlocks.MANGROVE_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(2.5f, 1.5f, ConstantInt.of(0), 2, 0, -0.25f, (int) (LEAF_SHAG_FACTOR * 0.666f)),
            new TwoLayersFeatureSize(4, 1, 1)
    )
            .decorators(ImmutableList.of(
                    TreeDecorators.FIREFLY,
                    new TreeRootsDecorator(3, 1, 12, BlockStateProvider.simple(TFBlocks.MANGROVE_ROOT.get().defaultBlockState()), (new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(TFBlocks.ROOT_BLOCK.get().defaultBlockState(), 4)
                            .add(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), 1).build()))),
                    LeaveVineDecorator.INSTANCE
                    )
            )
            .build();

    public static final TreeConfiguration DARKWOOD_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
            new BranchingTrunkPlacer(6, 1, 1, 3, new BranchesConfig(4, 0, 8, 2, 0.23, 0.23), false),
            BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
            new TwoLayersFeatureSize(4, 1, 1)
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .ignoreVines()
            .build();

    public static final TreeConfiguration HOMEGROWN_DARKWOOD_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
            new BranchingTrunkPlacer(6, 1, 1, 3, new BranchesConfig(4, 0, 8, 2, 0.23, 0.23), false),
            BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
            new TwoLayersFeatureSize(4, 1, 1)
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .ignoreVines()
            .build();

    public static final TreeConfiguration DARKWOOD_LANTERN_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
            new BranchingTrunkPlacer(6, 1, 1, 3, new BranchesConfig(4, 0, 8, 2, 0.23, 0.23), false),
            BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f,  (int) (LEAF_SHAG_FACTOR * 1.5f)),
            new TwoLayersFeatureSize(4, 1, 1)
    )
            .decorators(ImmutableList.of(
                    TreeDecorators.LIVING_ROOTS,
                    new DangleFromTreeDecorator(
                            0,
                            1,
                            2,
                            4,
                            2,
                            new WeightedStateProvider(createBlockList().add(Blocks.CHAIN.defaultBlockState(), 1)),
                            new WeightedStateProvider(createBlockList().add(Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), 1))
                    )
            ))
            .ignoreVines()
            .build();

    //[VanillaCopy] of Features.MEGA_SPRUCE, just without all the nasty podzol so it fits better
    public static final TreeConfiguration BIG_SPRUCE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.SPRUCE_LOG),
            new GiantTrunkPlacer(13, 2, 14),
            BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
            new MegaPineFoliagePlacer(ConstantInt.of(0),
                    ConstantInt.of(0),
                    UniformInt.of(13, 17)),
            new TwoLayersFeatureSize(4, 1, 2))
        .build();

    // Requires Hollowtree gen
    public static final TFTreeFeatureConfig TIME_TREE = new TFTreeFeatureConfig.Builder(
            BlockStateProvider.simple(TFBlocks.TIME_LOG.get()),
            BlockStateProvider.simple(TFBlocks.TIME_LEAVES.get()),
            BlockStateProvider.simple(TFBlocks.TIME_WOOD.get()),
            BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
    )
            .build();

    public static final TreeConfiguration TRANSFORM_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LOG.get()),
            new BranchingTrunkPlacer(6, 5, 5, 7, new BranchesConfig(3, 1, 10, 1, 0.3, 0.2), false),
            BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, 0),
            new TwoLayersFeatureSize(4, 1, canopyDistancing)
    )
            .ignoreVines()
            .decorators(ImmutableList.of(new TreeCorePlacer(3, BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LOG_CORE.get().defaultBlockState().setValue(TFLogBlock.AXIS, Direction.Axis.Y)))))
            .build();

    public static final TFTreeFeatureConfig MINING_TREE = new TFTreeFeatureConfig.Builder(
            BlockStateProvider.simple(TFBlocks.MINING_LOG.get()),
            BlockStateProvider.simple(TFBlocks.MINING_LEAVES.get()),
            BlockStateProvider.simple(TFBlocks.MINING_WOOD.get()),
            BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
    )
            .build();

    public static final TreeConfiguration SORT_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.SORTING_LOG.get()),
            new StraightTrunkPlacer(3, 0, 0),
            BlockStateProvider.simple(TFBlocks.SORTING_LEAVES.get()),
            new LeafSpheroidFoliagePlacer(1.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.5f, 0),
            new TwoLayersFeatureSize(1, 1, 1)
    )
            .ignoreVines()
            .decorators(ImmutableList.of(new TreeCorePlacer(2, BlockStateProvider.simple(TFBlocks.SORTING_LOG_CORE.get().defaultBlockState().setValue(TFLogBlock.AXIS, Direction.Axis.Y)))))
            .build();

    public static final TFTreeFeatureConfig LARGE_WINTER = new TFTreeFeatureConfig.Builder(
            BlockStateProvider.simple(Blocks.SPRUCE_LOG),
            BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
            BlockStateProvider.simple(Blocks.SPRUCE_LOG),
            BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
    )
            .build();

    public static final TFTreeFeatureConfig DENSE_OAK = new TFTreeFeatureConfig.Builder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
            BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
    )
            .build();

    public static final TFTreeFeatureConfig HOLLOW_TREE = new TFTreeFeatureConfig.Builder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
            BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
    )
            .build();

    public static final TreeConfiguration RAINBOAK_TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            new StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.simple(TFBlocks.RAINBOW_OAK_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 1, 1)
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    public static final TreeConfiguration LARGE_RAINBOAK_TREE =  new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
            new FancyTrunkPlacer(3, 11, 0),
            BlockStateProvider.simple(TFBlocks.RAINBOW_OAK_LEAVES.get()),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
            new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4))
    )
            .decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
            .build();

    // TODO Smarter "foilage placer" for mushrooms that actually set the mushroom block states properly
    public static final TreeConfiguration MUSHROOM_BROWN = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.MUSHROOM_STEM),
            new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfig(3, 1, 9, 1, 0.3, 0.2), true),
            BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK),
            new LeafSpheroidFoliagePlacer(4.25f, 0f, ConstantInt.of(1), 1, 0, 0f, 0),
            new TwoLayersFeatureSize(11, 1, canopyDistancing)
    )
            .decorators(ImmutableList.of(TreeDecorators.FIREFLY))
            .ignoreVines()
            .build();

    public static final TreeConfiguration MUSHROOM_RED = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.MUSHROOM_STEM),
            new BranchingTrunkPlacer(12, 5, 5, 6, new BranchesConfig(3, 1, 9, 1, 0.3, 0.2), true),
            BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK),
            new LeafSpheroidFoliagePlacer(4.25f, 1.75f, ConstantInt.of(1), 0, 0, -0.45f, 0),
            new TwoLayersFeatureSize(11, 1, canopyDistancing)
    )
            .decorators(ImmutableList.of(TreeDecorators.FIREFLY))
            .ignoreVines()
            .build();

}
