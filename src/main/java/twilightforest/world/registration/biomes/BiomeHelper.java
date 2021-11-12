package twilightforest.world.registration.biomes;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.TFEntities;
import twilightforest.world.registration.ConfiguredFeatures;
import twilightforest.world.registration.ConfiguredSurfaceBuilders;
import twilightforest.world.registration.ConfiguredWorldCarvers;
import twilightforest.world.registration.TFStructures;

public abstract class BiomeHelper {

    public static BiomeGenerationSettings.Builder twilightForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DENSE_CANOPY_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DENSE_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.JUNGLE_BUSH);
		addForestVegetation(biome);
		addHollowOakTrees(biome);

		addDefaultStructures(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder denseForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TWILIGHT_OAK_TREES);
		addForestVegetation(biome);
		addCanopyTrees(biome);
		addHollowOakTrees(biome);

		addDefaultStructures(biome);
		
		return biome;
	}

	public static BiomeGenerationSettings.Builder fireflyForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FIREFLY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TWILIGHT_OAK_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEFAULT_FALLEN_LOGS);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.LAMPPOST_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MUSHGLOOM_CLUSTER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION_COMMON);
		addForestVegetation(biome);

		addDefaultStructures(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder clearingGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION_COMMON);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEFAULT_FALLEN_LOGS);
		//TODO just to call the glitched feature - remove me later
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SMALL_LOG);

		addForestVegetation(biome);
		addDefaultStructures(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder oakSavannaGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SAVANNAH_OAK_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEFAULT_FALLEN_LOGS);
		addHollowOakTrees(biome);

		addForestVegetation(biome);
		addDefaultStructures(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder enchantedForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder().addStructureStart(TFStructures.CONFIGURED_QUEST_GROVE);
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.ENCHANTED_FOREST_TREES);
		addCanopyTrees(biome);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FIDDLEHEAD);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.GRASS_PLACER);
		addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder spookyForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TWILIGHT_OAK_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEAD_CANOPY_TREES);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.PUMPKIN_LAMPPOST);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TF_OAK_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.CANOPY_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.WEBS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FALLEN_LEAVES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
		
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ConfiguredFeatures.GRAVEYARD);
		addDefaultStructures(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder mushroomForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder();
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TWILIGHT_OAK_TREES);
		addHollowOakTrees(biome);
		addCanopyTrees(biome);
		addCanopyMushrooms(biome, false);
		addDefaultStructures(biome);
		addForestVegetation(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder denseMushroomForestGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder()/*.addStructureStart(TFStructures.CONFIGURED_MUSHROOM_TOWER)*/;
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.TWILIGHT_OAK_TREES);
		addCanopyMushrooms(biome, true);
		addCanopyTrees(biome);
		addHollowOakTrees(biome);
		addForestVegetation(biome);
		addDefaultStructures(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder plateauGen() {
		return new BiomeGenerationSettings.Builder()
				.surfaceBuilder(ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU)
				.addStructureStart(TFStructures.CONFIGURED_FINAL_CASTLE);
	}
	
	public static BiomeGenerationSettings.Builder thornlandsGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU);

		commonFeaturesWithoutBuildings(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.THORNS);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder highlandsGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(ConfiguredSurfaceBuilders.CONFIGURED_HIGHLANDS)
				.addStructureStart(TFStructures.CONFIGURED_TROLL_CAVE);

		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HIGHLANDS_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_ROCK);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_TAIGA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SPRUCE_FALLEN_LOG);

		addSmallStoneClusters(biome);
		addHighlandCaves(biome);

		return biome;
	}

	public static BiomeGenerationSettings.Builder streamsAndLakes(boolean isLake) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(SurfaceBuilders.OCEAN_SAND);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, isLake ? Features.SEAGRASS_DEEP : Features.SEAGRASS_NORMAL);

		BiomeDefaultFeatures.addDefaultOres(biome);
		BiomeDefaultFeatures.addDefaultSeagrass(biome);

		addSmallStoneClusters(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder swampGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder()
				.addStructureStart(TFStructures.CONFIGURED_LABYRINTH);
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MANGROVE_TREES);
		addSwampTrees(biome);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MANGROVE_FALLEN_LOG);

		addForestVegetation(biome);
		lilypads(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder fireSwampGen() {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder()
				.addStructureStart(TFStructures.CONFIGURED_HYDRA_LAIR);

		commonFeaturesWithoutBuildings(biome);
		addSwampTrees(biome);
		addHollowOakTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FIRE_JET);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SMOKER);
		biome.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_SWAMP);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder darkForestGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
                .surfaceBuilder(SurfaceBuilders.GRASS)
				.addStructureStart(TFStructures.CONFIGURED_KNIGHT_STRONGHOLD);

		addDarkForestVegetation(biome);
		addForestVegetation(biome);
		addCaves(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder darkForestCenterGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
                .surfaceBuilder(SurfaceBuilders.GRASS)
				.addStructureStart(TFStructures.CONFIGURED_DARK_TOWER);

		addDarkForestVegetation(biome);
		addCaves(biome);
		
		return biome;
	}
	
	public static BiomeGenerationSettings.Builder snowyForestGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
				.surfaceBuilder(ConfiguredSurfaceBuilders.CONFIGURED_SNOW)
				.addStructureStart(TFStructures.CONFIGURED_YETI_CAVE);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SNOWY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ConfiguredFeatures.SNOW_UNDER_TREES);

		biome.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SPRUCE_FALLEN_LOG);

		BiomeDefaultFeatures.addDefaultOres(biome);
		BiomeDefaultFeatures.addSurfaceFreezing(biome);

		addCaves(biome);

		return biome;
	}
	
	public static BiomeGenerationSettings.Builder glacierGen() {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
                .surfaceBuilder(ConfiguredSurfaceBuilders.CONFIGURED_GLACIER)
				.addStructureStart(TFStructures.CONFIGURED_AURORA_PALACE);
		addCaves(biome);
		return biome;
	}
	
	public static void withWoodRoots(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ConfiguredFeatures.WOOD_ROOTS_SPREAD);
	}

    public static void addDefaultStructures(BiomeGenerationSettings.Builder biome) {
    	biome.addStructureStart(TFStructures.CONFIGURED_HEDGE_MAZE);
		biome.addStructureStart(TFStructures.CONFIGURED_HOLLOW_HILL_SMALL);
		biome.addStructureStart(TFStructures.CONFIGURED_HOLLOW_HILL_MEDIUM);
		biome.addStructureStart(TFStructures.CONFIGURED_HOLLOW_HILL_LARGE);
		biome.addStructureStart(TFStructures.CONFIGURED_NAGA_COURTYARD);
		biome.addStructureStart(TFStructures.CONFIGURED_LICH_TOWER);
	}

	public static void commonFeatures(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ConfiguredFeatures.DRUID_HUT);
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ConfiguredFeatures.WELL_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.GROVE_RUINS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FOUNDATION);

		commonFeaturesWithoutBuildings(biome);
	}

	public static void commonFeaturesWithoutBuildings(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.STONE_CIRCLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.OUTSIDE_STALAGMITE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MONOLITH);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HOLLOW_STUMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HOLLOW_LOG);
	}

	public static void lilypads(BiomeGenerationSettings.Builder biome) {
    	biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HUGE_LILY_PAD);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HUGE_WATER_LILY);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);
	}

    public static void addForestVegetation(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_GRASS_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.FLOWER_PLACER);
	}

	public static void addDarkForestVegetation(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARKWOOD_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_GRASS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_FERNS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_MUSHGLOOMS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_DEAD_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_PUMPKINS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARK_MUSHROOMS);
	}

    //Canopies, trees, and anything resembling a forest thing
    public static void addCanopyTrees(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.CANOPY_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DEFAULT_FALLEN_LOGS);
	}

    public static void addCanopyMushrooms(BiomeGenerationSettings.Builder biome, boolean dense) {
        BiomeDefaultFeatures.addDefaultMushrooms(biome); // Add small mushrooms
		//Same config as DefaultBiomeFeatures.withMushroomBiomeVegetation, we just use our custom large mushrooms instead
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.VANILLA_TF_BIG_MUSH);

        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, dense ? ConfiguredFeatures.CANOPY_MUSHROOMS_DENSE : ConfiguredFeatures.CANOPY_MUSHROOMS_SPARSE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, dense ? ConfiguredFeatures.BIG_MUSHGLOOM : ConfiguredFeatures.MUSHGLOOM_CLUSTER);
	}

    public static void addHollowOakTrees(BiomeGenerationSettings.Builder biome) {
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.HOLLOW_TREE_PLACER);
	}

	public static void addSwampTrees(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.SWAMPY_OAK_TREES);
	}

	public static void addSmallStoneClusters(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ConfiguredFeatures.SMALL_ANDESITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ConfiguredFeatures.SMALL_DIORITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ConfiguredFeatures.SMALL_GRANITE);
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
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ConfiguredFeatures.PLANT_ROOTS);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ConfiguredFeatures.TORCH_BERRIES);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ConfiguredFeatures.VANILLA_ROOTS);
		BiomeDefaultFeatures.addDefaultOres(biome);
	}

	public static void addHighlandCaves(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(GenerationStep.Carving.AIR, ConfiguredWorldCarvers.HIGHLANDCAVES_CONFIGURED);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ConfiguredFeatures.TROLL_ROOTS);
		BiomeDefaultFeatures.addDefaultOres(biome);
	}

	//Special mob spawns. EntityClassification.MONSTER is forced underground, so use CREATURE for above ground spawns.
	public static MobSpawnSettings.Builder penguinSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.2f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.PENGUIN, 10, 2, 4));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder darkForestSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.1f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 5, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.MIST_WOLF, 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID, 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.KING_SPIDER, 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD, 10, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder snowForestSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.1f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.WINTER_WOLF, 5, 1, 2));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.YETI, 5, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder ravenSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.3f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN, 10, 4, 4));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder swampSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.2f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 10, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.MOSQUITO_SWARM, 10, 1, 1));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder spookSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.4f);
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 50, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 1, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID, 5, 1, 1));
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
                .backgroundMusic(ConfiguredFeatures.TFMUSICTYPE);
        
    }

    public static BiomeGenerationSettings.Builder defaultGenSettingBuilder() {
        BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder()
                .surfaceBuilder(SurfaceBuilders.OCEAN_SAND);

        BiomeDefaultFeatures.addSwampClayDisk(biome);
		BiomeDefaultFeatures.addDefaultSoftDisks(biome);
        BiomeDefaultFeatures.addForestGrass(biome);
        BiomeDefaultFeatures.addSavannaGrass(biome);
        biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE);

        addSmallStoneClusters(biome);
        withWoodRoots(biome);
		addCaves(biome);
        return biome;
    }

    public static MobSpawnSettings.Builder defaultMobSpawning() {
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

        spawnInfo.creatureGenerationProbability(0.1f);

        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.BIGHORN_SHEEP, 12, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.BOAR, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.DEER, 15, 4, 5));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.TINY_BIRD, 15, 4, 8));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.SQUIRREL, 10, 2, 4));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.DWARF_RABBIT, 10, 4, 5));
        spawnInfo.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN, 10, 1, 2));

        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 10, 4, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 4));
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD, 10, 2, 4));
		//not a monster, but we want them to go underground
        spawnInfo.addSpawn(MobCategory. MONSTER, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 1, 2));

        return spawnInfo;
    }

	public static Biome.BiomeBuilder biomeWithDefaults(BiomeSpecialEffects.Builder biomeAmbience, MobSpawnSettings.Builder mobSpawnInfo, BiomeGenerationSettings.Builder biomeGenerationSettings) {
        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .biomeCategory(Biome.BiomeCategory.FOREST)
                .depth(0.025f)
                .scale(0.05f)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(biomeAmbience.build())
                .mobSpawnSettings(mobSpawnInfo.build())
                .generationSettings(biomeGenerationSettings.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE);
    }
}
