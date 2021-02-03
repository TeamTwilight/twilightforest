package twilightforest.worldgen;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import twilightforest.TFStructures;
import twilightforest.biomes.TFBiomes;

import java.util.Map;
import java.util.function.Supplier;

public final class BiomeMaker extends BiomeHelper {
	public static Map<RegistryKey<Biome>, Biome> generateBiomes() {
		final ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes = ImmutableMap.builder();

		commonBiomes(biomes);
		mushroomBiomes(biomes);
		rareBiomes(biomes);
		swampBiomes(biomes);
		darkForestBiomes(biomes);
		snowRegionBiomes(biomes);
		highlandsBiomes(biomes);

		return biomes.build();
	}

	private static void commonBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(TFBiomes.twilightForest,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopy(defaultStructures(defaultGenSettingBuilder())))
						.build()
		);

		biomes.put(TFBiomes.denseTwilightForest,
				biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0x005522), defaultMobSpawning(), addCanopy(addCanopy(defaultStructures(defaultGenSettingBuilder()))))
						.temperature(0.7F)
						.downfall(0.8F)
						.depth(0.2F)
						.scale(0.2F)
						.build()
		);

		biomes.put(TFBiomes.fireflyForest,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopyFirefly(defaultStructures(defaultGenSettingBuilder())))
						.temperature(0.5F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(TFBiomes.clearing,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultStructures(defaultGenSettingBuilder()))
						.category(Biome.Category.PLAINS)
						.temperature(0.8F)
						.downfall(0.4F)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(TFBiomes.oakSavanna,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), modify(withWildcardTrees(defaultGenSettingBuilder()), BiomeHelper::defaultStructures))
						.category(Biome.Category.SAVANNA)
						.temperature(0.9F)
						.downfall(0)
						.depth(0.2F)
						.scale(0.2F)
						.build()
		);
	}

	private static void mushroomBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(TFBiomes.mushrooms,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addMushroomCanopy(defaultStructures(defaultGenSettingBuilder()), false))
						.temperature(0.8F)
						.downfall(0.8F)
						.build()
		);

		biomes.put(TFBiomes.deepMushrooms,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addMushroomCanopy(defaultStructures(defaultGenSettingBuilder().withStructure(TFStructures.CONFIGURED_MUSHROOM_TOWER)), true))
						.temperature(0.8F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);
	}

	private static void rareBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(TFBiomes.spookyForest,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0xC45123).withFoliageColor(0xFF8501).setWaterColor(0xFA9111), defaultMobSpawning(), modify(addCanopyDead(defaultGenSettingBuilder()), BiomeHelper::defaultStructures))
						.temperature(0.5F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(TFBiomes.enchantedForest, // FIXME: colors
				biomeWithDefaults(defaultAmbientBuilder().withFoliageColor(0x00FFFF).withGrassColor(0x00FFFF), defaultMobSpawning(), modify(defaultGenSettingBuilder(), c -> c
						.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.RAINBOW_OAK_TREE_BASE.square())
						.withStructure(TFStructures.CONFIGURED_QUEST_GROVE)))
						.build()
		);

		biomes.put(TFBiomes.stream,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), withWildcardTrees(defaultGenSettingBuilder()))
						.category(Biome.Category.RIVER)
						.temperature(0.5F)
						.downfall(0.1F)
						.depth(-0.8F)
						.scale(0)
						.build()
		);

		biomes.put(TFBiomes.tfLake,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), withWildcardTrees(defaultGenSettingBuilder()))
						.category(Biome.Category.OCEAN)
						.temperature(0.66F)
						.downfall(1)
						.depth(-1.8F)
						.scale(0.1F)
						.build()
		);
	}

	private static void swampBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		Supplier<BiomeGenerationSettings.Builder> swampGenerationBuilder = () -> modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.MANGROVE_TREE_BASE.square()));

		biomes.put(TFBiomes.tfSwamp,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x5C694E).withFoliageColor(0x496137).setWaterColor(0xE0FFAE), defaultMobSpawning(), swampGenerationBuilder.get().withStructure(TFStructures.CONFIGURED_LABYRINTH))
						.category(Biome.Category.SWAMP)
						.temperature(0.8F)
						.downfall(0.9F)
						.depth(-0.25F)
						.scale(0.25F)
						.build()
		);

		biomes.put(TFBiomes.fireSwamp,
				biomeWithDefaults(whiteAshParticles(defaultAmbientBuilder().withGrassColor(0x572E23).withFoliageColor(0x64260F).setWaterColor(0x6C2C2C)), defaultMobSpawning(), swampGenerationBuilder.get().withStructure(TFStructures.CONFIGURED_HYDRA_LAIR))
						.category(Biome.Category.SWAMP)
						.temperature(1)
						.downfall(0.4F)
						.depth(0.1F)
						.scale(0.2F)
						.build()
		);
	}

	private static void darkForestBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		Supplier<BiomeGenerationSettings.Builder> darkForestGenerationBuilder = () -> modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ConfiguredFeatures.DARKWOOD_TREE_BASE.square()));

		biomes.put(TFBiomes.darkForest,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x4B6754).withFoliageColor(0x3B5E3F), defaultMobSpawning(), darkForestGenerationBuilder.get().withStructure(TFStructures.CONFIGURED_KNIGHT_STRONGHOLD))
						.temperature(0.7F)
						.downfall(0.8F)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(TFBiomes.darkForestCenter, // FIXME: colors
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x667540).withFoliageColor(0xF9821E), defaultMobSpawning(), darkForestGenerationBuilder.get())
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);
	}

	private static void snowRegionBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(TFBiomes.snowy_forest,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), modify(defaultGenSettingBuilder(), DefaultBiomeFeatures::withMountainEdgeTrees))
						.temperature(0.09F)
						.downfall(0.9F)
						.depth(0.2F)
						.scale(0.2F)
						.precipitation(Biome.RainType.SNOW)
						.build()
		);

		biomes.put(TFBiomes.glacier,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder())
						.category(Biome.Category.ICY)
						.temperature(0.8F)
						.downfall(0.1F)
						.precipitation(Biome.RainType.SNOW)
						.build()
		);
	}

	private static void highlandsBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(TFBiomes.highlands,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), withWildcardTrees(defaultGenSettingBuilder()))
						.category(Biome.Category.MESA)
						.temperature(0.4F)
						.downfall(0.7F)
						.depth(3.5F)
						.scale(0.05F)
						.build()
		);

		biomes.put(TFBiomes.thornlands,
				biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnInfo.Builder(), defaultGenSettingBuilder())
						.category(Biome.Category.NONE)
						.temperature(0.3F)
						.downfall(0.2F)
						.depth(6F)
						.scale(0.1F)
						.build()
		);

		biomes.put(TFBiomes.finalPlateau,
				biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnInfo.Builder(), defaultGenSettingBuilder())
						.category(Biome.Category.MESA)
						.temperature(0.3F)
						.downfall(0.2F)
						.depth(10.5F)
						.scale(0.025F)
						.build()
		);
	}
}
