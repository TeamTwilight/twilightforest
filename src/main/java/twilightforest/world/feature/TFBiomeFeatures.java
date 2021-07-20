package twilightforest.world.feature;

import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.world.feature.config.TFTreeFeatureConfig;
import twilightforest.world.feature.tree.SnowTreePlacer;
import twilightforest.world.feature.tree.SnowUnderTrees;
import twilightforest.worldgen.structures.GenDruidHut;
import twilightforest.worldgen.structures.TFGenGraveyard;

//I'd call this TFFeatures, but that'd be confused with TFFeature.
public class TFBiomeFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TwilightForestMod.ID);
	
	public static final RegistryObject<Feature<NoFeatureConfig>> BIG_MUSHGLOOM = FEATURES.register("big_mushgloom", () ->
			new TFGenBigMushgloom(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> CANOPY_OAK = FEATURES.register("canopy_oak", () ->
			new TFGenCanopyOak(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<CaveStalactiteConfig>> CAVE_STALACTITE = FEATURES.register("cave_stalactite", () ->
			new TFGenCaveStalactite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final RegistryObject<Feature<BaseTreeFeatureConfig>> DARK_CANOPY_TREE = FEATURES.register("dark_canopy_tree", () ->
			new TFGenDarkCanopyTree(BaseTreeFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> DRUID_HUT = FEATURES.register("druid_hut", () ->
			new GenDruidHut(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_HOLLOW_LOG = FEATURES.register("fallen_hollow_log", () ->
			new TFGenFallenHollowLog(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_LEAVES = FEATURES.register("fallen_leaves", () ->
			new TFGenFallenLeaves(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_SMALL_LOG = FEATURES.register("fallen_small_log", () ->
			new TFGenFallenSmallLog(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<BlockStateFeatureConfig>> FIRE_JET = FEATURES.register("fire_jet", () ->
			new TFGenFireJet(BlockStateFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> FOUNDATION = FEATURES.register("foundation", () ->
			new TFGenFoundation(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> GRAVEYARD = FEATURES.register("graveyard", () ->
			new TFGenGraveyard(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> GROVE_RUINS = FEATURES.register("grove_ruins", () ->
			new TFGenGroveRuins(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = FEATURES.register("hollow_stump", () ->
			new TFGenHollowStump(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_TREE = FEATURES.register("hollow_tree", () ->
			new TFGenHollowTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> HUGE_LILY_PAD = FEATURES.register("huge_lily_pad", () ->
			new TFGenHugeLilyPad(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> HUGE_WATER_LILY = FEATURES.register("huge_water_lily", () ->
			new TFGenHugeWaterLily(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<BlockStateFeatureConfig>> LAMPPOSTS = FEATURES.register("lampposts", () ->
			new TFGenLampposts(BlockStateFeatureConfig.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> MINERS_TREE = FEATURES.register("miners_tree", () ->
			new TFGenMinersTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> MONOLITH = FEATURES.register("monolith", () ->
			new TFGenMonolith(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<SphereReplaceConfig>> MYCELIUM_BLOB = FEATURES.register("mycelium_blob", () ->
			new TFGenMyceliumBlob(SphereReplaceConfig.CODEC));
	public static final RegistryObject<Feature<CaveStalactiteConfig>> OUTSIDE_STALAGMITE = FEATURES.register("outside_stalagmite", () ->
			new TFGenOutsideStalagmite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final RegistryObject<Feature<NoFeatureConfig>> PLANT_ROOTS = FEATURES.register("plant_roots", () ->
			new TFGenPlantRoots(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> STONE_CIRCLE = FEATURES.register("stone_circle", () ->
			new TFGenStoneCircle(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> THORNS = FEATURES.register("thorns", () ->
			new TFGenThorns(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> TORCH_BERRIES = FEATURES.register("torch_berries", () ->
			new TFGenTorchBerries(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> TREE_OF_TIME = FEATURES.register("tree_of_time", () ->
			new TFGenTreeOfTime(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> TROLL_ROOTS = FEATURES.register("troll_roots", () ->
			new TFGenTrollRoots(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> WEBS = FEATURES.register("webs", () ->
			new TFGenWebs(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> WELL = FEATURES.register("well", () ->
			new TFGenWell(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> WOOD_ROOTS = FEATURES.register("wood_roots", () ->
			new TFGenWoodRoots(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<NoFeatureConfig>> SNOW_UNDER_TREES = FEATURES.register("snow_under_trees", () ->
			new SnowUnderTrees(NoFeatureConfig.CODEC));
	public static final RegistryObject<Feature<BaseTreeFeatureConfig>> SNOW_TREE = FEATURES.register("anywhere_tree", () ->
			new SnowTreePlacer(BaseTreeFeatureConfig.CODEC));
	public static final RegistryObject<Feature<BlockClusterFeatureConfig>> DARK_FOREST_PLACER = FEATURES.register("dark_forest_placer", () ->
			new TFGenDarkForestFeature(BlockClusterFeatureConfig.CODEC));
}
