package twilightforest.world.registration;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import twilightforest.block.FireflyBlock;
import twilightforest.init.TFBlocks;
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

	public static final TreeConfiguration LARGE_TWILIGHT_OAK = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new FancyTrunkPlacer(3, 11, 0),
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
		new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4))
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
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS, new LeaveVineDecorator(0.125F)))
		.build();

	private final static int LEAF_SHAG_FACTOR = 24;
	public static final TreeConfiguration CANOPY_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
		new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
		new TwoLayersFeatureSize(20, 0, canopyDistancing)
	)
		.decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeConfiguration CANOPY_TREE_FIREFLY = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
		new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
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
					.add(TFBlocks.ROPE.get().defaultBlockState(), 3)
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
		new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
		BlockStateProvider.simple(Blocks.AIR),
		LeafSpheroidFoliagePlacer.NO_OP,
		new TwoLayersFeatureSize(20, 0, canopyDistancing)
	)
		.decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TFTreeFeatureConfig MEGA_CANOPY = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
		BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
		BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()),
		BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(30)
		.chanceFirstFive(2)
		.chanceSecondFive(3)
		.build();

	public static final TreeConfiguration MANGROVE_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.MANGROVE_LOG.get()),
		new TrunkRiser(4, new BranchingTrunkPlacer(7, 4, 0, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.MANGROVE_WOOD.get()), 0, 3, 6, 2, 0.3, 0.25), false, false)),
		BlockStateProvider.simple(TFBlocks.MANGROVE_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(2.5f, 1.5f, ConstantInt.of(0), 2, 0, -0.25f, (int) (LEAF_SHAG_FACTOR * 0.666f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(
				TreeDecorators.FIREFLY,
				new TreeRootsDecorator(3, 1, 12, 0, BlockStateProvider.simple(TFBlocks.MANGROVE_ROOT.get().defaultBlockState()), (new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
					.add(TFBlocks.ROOT_BLOCK.get().defaultBlockState(), 4)
					.add(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), 1).build())), 1),
				new LeaveVineDecorator(0.125F)
			)
		)
		.build();

	public static final TreeConfiguration DARKWOOD_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeConfiguration HOMEGROWN_DARKWOOD_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
		new TwoLayersFeatureSize(4, 1, 1)
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.ignoreVines()
		.build();

	public static final TreeConfiguration DARKWOOD_LANTERN_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
		new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
		BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
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

	public static final TreeConfiguration SMALL_JUNGLE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(Blocks.JUNGLE_LOG),
		new StraightTrunkPlacer(3, 2, 2),
		BlockStateProvider.simple(Blocks.JUNGLE_LEAVES),
		new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
		new TwoLayersFeatureSize(1, 0, 1)
	)
		.decorators(ImmutableList.of(
			new CocoaDecorator(0.7F),
			TrunkVineDecorator.INSTANCE,
			new LeaveVineDecorator(0.25F)))
		.ignoreVines()
		.build();

	//[VanillaCopy] of JUNGLE_BUSH
	public static final TreeConfiguration OAK_BUSH = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(Blocks.OAK_LOG),
		new StraightTrunkPlacer(1, 0, 0),
		BlockStateProvider.simple(Blocks.OAK_LEAVES),
		new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
		new TwoLayersFeatureSize(0, 0, 0))
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
		new BranchingTrunkPlacer(6, 5, 5, 7, new BranchesConfig(BlockStateProvider.simple(TFBlocks.TRANSFORMATION_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, false),
		BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LEAVES.get()),
		new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, 0),
		new TwoLayersFeatureSize(4, 1, canopyDistancing)
	)
		.ignoreVines()
		.decorators(ImmutableList.of(new TreeCorePlacer(3, BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LOG_CORE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))))
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
		.forceDirt()
		.decorators(ImmutableList.of(new TreeCorePlacer(2, BlockStateProvider.simple(TFBlocks.SORTING_LOG_CORE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))))
		.build();

	public static final TFTreeFeatureConfig LARGE_WINTER = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.simple(Blocks.SPRUCE_LOG),
		BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
		BlockStateProvider.simple(Blocks.SPRUCE_LOG),
		BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
	)
		.build();

	public static final TFTreeFeatureConfig FOREST_MEGA_OAK = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
		BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(24)
		.build();

	public static final TFTreeFeatureConfig SAVANNAH_MEGA_OAK = new TFTreeFeatureConfig.Builder(
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
		BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
	)
		.minHeight(16)
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

	public static final TreeConfiguration LARGE_RAINBOAK_TREE = new TreeConfiguration.TreeConfigurationBuilder(
		BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
		new FancyTrunkPlacer(3, 11, 0),
		BlockStateProvider.simple(TFBlocks.RAINBOW_OAK_LEAVES.get()),
		new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
		new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4))
	)
		.decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS))
		.build();
}
