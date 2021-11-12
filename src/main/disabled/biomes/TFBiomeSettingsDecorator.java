import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BushFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.OptionalInt;
import java.util.Random;

// Duplicated TFBiomeDecorator, but with BiomeGenerationSettings.Builder

public class TFBiomeDecorator {
	//TODO: Should some of these be grouped into a sort of "decorate per biome" method, or keep them separate?
	//FIXME: Not all values are 100% accurate
	//TODO: Somehow make it so that Features may not generate if a major Structure exists. Ties into first note?

	//shush, I want random
	public static Random random = new Random();

	public static TFConfig.Common.Dimension.WorldGenWeights weights = TFConfig.COMMON_CONFIG.DIMENSION.worldGenWeights;

	//Blockstates
	public static final BlockState WATER = Blocks.WATER.getDefaultState();
	public static final BlockState LAVA = Blocks.LAVA.getDefaultState();
	public static final BlockState SPRUCE_LOG = Blocks.SPRUCE_LOG.getDefaultState();
	public static final BlockState SPRUCE_WOOD = Blocks.SPRUCE_WOOD.getDefaultState();
	public static final BlockState SPRUCE_LEAVES = Blocks.SPRUCE_LEAVES.getDefaultState();
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
	public static final BlockState GRASS = Blocks.GRASS.getDefaultState();
	public static final BlockState FERN = Blocks.FERN.getDefaultState();
	public static final BlockState MAYAPPLE = TFBlocks.mayapple.get().getDefaultState();
	public static final BlockState FIDDLEHEAD = TFBlocks.fiddlehead.get().getDefaultState();
	public static final BlockState MUSHGLOOM = TFBlocks.mushgloom.get().getDefaultState();
	public static final BlockState DEAD_BUSH = Blocks.DEAD_BUSH.getDefaultState();
	public static final BlockState FOREST_GRASS = Blocks.GRASS.getDefaultState();
	public static final BlockState FIRE_JET = TFBlocks.fire_jet.get().getDefaultState();
	public static final BlockState SMOKER = TFBlocks.smoker.get().getDefaultState();

