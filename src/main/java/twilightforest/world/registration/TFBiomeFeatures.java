package twilightforest.world.registration;

import net.minecraft.world.level.levelgen.feature.BaseDiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.templates.*;
import twilightforest.world.components.feature.trees.*;
import twilightforest.world.components.feature.trees.growers.SnowTreePlacer;
import twilightforest.world.components.feature.trees.growers.SnowUnderTrees;

//I'd call this TFFeatures, but that'd be confused with TFFeature.

public class TFBiomeFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TwilightForestMod.ID);
	
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BIG_MUSHGLOOM = FEATURES.register("big_mushgloom", () ->
			new TFGenBigMushgloom(NoneFeatureConfiguration.CODEC));
	//public static final RegistryObject<Feature<TFTreeFeatureConfig>> CANOPY_OAK = FEATURES.register("canopy_oak", () ->
	//		new TFGenCanopyOak(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<SpikeConfig>> CAVE_STALACTITE = FEATURES.register("block_spike", () ->
			new BlockSpikeFeature(SpikeConfig.CODEC));
	public static final RegistryObject<Feature<TreeConfiguration>> DARK_CANOPY_TREE = FEATURES.register("dark_canopy_tree", () ->
			new TFGenDarkCanopyTree(TreeConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> DRUID_HUT = FEATURES.register("druid_hut", () ->
			new DruidHutFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FALLEN_HOLLOW_LOG = FEATURES.register("fallen_hollow_log", () ->
			new TFGenFallenHollowLog(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FALLEN_LEAVES = FEATURES.register("fallen_leaves", () ->
			new TFGenFallenLeaves(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<HollowLogConfig>> FALLEN_SMALL_LOG = FEATURES.register("fallen_small_log", () ->
			new TFGenFallenSmallLog(HollowLogConfig.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> FIRE_JET = FEATURES.register("fire_jet", () ->
			new TFGenFireJet(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FOUNDATION = FEATURES.register("foundation", () ->
			new TFGenFoundation(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> GRAVEYARD = FEATURES.register("graveyard", () ->
			new GraveyardFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> GROVE_RUINS = FEATURES.register("grove_ruins", () ->
			new GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = FEATURES.register("hollow_stump", () ->
			new TFGenHollowStump(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_TREE = FEATURES.register("hollow_tree", () ->
			new TFGenHollowTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> HUGE_LILY_PAD = FEATURES.register("huge_lily_pad", () ->
			new TFGenHugeLilyPad(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> HUGE_WATER_LILY = FEATURES.register("huge_water_lily", () ->
			new TFGenHugeWaterLily(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> LAMPPOSTS = FEATURES.register("lampposts", () ->
			new TFGenLampposts(BlockStateConfiguration.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> LARGE_WINTER_TREE = FEATURES.register("large_winter_tree", () ->
			new TFGenLargeWinter(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> MINERS_TREE = FEATURES.register("miners_tree", () ->
			new TFGenMinersTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> MONOLITH = FEATURES.register("monolith", () ->
			new TFGenMonolith(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<DiskConfiguration>> MYCELIUM_BLOB = FEATURES.register("mycelium_blob", () ->
			new CheckAbovePatchFeature(DiskConfiguration.CODEC));
	public static final RegistryObject<Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS = FEATURES.register("underground_plants", () ->
			new UndergroundPlantFeature(BlockStateConfiguration.CODEC, false));
	public static final RegistryObject<Feature<BlockStateConfiguration>> TROLL_VINES = FEATURES.register("troll_vines", () ->
			new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> STONE_CIRCLE = FEATURES.register("stone_circle", () ->
			new StoneCircleFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<ThornsConfig>> THORNS = FEATURES.register("thorns", () ->
			new TwilightThorns(ThornsConfig.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> TREE_OF_TIME = FEATURES.register("tree_of_time", () ->
			new TFGenTreeOfTime(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WEBS = FEATURES.register("webs", () ->
			new TFGenWebs(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SIMPLE_WELL = FEATURES.register("simple_well", () ->
			new SimpleWellFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> FANCY_WELL = FEATURES.register("fancy_well", () ->
			new FancyWellFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> WOOD_ROOTS = FEATURES.register("wood_roots", () ->
			new TFGenWoodRoots(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> SNOW_UNDER_TREES = FEATURES.register("snow_under_trees", () ->
			new SnowUnderTrees(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<TreeConfiguration>> SNOW_TREE = FEATURES.register("anywhere_tree", () ->
			new SnowTreePlacer(TreeConfiguration.CODEC));
	public static final RegistryObject<Feature<RandomPatchConfiguration>> DARK_FOREST_PLACER = FEATURES.register("dark_forest_placer", () ->
			new TFGenDarkForestFeature(RandomPatchConfiguration.CODEC));
}
