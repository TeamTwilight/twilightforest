package twilightforest.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TorchberryPlantBlock;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.util.WoodPalette;
import twilightforest.world.components.feature.config.HollowLogConfig;
import twilightforest.world.components.feature.config.RootConfig;
import twilightforest.world.components.feature.config.SwizzleConfig;
import twilightforest.world.components.feature.config.ThornsConfig;
import twilightforest.world.registration.TreeConfigurations;
import twilightforest.world.registration.TreeDecorators;

import java.util.List;

public final class TFConfiguredFeatures {

	//vanilla features with custom placement code
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_LAVA = registerKey("lava_lake");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_WATER = registerKey("water_lake");

	//"structures" that arent actually structures
	public static final ResourceKey<ConfiguredFeature<?, ?>> SIMPLE_WELL = registerKey("simple_well");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_WELL = registerKey("fancy_well");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DRUID_HUT = registerKey("druid_hut");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GRAVEYARD = registerKey("graveyard");

	//all the fun little things you find around the dimension
	public static final ResourceKey<ConfiguredFeature<?, ?>> BIG_MUSHGLOOM = registerKey("mushroom/big_mushgloom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_LEAVES = registerKey("fallen_leaves");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MAYAPPLE = registerKey("mayapple");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIDDLEHEAD = registerKey("fiddlehead");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIRE_JET = registerKey("fire_jet");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOUNDATION = registerKey("foundation");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GROVE_RUINS = registerKey("grove_ruins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOW_LOG = registerKey("hollow_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOW_STUMP = registerKey("hollow_stump");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_LILY_PAD = registerKey("huge_lily_pad");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_WATER_LILY = registerKey("huge_water_lily");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CICADA_LAMPPOST = registerKey("cicada_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_LAMPPOST = registerKey("firefly_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MONOLITH = registerKey("monolith");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHGLOOM_CLUSTER = registerKey("mushgloom_cluster");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYCELIUM_BLOB = registerKey("mycelium_blob");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OUTSIDE_STALAGMITE = registerKey("outside_stalagmite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PLANT_ROOTS = registerKey("plant_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PUMPKIN_LAMPPOST = registerKey("pumpkin_lamppost");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMOKER = registerKey("smoker");
	public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_CIRCLE = registerKey("stone_circle");
	public static final ResourceKey<ConfiguredFeature<?, ?>> THORNS = registerKey("thorns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TORCH_BERRIES = registerKey("torch_berries");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_ROOTS = registerKey("troll_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_ROOTS = registerKey("vanilla_roots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WEBS = registerKey("webs");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WOOD_ROOTS_SPREAD = registerKey("ore/wood_roots_spread");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_UNDER_TREES = registerKey("snow_under_trees");

	//fallen logs!
	public static final ResourceKey<ConfiguredFeature<?, ?>> TF_OAK_FALLEN_LOG = registerKey("tf_oak_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_FALLEN_LOG = registerKey("canopy_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_FALLEN_LOG = registerKey("mangrove_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_FALLEN_LOG = registerKey("oak_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE_FALLEN_LOG = registerKey("spruce_fallen_log");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_FALLEN_LOG = registerKey("birch_fallen_log");

	//smol stone veins
	public static final ResourceKey<ConfiguredFeature<?,?>> SMALL_GRANITE = registerKey("small_granite");
	public static final ResourceKey<ConfiguredFeature<?,?>> SMALL_DIORITE = registerKey("small_diorite");
	public static final ResourceKey<ConfiguredFeature<?,?>> SMALL_ANDESITE = registerKey("small_andesite");

	//Ores! Lets keep pre 1.18 ore rates :)
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_COAL_ORE = registerKey("legacy_coal_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_IRON_ORE = registerKey("legacy_iron_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_GOLD_ORE = registerKey("legacy_gold_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_REDSTONE_ORE = registerKey("legacy_redstone_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_DIAMOND_ORE = registerKey("legacy_diamond_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_LAPIS_ORE = registerKey("legacy_lapis_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEGACY_COPPER_ORE = registerKey("legacy_copper_ore");

	//Dark Forest needs special placements, so here we go
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_MUSHGLOOMS = registerKey("dark_mushglooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_PUMPKINS = registerKey("dark_pumpkins");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_GRASS = registerKey("dark_grass");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FERNS = registerKey("dark_ferns");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_MUSHROOMS = registerKey("dark_mushrooms");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_DEAD_BUSHES = registerKey("dark_dead_bushes");

	//troll caves special stuff
	public static final ResourceKey<ConfiguredFeature<?, ?>> UBEROUS_SOIL_PATCH_BIG = registerKey("uberous_soil_patch_big");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_CAVE_MYCELIUM =  registerKey("troll_cave_mycelium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TROLL_CAVE_DIRT = registerKey("troll_cave_dirt");
	public static final ResourceKey<ConfiguredFeature<?, ?>> UBEROUS_SOIL_PATCH_SMALL = registerKey("uberous_soil_patch_small");

	//Trees!
	public static final ResourceKey<ConfiguredFeature<?, ?>> TWILIGHT_OAK_TREE = registerKey("tree/twilight_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_TWILIGHT_OAK_TREE = registerKey("tree/large_twilight_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SWAMPY_OAK_TREE = registerKey("tree/swampy_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_TREE = registerKey("tree/canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_CANOPY_TREE = registerKey("tree/firefly_canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_CANOPY_TREE = registerKey("tree/dead_canopy_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_TREE = registerKey("tree/mangrove_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_TREE = registerKey("tree/darkwood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOMEGROWN_DARKWOOD_TREE = registerKey("tree/homegrown_darkwood_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARKWOOD_LANTERN_TREE = registerKey("tree/darkwood_lantern_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TIME_TREE = registerKey("tree/time_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TRANSFORMATION_TREE = registerKey("tree/transformation_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MINING_TREE = registerKey("tree/mining_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SORTING_TREE = registerKey("tree/sorting_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_CANOPY_OAK_TREE = registerKey("tree/forest_canopy_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SAVANNAH_CANOPY_OAK_TREE = registerKey("tree/savannah_canopy_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOW_TREE = registerKey("tree/hollow_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RAINBOW_OAK_TREE = registerKey("tree/rainbow_oak");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_RAINBOW_OAK_TREE = registerKey("tree/large_rainbow_oak");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_CANOPY_MUSHROOM_TREE = registerKey("mushroom/brown_canopy_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_CANOPY_MUSHROOM_TREE = registerKey("mushroom/red_canopy_mushroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_SPRUCE_TREE = registerKey("tree/mega_spruce_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_WINTER_TREE = registerKey("tree/large_winter_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWY_SPRUCE_TREE = registerKey("tree/snowy_spruce_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_OAK_TREE = registerKey("tree/dark_forest_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_BIRCH_TREE = registerKey("tree/dark_forest_birch_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_OAK_TREE = registerKey("tree/vanilla_oak_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_BIRCH_TREE = registerKey("tree/vanilla_birch_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SMALLER_JUNGLE_TREE = registerKey("tree/smaller_jungle_tree");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DUMMY_TREE = registerKey("tree/dummy");

	//random selectors
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_TREES = registerKey("tree/selector/canopy_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DENSE_CANOPY_TREES = registerKey("tree/selector/dense_canopy_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> FIREFLY_FOREST_TREES = registerKey("tree/selector/firefly_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_TREES = registerKey("tree/selector/dark_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> HIGHLANDS_TREES = registerKey("tree/selector/highlands_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ENCHANTED_FOREST_TREES = registerKey("tree/selector/enchanted_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWY_FOREST_TREES = registerKey("tree/selector/snowy_forest_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_TF_TREES = registerKey("tree/selector/vanilla_trees");
	public static final ResourceKey<ConfiguredFeature<?, ?>> VANILLA_TF_BIG_MUSH = registerKey("tree/selector/vanilla/vanilla_mushrooms");

	public static final ResourceKey<ConfiguredFeature<?, ?>> WELL_PLACER = TFConfiguredFeatures.registerKey("well_placer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAMPPOST_PLACER = TFConfiguredFeatures.registerKey("lamppost_placer");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DEFAULT_FALLEN_LOGS = TFConfiguredFeatures.registerKey("default_fallen_logs");

	//super funky tree placement lists
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_MUSHROOMS_SPARSE = registerKey("mushroom/canopy_mushrooms_sparse");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CANOPY_MUSHROOMS_DENSE = registerKey("mushroom/canopy_mushrooms_dense");

	//ground decoration
	public static final RandomPatchConfiguration SMALL_FLOWER_CONFIG = (new RandomPatchConfiguration(32, 7, 7,
			PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
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
					)), BlockPredicate.ONLY_IN_AIR_PREDICATE)));

	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLACER = registerKey("flower_placer");

	public static final RandomPatchConfiguration SMALL_FLOWER_CONFIG_ALT = (new RandomPatchConfiguration(32, 7, 7,
			PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
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
					)), BlockPredicate.ONLY_IN_AIR_PREDICATE)));

	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLACER_ALT = registerKey("flower_placer_alt");

	//music!
	public static final Music TFMUSICTYPE = new Music(TFSounds.MUSIC, 1200, 12000, true);

	public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
		context.register(LAKE_LAVA, new ConfiguredFeature<>(Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.LAVA), BlockStateProvider.simple(Blocks.STONE))));
		context.register(LAKE_WATER, new ConfiguredFeature<>(Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER), BlockStateProvider.simple(Blocks.STONE))));

		registerTemplateFeatures(context);

		context.register(BIG_MUSHGLOOM, new ConfiguredFeature<>(TFFeatures.BIG_MUSHGLOOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(TFBlocks.HUGE_MUSHGLOOM_STEM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 1)));
		context.register(FALLEN_LEAVES, new ConfiguredFeature<>(TFFeatures.FALLEN_LEAVES.get(), FeatureConfiguration.NONE));
		context.register(MAYAPPLE, new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MAYAPPLE.get())))));
		context.register(FIDDLEHEAD, new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.FIDDLEHEAD.get())))));
		context.register(FIRE_JET, new ConfiguredFeature<>(TFFeatures.FIRE_JET.get(), new BlockStateConfiguration(TFBlocks.FIRE_JET.get().defaultBlockState())));
		context.register(FOUNDATION, new ConfiguredFeature<>(TFFeatures.FOUNDATION.get(), NoneFeatureConfiguration.NONE));
		context.register(GROVE_RUINS, new ConfiguredFeature<>(TFFeatures.GROVE_RUINS.get(), NoneFeatureConfiguration.NONE));
		context.register(HOLLOW_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_HOLLOW_LOG.get(), NoneFeatureConfiguration.NONE));
		context.register(HOLLOW_STUMP, new ConfiguredFeature<>(TFFeatures.HOLLOW_STUMP.get(), TreeConfigurations.HOLLOW_TREE));
		context.register(HUGE_LILY_PAD, new ConfiguredFeature<>(TFFeatures.HUGE_LILY_PAD.get(), NoneFeatureConfiguration.NONE));
		context.register(HUGE_WATER_LILY, new ConfiguredFeature<>(TFFeatures.HUGE_WATER_LILY.get(), NoneFeatureConfiguration.NONE));
		context.register(CICADA_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(TFBlocks.CICADA_JAR.get().defaultBlockState())));
		context.register(FIREFLY_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(TFBlocks.FIREFLY_JAR.get().defaultBlockState())));
		context.register(MONOLITH, new ConfiguredFeature<>(TFFeatures.MONOLITH.get(), NoneFeatureConfiguration.NONE));
		context.register(MUSHGLOOM_CLUSTER, new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MUSHGLOOM.get())))));
		context.register(MYCELIUM_BLOB, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.MYCELIUM), BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK), UniformInt.of(4, 6), 3)));
		context.register(OUTSIDE_STALAGMITE, new ConfiguredFeature<>(TFFeatures.CAVE_STALACTITE.get(), NoneFeatureConfiguration.NONE));
		context.register(PLANT_ROOTS, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(TFBlocks.ROOT_STRAND.get().defaultBlockState())));
		context.register(PUMPKIN_LAMPPOST, new ConfiguredFeature<>(TFFeatures.LAMPPOSTS.get(), new BlockStateConfiguration(Blocks.JACK_O_LANTERN.defaultBlockState())));
		context.register(SMOKER, new ConfiguredFeature<>(TFFeatures.FIRE_JET.get(), new BlockStateConfiguration(TFBlocks.SMOKER.get().defaultBlockState())));
		context.register(STONE_CIRCLE, new ConfiguredFeature<>(TFFeatures.STONE_CIRCLE.get(), NoneFeatureConfiguration.NONE));
		context.register(THORNS, new ConfiguredFeature<>(TFFeatures.THORNS.get(), new ThornsConfig(7, 3, 3, 50)));
		context.register(TORCH_BERRIES, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(TFBlocks.TORCHBERRY_PLANT.get().defaultBlockState().setValue(TorchberryPlantBlock.HAS_BERRIES, true))));
		context.register(TROLL_ROOTS, new ConfiguredFeature<>(TFFeatures.TROLL_VINES.get(), new BlockStateConfiguration(TFBlocks.TROLLVIDR.get().defaultBlockState())));
		context.register(VANILLA_ROOTS, new ConfiguredFeature<>(TFFeatures.UNDERGROUND_PLANTS.get(), new BlockStateConfiguration(Blocks.HANGING_ROOTS.defaultBlockState())));
		context.register(WEBS, new ConfiguredFeature<>(TFFeatures.WEBS.get(), NoneFeatureConfiguration.NONE));
		context.register(WOOD_ROOTS_SPREAD, new ConfiguredFeature<>(TFFeatures.WOOD_ROOTS.get(), new RootConfig(TreeDecorators.ROOT_BLEND_PROVIDER, BlockStateProvider.simple(TFBlocks.LIVEROOT_BLOCK.get()))));
		context.register(SNOW_UNDER_TREES, new ConfiguredFeature<>(TFFeatures.SNOW_UNDER_TREES.get(), NoneFeatureConfiguration.NONE));

