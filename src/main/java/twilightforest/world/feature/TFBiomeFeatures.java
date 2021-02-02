package twilightforest.world.feature;

import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.worldgen.structures.GenDruidHut;
import twilightforest.worldgen.structures.TFGenGraveyard;
import twilightforest.world.TFCavesCarver;
import twilightforest.world.feature.config.CaveStalactiteConfig;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

//I'd call this TFFeatures, but that'd be confused with TFFeature.
public class TFBiomeFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TwilightForestMod.ID);
	public static final DeferredRegister<WorldCarver<?>> WORLD_CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, TwilightForestMod.ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> BIG_MUSHGLOOM = FEATURES.register("big_mushgloom", () ->
			new TFGenBigMushgloom(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> CANOPY_MUSHROOM = FEATURES.register("canopy_mushroom", () ->
			new TFGenCanopyMushroom(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> CANOPY_OAK = FEATURES.register("canopy_oak", () ->
			new TFGenCanopyOak(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> CANOPY_TREE = FEATURES.register("canopy_tree", () ->
			new TFGenCanopyTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<CaveStalactiteConfig>> CAVE_STALACTITE = FEATURES.register("cave_stalactite", () ->
			new TFGenCaveStalactite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> DARK_CANOPY_TREE = FEATURES.register("dark_canopy_tree", () ->
			new TFGenDarkCanopyTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> DRUID_HUT = FEATURES.register("druid_hut", () ->
			new GenDruidHut(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_HOLLOW_LOG = FEATURES.register("fallen_hollow_log", () ->
			new TFGenFallenHollowLog(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_LEAVES = FEATURES.register("fallen_leaves", () ->
			new TFGenFallenLeaves(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> FALLEN_SMALL_LOG = FEATURES.register("fallen_small_log", () ->
			new TFGenFallenSmallLog(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<BlockStateFeatureConfig>> FIRE_JET = FEATURES.register("fire_jet", () ->
			new TFGenFireJet(BlockStateFeatureConfig.field_236455_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> FOUNDATION = FEATURES.register("foundation", () ->
			new TFGenFoundation(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> GRAVEYARD = FEATURES.register("graveyard", () ->
			new TFGenGraveyard(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> GROVE_RUINS = FEATURES.register("grove_ruins", () ->
			new TFGenGroveRuins(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> HANGING_LAMPS = FEATURES.register("hanging_lamps", () ->
			new TFGenHangingLamps(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_STUMP = FEATURES.register("hollow_stump", () ->
			new TFGenHollowStump(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> HOLLOW_TREE = FEATURES.register("hollow_tree", () ->
			new TFGenHollowTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> HUGE_LILY_PAD = FEATURES.register("huge_lily_pad", () ->
			new TFGenHugeLilyPad(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> HUGE_WATER_LILY = FEATURES.register("huge_water_lily", () ->
			new TFGenHugeWaterLily(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<BlockStateFeatureConfig>> LAMPPOSTS = FEATURES.register("lampposts", () ->
			new TFGenLampposts(BlockStateFeatureConfig.field_236455_a_));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> LARGE_WINTER_TREE = FEATURES.register("large_winter_tree", () ->
			new TFGenLargeWinter(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> MANGROVE_TREE = FEATURES.register("mangrove_tree", () ->
			new TFGenMangroveTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> MINERS_TREE = FEATURES.register("miners_tree", () ->
			new TFGenMinersTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> MONOLITH = FEATURES.register("monolith", () ->
			new TFGenMonolith(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<SphereReplaceConfig>> MYCELIUM_BLOB = FEATURES.register("mycelium_blob", () ->
			new TFGenMyceliumBlob(SphereReplaceConfig.field_236516_a_));
	public static final RegistryObject<Feature<CaveStalactiteConfig>> OUTSIDE_STALAGMITE = FEATURES.register("outside_stalagmite", () ->
			new TFGenOutsideStalagmite(CaveStalactiteConfig.caveStalactiteCodec));
	public static final RegistryObject<Feature<NoFeatureConfig>> PENGUINS = FEATURES.register("penguins", () ->
			new TFGenPenguins(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> PLANT_ROOTS = FEATURES.register("plant_roots", () ->
			new TFGenPlantRoots(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> SORTING_TREE = FEATURES.register("sorting_tree", () ->
			new TFGenSortingTree(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> STONE_CIRCLE = FEATURES.register("stone_circle", () ->
			new TFGenStoneCircle(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> THORNS = FEATURES.register("thorns", () ->
			new TFGenThorns(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> TORCH_BERRIES = FEATURES.register("torch_berries", () ->
			new TFGenTorchBerries(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> TREE_OF_TIME = FEATURES.register("tree_of_time", () ->
			new TFGenTreeOfTime(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<TFTreeFeatureConfig>> TREE_OF_TRANSFORMATION = FEATURES.register("tree_of_transformation", () ->
			new TFGenTreeOfTransformation(TFTreeFeatureConfig.codecTFTreeConfig));
	public static final RegistryObject<Feature<NoFeatureConfig>> TROLL_ROOTS = FEATURES.register("troll_roots", () ->
			new TFGenTrollRoots(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> WEBS = FEATURES.register("webs", () ->
			new TFGenWebs(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> WELL = FEATURES.register("well", () ->
			new TFGenWell(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> WOOD_ROOTS = FEATURES.register("wood_roots", () ->
			new TFGenWoodRoots(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<WorldCarver<ProbabilityConfig>> TF_CAVES = WORLD_CARVERS.register("tf_caves",  () ->
			new TFCavesCarver(ProbabilityConfig.CODEC, 256));
}
