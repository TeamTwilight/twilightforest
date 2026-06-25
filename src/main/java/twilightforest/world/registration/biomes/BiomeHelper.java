package twilightforest.world.registration.biomes;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import tamaized.beanification.Autowired;
import twilightforest.enums.extensions.TFGrassColorModifierEnumExtension;
import twilightforest.init.*;

import java.util.List;

public abstract class BiomeHelper {

	@Autowired
	private static TFGrassColorModifierEnumExtension grassColorModifierEnumExtension;

	public static Biome.BiomeBuilder twilightForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_LARGE_FERN);
		addForestVegetationAlt(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		addCanopyTrees(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MEGA_CANOPY_TREE);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.5F)
			.downfall(0.5F)
			.putAttributes(defaultEnvironmentBuilder())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder denseForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_RASPBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLACKBERRY_BUSHES);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_LARGE_FERN);

		addForestVegetationAlt(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_CANOPY_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_OAK_BUSH_DENSE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FOREST_MEGA_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MEGA_CANOPY_TREE);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.7F)
			.downfall(0.8F)
			.putAttributes(defaultEnvironmentBuilder()
				.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x005522)
				.set(EnvironmentAttributes.AMBIENT_PARTICLES, List.of(new AmbientParticle(TFParticleType.WANDERING_FIREFLY.get(), 0.001F))))
			.specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x005522).build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder fireflyForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLACKBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);

		addForestVegetationAlt(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIREFLY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LAMPPOST_PLACER);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MUSHGLOOM_CLUSTER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.FLOWER_FOREST_FLOWERS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.5F)
			.downfall(1.0F)
			.putAttributes(defaultEnvironmentBuilder())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder clearing(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_RASPBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_FOREST);
		addForestVegetation(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.FLOWER_FOREST_FLOWERS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.8F)
			.downfall(0.4F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder oakSavanna(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_TAIGA_2);

		addForestVegetation(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SAVANNAH_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEFAULT_FALLEN_LOGS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SAVANNAH_MEGA_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0.9F)
			.downfall(0.0F)
			.putAttributes(defaultEnvironmentBuilderNoParticles()
				.set(EnvironmentAttributes.SNOW_GOLEM_MELTS, true))
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder enchantedForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);
		BiomeDefaultFeatures.addDefaultSoftDisks(biome);
		BiomeDefaultFeatures.addDefaultGrass(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
		BiomeDefaultFeatures.addSurfaceFreezing(biome);
		withWoodRoots(biome);
		addCaves(biome);
		addSmallStoneClusters(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_FERNS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_LARGE_FERNS);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_DENSE_LAKE_WATER);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FLOWER_PLACER);
		commonFeatures(biome);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_IRON_OREBERRIES_ENCHANTED_FOREST);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_GOLD_OREBERRIES_ENCHANTED_FOREST);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_COPPER_OREBERRIES_ENCHANTED_FOREST);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_ESSENCE_OREBERRIES_ENCHANTED_FOREST);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_ENCHANTED_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DENSE_CANOPY_TREES);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIDDLEHEAD);
		biome.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TFPlacedFeatures.PLACED_ENCHANTED_FOREST_VINES);

		addCanopyTrees(biome);

		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0.5F)
			.downfall(0.0F)
			.putAttributes(defaultEnvironmentBuilder())
			.specialEffects(defaultAmbientBuilder()
				.foliageColorOverride(0x00FFFF)
				.grassColorOverride(0x00FFFF)
				.grassColorModifier(grassColorModifierEnumExtension.ENCHANTED_FOREST).build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder spookyForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MAYAPPLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DEAD_CANOPY_TREE);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_PUMPKIN_LAMPPOST);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TF_OAK_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_CANOPY_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_WEBS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FALLEN_LEAVES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);

		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_GRAVEYARD);

		return new Biome.BiomeBuilder()
			.temperature(0.5F)
			.downfall(1.0F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0x827391)
				.set(EnvironmentAttributes.WATER_FOG_COLOR, 0xBC8857)
				.set(EnvironmentAttributes.SKY_COLOR, 0x20224A))
			.specialEffects(defaultAmbientBuilder()
				.waterColor(0xBC8857)
				.grassColorOverride(0xC45123)
				.foliageColorOverride(0xFF8501)
				.grassColorModifier(grassColorModifierEnumExtension.SPOOKY_FOREST).build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 1, 2))
				.addMobCharge(EntityType.SPIDER, 0.75D, 0.25D)
				.addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2))
				.addMobCharge(EntityType.SKELETON, 0.85D, 0.25D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID.get(), 1, 1))
				.addMobCharge(TFEntities.SKELETON_DRUID.get(), 0.95D, 0.25D)
				.addSpawn(MobCategory.AMBIENT, 20, new MobSpawnSettings.SpawnerData(EntityType.BAT, 2, 4)).build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder mushroomForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_RASPBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLACKBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);

		addForestVegetationAlt(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		addCanopyMushrooms(biome, false);
		addCanopyTrees(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MEGA_CANOPY_TREE);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.8F)
			.downfall(0.8F)
			.putAttributes(defaultEnvironmentBuilder())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder denseMushroomForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLACKBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_JUNGLE);

		addForestVegetationAlt(biome);
		commonFeatures(biome);
		addOreberries(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MYCELIUM_BLOB);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_VANILLA_TF_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TWILIGHT_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_LARGE_TWILIGHT_OAK_TREE);
		addCanopyMushrooms(biome, true);
		addCanopyTrees(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MEGA_CANOPY_TREE);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_WATER);

		return new Biome.BiomeBuilder()
			.temperature(0.8F)
			.downfall(1.0F)
			.putAttributes(defaultEnvironmentBuilder())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder finalPlateau(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		return new Biome.BiomeBuilder()
			.temperature(1.0F)
			.downfall(0.2F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0.3f)
				.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN.get(), 4, 4))
				.build())
			.generationSettings(new BiomeGenerationSettings.Builder(featureGetter, carverGetter).build());
	}

	public static Biome.BiomeBuilder thornlands(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		commonFeaturesWithoutBuildings(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_THORNS);

		return new Biome.BiomeBuilder()
			.temperature(0.3F)
			.downfall(0.2F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(new MobSpawnSettings.Builder().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder highlands(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN);
		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_HIGHLANDS_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.FOREST_ROCK);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SPARSE_MUSHGLOOMS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SPRUCE_FALLEN_LOG);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FERNS);

		addHighlandCaves(biome);
		addSmallStoneClusters(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.4F)
			.downfall(0.7F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(defaultMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder highlandsUnderground(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		addHighlandCaves(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.35F)
			.downfall(0.0F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(undergroundMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder stream(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_NORMAL);

		BiomeDefaultFeatures.addSurfaceFreezing(biome);

		addLegacyOres(biome);
		addOreberries(biome);
		addSmallStoneClusters(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.5F)
			.downfall(0.1F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4))
				.addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5))
				.build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder lake(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_DEEP);

		BiomeDefaultFeatures.addSurfaceFreezing(biome);

		addLegacyOres(biome);
		addOreberries(biome);
		addSmallStoneClusters(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.5F)
			.downfall(0.1F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.WATER_CREATURE, 2, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 4))
				.addSpawn(MobCategory.WATER_AMBIENT, 5, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 1, 5))
				.build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder swamp(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SWAMP_RASPBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SWAMP_BLACKBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);

		commonFeatures(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MANGROVE_TREE);
		addSwampTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MANGROVE_FALLEN_LOG);

		lilypads(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.8F)
			.downfall(0.9F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0x003F21)
				.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x95B55F)
				.set(EnvironmentAttributes.SKY_COLOR, 0x002112))
			.specialEffects(defaultAmbientBuilder()
				.grassColorOverride(0x5C694E)
				.foliageColorOverride(0x496137)
				.waterColor(0x95B55F)
				.grassColorModifier(grassColorModifierEnumExtension.SWAMP)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2))
				.addMobCharge(EntityType.ZOMBIE, 0.7D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(TFEntities.MOSQUITO_SWARM.get(), 1, 1))
				.addMobCharge(TFEntities.MOSQUITO_SWARM.get(), 0.7D, 0.15D)
				.build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder fireSwamp(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = defaultGenSettingBuilder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);

		commonFeaturesWithoutBuildings(biome);
		addSwampTrees(biome);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FIRE_JET);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SMOKER);
		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_LAVA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE_SWAMP);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.VINES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_SWAMP);

		return new Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(1.0F)
			.downfall(0.4F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0x380A00)
				.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x6C2C2C)
				.set(EnvironmentAttributes.SKY_COLOR, 0x002112)
				.set(EnvironmentAttributes.SNOW_GOLEM_MELTS, true)
				.set(EnvironmentAttributes.AMBIENT_PARTICLES, List.of(new AmbientParticle(ParticleTypes.WHITE_ASH, 0.015f))))
			.specialEffects(defaultAmbientBuilder()
				.waterColor(0x2D0700)
				.grassColorOverride(0x572E23)
				.foliageColorOverride(0x64260F)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder().build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder darkForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		addDarkForestVegetation(biome);
		addCaves(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.7F)
			.downfall(0.8F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0x000000)
				.set(EnvironmentAttributes.SKY_COLOR, 0x000000))
			.specialEffects(defaultAmbientBuilder()
				.grassColorOverride(0x4B6754)
				.foliageColorOverride(0x3B5E3F)
				.grassColorModifier(grassColorModifierEnumExtension.DARK_FOREST)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0.05f)
				.addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2))
				.addMobCharge(EntityType.ENDERMAN, 0.75D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2))
				.addMobCharge(EntityType.ZOMBIE, 0.7D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 2))
				.addMobCharge(EntityType.SKELETON, 0.8D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(TFEntities.MIST_WOLF.get(), 1, 1))
				.addMobCharge(TFEntities.MIST_WOLF.get(), 0.75D, 0.2D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(TFEntities.SKELETON_DRUID.get(), 1, 1))
				.addMobCharge(TFEntities.SKELETON_DRUID.get(), 0.8D, 0.2D)
				.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(TFEntities.KING_SPIDER.get(), 1, 1))
				.addMobCharge(TFEntities.KING_SPIDER.get(), 0.85D, 0.25D)
				.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 3))
				.addMobCharge(TFEntities.KOBOLD.get(), 0.7D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1))
				.addMobCharge(EntityType.WITCH, 0.75D, 0.15D)
				.build())
			.generationSettings(biome.build());
	}

	public static Biome.BiomeBuilder darkForestCenter(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		return darkForest(featureGetter, carverGetter)
			.setAttribute(EnvironmentAttributes.FOG_COLOR, 0x493000)
			.specialEffects(defaultAmbientBuilder()
				.grassColorOverride(0x667540)
				.foliageColorOverride(0xF9821E)
				.grassColorModifier(grassColorModifierEnumExtension.DARK_FOREST_CENTER)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder().build());
	}

	public static Biome.BiomeBuilder snowyForest(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SNOWY_BLUEBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MALOBERRY_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SNOWY_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TFPlacedFeatures.PLACED_SNOW_UNDER_TREES);

		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SPRUCE_FALLEN_LOG);

		BiomeDefaultFeatures.addSurfaceFreezing(biome);

		addCaves(biome);

		biome.addFeature(GenerationStep.Decoration.LAKES, TFPlacedFeatures.PLACED_LAKE_FROZEN);

		return new Biome.BiomeBuilder()
			.temperature(0.09F)
			.downfall(0.9F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0xFFFFFF)
				.set(EnvironmentAttributes.SKY_COLOR, 0x808080))
			.specialEffects(defaultAmbientBuilder()
				.foliageColorOverride(0xFFFFFF)
				.grassColorOverride(0xFFFFFF)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0.05F)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(TFEntities.WINTER_WOLF.get(), 1, 1))
				.addMobCharge(TFEntities.WINTER_WOLF.get(), 0.6D, 0.15D)
				.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(TFEntities.YETI.get(), 1, 1))
				.addMobCharge(TFEntities.YETI.get(), 0.6D, 0.15D)
				.build())
			.generationSettings(biome.build())
			.temperatureAdjustment(Biome.TemperatureModifier.FROZEN);
	}

	public static Biome.BiomeBuilder glacier(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);
		BiomeDefaultFeatures.addSurfaceFreezing(biome);
		addCaves(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.08F)
			.downfall(0.1F)
			.putAttributes(EnvironmentAttributeMap.builder()
				.set(EnvironmentAttributes.FOG_COLOR, 0x361F88)
				.set(EnvironmentAttributes.SKY_COLOR, 0x130D28))
			.specialEffects(defaultAmbientBuilder()
				.foliageColorOverride(0xFFFFFF)
				.grassColorOverride(0xFFFFFF)
				.build())
			.mobSpawnSettings(new MobSpawnSettings.Builder()
				.creatureGenerationProbability(0.15f)
				.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.PENGUIN.get(), 2, 4))
				.build())
			.generationSettings(biome.build())
			.temperatureAdjustment(Biome.TemperatureModifier.FROZEN);
	}

	public static Biome.BiomeBuilder underground(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		BiomeDefaultFeatures.addDefaultSoftDisks(biome);
		BiomeDefaultFeatures.addSurfaceFreezing(biome);
		withWoodRoots(biome);
		addCaves(biome);
		addSmallStoneClusters(biome);

		return new Biome.BiomeBuilder()
			.temperature(0.7F)
			.downfall(0.0F)
			.putAttributes(defaultEnvironmentBuilderNoParticles())
			.specialEffects(defaultAmbientBuilder().build())
			.mobSpawnSettings(undergroundMobSpawning().build())
			.generationSettings(biome.build());
	}

	public static void withWoodRoots(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_WOOD_ROOTS_SPREAD);
	}

	public static void commonFeatures(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_DRUID_HUT);
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_WELL_PLACER);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_GROVE_RUINS);
		biome.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, TFPlacedFeatures.PLACED_FOUNDATION);

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
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MAYAPPLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FLOWER_PLACER);
	}

	public static void addForestVegetationAlt(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MAYAPPLE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_FLOWER_PLACER_ALT);
	}

	public static void addDarkForestVegetation(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FOREST_TREE_MIX);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FOREST_TREES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARKWOOD_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_GRASS);
