package twilightforest.datagen.data.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFRegistries;
import twilightforest.block.FireflyBlock;
import twilightforest.block.TorchberryPlantBlock;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFFeatures;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFWoodPaletteTags;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.feature.TFSmallLakeFeature;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.trees.treeplacers.*;
import twilightforest.world.registration.TreeDecorators;

import java.util.List;
import java.util.OptionalInt;

public class TFConfiguredFeatureGenerator {

	private static final int CANOPY_DISTANCING = 5;
	private static final int LEAF_SHAG_FACTOR = 24;

    public static final SimpleBlockConfiguration SMALL_FLOWER_CONFIG = new SimpleBlockConfiguration(
            new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.020833334F, List.of(
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.RED_TULIP.defaultBlockState(),
                    Blocks.ORANGE_TULIP.defaultBlockState(),
                    Blocks.PINK_TULIP.defaultBlockState(),
                    Blocks.WHITE_TULIP.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState(),
                    Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
                    Blocks.BLUE_ORCHID.defaultBlockState(),
                    Blocks.ALLIUM.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.OXEYE_DAISY.defaultBlockState())
            ));

    public static final SimpleBlockConfiguration SMALL_FLOWER_CONFIG_ALT = new SimpleBlockConfiguration(
            new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.020833334F, List.of(
                    Blocks.WHITE_TULIP.defaultBlockState(),
                    Blocks.PINK_TULIP.defaultBlockState(),
                    Blocks.ORANGE_TULIP.defaultBlockState(),
                    Blocks.RED_TULIP.defaultBlockState(),
                    Blocks.DANDELION.defaultBlockState(),
                    Blocks.POPPY.defaultBlockState(),
                    Blocks.OXEYE_DAISY.defaultBlockState(),
                    Blocks.AZURE_BLUET.defaultBlockState(),
                    Blocks.ALLIUM.defaultBlockState(),
                    Blocks.BLUE_ORCHID.defaultBlockState(),
                    Blocks.LILY_OF_THE_VALLEY.defaultBlockState(),
                    Blocks.CORNFLOWER.defaultBlockState())
            ));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		BlockStateProvider belowTrunkProvider = TreeConfiguration.defaultPlaceBelowTreeTrunkProvider(biomes);
        context.register(TFConfiguredFeatures.LAKE_LAVA, new ConfiguredFeature<>(TFFeatures.SMALL_LAKE.get(), new TFSmallLakeFeature.Configuration(BlockStateProvider.simple(Blocks.LAVA), BlockStateProvider.simple(Blocks.STONE), null)));
        context.register(TFConfiguredFeatures.LAKE_WATER, new ConfiguredFeature<>(TFFeatures.SMALL_LAKE.get(), new TFSmallLakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER), null, null)));
        context.register(TFConfiguredFeatures.LAKE_FROZEN, new ConfiguredFeature<>(TFFeatures.SMALL_LAKE.get(), new TFSmallLakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER), null, BlockStateProvider.simple(Blocks.ICE))));

        registerTemplateFeatures(context);

        context.register(TFConfiguredFeatures.BIG_MUSHGLOOM, new ConfiguredFeature<>(TFFeatures.BIG_MUSHGLOOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM_STEM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 1, BlockPredicate.matchesTag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE))));
        context.register(TFConfiguredFeatures.DENSE_FERNS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FERN))));
        context.register(TFConfiguredFeatures.DENSE_LARGE_FERNS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LARGE_FERN))));
        context.register(TFConfiguredFeatures.FALLEN_LEAVES, new ConfiguredFeature<>(TFFeatures.FALLEN_LEAVES.get(), FeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.MAYAPPLE, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MAYAPPLE.get()))));
        context.register(TFConfiguredFeatures.FIDDLEHEAD, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.FIDDLEHEAD.get()))));
        context.register(TFConfiguredFeatures.FIRE_JET, new ConfiguredFeature<>(TFFeatures.FIRE_JET.get(), new BlockStateConfiguration(TFBlocks.FIRE_JET.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.FOUNDATION, new ConfiguredFeature<>(TFFeatures.FOUNDATION.get(), RuinedFoundationConfig.withDefaultBlocks(false)));
        context.register(TFConfiguredFeatures.GROVE_RUINS, new ConfiguredFeature<>(TFFeatures.GROVE_RUINS.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.HOLLOW_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_HOLLOW_LOG.get(), NoneFeatureConfiguration.NONE));
        context.register(
			TFConfiguredFeatures.HOLLOW_STUMP,
			new ConfiguredFeature<>(
				TFFeatures.HOLLOW_STUMP.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).build()
			)
		);
        context.register(TFConfiguredFeatures.HUGE_LILY_PAD, new ConfiguredFeature<>(TFFeatures.HUGE_LILY_PAD.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.HUGE_WATER_LILY, new ConfiguredFeature<>(TFFeatures.HUGE_WATER_LILY.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.CICADA_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(TFBlocks.CICADA_JAR.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.FIREFLY_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(TFBlocks.FIREFLY_JAR.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.MONOLITH, new ConfiguredFeature<>(TFFeatures.MONOLITH.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.MUSHGLOOM_CLUSTER, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MUSHGLOOM.get()))));
        context.register(TFConfiguredFeatures.MYCELIUM_BLOB, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(BlockStateProvider.simple(Blocks.MYCELIUM), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK), UniformInt.of(4, 6), 3)));
        context.register(TFConfiguredFeatures.OUTSIDE_STALAGMITE, new ConfiguredFeature<>(TFFeatures.CAVE_STALACTITE.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.PLANT_ROOTS, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(TFBlocks.ROOT_STRAND.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.IRON_OREBERRIES, new ConfiguredFeature<>(TFFeatures.OREBERRY_BUSHES.get(), new BlockStateConfiguration(TFBlocks.IRON_OREBERRY_BUSH.get().defaultBlockState().setValue(BlockStateProperties.AGE_3, 3))));
        context.register(TFConfiguredFeatures.GOLD_OREBERRIES, new ConfiguredFeature<>(TFFeatures.OREBERRY_BUSHES.get(), new BlockStateConfiguration(TFBlocks.GOLD_OREBERRY_BUSH.get().defaultBlockState().setValue(BlockStateProperties.AGE_3, 3))));
        context.register(TFConfiguredFeatures.COPPER_OREBERRIES, new ConfiguredFeature<>(TFFeatures.OREBERRY_BUSHES.get(), new BlockStateConfiguration(TFBlocks.COPPER_OREBERRY_BUSH.get().defaultBlockState().setValue(BlockStateProperties.AGE_3, 3))));
        context.register(TFConfiguredFeatures.ESSENCE_OREBERRIES, new ConfiguredFeature<>(TFFeatures.OREBERRY_BUSHES.get(), new BlockStateConfiguration(TFBlocks.ESSENCE_OREBERRY_BUSH.get().defaultBlockState().setValue(BlockStateProperties.AGE_3, 3))));
        context.register(TFConfiguredFeatures.PUMPKIN_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(Blocks.JACK_O_LANTERN.defaultBlockState())));
        context.register(TFConfiguredFeatures.RASPBERRY_BUSHES, new ConfiguredFeature<>(TFFeatures.BERRY_BUSH.get(), new BerryBushConfig(TFBlocks.RASPBERRY_BUSH.get().defaultBlockState(), TFBlockTags.TF_BERRY_BUSHES_SURVIVE, true)));
        context.register(TFConfiguredFeatures.BLUEBERRY_BUSHES, new ConfiguredFeature<>(TFFeatures.BERRY_BUSH.get(), new BerryBushConfig(TFBlocks.BLUEBERRY_BUSH.get().defaultBlockState(), TFBlockTags.TF_BERRY_BUSHES_SURVIVE, true)));
        context.register(TFConfiguredFeatures.BLACKBERRY_BUSHES, new ConfiguredFeature<>(TFFeatures.BERRY_BUSH.get(), new BerryBushConfig(TFBlocks.BLACKBERRY_BUSH.get().defaultBlockState(), TFBlockTags.TF_BERRY_BUSHES_SURVIVE, true)));
        context.register(TFConfiguredFeatures.MALOBERRY_BUSHES, new ConfiguredFeature<>(TFFeatures.BERRY_BUSH.get(), new BerryBushConfig(TFBlocks.MALOBERRY_BUSH.get().defaultBlockState(), TFBlockTags.TF_BERRY_BUSHES_SURVIVE, true)));
        context.register(TFConfiguredFeatures.SMOKER, new ConfiguredFeature<>(TFFeatures.FIRE_JET.get(), new BlockStateConfiguration(TFBlocks.SMOKER.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.STONE_CIRCLE, new ConfiguredFeature<>(TFFeatures.STONE_CIRCLE.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.THORNS, new ConfiguredFeature<>(TFFeatures.THORNS.get(), new ThornsConfig(7, 3, 3, 50)));
        context.register(TFConfiguredFeatures.TORCH_BERRIES, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(TFBlocks.TORCHBERRY_PLANT.get().defaultBlockState().setValue(TorchberryPlantBlock.HAS_BERRIES, true))));
        context.register(TFConfiguredFeatures.TROLL_ROOTS, new ConfiguredFeature<>(TFFeatures.TROLL_VINES.get(), new BlockStateConfiguration(TFBlocks.TROLLVIDR.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.TROLL_BIG_MUSHGLOOMS, new ConfiguredFeature<>(TFFeatures.TROLL_BIG_MUSHGLOOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM_STEM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 1, BlockPredicate.matchesTag(TFBlockTags.HUGE_MUSHGLOOM_PLACEABLE))));
        context.register(TFConfiguredFeatures.TROLL_HUGE_RED_MUSHROOMS, new ConfiguredFeature<>(TFFeatures.TROLL_HUGE_RED_MUSHROOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 2, BlockPredicate.matchesTag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON))));
        context.register(TFConfiguredFeatures.TROLL_HUGE_BROWN_MUSHROOMS, new ConfiguredFeature<>(TFFeatures.TROLL_HUGE_BROWN_MUSHROOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 3, BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON))));
        context.register(TFConfiguredFeatures.TROLL_MUSHGLOOMS, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS_IN_STRUCTURE.get(), new BlockStateConfiguration(TFBlocks.MUSHGLOOM.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.VANILLA_ROOTS, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(Blocks.HANGING_ROOTS.defaultBlockState())));
        context.register(TFConfiguredFeatures.WEBS, new ConfiguredFeature<>(TFFeatures.WEBS.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.WOOD_ROOTS_SPREAD, new ConfiguredFeature<>(TFFeatures.WOOD_ROOTS.get(), new RootConfig(TreeDecorators.ROOT_BLEND_PROVIDER, BlockStateProvider.simple(TFBlocks.LIVEROOT_BLOCK.get()))));
        context.register(TFConfiguredFeatures.SNOW_UNDER_TREES, new ConfiguredFeature<>(TFFeatures.SNOW_UNDER_TREES.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.ENCHANTED_FOREST_VINES, new ConfiguredFeature<>(TFFeatures.ENCHANTED_FOREST_VINES.get(), NoneFeatureConfiguration.NONE));
        context.register(TFConfiguredFeatures.OAK_BUSH, new ConfiguredFeature<>(Feature.TREE, createOakBush(belowTrunkProvider)));

        context.register(TFConfiguredFeatures.TF_OAK_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.TWILIGHT_OAK_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.CANOPY_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.CANOPY_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.MANGROVE_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.MANGROVE_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.OAK_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.OAK_LOG.defaultBlockState(), TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.SPRUCE_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.SPRUCE_LOG.defaultBlockState(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get().defaultBlockState())));
        context.register(TFConfiguredFeatures.BIRCH_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.BIRCH_LOG.defaultBlockState(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get().defaultBlockState())));

        context.register(TFConfiguredFeatures.SMALL_GRANITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.GRANITE.defaultBlockState(), 16)));
        context.register(TFConfiguredFeatures.SMALL_DIORITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.DIORITE.defaultBlockState(), 16)));
        context.register(TFConfiguredFeatures.SMALL_ANDESITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.ANDESITE.defaultBlockState(), 16)));

        context.register(TFConfiguredFeatures.LEGACY_COAL_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.COAL_ORE.defaultBlockState(), 16)));
        context.register(TFConfiguredFeatures.LEGACY_IRON_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.IRON_ORE.defaultBlockState(), 9)));
        context.register(TFConfiguredFeatures.LEGACY_GOLD_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.GOLD_ORE.defaultBlockState(), 9)));
        context.register(TFConfiguredFeatures.LEGACY_REDSTONE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.REDSTONE_ORE.defaultBlockState(), 8)));
        context.register(TFConfiguredFeatures.LEGACY_DIAMOND_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.DIAMOND_ORE.defaultBlockState(), 8)));
        context.register(TFConfiguredFeatures.LEGACY_LAPIS_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.LAPIS_ORE.defaultBlockState(), 7)));
        context.register(TFConfiguredFeatures.LEGACY_COPPER_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.COPPER_ORE.defaultBlockState(), 10)));

        context.register(TFConfiguredFeatures.DARK_PUMPKINS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN))));
        context.register(TFConfiguredFeatures.DARK_GRASS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SHORT_GRASS)))); // [VanillaCopy] Registration of PATCH_GRASS_JUNGLE in VegetationFeatures
        context.register(TFConfiguredFeatures.DARK_FERNS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FERN))));
        context.register(TFConfiguredFeatures.DARK_MUSHGLOOMS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MUSHGLOOM.get()))));
        context.register(TFConfiguredFeatures.DARK_BROWN_MUSHROOMS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM))));
        context.register(TFConfiguredFeatures.DARK_RED_MUSHROOMS, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM))));
        context.register(TFConfiguredFeatures.DARK_DEAD_BUSHES, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DEAD_BUSH))));

        context.register(TFConfiguredFeatures.UBEROUS_SOIL_PATCH_BIG, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(BlockStateProvider.simple(TFBlocks.UBEROUS_SOIL.get()), BlockPredicate.matchesBlocks(Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.DIRT), UniformInt.of(4, 8), 1)));
        context.register(TFConfiguredFeatures.UBEROUS_SOIL_PATCH_SMALL, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(BlockStateProvider.simple(TFBlocks.UBEROUS_SOIL.get()), BlockPredicate.matchesBlocks(Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.DIRT), UniformInt.of(2, 3), 0)));
        context.register(TFConfiguredFeatures.TROLL_CAVE_MYCELIUM, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(BlockStateProvider.simple(Blocks.MYCELIUM), BlockPredicate.matchesBlocks(Blocks.STONE, TFBlocks.DEADROCK.get()), UniformInt.of(3, 5), 0)));
        context.register(TFConfiguredFeatures.TROLL_CAVE_DIRT, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(BlockStateProvider.simple(Blocks.DIRT), BlockPredicate.matchesBlocks(Blocks.STONE, TFBlocks.DEADROCK.get()), UniformInt.of(2, 5), 0)));

        context.register(
			TFConfiguredFeatures.TWILIGHT_OAK_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					new StraightTrunkPlacer(4, 2, 0),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 0, 1),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).build()
			)
		);
        context.register(
			TFConfiguredFeatures.LARGE_TWILIGHT_OAK_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					new FancyTrunkPlacer(3, 11, 0),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
					new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4)),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).build()
			)
		);
        context.register(
			TFConfiguredFeatures.SWAMPY_OAK_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					new StraightTrunkPlacer(4, 2, 0),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 0, 1),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS, new LeaveVineDecorator(0.125F))).build()
			)
		);
        context.register(
			TFConfiguredFeatures.CANOPY_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
					new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
					BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
					new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
					new TwoLayersFeatureSize(20, 0, CANOPY_DISTANCING),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.MEGA_CANOPY_TREE,
			new ConfiguredFeature<>(
				TFFeatures.MEGA_CANOPY.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
					BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).minHeight(30).chanceFirstFive(2).chanceSecondFive(3).build()
			)
		);
        context.register(
			TFConfiguredFeatures.FIREFLY_CANOPY_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
					new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
					BlockStateProvider.simple(TFBlocks.CANOPY_LEAVES.get()),
					new LeafSpheroidFoliagePlacer((float) Math.sqrt(Math.pow(4, 2) + Math.pow(1, 2)), 1.5f, ConstantInt.of(0), 0, 0, -0.2f, LEAF_SHAG_FACTOR),
					new TwoLayersFeatureSize(20, 1, CANOPY_DISTANCING),
					belowTrunkProvider
				).decorators(
					ImmutableList.of(
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
							new WeightedStateProvider(WeightedList.<BlockState>builder()
								.add(TFBlocks.ROPE.get().defaultBlockState(), 3)
								.add(Blocks.IRON_CHAIN.defaultBlockState(), 1).build()),
							new WeightedStateProvider(WeightedList.<BlockState>builder()
								.add(TFBlocks.FIREFLY_JAR.get().defaultBlockState(), 10)
								.add(TFBlocks.CICADA_JAR.get().defaultBlockState(), 1).build())
						)
					)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.DEAD_CANOPY_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.CANOPY_LOG.get()),
					new BranchingTrunkPlacer(20, 5, 5, 12, new BranchesConfig(BlockStateProvider.simple(TFBlocks.CANOPY_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, true),
					BlockStateProvider.simple(Blocks.AIR),
					LeafSpheroidFoliagePlacer.NO_OP,
					new TwoLayersFeatureSize(20, 0, CANOPY_DISTANCING),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.FIREFLY, TreeDecorators.LIVING_ROOTS)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.MANGROVE_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.MANGROVE_LOG.get()),
					new TrunkRiser(4, new BranchingTrunkPlacer(7, 4, 0, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.MANGROVE_WOOD.get()), 0, 3, 6, 2, 0.3, 0.25), false, false)),
					BlockStateProvider.simple(TFBlocks.MANGROVE_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(2.5f, 1.5f, ConstantInt.of(0), 2, 0, -0.25f, (int) (LEAF_SHAG_FACTOR * 0.666f)),
					new TwoLayersFeatureSize(4, 1, 1),
					belowTrunkProvider
				).decorators(
					ImmutableList.of(
						TreeDecorators.FIREFLY,
						new TreeRootsDecorator(3, 1, 12, 0, BlockStateProvider.simple(TFBlocks.MANGROVE_ROOT.get().defaultBlockState()), (new WeightedStateProvider(WeightedList.<BlockState>builder()
							.add(TFBlocks.ROOT_BLOCK.get().defaultBlockState(), 4)
							.add(TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState(), 1).build())), 1),
						new LeaveVineDecorator(0.125F)
					)).build()
			)
		);
        context.register(
			TFConfiguredFeatures.DARKWOOD_TREE,
			new ConfiguredFeature<>(
				TFFeatures.DARK_CANOPY_TREE.get(),
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
					new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
					BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
					new TwoLayersFeatureSize(4, 1, 1),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.HOMEGROWN_DARKWOOD_TREE,
			new ConfiguredFeature<>(
				TFFeatures.DARK_CANOPY_TREE.get(),
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
					new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
					BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
					new TwoLayersFeatureSize(4, 1, 1),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.DARKWOOD_LANTERN_TREE,
			new ConfiguredFeature<>(
				TFFeatures.DARK_CANOPY_TREE.get(),
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.DARK_LOG.get()),
					new BranchingTrunkPlacer(9, 1, 1, 6, new BranchesConfig(BlockStateProvider.simple(TFBlocks.DARK_WOOD.get()), 4, 0, 8, 2, 0.23, 0.23), false, false),
					BlockStateProvider.simple(TFBlocks.DARK_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(4.5f, 2.25f, ConstantInt.of(0), 1, 0, 0.45f, (int) (LEAF_SHAG_FACTOR * 1.5f)),
					new TwoLayersFeatureSize(4, 1, 1),
					belowTrunkProvider
				).decorators(
					ImmutableList.of(
						TreeDecorators.LIVING_ROOTS,
						new DangleFromTreeDecorator(
							0,
							1,
							2,
							4,
							2,
							new WeightedStateProvider(WeightedList.<BlockState>builder().add(Blocks.IRON_CHAIN.defaultBlockState(), 1).build()),
							new WeightedStateProvider(WeightedList.<BlockState>builder().add(Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), 1).build())
						)
					)).ignoreVines().build()
			)
		);
        context.register(
			TFConfiguredFeatures.TIME_TREE,
			new ConfiguredFeature<>(
				TFFeatures.TREE_OF_TIME.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.TIME_LOG.get()),
					BlockStateProvider.simple(TFBlocks.TIME_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.TIME_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).build()
			)
		);
        context.register(
			TFConfiguredFeatures.TRANSFORMATION_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LOG.get()),
					new BranchingTrunkPlacer(6, 5, 5, 7, new BranchesConfig(BlockStateProvider.simple(TFBlocks.TRANSFORMATION_WOOD.get()), 3, 1, 10, 1, 0.3, 0.2), false, false),
					BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(4.5f, 1.5f, ConstantInt.of(0), 1, 0, -0.25f, 0),
					new TwoLayersFeatureSize(4, 1, CANOPY_DISTANCING),
					belowTrunkProvider
				).ignoreVines().decorators(ImmutableList.of(new TreeCorePlacer(3, BlockStateProvider.simple(TFBlocks.TRANSFORMATION_LOG_CORE.get().defaultBlockState())))).build()
			)
		);
        context.register(
			TFConfiguredFeatures.MINING_TREE,
			new ConfiguredFeature<>(
				TFFeatures.MINERS_TREE.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.MINING_LOG.get()),
					BlockStateProvider.simple(TFBlocks.MINING_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.MINING_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).build()
			)
		);
        context.register(
			TFConfiguredFeatures.SORTING_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.SORTING_LOG.get()),
					new StraightTrunkPlacer(5, 0, 0),
					BlockStateProvider.simple(TFBlocks.SORTING_LEAVES.get()),
					new LeafSpheroidFoliagePlacer(1.5F, 2.5F, ConstantInt.of(-2), 0, 0, 0.5F, 0),
					new TwoLayersFeatureSize(1, 1, 1),
					belowTrunkProvider
				).ignoreVines().decorators(ImmutableList.of(new TreeCorePlacer(2, BlockStateProvider.simple(TFBlocks.SORTING_LOG_CORE.get().defaultBlockState())))).build()
			)
		);
        context.register(
			TFConfiguredFeatures.FOREST_MEGA_OAK_TREE,
			new ConfiguredFeature<>(
				TFFeatures.MEGA_OAK.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).minHeight(24).build()
			)
		);
        context.register(
			TFConfiguredFeatures.SAVANNAH_MEGA_OAK_TREE,
			new ConfiguredFeature<>(
				TFFeatures.MEGA_OAK.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.get()),
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.get()),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).minHeight(16).build()
			)
		);
        context.register(
			TFConfiguredFeatures.RAINBOW_OAK_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					new StraightTrunkPlacer(4, 2, 0),
					BlockStateProvider.simple(TFBlocks.RAINBOW_OAK_LEAVES.get()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 1, 1),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).build()
			)
		);
        context.register(
			TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LOG.get()),
					new FancyTrunkPlacer(3, 11, 0),
					BlockStateProvider.simple(TFBlocks.RAINBOW_OAK_LEAVES.get()),
					new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
					new TwoLayersFeatureSize(4, 1, 0, OptionalInt.of(4)),
					belowTrunkProvider
				).decorators(ImmutableList.of(TreeDecorators.LIVING_ROOTS)).build()
			)
		);
        context.register(TFConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE, new ConfiguredFeature<>(TFFeatures.CANOPY_BROWN_MUSHROOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 3, BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON))));

        HugeMushroomFeatureConfiguration redCanopyMushroom = new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 3, BlockPredicate.matchesTag(BlockTags.HUGE_RED_MUSHROOM_CAN_PLACE_ON));
        context.register(TFConfiguredFeatures.CANOPY_RED_VANILLA_MUSHROOM, new ConfiguredFeature<>(TFFeatures.CANOPY_RED_VANILLA_MUSHROOM.get(), redCanopyMushroom));
        context.register(TFConfiguredFeatures.CANOPY_RED_SMOOTH_MUSHROOM, new ConfiguredFeature<>(TFFeatures.CANOPY_RED_SMOOTH_MUSHROOM.get(), redCanopyMushroom));
        context.register(TFConfiguredFeatures.CANOPY_RED_SPHEROID_MUSHROOM, new ConfiguredFeature<>(TFFeatures.CANOPY_RED_SPHEROID_MUSHROOM.get(), redCanopyMushroom));
        context.register(TFConfiguredFeatures.CANOPY_RED_FLAT_MUSHROOM, new ConfiguredFeature<>(TFFeatures.CANOPY_RED_FLAT_MUSHROOM.get(), redCanopyMushroom));

        context.register(TFConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE, new ConfiguredFeature<>(TFFeatures.WEIGHTED_LIST_SELECTOR.value(), new WeightedListFeatureConfig(WeightedList.<Holder<PlacedFeature>>builder()
            .add(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_RED_VANILLA_MUSHROOM)), 33)
            .add(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_RED_SMOOTH_MUSHROOM)), 33)
            .add(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_RED_SPHEROID_MUSHROOM)), 33)
            .add(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_RED_FLAT_MUSHROOM)), 1)
            .build())));

		//[VanillaCopy] of Features.MEGA_SPRUCE, just without all the nasty podzol so it fits better
        context.register(
			TFConfiguredFeatures.MEGA_SPRUCE_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(Blocks.SPRUCE_LOG),
					new GiantTrunkPlacer(13, 2, 14),
					BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
					new MegaPineFoliagePlacer(ConstantInt.of(0),
						ConstantInt.of(0),
						UniformInt.of(13, 17)),
					new TwoLayersFeatureSize(4, 1, 2),
					belowTrunkProvider
				).build()
			)
		);
        context.register(
			TFConfiguredFeatures.LARGE_WINTER_TREE,
			new ConfiguredFeature<>(
				TFFeatures.LARGE_WINTER_TREE.get(),
				new TFTreeFeatureConfig.Builder(
					BlockStateProvider.simple(Blocks.SPRUCE_LOG),
					BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
					BlockStateProvider.simple(Blocks.SPRUCE_LOG),
					BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get())
				).build()
			)
		);
        context.register(TFConfiguredFeatures.SNOWY_SPRUCE_TREE, new ConfiguredFeature<>(TFFeatures.SNOW_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(5, 2, 1), BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)), new TwoLayersFeatureSize(2, 0, 2), belowTrunkProvider).ignoreVines().build()));
        context.register(TFConfiguredFeatures.DARK_FOREST_OAK_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.OAK_LOG), new StraightTrunkPlacer(4, 2, 0), BlockStateProvider.simple(Blocks.OAK_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build()));
        context.register(TFConfiguredFeatures.DARK_FOREST_BIRCH_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.BIRCH_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(Blocks.BIRCH_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build()));
        context.register(TFConfiguredFeatures.DARK_OAK_BUSH, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), createOakBush(belowTrunkProvider)));
        context.register(TFConfiguredFeatures.VANILLA_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.OAK_LOG), new StraightTrunkPlacer(4, 2, 0), BlockStateProvider.simple(Blocks.OAK_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build()));
        context.register(TFConfiguredFeatures.VANILLA_BIRCH_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.BIRCH_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(Blocks.BIRCH_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1), belowTrunkProvider).ignoreVines().build()));
        context.register(
			TFConfiguredFeatures.SMALLER_JUNGLE_TREE,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(Blocks.JUNGLE_LOG),
					new StraightTrunkPlacer(3, 2, 2),
					BlockStateProvider.simple(Blocks.JUNGLE_LEAVES),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(1, 0, 1),
					belowTrunkProvider
				).decorators(
					ImmutableList.of(
						new CocoaDecorator(0.7F),
						TrunkVineDecorator.INSTANCE,
						new LeaveVineDecorator(0.25F)
					)).ignoreVines().build()
			)
		);
        context.register(TFConfiguredFeatures.DUMMY_TREE, new ConfiguredFeature<>(Feature.NO_OP, NoneFeatureConfiguration.INSTANCE));

        context.register(TFConfiguredFeatures.WELL_PLACER, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.FANCY_WELL)), 0.05F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.SIMPLE_WELL)))));
        context.register(TFConfiguredFeatures.LAMPPOST_PLACER, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CICADA_LAMPPOST)), 0.1F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.FIREFLY_LAMPPOST)))));
        context.register(TFConfiguredFeatures.DEFAULT_FALLEN_LOGS, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.BIRCH_FALLEN_LOG)), 0.1F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.OAK_FALLEN_LOG)), 0.2F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_FALLEN_LOG)), 0.4F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.TF_OAK_FALLEN_LOG)))));

        context.register(TFConfiguredFeatures.CANOPY_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_TREE)), 0.6F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE)))));
        context.register(TFConfiguredFeatures.DENSE_CANOPY_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_TREE)), 0.7F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE)))));
        context.register(TFConfiguredFeatures.FIREFLY_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.CANOPY_TREE)), 0.33F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.FIREFLY_CANOPY_TREE)), 0.45F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE)))));
        context.register(TFConfiguredFeatures.DARK_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DARK_FOREST_BIRCH_TREE)), 0.2F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DARK_FOREST_OAK_TREE)), 0.2F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DARK_OAK_BUSH)), 0.4F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DARKWOOD_TREE)))));
        context.register(TFConfiguredFeatures.HIGHLANDS_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.VANILLA_BIRCH_TREE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.SPRUCE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.PINE)), 0.1F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.MEGA_SPRUCE_TREE)))));
        context.register(TFConfiguredFeatures.ENCHANTED_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.VANILLA_OAK_TREE)), 0.15F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.VANILLA_BIRCH_TREE)), 0.15F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.LARGE_RAINBOW_OAK_TREE)), 0.15F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.RAINBOW_OAK_TREE)))));
        context.register(TFConfiguredFeatures.SNOWY_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.MEGA_SPRUCE_TREE)), 0.33F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.LARGE_WINTER_TREE)), 0.125F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.SNOWY_SPRUCE_TREE)))));
        context.register(TFConfiguredFeatures.VANILLA_TF_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.VANILLA_BIRCH_TREE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.VANILLA_OAK_TREE)), 0.25F)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.TWILIGHT_OAK_TREE)))));
        context.register(TFConfiguredFeatures.VANILLA_TF_BIG_MUSH, new ConfiguredFeature<>(Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)))));

        context.register(TFConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE)), 0.15f), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE)), 0.05f)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DUMMY_TREE)))));
        context.register(TFConfiguredFeatures.CANOPY_MUSHROOMS_DENSE, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.BROWN_CANOPY_MUSHROOM_TREE)), 0.675f), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.RED_CANOPY_MUSHROOM_TREE)), 0.225f)), PlacementUtils.inlinePlaced(features.getOrThrow(TFConfiguredFeatures.DUMMY_TREE)))));
        context.register(TFConfiguredFeatures.FLOWER_PLACER, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, TFConfiguredFeatureGenerator.SMALL_FLOWER_CONFIG));
        context.register(TFConfiguredFeatures.FLOWER_PLACER_ALT, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, TFConfiguredFeatureGenerator.SMALL_FLOWER_CONFIG_ALT));
    }

    private static void registerTemplateFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<WoodPalette> paletteHolders = context.lookup(TFRegistries.Keys.WOOD_PALETTES);
        var paletteChoices = SwizzleConfig.buildRarityPalette(paletteHolders);

        ProcessorRule processorCobbleBlock = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState());
        ProcessorRule processorCobbleStair = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_STAIRS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState());
        ProcessorRule processorCobbleSlab = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_SLAB, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState());
        ProcessorRule processorCobbleWall = new ProcessorRule(new RandomBlockMatchTest(Blocks.COBBLESTONE_WALL, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState());

        ProcessorRule processorStoneBrickBlock = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
        ProcessorRule processorStoneBrickStair = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_STAIRS, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState());
        ProcessorRule processorStoneBrickSlab = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_SLAB, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState());
        ProcessorRule processorStoneBrickWall = new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICK_WALL, 0.5f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState());

        SwizzleConfig simpleWellConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall);
        context.register(TFConfiguredFeatures.SIMPLE_WELL, new ConfiguredFeature<>(TFFeatures.SIMPLE_WELL.get(), simpleWellConfig));

        SwizzleConfig fancyWellConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
        context.register(TFConfiguredFeatures.FANCY_WELL, new ConfiguredFeature<>(TFFeatures.FANCY_WELL.get(), fancyWellConfig));

        SwizzleConfig hutConfig = SwizzleConfig.generate(paletteHolders, TFWoodPaletteTags.DRUID_HUT_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
        context.register(TFConfiguredFeatures.DRUID_HUT, new ConfiguredFeature<>(TFFeatures.DRUID_HUT.get(), hutConfig));

        context.register(TFConfiguredFeatures.GRAVEYARD, new ConfiguredFeature<>(TFFeatures.GRAVEYARD.get(), FeatureConfiguration.NONE));
    }

	//[VanillaCopy] of JUNGLE_BUSH
	private static TreeConfiguration createOakBush(BlockStateProvider belowTrunkProvider) {
		return new TreeConfiguration.TreeConfigurationBuilder(
			BlockStateProvider.simple(Blocks.OAK_LOG),
			new StraightTrunkPlacer(1, 0, 0),
			BlockStateProvider.simple(Blocks.OAK_LEAVES),
			new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
			new TwoLayersFeatureSize(0, 0, 0),
			belowTrunkProvider
		).ignoreVines().build();
	}
}