		context.register(TF_OAK_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.TWILIGHT_OAK_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get().defaultBlockState())));
		context.register(CANOPY_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.CANOPY_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get().defaultBlockState())));
		context.register(MANGROVE_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(TFBlocks.MANGROVE_LOG.get().defaultBlockState(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get().defaultBlockState())));
		context.register(OAK_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.OAK_LOG.defaultBlockState(), TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get().defaultBlockState())));
		context.register(SPRUCE_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.SPRUCE_LOG.defaultBlockState(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get().defaultBlockState())));
		context.register(BIRCH_FALLEN_LOG, new ConfiguredFeature<>(TFFeatures.FALLEN_SMALL_LOG.get(), new HollowLogConfig(Blocks.BIRCH_LOG.defaultBlockState(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get().defaultBlockState())));

		context.register(SMALL_GRANITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.GRANITE.defaultBlockState(), 16)));
		context.register(SMALL_DIORITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.DIORITE.defaultBlockState(), 16)));
		context.register(SMALL_ANDESITE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.ANDESITE.defaultBlockState(), 16)));

		context.register(LEGACY_COAL_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.COAL_ORE.defaultBlockState(), 16)));
		context.register(LEGACY_IRON_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.IRON_ORE.defaultBlockState(), 9)));
		context.register(LEGACY_GOLD_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.GOLD_ORE.defaultBlockState(), 9)));
		context.register(LEGACY_REDSTONE_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.REDSTONE_ORE.defaultBlockState(), 8)));
		context.register(LEGACY_DIAMOND_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.DIAMOND_ORE.defaultBlockState(), 8)));
		context.register(LEGACY_LAPIS_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.LAPIS_ORE.defaultBlockState(), 7)));
		context.register(LEGACY_COPPER_ORE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), Blocks.COPPER_ORE.defaultBlockState(), 10)));

		context.register(DARK_MUSHGLOOMS, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(TFBlocks.MUSHGLOOM.get())), List.of(Blocks.GRASS_BLOCK), 50)));
		context.register(DARK_PUMPKINS, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN)), List.of(Blocks.GRASS_BLOCK), 50)));
		context.register(DARK_GRASS, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SHORT_GRASS)), List.of(Blocks.GRASS_BLOCK), 128)));
		context.register(DARK_FERNS, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FERN)), List.of(Blocks.GRASS_BLOCK), 128)));
		context.register(DARK_MUSHROOMS, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM)), List.of(Blocks.GRASS_BLOCK), 50)));
		context.register(DARK_DEAD_BUSHES, new ConfiguredFeature<>(TFFeatures.DARK_FOREST_PLACER.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DEAD_BUSH)), List.of(Blocks.GRASS_BLOCK), 50)));

		context.register(UBEROUS_SOIL_PATCH_BIG, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(TFBlocks.UBEROUS_SOIL.get()), BlockPredicate.matchesBlocks(Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.DIRT), UniformInt.of(4, 8), 1)));
		context.register(TROLL_CAVE_MYCELIUM, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.MYCELIUM), BlockPredicate.matchesBlocks(Blocks.STONE, TFBlocks.DEADROCK.get()), UniformInt.of(3, 5), 0)));
		context.register(TROLL_CAVE_DIRT, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(Blocks.DIRT), BlockPredicate.matchesBlocks(Blocks.STONE, TFBlocks.DEADROCK.get()), UniformInt.of(2, 5), 0)));
		context.register(UBEROUS_SOIL_PATCH_SMALL, new ConfiguredFeature<>(TFFeatures.MYCELIUM_BLOB.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(TFBlocks.UBEROUS_SOIL.get()), BlockPredicate.matchesBlocks(Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.DIRT), UniformInt.of(2, 3), 0)));

		context.register(TWILIGHT_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.TWILIGHT_OAK));
		context.register(LARGE_TWILIGHT_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.LARGE_TWILIGHT_OAK));
		context.register(SWAMPY_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.SWAMPY_OAK));
		context.register(CANOPY_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.CANOPY_TREE));
		context.register(FIREFLY_CANOPY_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.CANOPY_TREE_FIREFLY));
		context.register(DEAD_CANOPY_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.CANOPY_TREE_DEAD));
		context.register(MANGROVE_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.MANGROVE_TREE));
		context.register(DARKWOOD_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), TreeConfigurations.DARKWOOD_TREE));
		context.register(HOMEGROWN_DARKWOOD_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), TreeConfigurations.HOMEGROWN_DARKWOOD_TREE));
		context.register(DARKWOOD_LANTERN_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), TreeConfigurations.DARKWOOD_LANTERN_TREE));
		context.register(TIME_TREE, new ConfiguredFeature<>(TFFeatures.TREE_OF_TIME.get(), TreeConfigurations.TIME_TREE));
		context.register(TRANSFORMATION_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.TRANSFORM_TREE));
		context.register(MINING_TREE, new ConfiguredFeature<>(TFFeatures.MINERS_TREE.get(), TreeConfigurations.MINING_TREE));
		context.register(SORTING_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.SORT_TREE));
		context.register(FOREST_CANOPY_OAK_TREE, new ConfiguredFeature<>(TFFeatures.CANOPY_OAK.get(), TreeConfigurations.FOREST_CANOPY_OAK));
		context.register(SAVANNAH_CANOPY_OAK_TREE, new ConfiguredFeature<>(TFFeatures.CANOPY_OAK.get(), TreeConfigurations.SAVANNAH_CANOPY_OAK));
		context.register(HOLLOW_TREE, new ConfiguredFeature<>(TFFeatures.HOLLOW_TREE.get(), TreeConfigurations.HOLLOW_TREE));
		context.register(RAINBOW_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.RAINBOAK_TREE));
		context.register(LARGE_RAINBOW_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.LARGE_RAINBOAK_TREE));
		context.register(BROWN_CANOPY_MUSHROOM_TREE, new ConfiguredFeature<>(TFFeatures.CANOPY_BROWN_MUSHROOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 3)));
		context.register(RED_CANOPY_MUSHROOM_TREE, new ConfiguredFeature<>(TFFeatures.CANOPY_RED_MUSHROOM.get(), new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.TRUE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, Boolean.FALSE).setValue(HugeMushroomBlock.DOWN, Boolean.FALSE)), 3)));
		context.register(MEGA_SPRUCE_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.BIG_SPRUCE));
		context.register(LARGE_WINTER_TREE, new ConfiguredFeature<>(TFFeatures.LARGE_WINTER_TREE.get(), TreeConfigurations.LARGE_WINTER));
		context.register(SNOWY_SPRUCE_TREE, new ConfiguredFeature<>(TFFeatures.SNOW_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(5, 2, 1), BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)), new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build()));
		context.register(DARK_FOREST_OAK_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.OAK_LOG), new StraightTrunkPlacer(4, 2, 0), BlockStateProvider.simple(Blocks.OAK_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
		context.register(DARK_FOREST_BIRCH_TREE, new ConfiguredFeature<>(TFFeatures.DARK_CANOPY_TREE.get(), new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.BIRCH_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(Blocks.BIRCH_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
		context.register(VANILLA_OAK_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.OAK_LOG), new StraightTrunkPlacer(4, 2, 0), BlockStateProvider.simple(Blocks.OAK_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
		context.register(VANILLA_BIRCH_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.BIRCH_LOG), new StraightTrunkPlacer(5, 2, 0), BlockStateProvider.simple(Blocks.BIRCH_LEAVES), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build()));
		context.register(SMALLER_JUNGLE_TREE, new ConfiguredFeature<>(Feature.TREE, TreeConfigurations.SMALL_JUNGLE));
		context.register(DUMMY_TREE, new ConfiguredFeature<>(Feature.NO_OP, NoneFeatureConfiguration.INSTANCE));

		context.register(WELL_PLACER, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(FANCY_WELL)), 0.05F)), PlacementUtils.inlinePlaced(features.getOrThrow(SIMPLE_WELL)))));
		context.register(LAMPPOST_PLACER, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CICADA_LAMPPOST)), 0.1F)), PlacementUtils.inlinePlaced(features.getOrThrow(FIREFLY_LAMPPOST)))));
		context.register(DEFAULT_FALLEN_LOGS, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(BIRCH_FALLEN_LOG)), 0.1F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(OAK_FALLEN_LOG)), 0.2F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_FALLEN_LOG)), 0.4F)), PlacementUtils.inlinePlaced(features.getOrThrow(TF_OAK_FALLEN_LOG)))));

		context.register(CANOPY_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_TREE)), 0.6F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)))));
		context.register(DENSE_CANOPY_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_TREE)), 0.7F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)))));
		context.register(FIREFLY_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(CANOPY_TREE)), 0.33F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(FIREFLY_CANOPY_TREE)), 0.45F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)))));
		context.register(DARK_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(DARK_FOREST_BIRCH_TREE)), 0.2F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(DARK_FOREST_OAK_TREE)), 0.2F)), PlacementUtils.inlinePlaced(features.getOrThrow(DARKWOOD_TREE)))));
		context.register(HIGHLANDS_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(VANILLA_BIRCH_TREE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.SPRUCE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.PINE)), 0.1F)), PlacementUtils.inlinePlaced(features.getOrThrow(MEGA_SPRUCE_TREE)))));
		context.register(ENCHANTED_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(VANILLA_OAK_TREE)), 0.15F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(VANILLA_BIRCH_TREE)), 0.15F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(LARGE_RAINBOW_OAK_TREE)), 0.1F)), PlacementUtils.inlinePlaced(features.getOrThrow(RAINBOW_OAK_TREE)))));
		context.register(SNOWY_FOREST_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(MEGA_SPRUCE_TREE)), 0.1F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(LARGE_WINTER_TREE)), 0.01F)), PlacementUtils.inlinePlaced(features.getOrThrow(SNOWY_SPRUCE_TREE)))));
		context.register(VANILLA_TF_TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(VANILLA_BIRCH_TREE)), 0.25F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(VANILLA_OAK_TREE)), 0.25F)), PlacementUtils.inlinePlaced(features.getOrThrow(TWILIGHT_OAK_TREE)))));
		context.register(VANILLA_TF_BIG_MUSH, new ConfiguredFeature<>(Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)))));

		context.register(CANOPY_MUSHROOMS_SPARSE, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(BROWN_CANOPY_MUSHROOM_TREE)), 0.15f), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(RED_CANOPY_MUSHROOM_TREE)), 0.05f)), PlacementUtils.inlinePlaced(features.getOrThrow(DUMMY_TREE)))));
		context.register(CANOPY_MUSHROOMS_DENSE, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(BROWN_CANOPY_MUSHROOM_TREE)), 0.675f), new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(RED_CANOPY_MUSHROOM_TREE)), 0.225f)), PlacementUtils.inlinePlaced(features.getOrThrow(DUMMY_TREE)))));
		context.register(FLOWER_PLACER, new ConfiguredFeature<>(Feature.FLOWER, SMALL_FLOWER_CONFIG));
		context.register(FLOWER_PLACER_ALT, new ConfiguredFeature<>(Feature.FLOWER, SMALL_FLOWER_CONFIG_ALT));
	}

	private static void registerTemplateFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
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

		SwizzleConfig simpleWellConfig = SwizzleConfig.generate(paletteHolders, CustomTagGenerator.WoodPaletteTagGenerator.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall);
		context.register(SIMPLE_WELL, new ConfiguredFeature<>(TFFeatures.SIMPLE_WELL.get(), simpleWellConfig));

		SwizzleConfig fancyWellConfig = SwizzleConfig.generate(paletteHolders, CustomTagGenerator.WoodPaletteTagGenerator.WELL_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
		context.register(FANCY_WELL, new ConfiguredFeature<>(TFFeatures.FANCY_WELL.get(), fancyWellConfig));

		SwizzleConfig hutConfig = SwizzleConfig.generate(paletteHolders, CustomTagGenerator.WoodPaletteTagGenerator.DRUID_HUT_SWIZZLE_MASK, paletteChoices, processorCobbleBlock, processorCobbleStair, processorCobbleSlab, processorCobbleWall, processorStoneBrickBlock, processorStoneBrickStair, processorStoneBrickSlab, processorStoneBrickWall);
		context.register(DRUID_HUT, new ConfiguredFeature<>(TFFeatures.DRUID_HUT.get(), hutConfig));

		context.register(GRAVEYARD, new ConfiguredFeature<>(TFFeatures.GRAVEYARD.get(), FeatureConfiguration.NONE));
	}
}