package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.feature.*;
import twilightforest.world.components.feature.config.*;
import twilightforest.world.components.feature.templates.*;
import twilightforest.world.components.feature.trees.*;

public class TFFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TwilightForestMod.ID);
	
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> BIG_MUSHGLOOM = FEATURES.register("big_mushgloom", () -> new BigMushgloomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_BROWN_MUSHROOM = FEATURES.register("canopy_brown_mushroom", () -> new BrownCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_MUSHROOM = FEATURES.register("canopy_red_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> CANOPY_OAK = FEATURES.register("canopy_oak", () -> new OakCanopyTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CAVE_STALACTITE = FEATURES.register("block_spike", () -> new BlockSpikeFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> DARK_CANOPY_TREE = FEATURES.register("dark_canopy_tree", () -> new DarkCanopyTreeFeature(TreeConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<SwizzleConfig>> DRUID_HUT = FEATURES.register("druid_hut", () -> new DruidHutFeature(SwizzleConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> FALLEN_HOLLOW_LOG = FEATURES.register("fallen_hollow_log", () -> new FallenHollowLogFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> FALLEN_LEAVES = FEATURES.register("fallen_leaves", () -> new FallenLeavesFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<HollowLogConfig>> FALLEN_SMALL_LOG = FEATURES.register("fallen_small_log", () -> new SmallFallenLogFeature(HollowLogConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> FIRE_JET = FEATURES.register("fire_jet", () -> new FireJetFeature(BlockStateConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> FOUNDATION = FEATURES.register("foundation", () -> new FoundationFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GRAVEYARD = FEATURES.register("graveyard", () -> new GraveyardFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GROVE_RUINS = FEATURES.register("grove_ruins", () -> new GroveRuinsFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = FEATURES.register("hollow_stump", () -> new HollowStumpFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> HOLLOW_TREE = FEATURES.register("hollow_tree", () -> new HollowTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> HUGE_LILY_PAD = FEATURES.register("huge_lily_pad", () -> new HugeLilypadFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> HUGE_WATER_LILY = FEATURES.register("huge_water_lily", () -> new HugeWaterLilyFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> LAMPPOSTS = FEATURES.register("lampposts", () -> new LampostFeature(BlockStateConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> LARGE_WINTER_TREE = FEATURES.register("large_winter_tree", () -> new LargeWinterTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> MINERS_TREE = FEATURES.register("miners_tree", () -> new MiningTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> MONOLITH = FEATURES.register("monolith", () -> new MonolithFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<DiskConfiguration>> MYCELIUM_BLOB = FEATURES.register("mycelium_blob", () -> new CheckAbovePatchFeature(DiskConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS = FEATURES.register("underground_plants", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> TROLL_VINES = FEATURES.register("troll_vines", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> STONE_CIRCLE = FEATURES.register("stone_circle", () -> new StoneCircleFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<ThornsConfig>> THORNS = FEATURES.register("thorns", () -> new ThornFeature(ThornsConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> TREE_OF_TIME = FEATURES.register("tree_of_time", () -> new TimeTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> WEBS = FEATURES.register("webs", () -> new WebFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<SwizzleConfig>> SIMPLE_WELL = FEATURES.register("simple_well", () -> new SimpleWellFeature(SwizzleConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<SwizzleConfig>> FANCY_WELL = FEATURES.register("fancy_well", () -> new FancyWellFeature(SwizzleConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<RootConfig>> WOOD_ROOTS = FEATURES.register("wood_roots", () -> new WoodRootFeature(RootConfig.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> SNOW_UNDER_TREES = FEATURES.register("snow_under_trees", () -> new SnowUnderTreeFeature(NoneFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> SNOW_TREE = FEATURES.register("anywhere_tree", () -> new SnowTreeFeature(TreeConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<RandomPatchConfiguration>> DARK_FOREST_PLACER = FEATURES.register("dark_forest_placer", () -> new DarkForestFeature(RandomPatchConfiguration.CODEC));
}
