package twilightforest.init.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.world.components.layer.*;
import twilightforest.world.components.layer.vanillalegacy.*;
import twilightforest.world.registration.biomes.BiomeMaker;

import java.util.List;


public class BiomeLayerStack {
    public static final Codec<BiomeLayerFactory> DISPATCH_CODEC = BiomeLayerTypes.CODEC.dispatch("layer_type", BiomeLayerFactory::getType, BiomeLayerType::getCodec);
    public static final Codec<Holder<BiomeLayerFactory>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC, true);

    public static final ResourceKey<BiomeLayerFactory> RANDOM_FOREST_BIOMES = registerKey("random_forest_biomes");
    public static final ResourceKey<BiomeLayerFactory> BIOMES_ALONG_STREAMS = registerKey("biomes_along_streams");

    public static final ResourceKey<BiomeDensitySource> BIOME_GRID = ResourceKey.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, TwilightForestMod.prefix("biome_grid"));

    public static ResourceKey<BiomeLayerFactory> registerKey(String name) {
        return ResourceKey.create(TFRegistries.Keys.BIOME_STACK, TwilightForestMod.prefix(name));
    }

    public static void bootstrap(BootstapContext<BiomeLayerFactory> context) {
        BiomeLayerFactory biomes = new RandomBiomeLayer.Factory(1L, 15, ImmutableList.of(
                TFBiomes.FOREST,
                TFBiomes.DENSE_FOREST,
                TFBiomes.MUSHROOM_FOREST,
                TFBiomes.OAK_SAVANNAH,
                TFBiomes.FIREFLY_FOREST
        ), ImmutableList.of(
                TFBiomes.LAKE,
                TFBiomes.DENSE_MUSHROOM_FOREST,
                TFBiomes.ENCHANTED_FOREST,
                TFBiomes.CLEARING,
                TFBiomes.SPOOKY_FOREST
        ));

        biomes = new KeyBiomesLayer.Factory(1000L, List.of(TFBiomes.GLACIER, TFBiomes.FIRE_SWAMP, TFBiomes.DARK_FOREST_CENTER, TFBiomes.FINAL_PLATEAU), Holder.direct(biomes));
        biomes = new CompanionBiomesLayer.Factory(1000L, List.of(
                Pair.of(TFBiomes.FIRE_SWAMP, TFBiomes.SWAMP),
                Pair.of(TFBiomes.GLACIER, TFBiomes.SNOWY_FOREST),
                Pair.of(TFBiomes.DARK_FOREST_CENTER, TFBiomes.DARK_FOREST),
                Pair.of(TFBiomes.FINAL_PLATEAU, TFBiomes.HIGHLANDS)
        ), Holder.direct(biomes));

        biomes = new ZoomLayer.Factory(1000L, false, Holder.direct(biomes));
        biomes = new ZoomLayer.Factory(1001L, false, Holder.direct(biomes));

        biomes = new StabilizeLayer.Factory(700L, Holder.direct(biomes));

        biomes = new BorderLayer.Factory(500L, TFBiomes.FINAL_PLATEAU, TFBiomes.THORNLANDS, Holder.direct(biomes));

        biomes = new ZoomLayer.Factory(1002L, false, Holder.direct(biomes));
        biomes = new ZoomLayer.Factory(1003L, false, Holder.direct(biomes));
        biomes = new ZoomLayer.Factory(1004L, false, Holder.direct(biomes));
        biomes = new ZoomLayer.Factory(1005L, false, Holder.direct(biomes));

        Holder.Reference<BiomeLayerFactory> randomBiomes = context.register(RANDOM_FOREST_BIOMES, biomes);

        BiomeLayerFactory riverLayer = new SeamLayer.Factory(1L, TFBiomes.STREAM, List.of(TFBiomes.LAKE, TFBiomes.THORNLANDS, TFBiomes.CLEARING, TFBiomes.OAK_SAVANNAH), List.of(
                Pair.of(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER),
                Pair.of(TFBiomes.MUSHROOM_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST),
                Pair.of(TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP),
                Pair.of(TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER),
                Pair.of(TFBiomes.HIGHLANDS, TFBiomes.FINAL_PLATEAU)
        ), randomBiomes);
        riverLayer = new SmoothLayer.Factory(7000L, Holder.direct(riverLayer));

        context.register(BIOMES_ALONG_STREAMS, new FilteredBiomeLayer.Factory(100L, TFBiomes.STREAM, Holder.direct(riverLayer), randomBiomes));
    }

    public static void bootstrapData(BootstapContext<BiomeDensitySource> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);

        context.register(BIOME_GRID, new BiomeDensitySource(
                BiomeMaker.makeBiomeList(biomeRegistry, biomeRegistry.getOrThrow(TFBiomes.UNDERGROUND)),
                context.lookup(TFRegistries.Keys.BIOME_STACK).getOrThrow(BiomeLayerStack.BIOMES_ALONG_STREAMS)
        ));
    }
}