	//Configs
	public static final OreFeatureConfig DIRT_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.DIRT.getDefaultState(), 33));
	public static final OreFeatureConfig GRAVEL_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.GRAVEL.getDefaultState(), 33));
	public static final OreFeatureConfig GRANITE_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.GRANITE.getDefaultState(), 33));
	public static final OreFeatureConfig DIORITE_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.DIORITE.getDefaultState(), 33));
	public static final OreFeatureConfig ANDESITE_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.ANDESITE.getDefaultState(), 33));
	public static final OreFeatureConfig COAL_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.COAL_ORE.getDefaultState(), 17));
	public static final OreFeatureConfig IRON_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.IRON_ORE.getDefaultState(), 9));
	public static final OreFeatureConfig GOLD_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.GOLD_ORE.getDefaultState(), 9));
	public static final OreFeatureConfig REDSTONE_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.REDSTONE_ORE.getDefaultState(), 8));
	public static final OreFeatureConfig DIAMOND_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.DIAMOND_ORE.getDefaultState(), 8));
	public static final OreFeatureConfig LAPIS_ORE_CONFIG = (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.LAPIS_ORE.getDefaultState(), 7));
	public static final CaveStalactiteConfig OUTSIDE_STALAG_CONFIG = (new CaveStalactiteConfig(Blocks.STONE.getDefaultState(), 1.0F, -1, -1, false));
	public static final BaseTreeFeatureConfig OAK_TREE = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1)))/*.setSapling(TFBlocks.oak_sapling.get())*/.build();
	public static final TFTreeFeatureConfig CANOPY_TREE_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(CANOPY_LOG), new SimpleBlockStateProvider(CANOPY_LEAVES), new SimpleBlockStateProvider(CANOPY_WOOD), new SimpleBlockStateProvider(ROOTS)).chanceFirstFive(3).chanceSecondFive(8).setSapling(TFBlocks.canopy_sapling.get())).build();
	public static final TFTreeFeatureConfig CANOPY_TREE_DEAD_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(CANOPY_LOG), new SimpleBlockStateProvider(CANOPY_LEAVES), new SimpleBlockStateProvider(CANOPY_WOOD), new SimpleBlockStateProvider(ROOTS)).chanceFirstFive(3).chanceSecondFive(8).noLeaves().setSapling(TFBlocks.canopy_sapling.get())).build();
	public static final TFTreeFeatureConfig CANOPY_OAK_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new SimpleBlockStateProvider(OAK_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.oak_sapling.get())).build();
	public static final TFTreeFeatureConfig MANGROVE_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(MANGROVE_LOG), new SimpleBlockStateProvider(MANGROVE_LEAVES), new SimpleBlockStateProvider(MANGROVE_WOOD), new SimpleBlockStateProvider(ROOTS)).checksWater().setSapling(TFBlocks.mangrove_sapling.get())).build();
	public static final TFTreeFeatureConfig MANGROVE_TREE_NO_WATER = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(MANGROVE_LOG), new SimpleBlockStateProvider(MANGROVE_LEAVES), new SimpleBlockStateProvider(MANGROVE_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.mangrove_sapling.get())).build();
	public static final TFTreeFeatureConfig DARK_OAK_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(DARKWOOD_LOG), new SimpleBlockStateProvider(DARKWOOD_LEAVES), new SimpleBlockStateProvider(DARKWOOD_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.darkwood_sapling.get())).build();
	public static final TFTreeFeatureConfig TIME_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(TIME_LOG), new SimpleBlockStateProvider(TIME_LEAVES), new SimpleBlockStateProvider(TIME_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.time_sapling.get())).build();
	public static final TFTreeFeatureConfig TRANSFORM_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(TRANSFORM_LOG), new SimpleBlockStateProvider(TRANSFORM_LEAVES), new SimpleBlockStateProvider(TRANSFORM_WOOD), new SimpleBlockStateProvider(ROOTS)).minHeight(11).chanceFirstFive(Integer.MAX_VALUE).chanceSecondFive(Integer.MAX_VALUE).setSapling(TFBlocks.transformation_sapling.get())).build();
	public static final TFTreeFeatureConfig MINING_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(MINING_LOG), new SimpleBlockStateProvider(MINING_LEAVES), new SimpleBlockStateProvider(MINING_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.mining_sapling.get())).build();
	public static final TFTreeFeatureConfig SORT_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SORT_LOG), new SimpleBlockStateProvider(SORT_LEAVES), new SimpleBlockStateProvider(SORT_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.sorting_sapling.get())).build();
	public static final TFTreeFeatureConfig HOLLOW_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new SimpleBlockStateProvider(OAK_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.hollow_oak_sapling.get())).build();
	public static final TFTreeFeatureConfig WINTER_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SPRUCE_LOG), new SimpleBlockStateProvider(SPRUCE_LEAVES), new SimpleBlockStateProvider(SPRUCE_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling((SaplingBlock)Blocks.SPRUCE_SAPLING)).build();
	public static final BaseTreeFeatureConfig RAINBOAK_TREE = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(RAINBOW_LEAVES), new BlobFoliagePlacer(2, 0, 0, 0, 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1)))/*.setSapling(TFBlocks.rainboak_sapling.get())*/.build();
	public static final BaseTreeFeatureConfig FANCY_RAINBOAK_TREE = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(RAINBOW_LEAVES), new FancyFoliagePlacer(2, 0, 4, 0, 4), new FancyTrunkPlacer(3, 11, 0), new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))))/*.setSapling(TFBlocks.rainboak_sapling.get())*/.build();
	public static final BaseTreeFeatureConfig DARK_FOREST_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayerFeature(0, 0, 0))).build();
	public static final BaseTreeFeatureConfig OAK_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new BushFoliagePlacer(2, 0, 1, 0, 2), new StraightTrunkPlacer(1, 0, 0), new TwoLayerFeature(0, 0, 0))).build();
	public static final BlockClusterFeatureConfig MUSHGLOOM_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MUSHGLOOM), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig DEAD_BUSH_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(DEAD_BUSH), new SimpleBlockPlacer())).tries(8).build();
	public static final BlockClusterFeatureConfig FOREST_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(FOREST_GRASS), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig TWILIGHT_VARIANT_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(GRASS, 1).addWeightedBlockstate(MAYAPPLE, 3).addWeightedBlockstate(FERN, 2), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig SAVANNAH_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(GRASS, 1).addWeightedBlockstate(MAYAPPLE, 10).addWeightedBlockstate(FERN, 10), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig SWAMP_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(GRASS, 1).addWeightedBlockstate(MAYAPPLE, 4).addWeightedBlockstate(FERN, 4), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig ENCHANTED_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addWeightedBlockstate(GRASS, 1).addWeightedBlockstate(FIDDLEHEAD, 3).addWeightedBlockstate(FERN, 2), new SimpleBlockPlacer())).tries(64).build();
	public static final MultipleRandomFeatureConfig NORMAL_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.2F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig VARIANT_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(OAK_BUSH_CONFIG).withChance(0.2F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig DARK_FOREST_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DARK_FOREST_BUSH_CONFIG).withChance(0.2F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.25F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SWAMP_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DARK_FOREST_BUSH_CONFIG).withChance(0.35F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.SWAMP_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig GLACIER_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.35F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig HIGHLANDS_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG).withChance(0.25F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.1F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.MEGA_SPRUCE_TREE_CONFIG).withChance(0.35F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.MEGA_PINE_TREE_CONFIG).withChance(0.07F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.BIRCH_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SAVANNAH_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SNOWY_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.35F), TFBiomeFeatures.LARGE_WINTER_TREE.get().withConfiguration(WINTER_TREE).withChance(0.125F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig ENCHANTED_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.field_236291_c_.withConfiguration(RAINBOAK_TREE).withChance(0.06F), Feature.field_236291_c_.withConfiguration(FANCY_RAINBOAK_TREE).withChance(0.02F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.2F), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.field_236291_c_.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG)));

	//TODO: These are using floats of a weighted config. THIS WILL BREAK GENERATION CHANCES. THIS NEEDS TO BE A FLOAT CHANCE. REMOVE floatValue() ON CHANGE
	public static final MultipleRandomFeatureConfig RUINS_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(
			TFBiomeFeatures.STONE_CIRCLE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.stoneCircleWeight.get().floatValue()),
			TFBiomeFeatures.WELL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.wellWeight.get().floatValue()),
			TFBiomeFeatures.OUTSIDE_STALAGMITE.get().withConfiguration(OUTSIDE_STALAG_CONFIG).withChance(weights.stalagmiteWeight.get().floatValue()),
			TFBiomeFeatures.FOUNDATION.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.foundationWeight.get().floatValue()),
			TFBiomeFeatures.MONOLITH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.monolithWeight.get().floatValue()),
			TFBiomeFeatures.GROVE_RUINS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.groveRuinsWeight.get().floatValue()),
			TFBiomeFeatures.HOLLOW_STUMP.get().withConfiguration(HOLLOW_TREE).withChance(weights.hollowStumpWeight.get().floatValue()),
			TFBiomeFeatures.FALLEN_HOLLOW_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.fallenHollowLogWeight.get().floatValue()),
			TFBiomeFeatures.FALLEN_SMALL_LOG.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.fallenSmallLogWeight.get().floatValue()),
			TFBiomeFeatures.DRUID_HUT.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.druidHutWeight.get().floatValue())
	), Feature.NO_OP.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)));

	//Carvers and terrain modification
	public static void addCarvers(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(TFBiomeFeatures.TF_CAVES.get(), new ProbabilityConfig(0.15F)));
	}
	public static void addLakes(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.withConfiguration(new BlockStateFeatureConfig(WATER)).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(4))));
		biome.func_242513_a(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.withConfiguration(new BlockStateFeatureConfig(LAVA)).withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(320))));
	}

	public static void addExtraPoolsWater(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.withConfiguration(new BlockStateFeatureConfig(WATER)).withPlacement(Placement.WATER_LAKE.configure(new ChanceConfig(count))));
	}

	public static void addExtraPoolsLava(BiomeGenerationSettings.Builder biome, float chance) {
		biome.func_242513_a(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.withConfiguration(new BlockStateFeatureConfig(LAVA)).withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(40))));
	}

	public static void addSprings(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(DefaultBiomeFeatures.WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(50, 8, 8, 256))));
	}

	//Ores
	public static void addWoodRoots(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, TFBiomeFeatures.WOOD_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(20))));
	}

	public static void addOres(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(DIRT_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 256))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(GRAVEL_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(GRANITE_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(DIORITE_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(ANDESITE_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(COAL_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 128))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(IRON_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(GOLD_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(REDSTONE_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 16))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(DIAMOND_ORE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(LAPIS_ORE_CONFIG).withPlacement(Placement.COUNT_DEPTH_AVERAGE.configure(new DepthAverageConfig(1, 16, 16))));
	}

	//Canopies, trees, and anything resembling a forest thing
	public static void addCanopy(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().withConfiguration(CANOPY_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
	}

	public static void addCanopyDead(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().withConfiguration(CANOPY_TREE_DEAD_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
	}

	public static void addCanopyAlt(BiomeGenerationSettings.Builder biome, float chance) {
		// FIXME the chance here is being used to conditionally register the decorator
		if (random.nextFloat() < chance) {
			biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_MUSHROOM.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
		} else {
			biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().withConfiguration(CANOPY_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
		}
	}

	public static void addCanopySavannah(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_OAK.get().withConfiguration(CANOPY_OAK_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
	}

	public static void addMultipleTrees(BiomeGenerationSettings.Builder biome, MultipleRandomFeatureConfig trees, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.withConfiguration(trees).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, 0.1F, 1))));
	}

	public static void addHugeMushrooms(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.withConfiguration(new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_RED_MUSHROOM), Feature.HUGE_BROWN_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_BROWN_MUSHROOM))).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(count))));
	}

	public static void addMangroves(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.MANGROVE_TREE.get().withConfiguration(MANGROVE_TREE).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, 0.1F, 1))));
	}

	public static void addDarkTrees(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.DARK_CANOPY_TREE.get().withConfiguration(DARK_OAK_TREE).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(5, 0.5F, 1))));
	}

	//Grass and plants
	public static void addPlantRoots(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.PLANT_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(12))));
	}

	public static void addGrass(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addGrassWithFern(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LUSH_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addFernWithGrass(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.TAIGA_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTwilightGrass(BiomeGenerationSettings.Builder biome, BlockClusterFeatureConfig config, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(config).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTallGrass(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.TALL_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addTallFerns(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LARGE_FERN_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addFlowers(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.DEFAULT_FLOWER_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addFlowersMore(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(DefaultBiomeFeatures.FOREST_FLOWER_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(8))));
	}

	public static void addTallFlowers(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_RANDOM_SELECTOR.withConfiguration(new MultipleWithChanceRandomFeatureConfig(ImmutableList.of(Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILAC_CONFIG), Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.ROSE_BUSH_CONFIG), Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.PEONY_CONFIG)), 0)).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2))));
	}

	public static void addHugeLilyPads(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.HUGE_LILY_PAD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
	}

	public static void addHugeWaterLiles(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.HUGE_WATER_LILY.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
	}

	public static void addMushgloom(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MUSHGLOOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addPumpkins(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.PUMPKIN_PATCH_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addMushrooms(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(8))));
	}

	public static void addMushroomsSometimes(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(6))));
	}

	public static void addMushroomsCommon(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(1))));
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));
	}

	public static void addMushroomsDark(BiomeGenerationSettings.Builder biome) {
		// FIXME the random here is being used to conditionally register the decorator
		if (random.nextInt(8) == 0)
			biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
		if (random.nextInt(16) == 0)
			biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
		if (random.nextInt(24) == 0)
			biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MUSHGLOOM_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
	}

	public static void addVines(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.VINES.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHT_64.configure(new FrequencyConfig(count))));
	}

	public static void addDeadBushes(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DEAD_BUSH_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addReeds(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.SUGAR_CANE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addWaterlilies(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILY_PAD_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTorchberries(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.TORCH_BERRIES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(3))));
	}

	public static void addForestGrass(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(FOREST_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(count))));
	}

	//Other Features
	public static void addRuins(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.RANDOM_SELECTOR.withConfiguration(RUINS_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(6))));
	}

	public static void addHangingLamps(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.HANGING_LAMPS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addLamppost(BiomeGenerationSettings.Builder biome, BlockState state, int chance) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.LAMPPOSTS.get().withConfiguration(new BlockStateFeatureConfig(state)).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(chance))));
	}

	public static void addPenguins(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.PENGUINS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(4))));
	}

	public static void addTrollRoots(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_DECORATION, TFBiomeFeatures.TROLL_ROOTS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(24))));
	}

	public static void addWebs(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.WEBS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addFallenLeaves(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FALLEN_LEAVES.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addGraveyards(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.GRAVEYARD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(16))));
	}

	public static void addThorns(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.THORNS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(64))));
	}

	public static void addMyceliumBlobs(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(Blocks.MYCELIUM.getDefaultState(), 5, 1, Lists.newArrayList(Blocks.GRASS_BLOCK.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(count))));
	}

	public static void addJetsAndSmokers(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FIRE_JET.get().withConfiguration(new BlockStateFeatureConfig(FIRE_JET)).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
		biome.func_242513_a(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FIRE_JET.get().withConfiguration(new BlockStateFeatureConfig(SMOKER)).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(4))));
	}

	public static void addMossyBoulders(BiomeGenerationSettings.Builder biome) {
		biome.func_242513_a(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK.withConfiguration(new BlockBlobConfig(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0)).withPlacement(Placement.FOREST_ROCK.configure(new FrequencyConfig(2))));
	}

	public static void addClayDisks(BiomeGenerationSettings.Builder biome, int count) {
		biome.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(count))));
	}
}
