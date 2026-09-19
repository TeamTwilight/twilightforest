package twilightforest.datagen.data.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.init.TFBiomes;

public class TFBiomeGenerator {

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(TFBiomes.FOREST, BiomeHelper.twilightForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.DENSE_FOREST, BiomeHelper.denseForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.FIREFLY_FOREST, BiomeHelper.fireflyForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.CLEARING, BiomeHelper.clearing(featureGetter, carverGetter).build());
        context.register(TFBiomes.OAK_SAVANNAH, BiomeHelper.oakSavanna(featureGetter, carverGetter).build());

        context.register(TFBiomes.MUSHROOM_FOREST, BiomeHelper.mushroomForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.DENSE_MUSHROOM_FOREST, BiomeHelper.denseMushroomForest(featureGetter, carverGetter).build());

        context.register(TFBiomes.SPOOKY_FOREST, BiomeHelper.spookyForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.ENCHANTED_FOREST, BiomeHelper.enchantedForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.STREAM, BiomeHelper.stream(featureGetter, carverGetter).build());
        context.register(TFBiomes.LAKE, BiomeHelper.lake(featureGetter, carverGetter).build());

        context.register(TFBiomes.SWAMP, BiomeHelper.swamp(featureGetter, carverGetter).build());
        context.register(TFBiomes.FIRE_SWAMP, BiomeHelper.fireSwamp(featureGetter, carverGetter).build());

        context.register(TFBiomes.DARK_FOREST, BiomeHelper.darkForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.DARK_FOREST_CENTER, BiomeHelper.darkForestCenter(featureGetter, carverGetter).build());

        context.register(TFBiomes.SNOWY_FOREST, BiomeHelper.snowyForest(featureGetter, carverGetter).build());
        context.register(TFBiomes.GLACIER, BiomeHelper.glacier(featureGetter, carverGetter).build());

        context.register(TFBiomes.HIGHLANDS, BiomeHelper.highlands(featureGetter, carverGetter).build());
        context.register(TFBiomes.HIGHLANDS_UNDERGROUND, BiomeHelper.highlandsUnderground(featureGetter, carverGetter).build());
        context.register(TFBiomes.THORNLANDS, BiomeHelper.thornlands(featureGetter, carverGetter).build());
        context.register(TFBiomes.FINAL_PLATEAU, BiomeHelper.finalPlateau(featureGetter, carverGetter).build());

        context.register(TFBiomes.UNDERGROUND, BiomeHelper.underground(featureGetter, carverGetter).build());
    }

}
