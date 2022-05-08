package twilightforest.world.registration.biomes;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.TFEntities;
import twilightforest.world.registration.ConfiguredWorldCarvers;
import twilightforest.world.registration.features.TFConfiguredFeatures;
import twilightforest.world.registration.features.TFPlacedFeatures;

public abstract class BiomeHelper {

    public static BiomeGenerationSettings.Builder twilightForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_CANOPY_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.JUNGLE_BUSH);
		//addForestVegetation(biome);
		addHollowOakTrees(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder denseForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		//addForestVegetation(biome);
		addCanopyTrees(biome);
		addHollowOakTrees(biome);
		
		return biome;
	}

	public static BiomeGenerationSettings.Builder fireflyForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIREFLY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LAMPPOST_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MUSHGLOOM_CLUSTER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
		//addForestVegetation(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder clearingGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FOREST_FLOWERS);
//		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);

//		addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder oakSavannaGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SAVANNAH_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		addHollowOakTrees(biome);

		//addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder enchantedForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_ENCHANTED_FOREST_TREES);
		addCanopyTrees(biome);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIDDLEHEAD);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_GRASS_PLACER);
		//addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder spookyForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEAD_CANOPY_TREE);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_PUMPKIN_LAMPPOST);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TF_OAK_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_CANOPY_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_WEBS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FALLEN_LEAVES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);

		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_GRAVEYARD);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder mushroomForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		addHollowOakTrees(biome);
		addCanopyTrees(biome);
		addCanopyMushrooms(biome, false);
		//addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder denseMushroomForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
//		addCanopyMushrooms(biome, true); //TODO WHY IS THIS BROKEN
		addCanopyTrees(biome);
		addHollowOakTrees(biome);
//		addForestVegetation(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder plateauGen() {
		return new BiomeGenerationSettings.Builder();
	}
	
	public static BiomeGenerationSettings.Builder thornlandsGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		commonFeaturesWithoutBuildings(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_THORNS);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder highlandsGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HIGHLANDS_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.FOREST_ROCK);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SPRUCE_FALLEN_LOG);

