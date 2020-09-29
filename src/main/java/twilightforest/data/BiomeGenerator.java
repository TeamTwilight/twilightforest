package twilightforest.data;

import com.google.common.collect.ImmutableMap;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import twilightforest.TwilightForestMod;
import twilightforest.world.newfeature.TwilightFeatures;

import java.util.Map;

public final class BiomeGenerator extends BiomeDataHelper {
    public BiomeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected Map<ResourceLocation, Biome> generateBiomes() {
        final ImmutableMap.Builder<ResourceLocation, Biome> biomes = ImmutableMap.builder();
        // defaultBiomeBuilder() returns a Biome.Builder and Biome.Builder#build() builds it

        BiomeGenerationSettings.Builder defaultBiomeGenerationSettings = defaultGenSettingBuilder();

        defaultBiomeGenerationSettings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.DEFAULT_TWILIGHT_TREES);

        biomes.put(TwilightForestMod.prefix("forest"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopy(defaultGenSettingBuilder()))
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("dense_forest"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0x005522), defaultMobSpawning(), addCanopy(addCanopy(defaultGenSettingBuilder())))
                        .temperature(0.7F)
                        .downfall(0.8F)
                        .depth(0.2F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("firefly_forest"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), addCanopyFirefly(defaultGenSettingBuilder()))
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("clearing"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder())
                        .category(Biome.Category.PLAINS)
                        .temperature(0.8F)
                        .downfall(0.4F)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("oak_savannah"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.SAVANNA)
                        .temperature(0.9F)
                        .downfall(0)
                        .depth(0.2F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("mushroom_forest"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0xC0FFD8).setWaterFogColor(0x3F76E4), defaultMobSpawning(), addMushroomCanopy(defaultGenSettingBuilder(), 0.2f))
                        .temperature(0.8F)
                        .downfall(0.8F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("dense_mushroom_forest"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0xC0FFD8).setWaterFogColor(0x3F76E4), defaultMobSpawning(), addMushroomCanopy(defaultGenSettingBuilder(), 0.9f))
                        .temperature(0.8F)
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("spooky_forest"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0xFA9111), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .downfall(1)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("enchanted_forest"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), modify(defaultGenSettingBuilder(), c -> c.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.RAINBOAK_TREE.func_242728_a())))
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("stream"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.RIVER)
                        .depth(-0.7F)
                        .scale(0)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("lake"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0xC0FFD8).setWaterFogColor(0x3F76E4), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.OCEAN)
                        .temperature(0.66F)
                        .downfall(1)
                        .depth(-1.8F)
                        .scale(0.1F)
                        .build()
        );

        BiomeGenerationSettings.Builder swampGenerationBuilder = modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.MANGROVE_TREE.func_242728_a()));

        biomes.put(TwilightForestMod.prefix("swamp"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0xE0FFAE), defaultMobSpawning(), swampGenerationBuilder)
                        .category(Biome.Category.SWAMP)
                        .temperature(0.8F)
                        .downfall(0.9F)
                        .depth(-0.125F)
                        .scale(0.125F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("fire_swamp"),
                biomeWithDefaults(defaultAmbientBuilder().setWaterColor(0x6C2C2C), defaultMobSpawning(), swampGenerationBuilder)
                        .category(Biome.Category.SWAMP)
                        .temperature(1)
                        .downfall(0.4F)
                        .build()
        );

        BiomeGenerationSettings.Builder darkForestGenerationBuilder = modify(defaultGenSettingBuilder(), b -> b.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, TwilightFeatures.ConfiguredFeatures.DARKWOOD_TREE.func_242728_a()));

        biomes.put(TwilightForestMod.prefix("dark_forest"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), darkForestGenerationBuilder)
                        .temperature(0.7F)
                        .downfall(0.8F)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("dark_forest_center"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), darkForestGenerationBuilder)
                        .depth(0.125F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("snowy_forest"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), modify(defaultGenSettingBuilder(), DefaultBiomeFeatures::withMountainEdgeTrees))
                        .temperature(0.09F)
                        .downfall(0.9F)
                        .depth(0.2F)
                        .precipitation(Biome.RainType.SNOW)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("glacier"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultGenSettingBuilder())
                        .category(Biome.Category.ICY)
                        .temperature(0)
                        .downfall(0.1F)
                        .precipitation(Biome.RainType.SNOW)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("highlands"),
                biomeWithDefaults(defaultAmbientBuilder(), defaultMobSpawning(), defaultBiomeGenerationSettings)
                        .category(Biome.Category.MESA)
                        .temperature(0.4F)
                        .downfall(0.7F)
                        .depth(3.5F)
                        .scale(0.05F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("thornlands"),
                biomeWithDefaults(defaultAmbientBuilder(), new MobSpawnInfo.Builder(), defaultGenSettingBuilder())
                        .category(Biome.Category.NONE)
                        .temperature(0.3F)
                        .downfall(0.2F)
                        .depth(6)
                        .scale(0.1F)
                        .build()
        );

        biomes.put(TwilightForestMod.prefix("final_plateau"),
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
