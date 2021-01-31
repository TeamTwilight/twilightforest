package twilightforest.data;

import com.google.common.collect.ImmutableMap;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import twilightforest.TFStructures;
import twilightforest.biomes.TFBiomes;
import twilightforest.features.TwilightFeatures;

import java.util.Map;

public final class BiomeGenerator extends BiomeDataHelper {
    public BiomeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Map<RegistryKey<Biome>, Biome> generateBiomes() {
        final ImmutableMap.Builder<RegistryKey<Biome>, Biome> biomes = ImmutableMap.builder();
        // defaultBiomeBuilder() returns a Biome.Builder and Biome.Builder#build() builds it

        BiomeGenerationSettings.Builder defaultBiomeGenerationSettings = defaultGenSettingBuilder();

        defaultBiomeGenerationSettings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.DEFAULT_TWILIGHT_TREES);

        biomes.put(TFBiomes.twilightForest,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopy(defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD)))
                        .build()
        );

        biomes.put(TFBiomes.denseTwilightForest,
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0x005522), defaultMobSpawning(), addCanopy(addCanopy(defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD))))
                        .temperature(0.7F)
                        .downfall(0.8F)
                        .depth(0.2F)
						.scale(0.2F)
                        .build()
        );

        biomes.put(TFBiomes.fireflyForest,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopyFirefly(defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD)))
						.temperature(0.5F)
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TFBiomes.clearing,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD))
                        .category(Biome.Category.PLAINS)
                        .temperature(0.8F)
                        .downfall(0.4F)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TFBiomes.oakSavanna,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), modify(defaultBiomeGenerationSettings, c -> c
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD)))
                        .category(Biome.Category.SAVANNA)
                        .temperature(0.9F)
                        .downfall(0)
                        .depth(0.2F)
						.scale(0.2F)
                        .build()
        );

        biomes.put(TFBiomes.mushrooms,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addMushroomCanopy(defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD), 0.2f))
                        .temperature(0.8F)
                        .downfall(0.8F)
                        .build()
        );

        biomes.put(TFBiomes.deepMushrooms,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addMushroomCanopy(defaultGenSettingBuilder()
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD), 0.9f))
                        .temperature(0.8F)
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TFBiomes.spookyForest,
                biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0xC45123).withFoliageColor(0xFF8501).setWaterColor(0xFA9111), defaultMobSpawning(), modify(defaultBiomeGenerationSettings, c -> c
						.withStructure(TFStructures.CONFIGURED_NAGA_COURTYARD)))
						.temperature(0.5F)
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TFBiomes.enchantedForest, // FIXME: colors
                biomeWithDefaults(defaultAmbientBuilder().withFoliageColor(0x00FFFF).withGrassColor(0x00FFFF), defaultMobSpawning(), modify(defaultGenSettingBuilder(), c -> c
						.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.RAINBOAK_TREE.square())))
                        .build()
        );

        biomes.put(TFBiomes.stream,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.RIVER)
						.temperature(0.5F)
						.downfall(0.1F)
                        .depth(-0.8F)
                        .scale(0)
                        .build()
        );

        biomes.put(TFBiomes.tfLake,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.OCEAN)
                        .temperature(0.66F)
                        .downfall(1)
                        .depth(-1.8F)
                        .scale(0.1F)
                        .build()
        );

        BiomeGenerationSettings.Builder swampGenerationBuilder = modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.MANGROVE_TREE.square()));

        biomes.put(TFBiomes.tfSwamp,
                biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x5C694E).withFoliageColor(0x496137).setWaterColor(0xE0FFAE), defaultMobSpawning(), swampGenerationBuilder)
                        .category(Biome.Category.SWAMP)
                        .temperature(0.8F)
                        .downfall(0.9F)
                        .depth(-0.25F)
                        .scale(0.25F)
                        .build()
        );

        biomes.put(TFBiomes.fireSwamp,
                biomeWithDefaults(whiteAshParticles(defaultAmbientBuilder().withGrassColor(0x572E23).withFoliageColor(0x64260F).setWaterColor(0x6C2C2C)), defaultMobSpawning(), swampGenerationBuilder)
                        .category(Biome.Category.SWAMP)
                        .temperature(1)
                        .downfall(0.4F)
						.depth(0.1F)
						.scale(0.2F)
                        .build()
        );

        BiomeGenerationSettings.Builder darkForestGenerationBuilder = modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.DARKWOOD_TREE.square()));

        biomes.put(TFBiomes.darkForest,
                biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x4B6754).withFoliageColor(0x3B5E3F), defaultMobSpawning(), darkForestGenerationBuilder)
                        .temperature(0.7F)
                        .downfall(0.8F)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TFBiomes.darkForestCenter, // FIXME: colors
                biomeWithDefaults(defaultAmbientBuilder().withGrassColor(0x667540).withFoliageColor(0xF9821E), defaultMobSpawning(), darkForestGenerationBuilder)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

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

        biomes.put(TFBiomes.highlands,
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
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

        return biomes.build();
    }
}
