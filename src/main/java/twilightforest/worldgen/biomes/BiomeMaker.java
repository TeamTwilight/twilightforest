package twilightforest.worldgen.biomes;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import twilightforest.TFStructures;
import java.util.Map;

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
		biomes.put(BiomeKeys.FOREST,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), twilightForestGen(defaultGenSettingBuilder()))
						.build()
		);

		biomes.put(BiomeKeys.DENSE_FOREST,
				biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0x005522), defaultMobSpawning().isValidSpawnBiomeForPlayer(), denseForestGen(defaultGenSettingBuilder()))
						.temperature(0.7F)
						.downfall(0.8F)
						.depth(0.2F)
						.scale(0.2F)
						.build()
		);

		biomes.put(BiomeKeys.FIREFLY_FOREST,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), fireflyForestGen(defaultGenSettingBuilder()))
						.temperature(0.5F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(BiomeKeys.CLEARING,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), addDefaultStructures(defaultGenSettingBuilder()))
						.category(Biome.Category.PLAINS)
						.temperature(0.8F)
						.downfall(0.4F)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(BiomeKeys.OAK_SAVANNAH,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), oakSavannaGen(defaultGenSettingBuilder()))
						.category(Biome.Category.SAVANNA)
						.temperature(0.9F)
						.downfall(0)
						.depth(0.2F)
						.scale(0.2F)
						.build()
		);
	}

	private static void mushroomBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.MUSHROOM_FOREST,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), mushroomForestGen(defaultGenSettingBuilder()))
						.temperature(0.8F)
						.downfall(0.8F)
						.build()
		);

		biomes.put(BiomeKeys.DENSE_MUSHROOM_FOREST,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning().isValidSpawnBiomeForPlayer(), denseMushroomForestGen(defaultGenSettingBuilder().withStructure(TFStructures.CONFIGURED_MUSHROOM_TOWER)))
						.temperature(0.8F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);
	}

	private static void rareBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SPOOKY_FOREST,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0xC45123).withFoliageColor(0xFF8501).setWaterColor(0xFA9111),  spookSpawning(), spookyForestGen(defaultGenSettingBuilder()))
						.temperature(0.5F)
						.downfall(1)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(BiomeKeys.ENCHANTED_FOREST, // FIXME: colors
				biomeWithDefaults(defaultAmbientBuilder().withFoliageColor(0x00FFFF).withGrassColor(0x00FFFF), defaultMobSpawning(), enchantedForestGen(defaultGenSettingBuilder().withStructure(TFStructures.CONFIGURED_QUEST_GROVE)))
						.build()
		);

		biomes.put(BiomeKeys.STREAM,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder())
						.category(Biome.Category.RIVER)
						.temperature(0.5F)
						.downfall(0.1F)
						.depth(-0.8F)
						.scale(0)
						.build()
		);

		biomes.put(BiomeKeys.LAKE,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder())
						.category(Biome.Category.OCEAN)
						.temperature(0.66F)
						.downfall(1)
						.depth(-1.8F)
						.scale(0.1F)
						.build()
		);
	}

	private static void swampBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SWAMP,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x5C694E).withFoliageColor(0x496137).setWaterColor(0xE0FFAE), swampSpawning(), swampGen(defaultGenSettingBuilder()).withStructure(TFStructures.CONFIGURED_LABYRINTH))
						.category(Biome.Category.SWAMP)
						.temperature(0.8F)
						.downfall(0.9F)
						.depth(-0.25F)
						.scale(0.25F)
						.build()
		);

		biomes.put(BiomeKeys.FIRE_SWAMP,
				biomeWithDefaults(whiteAshParticles(defaultAmbientBuilder().withGrassColor(0x572E23).withFoliageColor(0x64260F).setWaterColor(0x6C2C2C)), new MobSpawnInfo.Builder(), fireSwampGen(defaultGenSettingBuilder()).withStructure(TFStructures.CONFIGURED_HYDRA_LAIR))
						.category(Biome.Category.SWAMP)
						.temperature(1)
						.downfall(0.4F)
						.depth(0.1F)
						.scale(0.2F)
						.build()
		);
	}

	private static void darkForestBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.DARK_FOREST,
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x4B6754).withFoliageColor(0x3B5E3F), darkForestSpawning(), darkForestGen().withStructure(TFStructures.CONFIGURED_KNIGHT_STRONGHOLD))
						.temperature(0.7F)
						.downfall(0.8F)
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);

		biomes.put(BiomeKeys.DARK_FOREST_CENTER, // FIXME: colors
				biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x667540).withFoliageColor(0xF9821E), new MobSpawnInfo.Builder(), darkForestCenterGen().withStructure(TFStructures.CONFIGURED_DARK_TOWER))
						.depth(0.125F)
						.scale(0.05F)
						.build()
		);
	}

	private static void snowRegionBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.SNOWY_FOREST,
				biomeWithDefaults(defaultAmbientBuilder(), winterWolfSpawning(), snowyForestGen().withStructure(TFStructures.CONFIGURED_YETI_CAVE))
						.precipitation(Biome.RainType.SNOW)
						.temperature(0.09F)
						.downfall(0.9F)
						.depth(0.2F)
						.scale(0.2F)
						.build()
		);

		biomes.put(BiomeKeys.GLACIER,
				biomeWithDefaults(defaultAmbientBuilder(), penguinSpawning(), glacierGen().withStructure(TFStructures.CONFIGURED_AURORA_PALACE))
						.category(Biome.Category.ICY)
						.temperature(0.8F)
						.downfall(0.1F)
						.precipitation(Biome.RainType.SNOW)
						.build()
		);
	}

	private static void highlandsBiomes(ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes) {
		biomes.put(BiomeKeys.HIGHLANDS,
				biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), highlandsGen(defaultGenSettingBuilder().withStructure(TFStructures.CONFIGURED_TROLL_CAVE)))
						.category(Biome.Category.MESA)
						.temperature(0.4F)
						.downfall(0.7F)
						.depth(3.5F)
						.scale(0.05F)
						.build()
		);

		biomes.put(BiomeKeys.THORNLANDS,
				biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnInfo.Builder(), thornlandsGen(defaultGenSettingBuilder()))
						.category(Biome.Category.NONE)
						.temperature(0.3F)
						.downfall(0.2F)
						.depth(6F)
						.scale(0.1F)
						.build()
		);

		biomes.put(BiomeKeys.FINAL_PLATEAU,
				biomeWithDefaults(defaultAmbientBuilder(), ravenSpawning(), defaultGenSettingBuilder().withSurfaceBuilder(twilightforest.worldgen.ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU).withStructure(TFStructures.CONFIGURED_FINAL_CASTLE))
						.category(Biome.Category.MESA)
						.temperature(0.3F)
						.downfall(0.2F)
						.depth(10.5F)
						.scale(0.025F)
						.build()
		);
	}
}