//		addSmallStoneClusters(biome);
		addHighlandCaves(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder streamsAndLakes(boolean isLake) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, isLake ? AquaticPlacements.SEAGRASS_DEEP : AquaticPlacements.SEAGRASS_NORMAL);

		addLegacyOres(biome);
		BiomeDefaultFeatures.addDefaultSeagrass(biome);

		addSmallStoneClusters(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder swampGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MANGROVE_TREE);
		addSwampTrees(biome);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MANGROVE_FALLEN_LOG);

		//addForestVegetation(biome);
		lilypads(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder fireSwampGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		commonFeaturesWithoutBuildings(biome);
		addSwampTrees(biome);
		addHollowOakTrees(biome);

		//biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIRE_JET);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SMOKER);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_LAVA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder darkForestGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		addDarkForestVegetation(biome);
		//addForestVegetation(biome);
		addCaves(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder darkForestCenterGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		addDarkForestVegetation(biome);
		addCaves(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder snowyForestGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SNOWY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TFPlacedFeatures.PLACED_SNOW_UNDER_TREES);

		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SPRUCE_FALLEN_LOG);

		BiomeDefaultFeatures.addSurfaceFreezing(biome);

		addCaves(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder glacierGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();
		addCaves(biome);
		return biome;
	}
	
	public static void withWoodRoots(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_WOOD_ROOTS_SPREAD);
	}

	public static void commonFeatures(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_DRUID_HUT);
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_WELL_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_GROVE_RUINS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FOUNDATION);

		commonFeaturesWithoutBuildings(biome);
	}

	public static void commonFeaturesWithoutBuildings(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_STONE_CIRCLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_OUTSIDE_STALAGMITE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MONOLITH);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HOLLOW_STUMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HOLLOW_LOG);
	}

	public static void lilypads(BiomeGenerationSettings.Builder biome) {
    	biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HUGE_LILY_PAD);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HUGE_WATER_LILY);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
	}

    public static void addForestVegetation(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FOREST_GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FLOWER_PLACER);
	}

	public static void addDarkForestVegetation(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARKWOOD_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_GRASS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FERNS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_MUSHGLOOMS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_DEAD_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_PUMPKINS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_MUSHROOMS);
	}

    //Canopies, trees, and anything resembling a forest thing
    public static void addCanopyTrees(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_CANOPY_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
	}

    public static void addCanopyMushrooms(BiomeGenerationSettings.Builder biome, boolean dense) {
        BiomeDefaultFeatures.addDefaultMushrooms(biome); // Add small mushrooms
		//Same config as DefaultBiomeFeatures.withMushroomBiomeVegetation, we just use our custom large mushrooms instead
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_BIG_MUSH);

        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, dense ? TFPlacedFeatures.PLACED_CANOPY_MUSHROOMS_DENSE : TFPlacedFeatures.PLACED_CANOPY_MUSHROOMS_SPARSE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, dense ? TFPlacedFeatures.PLACED_BIG_MUSHGLOOM : TFPlacedFeatures.PLACED_MUSHGLOOM_CLUSTER);
	}

    public static void addHollowOakTrees(BiomeGenerationSettings.Builder biome) {
        //biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HOLLOW_TREE_PLACER);
	}

	public static void addSwampTrees(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SWAMPY_OAK_TREE);
	}

	public static void addSmallStoneClusters(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_ANDESITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_DIORITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_GRANITE);
	}

    public static BiomeSpecialEffects.Builder whiteAshParticles(BiomeSpecialEffects.Builder builder) {
        builder.ambientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.1f));
        return builder;
    }

	public static BiomeSpecialEffects.Builder fireflyForestParticles(BiomeSpecialEffects.Builder builder) {
		builder.ambientParticle(new AmbientParticleSettings(TFParticleType.WANDERING_FIREFLY.get(), 0.001f));
		return builder;
	}

	public static BiomeSpecialEffects.Builder fireflyParticles(BiomeSpecialEffects.Builder builder) {
		builder.ambientParticle(new AmbientParticleSettings(TFParticleType.WANDERING_FIREFLY.get(), 0.00025f));
		return builder;
	}

    //Caves!
	public static void addCaves(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(GenerationStep.Carving.AIR, ConfiguredWorldCarvers.TFCAVES_CONFIGURED);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_PLANT_ROOTS);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_TORCH_BERRIES);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_VANILLA_ROOTS);
		addLegacyOres(biome);
	}

	public static void addHighlandCaves(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(GenerationStep.Carving.AIR, ConfiguredWorldCarvers.HIGHLANDCAVES_CONFIGURED);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_TROLL_ROOTS);
		addLegacyOres(biome);
	}

	public static void addLegacyOres(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_COAL_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_IRON_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_GOLD_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_REDSTONE_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_DIAMOND_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_LAPIS_ORE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_LEGACY_COPPER_ORE);
	}

	//Special mob spawns. EntityClassification.MONSTER is forced underground, so use CREATURE for above ground spawns.
	public static MobSpawnSettings.Builder penguinSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.2f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.PENGUIN.get(), 10, 2, 4));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder darkForestSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.1f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 5, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.MIST_WOLF.get(), 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID.get(), 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.KING_SPIDER.get(), 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder snowForestSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.1f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.WINTER_WOLF.get(), 5, 1, 2));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.YETI.get(), 5, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder ravenSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.3f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN.get(), 10, 4, 4));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder swampSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.2f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.MOSQUITO_SWARM.get(), 10, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder spookSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.4f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 50, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID.get(), 5, 1, 1));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.BAT, 20, 4, 4));

		return spawnInfo;
	}

	// Defaults
    public static BiomeSpecialEffects.Builder defaultAmbientBuilder() {
        return new BiomeSpecialEffects.Builder()
                .fogColor(0xC0FFD8) // TODO Change based on Biome. Not previously done before
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .skyColor(0x20224A) //TODO Change based on Biome. Not previously done before
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS) // We should probably change it
                .backgroundMusic(TFConfiguredFeatures.TFMUSICTYPE);
        
    }

    public static BiomeGenerationSettings.Builder defaultGenSettingBuilder() {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder();

		BiomeDefaultFeatures.addDefaultSoftDisks(biome);
		BiomeDefaultFeatures.addForestGrass(biome);
		BiomeDefaultFeatures.addSavannaGrass(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);

//		addSmallStoneClusters(biome);
		withWoodRoots(biome);
		addCaves(biome);
        return biome;
    }

    public static MobSpawnSettings.Builder defaultMobSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

        spawnInfo.creatureGenerationProbability(0.1f);

        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.BIGHORN_SHEEP.get(), 12, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.BOAR.get(), 10, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.DEER.get(), 15, 4, 5));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.TINY_BIRD.get(), 15, 4, 8));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SQUIRREL.get(), 10, 2, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.DWARF_RABBIT.get(), 10, 4, 5));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN.get(), 10, 1, 2));

        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 10, 2, 4));
		//not a monster, but we want them to go underground
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 1, 2));

        return spawnInfo;
    }

	public static Biome.BiomeBuilder biomeWithDefaults(BiomeSpecialEffects.Builder biomeAmbience, MobSpawnSettings.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .biomeCategory(Biome.BiomeCategory.FOREST)
                //.depth(0.025f)
                //.scale(0.05f)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(biomeAmbience.build())
                .mobSpawnSettings(mobSpawnInfo.build())
                .generationSettings(biomeGenerationSettings.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE);
    }
}
