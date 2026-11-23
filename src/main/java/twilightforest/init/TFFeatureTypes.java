package twilightforest.init;

import com.mojang.serialization.MapCodec;
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
import twilightforest.world.components.feature.trollcave.TrollBigMushgloomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeBrownMushroomFeature;
import twilightforest.world.components.feature.trollcave.TrollHugeRedMushroomFeature;

public class TFFeatureTypes {

	public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES = DeferredRegister.create(Registries.FEATURE_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<TFSmallLakeFeature>> SMALL_LAKE = FEATURES.register("small_lake", () -> TFSmallLakeFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<BigMushgloomFeature>> BIG_MUSHGLOOM = FEATURES.register("big_mushgloom", () -> BigMushgloomFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_BROWN_MUSHROOM = FEATURES.register("canopy_brown_mushroom", () -> new BrownCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_VANILLA_MUSHROOM = FEATURES.register("canopy_red_vanilla_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 0));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SMOOTH_MUSHROOM = FEATURES.register("canopy_red_smooth_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 1));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_SPHEROID_MUSHROOM = FEATURES.register("canopy_red_spheroid_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 2));
	public static final DeferredHolder<Feature<?>, Feature<HugeMushroomFeatureConfiguration>> CANOPY_RED_FLAT_MUSHROOM = FEATURES.register("canopy_red_flat_mushroom", () -> new RedCanopyMushroomFeature(HugeMushroomFeatureConfiguration.CODEC, 3));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> MEGA_OAK = FEATURES.register("mega_oak", () -> new MegaOakTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> MEGA_CANOPY = FEATURES.register("mega_canopy", () -> new MegaCanopyTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<BlockSpikeFeature>> CAVE_STALACTITE = FEATURES.register("block_spike", () -> BlockSpikeFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> DARK_CANOPY_TREE = FEATURES.register("dark_canopy_tree", () -> new DarkCanopyTreeFeature(TreeConfiguration.CODEC));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<DruidHutFeature>> DRUID_HUT = FEATURES.register("druid_hut", () -> DruidHutFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FallenHollowLogFeature>> FALLEN_HOLLOW_LOG = FEATURES.register("fallen_hollow_log", () -> FallenHollowLogFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FallenLeavesFeature>> FALLEN_LEAVES = FEATURES.register("fallen_leaves", () -> FallenLeavesFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SmallFallenLogFeature>> FALLEN_SMALL_LOG = FEATURES.register("fallen_small_log", () -> SmallFallenLogFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FireJetFeature>> FIRE_JET = FEATURES.register("fire_jet", () -> FireJetFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FoundationFeature>> FOUNDATION = FEATURES.register("foundation", () -> FoundationFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<GraveyardFeature>> GRAVEYARD = FEATURES.register("graveyard", () -> GraveyardFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<GroveRuinsFeature>> GROVE_RUINS = FEATURES.register("grove_ruins", () -> GroveRuinsFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = FEATURES.register("hollow_stump", () -> new HollowStumpFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<HugeLilypadFeature>> HUGE_LILY_PAD = FEATURES.register("huge_lily_pad", () -> HugeLilypadFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<HugeWaterLilyFeature>> HUGE_WATER_LILY = FEATURES.register("huge_water_lily", () -> HugeWaterLilyFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<LampostFeature>> LAMPPOSTS = FEATURES.register("lampposts", () -> LampostFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> LARGE_WINTER_TREE = FEATURES.register("large_winter_tree", () -> new LargeWinterTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> MINERS_TREE = FEATURES.register("miners_tree", () -> new MiningTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<MonolithFeature>> MONOLITH = FEATURES.register("monolith", () -> MonolithFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<CheckAbovePatchFeature>> MYCELIUM_BLOB = FEATURES.register("mycelium_blob", () -> CheckAbovePatchFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<BerryBushFeature>> BERRY_BUSH = FEATURES.register("berry_bush", () -> BerryBushFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS = FEATURES.register("underground_plants", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> UNDERGROUND_PLANTS_IN_STRUCTURE = FEATURES.register("underground_plants_in_structure", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> OREBERRY_BUSHES = FEATURES.register("oreberry_bushes", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, 1));
	public static final DeferredHolder<Feature<?>, Feature<BlockStateConfiguration>> TROLL_VINES = FEATURES.register("troll_vines", () -> new UndergroundPlantFeature(BlockStateConfiguration.CODEC, true));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<TrollBigMushgloomFeature>> TROLL_BIG_MUSHGLOOM = FEATURES.register("troll_cave_big_mushgloom", () -> TrollBigMushgloomFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<TrollHugeRedMushroomFeature>> TROLL_HUGE_RED_MUSHROOM = FEATURES.register("troll_cave_huge_red_mushroom", () -> TrollHugeRedMushroomFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<TrollHugeBrownMushroomFeature>> TROLL_HUGE_BROWN_MUSHROOM = FEATURES.register("troll_cave_huge_brown_mushroom", () -> TrollHugeBrownMushroomFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<StoneCircleFeature>> STONE_CIRCLE = FEATURES.register("stone_circle", () -> StoneCircleFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<ThornFeature>> THORNS = FEATURES.register("thorns", () -> ThornFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<TFTreeFeatureConfig>> TREE_OF_TIME = FEATURES.register("tree_of_time", () -> new TimeTreeFeature(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WebFeature>> WEBS = FEATURES.register("webs", () -> WebFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SimpleWellFeature>> SIMPLE_WELL = FEATURES.register("simple_well", () -> SimpleWellFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FancyWellFeature>> FANCY_WELL = FEATURES.register("fancy_well", () -> FancyWellFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<WoodRootFeature>> WOOD_ROOTS = FEATURES.register("wood_roots", () -> WoodRootFeature.MAP_CODEC);
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SnowUnderTreeFeature>> SNOW_UNDER_TREES = FEATURES.register("snow_under_trees", () -> SnowUnderTreeFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<TreeConfiguration>> SNOW_TREE = FEATURES.register("anywhere_tree", () -> new SnowTreeFeature(TreeConfiguration.CODEC));
	public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<EnchantedForestVinesFeature>> ENCHANTED_FOREST_VINES = FEATURES.register("enchanted_forest_vines", () -> EnchantedForestVinesFeature.MAP_CODEC);
	public static final DeferredHolder<Feature<?>, Feature<WeightedListFeatureConfig>> WEIGHTED_LIST_SELECTOR = FEATURES.register("weighted_list", () -> new WeightedListFeature(WeightedListFeatureConfig.CODEC));
}
