package twilightforest.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import twilightforest.world.layer.*;

import java.util.function.LongFunction;

public class TFBiomeDistributor extends BiomeProvider {
    public static final Codec<TFBiomeDistributor> tfBiomeProviderCodec = RecordCodecBuilder.create((instance) ->
            instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((obj) -> obj.seed))
                    .apply(instance, instance.stable(TFBiomeDistributor::new)));

    private final long seed;
    private final Layer genBiomes;

    //private static final List<Biome> BIOMES = ImmutableList.of( //TODO: Can we do this more efficiently?
    //        TFBiomes.tfLake.get(),
    //        TFBiomes.stream.get(),
    //        TFBiomes.clearing.get(),
    //        TFBiomes.oakSavanna.get(),
    //        TFBiomes.twilightForest.get(),
    //        TFBiomes.denseTwilightForest.get(),
    //        TFBiomes.fireflyForest.get(),
    //        TFBiomes.mushrooms.get(),
    //        TFBiomes.deepMushrooms.get(),
    //        TFBiomes.enchantedForest.get(),
    //        TFBiomes.tfSwamp.get(),
    //        TFBiomes.fireSwamp.get(),
    //        TFBiomes.darkForest.get(),
    //        TFBiomes.darkForestCenter.get(),
    //        TFBiomes.snowy_forest.get(),
    //        TFBiomes.glacier.get(),
    //        TFBiomes.thornlands.get(),
    //        TFBiomes.highlandsCenter.get(),
    //        TFBiomes.highlands.get()
    //);

    //private static final List<Biome> SAFE_BIOMES = ImmutableList.of(
    //        TFBiomes.clearing.get(),
    //        TFBiomes.oakSavanna.get(),
    //        TFBiomes.twilightForest.get(),
    //        TFBiomes.denseTwilightForest.get(),
    //        TFBiomes.fireflyForest.get(),
    //        TFBiomes.mushrooms.get(),
    //        TFBiomes.deepMushrooms.get(),
    //        TFBiomes.enchantedForest.get()
    //);

    protected TFBiomeDistributor(long seed) {
        super(ImmutableList.of()); // TODO Provide actual populated biomes array, not an empty one!
        this.seed = seed;

        genBiomes = makeLayers(seed);
    }

    public static Layer makeLayers(long rawSeed) {
        LongFunction<LazyAreaLayerContext> seed = (context) -> new LazyAreaLayerContext(25, rawSeed, context);

        long iterativeSeed = 1000L;

        IAreaFactory<LazyArea> biomes = GenLayerTFBiomes.INSTANCE.apply(seed.apply(1L));
        biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);
        biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);

        biomes = ZoomLayer.NORMAL.apply(seed.apply(iterativeSeed++), biomes);
        biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);

        //biomes = GenLayerTFKeyBiomes.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFCompanionBiomes.INSTANCE.apply(seed.apply(1000L), biomes);

        //biomes = GenLayerTFThornBorder.INSTANCE.apply(seed.apply(500L), biomes);

        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);
        //biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(1000L), biomes);  24419 25466

        //IAreaFactory<LazyArea> riverLayer = GenLayerTFStream.INSTANCE.apply(seed.apply(1L), biomes);
        //riverLayer = SmoothLayer.INSTANCE.apply(seed.apply(7000L), riverLayer);
        //biomes = GenLayerTFRiverMix.INSTANCE.apply(seed.apply(100L), biomes, riverLayer);

        return new Layer(biomes);
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return tfBiomeProviderCodec;
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return new TFBiomeDistributor(seed);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        //y = y / 8 + 32;
        //return BIOMES.get((int) (Math.sqrt(x*x+y*y+z*z)/2.0d) % BIOMES.size());

        //return genBiomes.func_215738_a(x, z);
        throw new UnsupportedOperationException("WHAT DO"); // FIXME
    }

    //@Override
    //public List<Biome> getBiomesToSpawnIn() {
    //    return SAFE_BIOMES;
    //}
}
