package twilightforest.biomes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.IPlantable;
import twilightforest.TFConfig;
import twilightforest.block.TFBlocks;
import twilightforest.world.feature.*;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;

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
	public static final BlockState DEAD_BUSH = TFBlocks.dead_bush.get().getDefaultState();
	public static final BlockState FOREST_GRASS = TFBlocks.forest_grass.get().getDefaultState();
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
	public static final TreeFeatureConfig OAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).setSapling(TFBlocks.oak_sapling.get()).build();
	public static final TFTreeFeatureConfig CANOPY_TREE_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(CANOPY_LOG), new SimpleBlockStateProvider(CANOPY_LEAVES), new SimpleBlockStateProvider(CANOPY_WOOD), new SimpleBlockStateProvider(ROOTS)).chanceFirstFive(3).chanceSecondFive(8).hasLeaves().setSapling(TFBlocks.canopy_sapling.get())).build();
	public static final TFTreeFeatureConfig CANOPY_TREE_DEAD_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(CANOPY_LOG), new SimpleBlockStateProvider(CANOPY_LEAVES), new SimpleBlockStateProvider(CANOPY_WOOD), new SimpleBlockStateProvider(ROOTS)).chanceFirstFive(3).chanceSecondFive(8).setSapling(TFBlocks.canopy_sapling.get())).build();
	public static final TFTreeFeatureConfig CANOPY_OAK_CONFIG = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new SimpleBlockStateProvider(OAK_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.oak_sapling.get())).build();
	public static final TFTreeFeatureConfig MANGROVE_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(MANGROVE_LOG), new SimpleBlockStateProvider(MANGROVE_WOOD), new SimpleBlockStateProvider(MANGROVE_LEAVES), new SimpleBlockStateProvider(ROOTS)).checksWater(true).setSapling(TFBlocks.mangrove_sapling.get())).build();
	public static final TFTreeFeatureConfig DARK_OAK_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(DARKWOOD_LOG), new SimpleBlockStateProvider(DARKWOOD_LEAVES), new SimpleBlockStateProvider(DARKWOOD_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.darkwood_sapling.get())).build();
	public static final TFTreeFeatureConfig TIME_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(TIME_LOG), new SimpleBlockStateProvider(TIME_LEAVES), new SimpleBlockStateProvider(TIME_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.time_sapling.get())).build();
	public static final TFTreeFeatureConfig TRANSFORM_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(TRANSFORM_LOG), new SimpleBlockStateProvider(TRANSFORM_LEAVES), new SimpleBlockStateProvider(TRANSFORM_WOOD), new SimpleBlockStateProvider(ROOTS)).baseHeight(11).chanceFirstFive(Integer.MAX_VALUE).chanceSecondFive(Integer.MAX_VALUE).hasLeaves().setSapling(TFBlocks.transformation_sapling.get())).build();
	public static final TFTreeFeatureConfig MINING_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(MINING_LOG), new SimpleBlockStateProvider(MINING_LEAVES), new SimpleBlockStateProvider(MINING_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.mining_sapling.get())).build();
	public static final TFTreeFeatureConfig SORT_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SORT_LOG), new SimpleBlockStateProvider(SORT_LEAVES), new SimpleBlockStateProvider(SORT_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.sorting_sapling.get())).build();
	public static final TFTreeFeatureConfig HOLLOW_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(OAK_LEAVES), new SimpleBlockStateProvider(OAK_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling(TFBlocks.hollow_oak_sapling.get())).build();
	public static final TFTreeFeatureConfig WINTER_TREE = (new TFTreeFeatureConfig.Builder(new SimpleBlockStateProvider(SPRUCE_LOG), new SimpleBlockStateProvider(SPRUCE_LEAVES), new SimpleBlockStateProvider(SPRUCE_WOOD), new SimpleBlockStateProvider(ROOTS)).setSapling((IPlantable)Blocks.SPRUCE_SAPLING)).build();
	public static final TreeFeatureConfig RAINBOAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(RAINBOW_LEAVES), new BlobFoliagePlacer(2, 0))).baseHeight(4).heightRandA(2).foliageHeight(3).setSapling(TFBlocks.rainboak_sapling.get()).build();
	public static final TreeFeatureConfig FANCY_RAINBOAK_TREE = (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(OAK_LOG), new SimpleBlockStateProvider(RAINBOW_LEAVES), new BlobFoliagePlacer(0, 0))).setSapling(TFBlocks.rainboak_sapling.get()).build();
	public static final BaseTreeFeatureConfig DARK_FOREST_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()))).build();
	public static final BaseTreeFeatureConfig OAK_BUSH_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()))).build();
	public static final BlockClusterFeatureConfig MUSHGLOOM_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MUSHGLOOM), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig DEAD_BUSH_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(DEAD_BUSH), new SimpleBlockPlacer())).tries(8).build();
	public static final BlockClusterFeatureConfig FOREST_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(FOREST_GRASS), new SimpleBlockPlacer())).tries(32).build();
	public static final BlockClusterFeatureConfig TWILIGHT_VARIANT_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(GRASS, 1).addState(MAYAPPLE, 3).addState(FERN, 2), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig SAVANNAH_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(GRASS, 1).addState(MAYAPPLE, 10).addState(FERN, 10), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig SWAMP_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(GRASS, 1).addState(MAYAPPLE, 4).addState(FERN, 4), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig ENCHANTED_GRASS_CONFIG = (new BlockClusterFeatureConfig.Builder((new WeightedBlockStateProvider()).addState(GRASS, 1).addState(FIDDLEHEAD, 3).addState(FERN, 2), new SimpleBlockPlacer())).tries(64).build();
	public static final MultipleRandomFeatureConfig NORMAL_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.2F), Feature.FANCY_TREE.configure(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig VARIANT_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.JUNGLE_GROUND_BUSH.configure(OAK_BUSH_CONFIG).withChance(0.2F), Feature.FANCY_TREE.configure(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig DARK_FOREST_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.JUNGLE_GROUND_BUSH.configure(DARK_FOREST_BUSH_CONFIG).withChance(0.2F), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.25F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SWAMP_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.JUNGLE_GROUND_BUSH.configure(DARK_FOREST_BUSH_CONFIG).withChance(0.35F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.SWAMP_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig GLACIER_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.35F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig HIGHLANDS_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG).withChance(0.25F), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.1F), Feature.MEGA_SPRUCE_TREE.configure(DefaultBiomeFeatures.MEGA_SPRUCE_TREE_CONFIG).withChance(0.35F), Feature.MEGA_SPRUCE_TREE.configure(DefaultBiomeFeatures.MEGA_PINE_TREE_CONFIG).withChance(0.07F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.BIRCH_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SAVANNAH_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.FANCY_TREE.configure(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig SNOWY_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.PINE_TREE_CONFIG).withChance(0.35F), TFBiomeFeatures.LARGE_WINTER_TREE.get().configure(WINTER_TREE).withChance(0.125F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG)));
	public static final MultipleRandomFeatureConfig ENCHANTED_TREES_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.configure(RAINBOAK_TREE).withChance(0.06F), Feature.FANCY_TREE.configure(FANCY_RAINBOAK_TREE).withChance(0.02F), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.BIRCH_TREE_CONFIG).withChance(0.2F), Feature.FANCY_TREE.configure(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withChance(0.1F)), Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG)));

	//TODO: These are using floats of a weighted config. THIS WILL BREAK GENERATION CHANCES. THIS NEEDS TO BE A FLOAT CHANCE. REMOVE floatValue() ON CHANGE
	public static final MultipleRandomFeatureConfig RUINS_CONFIG = (new MultipleRandomFeatureConfig(ImmutableList.of(
			TFBiomeFeatures.STONE_CIRCLE.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.stoneCircleWeight.get().floatValue()),
			TFBiomeFeatures.WELL.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.wellWeight.get().floatValue()),
			TFBiomeFeatures.OUTSIDE_STALAGMITE.get().configure(OUTSIDE_STALAG_CONFIG).withChance(weights.stalagmiteWeight.get().floatValue()),
			TFBiomeFeatures.FOUNDATION.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.foundationWeight.get().floatValue()),
			TFBiomeFeatures.MONOLITH.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.monolithWeight.get().floatValue()),
			TFBiomeFeatures.GROVE_RUINS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.groveRuinsWeight.get().floatValue()),
			TFBiomeFeatures.HOLLOW_STUMP.get().configure(HOLLOW_TREE).withChance(weights.hollowStumpWeight.get().floatValue()),
			TFBiomeFeatures.FALLEN_HOLLOW_LOG.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.fallenHollowLogWeight.get().floatValue()),
			TFBiomeFeatures.FALLEN_SMALL_LOG.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.fallenSmallLogWeight.get().floatValue()),
			TFBiomeFeatures.DRUID_HUT.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).withChance(weights.druidHutWeight.get().floatValue())
	), Feature.field_227245_q_.configure(IFeatureConfig.NO_FEATURE_CONFIG)));

	//Carvers and terrain modification
	public static void addLakes(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.configure(new BlockStateFeatureConfig(WATER)).createDecoratedFeature(Placement.WATER_LAKE.configure(new ChanceConfig(4))));
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.configure(new BlockStateFeatureConfig(LAVA)).createDecoratedFeature(Placement.LAVA_LAKE.configure(new ChanceConfig(320))));
	}

	public static void addExtraPoolsWater(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.configure(new BlockStateFeatureConfig(WATER)).createDecoratedFeature(Placement.WATER_LAKE.configure(new ChanceConfig(count))));
	}

	public static void addExtraPoolsLava(Biome biome, float chance) {
		if (random.nextFloat() < chance)
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.LAKE.configure(new BlockStateFeatureConfig(LAVA)).createDecoratedFeature(Placement.LAVA_LAKE.configure(new ChanceConfig(1))));
	}

	public static void addSprings(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(DefaultBiomeFeatures.WATER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(50, 8, 8, 256))));
	}

	//Ores
	public static void addWoodRoots(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, TFBiomeFeatures.WOOD_ROOTS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(20))));
	}

	public static void addOres(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(DIRT_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(GRAVEL_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(GRANITE_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(DIORITE_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(ANDESITE_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(10, 0, 0, 80))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(COAL_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 128))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(IRON_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(GOLD_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(REDSTONE_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 16))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(DIAMOND_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 16))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configure(LAPIS_ORE_CONFIG).createDecoratedFeature(Placement.COUNT_DEPTH_AVERAGE.configure(new DepthAverageConfig(1, 16, 16))));
	}

	//Canopies, trees, and anything resembling a forest thing
	public static void addCanopy(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().configure(CANOPY_TREE_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
	}

	public static void addCanopyDead(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().configure(CANOPY_TREE_DEAD_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
	}

	public static void addCanopyAlt(Biome biome, float chance) {
		if (random.nextFloat() < chance) {
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_MUSHROOM.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
		} else {
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_TREE.get().configure(CANOPY_TREE_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
		}
	}

	public static void addCanopySavannah(Biome biome) {
		if (random.nextFloat() > 0.8F) {
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.CANOPY_OAK.get().configure(CANOPY_OAK_CONFIG).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(TFConfig.COMMON_CONFIG.PERFORMANCE.canopyCoverage.get().intValue(), 0.1F, 1))));
		}
	}

	public static void addMultipleTrees(Biome biome, MultipleRandomFeatureConfig trees, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(trees).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, 0.1F, 1))));
	}

	public static void addHugeMushrooms(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.configure(new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM.configure(DefaultBiomeFeatures.HUGE_RED_MUSHROOM_CONFIG), Feature.HUGE_BROWN_MUSHROOM.configure(DefaultBiomeFeatures.HUGE_BROWN_MUSHROOM_CONFIG))).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(count))));
	}

	public static void addMangroves(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.MANGROVE_TREE.get().configure(MANGROVE_TREE).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(count, 0.1F, 1))));
	}

	public static void addDarkTrees(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.DARK_CANOPY_TREE.get().configure(DARK_OAK_TREE).createDecoratedFeature(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(5, 0.5F, 1))));
	}

	//Grass and plants
	public static void addPlantRoots(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.PLANT_ROOTS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(12))));
	}

	public static void addGrass(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.GRASS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addGrassWithFern(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.LUSH_GRASS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addFernWithGrass(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.TAIGA_GRASS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTwilightGrass(Biome biome, BlockClusterFeatureConfig config, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(config).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTallGrass(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.TALL_GRASS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addTallFerns(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.LARGE_FERN_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addFlowers(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.DEFAULT_FLOWER_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(count))));
	}

	public static void addFlowersMore(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.configure(DefaultBiomeFeatures.FOREST_FLOWER_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(8))));
	}

	public static void addTallFlowers(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_RANDOM_SELECTOR.configure(new MultipleWithChanceRandomFeatureConfig(ImmutableList.of(Feature.field_227248_z_.configure(DefaultBiomeFeatures.LILAC_CONFIG), Feature.field_227248_z_.configure(DefaultBiomeFeatures.ROSE_BUSH_CONFIG), Feature.field_227248_z_.configure(DefaultBiomeFeatures.PEONY_CONFIG)), 0)).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(2))));
	}

	public static void addHugeLilyPads(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.HUGE_LILY_PAD.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
	}

	public static void addHugeWaterLiles(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.HUGE_WATER_LILY.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
	}

	public static void addMushgloom(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(MUSHGLOOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addPumpkins(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.PUMPKIN_PATCH_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addMushrooms(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(8))));
	}

	public static void addMushroomsSometimes(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(6))));
	}

	public static void addMushroomsCommon(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(1))));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));
	}

	public static void addMushroomsDark(Biome biome) {
		if (random.nextInt(8) == 0)
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
		if (random.nextInt(16) == 0)
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
		if (random.nextInt(24) == 0)
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(MUSHGLOOM_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
	}

	public static void addVines(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.VINES.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHT_64.configure(new FrequencyConfig(count))));
	}

	public static void addDeadBushes(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DEAD_BUSH_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(count))));
	}

	public static void addReeds(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.SUGAR_CANE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addWaterlilies(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(DefaultBiomeFeatures.LILY_PAD_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(count))));
	}

	public static void addTorchberries(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.TORCH_BERRIES.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(3))));
	}

	public static void addForestGrass(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.configure(FOREST_GRASS_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(count))));
	}

	//Other Features
	public static void addRuins(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.RANDOM_SELECTOR.configure(RUINS_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(6))));
	}

	public static void addHangingLamps(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.HANGING_LAMPS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addLamppost(Biome biome, BlockState state, int chance) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.LAMPPOSTS.get().configure(new BlockStateFeatureConfig(state)).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(chance))));
	}

	public static void addPenguins(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.PENGUINS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(4))));
	}

	public static void addTrollRoots(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, TFBiomeFeatures.TROLL_ROOTS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(24))));
	}

	public static void addWebs(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.WEBS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addFallenLeaves(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FALLEN_LEAVES.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
	}

	public static void addGraveyards(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.GRAVEYARD.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(16))));
	}

	public static void addThorns(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TFBiomeFeatures.THORNS.get().configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(64))));
	}

	public static void addMyceliumBlobs(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, TFBiomeFeatures.MYCELIUM_BLOB.get().configure(new SphereReplaceConfig(Blocks.MYCELIUM.getDefaultState(), 5, 1, Lists.newArrayList(Blocks.GRASS_BLOCK.getDefaultState()))).createDecoratedFeature(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(count))));
	}

	public static void addJetsAndSmokers(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FIRE_JET.get().configure(new BlockStateFeatureConfig(FIRE_JET)).createDecoratedFeature(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, TFBiomeFeatures.FIRE_JET.get().configure(new BlockStateFeatureConfig(SMOKER)).createDecoratedFeature(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(4))));
	}

	public static void addMossyBoulders(Biome biome) {
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK.configure(new BlockBlobConfig(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0)).createDecoratedFeature(Placement.FOREST_ROCK.configure(new FrequencyConfig(2))));
	}

	public static void addClayDisks(Biome biome, int count) {
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.configure(new SphereReplaceConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState()))).createDecoratedFeature(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(count))));
	}
}