//		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_FERNS);  //  undefined opinion
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_DEAD_BUSHES);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_PUMPKINS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_MUSHGLOOMS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_BROWN_MUSHROOMS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_DARK_RED_MUSHROOMS);
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
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_MUSHGLOOM_CLUSTER);
	}

	public static void addSwampTrees(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_SWAMPY_OAK_TREE);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_OAK_BUSH);
	}

	public static void addSmallStoneClusters(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_ANDESITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_DIORITE);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, TFPlacedFeatures.PLACED_SMALL_GRANITE);
	}

	//Caves!
	public static void addCaves(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(TFCaveCarvers.TFCAVES_CONFIGURED);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_PLANT_ROOTS);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_TORCH_BERRIES);
		//biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_VANILLA_ROOTS);
		addLegacyOres(biome);
	}

	public static void addHighlandCaves(BiomeGenerationSettings.Builder biome) {
		biome.addCarver(TFCaveCarvers.HIGHLANDCAVES_CONFIGURED);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_TROLL_ROOTS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PLACED_TROLL_MUSHGLOOMS);
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

	public static void addOreberries(BiomeGenerationSettings.Builder biome) {
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_IRON_OREBERRIES);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_GOLD_OREBERRIES);
		biome.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, TFPlacedFeatures.PLACED_COPPER_OREBERRIES);
	}

	public static EnvironmentAttributeMap.Builder defaultEnvironmentBuilder() {
		return defaultEnvironmentBuilderNoParticles()
			.set(EnvironmentAttributes.AMBIENT_PARTICLES, List.of(new AmbientParticle(TFParticleType.WANDERING_FIREFLY.get(), 0.00025f)));
	}

	public static EnvironmentAttributeMap.Builder defaultEnvironmentBuilderNoParticles() {
		return EnvironmentAttributeMap.builder()
			.set(EnvironmentAttributes.FOG_COLOR, 0x3A4E42)
			.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x050533)
			.set(EnvironmentAttributes.SKY_COLOR, 0x20224A);
	}

	// Defaults
	// lol this is so dumb why is only the water color controlled here
	public static BiomeSpecialEffects.Builder defaultAmbientBuilder() {
		return new BiomeSpecialEffects.Builder().waterColor(0x3F76E4);
	}

	public static BiomeGenerationSettings.Builder defaultGenSettingBuilder(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
		BiomeGenerationSettings.Builder biome = new BiomeGenerationSettings.Builder(featureGetter, carverGetter);

		BiomeDefaultFeatures.addDefaultSoftDisks(biome);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_TALL_GRASS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_BADLANDS);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TFPlacedFeatures.PATCH_GRASS_SAVANNA);
		biome.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
		BiomeDefaultFeatures.addSurfaceFreezing(biome);
		withWoodRoots(biome);
		addCaves(biome);
		addSmallStoneClusters(biome);
		return biome;
	}

	public static MobSpawnSettings.Builder defaultMobSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.creatureGenerationProbability(0.15f);

		spawnInfo.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(TFEntities.BIGHORN_SHEEP.get(), 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.BOAR.get(), 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TFEntities.DEER.get(), 4, 5));
		spawnInfo.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 4, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, 15, new MobSpawnSettings.SpawnerData(TFEntities.TINY_BIRD.get(), 4, 8));
		spawnInfo.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.SQUIRREL.get(), 2, 4));
		spawnInfo.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.DWARF_RABBIT.get(), 4, 5));
		spawnInfo.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(TFEntities.RAVEN.get(), 1, 2));

		return spawnInfo;
	}

	public static MobSpawnSettings.Builder undergroundMobSpawning() {
		MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();

		spawnInfo.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 2, 3)).addMobCharge(EntityType.SPIDER, 0.2D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 1, 2)).addMobCharge(EntityType.ZOMBIE, 0.2D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 1, 1)).addMobCharge(EntityType.SKELETON, 0.3D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1)).addMobCharge(EntityType.CREEPER, 0.35D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 2, 4)).addMobCharge(EntityType.SLIME, 0.2D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 2)).addMobCharge(EntityType.ENDERMAN, 0.4D, 0.15D);
		spawnInfo.addSpawn(MobCategory.MONSTER, 10, new MobSpawnSettings.SpawnerData(TFEntities.KOBOLD.get(), 1, 3)).addMobCharge(TFEntities.KOBOLD.get(), 0.2D, 0.15D);
		spawnInfo.addSpawn(MobCategory.AMBIENT, 10, new MobSpawnSettings.SpawnerData(EntityType.BAT, 1, 1));

		return spawnInfo;
	}
}
