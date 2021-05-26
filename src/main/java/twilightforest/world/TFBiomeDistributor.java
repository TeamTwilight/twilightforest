package twilightforest.world;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import twilightforest.world.layer.*;
import twilightforest.worldgen.biomes.BiomeKeys;

import java.util.List;
import java.util.Optional;
import java.util.function.LongFunction;

public class TFBiomeDistributor extends BiomeProvider {
    public static final Codec<TFBiomeDistributor> TF_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("seed").stable().orElseGet(() -> TFDimensions.seed).forGetter((obj) -> obj.seed),
            RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter(provider -> provider.registry)
    ).apply(instance, instance.stable(TFBiomeDistributor::new)));

    private final long seed;
    private final Layer genBiomes;
    private final Registry<Biome> registry;

    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of( //TODO: Can we do this more efficiently?
            BiomeKeys.LAKE,
            BiomeKeys.FOREST,
            BiomeKeys.DENSE_FOREST,
            BiomeKeys.HIGHLANDS,
            BiomeKeys.MUSHROOM_FOREST,
            BiomeKeys.SWAMP,
            BiomeKeys.STREAM,
            BiomeKeys.SNOWY_FOREST,
            BiomeKeys.GLACIER,
            BiomeKeys.CLEARING,
            BiomeKeys.OAK_SAVANNAH,
            BiomeKeys.FIREFLY_FOREST,
            BiomeKeys.DENSE_MUSHROOM_FOREST,
            BiomeKeys.DARK_FOREST,
            BiomeKeys.ENCHANTED_FOREST,
            BiomeKeys.FIRE_SWAMP,
            BiomeKeys.DARK_FOREST_CENTER,
            BiomeKeys.FINAL_PLATEAU,
            BiomeKeys.THORNLANDS,
            BiomeKeys.SPOOKY_FOREST
    );

    public TFBiomeDistributor(long seed, Registry<Biome> registryIn) {
        super(BIOMES
                .stream()
                .map(RegistryKey::getLocation)
                .map(registryIn::getOptional)
                .filter(Optional::isPresent)
                .map(opt -> opt::get)
        ); // TODO Provide actual populated biomes array, not an empty one!
        this.seed = seed;
        this.registry = registryIn;
        this.genBiomes = makeLayers(seed, registryIn);
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry) {
        IAreaFactory<T> biomes = GenLayerTFBiomes.INSTANCE.setup(registry).apply(seed.apply(1L));
        /*biomes = GenLayerTFKeyBiomes.INSTANCE.setup(registry).apply(seed.apply(1000L), biomes);
        biomes = GenLayerTFCompanionBiomes.INSTANCE.setup(registry).apply(seed.apply(1000L), biomes);

        biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
        biomes = ZoomLayer.NORMAL.apply(seed.apply(1001L), biomes);

        biomes = GenLayerTFBiomeStabilize.INSTANCE.apply(seed.apply(700L), biomes);

        biomes = GenLayerTFThornBorder.INSTANCE.setup(registry).apply(seed.apply(500L), biomes);

        biomes = ZoomLayer.NORMAL.apply(seed.apply(1002), biomes);
        biomes = ZoomLayer.NORMAL.apply(seed.apply(1003), biomes);
        biomes = ZoomLayer.NORMAL.apply(seed.apply(1004), biomes);
        biomes = ZoomLayer.NORMAL.apply(seed.apply(1005), biomes);

        IAreaFactory<T> riverLayer = GenLayerTFStream.INSTANCE.setup(registry).apply(seed.apply(1L), biomes);
        riverLayer = SmoothLayer.INSTANCE.apply(seed.apply(7000L), riverLayer);
        biomes = GenLayerTFRiverMix.INSTANCE.setup(registry).apply(seed.apply(100L), biomes, riverLayer);*/

        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);

        //biomes = ZoomLayer.NORMAL.apply(seed.apply(iterativeSeed++), biomes);
        //biomes = GenLayerTFMedian.INSTANCE.apply(seed.apply(iterativeSeed++), biomes);

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

        return biomes;
    }

    public static Layer makeLayers(long seed, Registry<Biome> registry) {
        IAreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaLayerContext(25, seed, context), registry);

        return new Layer(areaFactory) {
            @Override
            public Biome func_242936_a(Registry<Biome> p_242936_1_, int p_242936_2_, int p_242936_3_) {
                int i = this.field_215742_b.getValue(p_242936_2_, p_242936_3_);
                Biome biome = registry.getByValue(i);
                if (biome == null)
                    throw new IllegalStateException("Unknown biome id emitted by layers: " + i);
                return biome;
            }
        };
    }

    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return TF_CODEC;
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return new TFBiomeDistributor(seed, registry);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        //y = y / 8 + 32;
        //return BIOMES.get((int) (Math.sqrt(x*x+y*y+z*z)/2.0d) % BIOMES.size());

        return genBiomes.func_242936_a(registry, x, y);
        //throw new UnsupportedOperationException("WHAT DO"); // FIXME
    }

    //@Override
    //public List<Biome> getBiomesToSpawnIn() {
    //    return SAFE_BIOMES;
    //}
}
