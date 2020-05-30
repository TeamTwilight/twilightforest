package twilightforest.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.layer.GenLayerTFBiomeStabilize;
import twilightforest.world.layer.GenLayerTFBiomes;
import twilightforest.world.layer.GenLayerTFCompanionBiomes;
import twilightforest.world.layer.GenLayerTFKeyBiomes;
import twilightforest.world.layer.GenLayerTFRiverMix;
import twilightforest.world.layer.GenLayerTFStream;
import twilightforest.world.layer.GenLayerTFThornBorder;

import java.util.Set;
import java.util.function.LongFunction;

public class TFBiomeProvider extends BiomeProvider {

	private final Layer genBiomes;
	private final TFBiomeCache mapCache;
	private static final Set<Biome> BIOMES = ImmutableSet.of( //TODO: Can we do this more efficiently?
			TFBiomes.tfLake.get(),
			TFBiomes.twilightForest.get(),
			TFBiomes.denseTwilightForest.get(),
			TFBiomes.highlands.get(),
			TFBiomes.mushrooms.get(),
			TFBiomes.tfSwamp.get(),
			TFBiomes.stream.get(),
			TFBiomes.snowy_forest.get(),
			TFBiomes.glacier.get(),
			TFBiomes.clearing.get(),
			TFBiomes.oakSavanna.get(),
			TFBiomes.fireflyForest.get(),
			TFBiomes.deepMushrooms.get(),
			TFBiomes.darkForest.get(),
			TFBiomes.enchantedForest.get(),
			TFBiomes.fireSwamp.get(),
			TFBiomes.darkForestCenter.get(),
			TFBiomes.highlandsCenter.get(),
			TFBiomes.thornlands.get(),
			TFBiomes.spookyForest.get()
	);

	public TFBiomeProvider(TFBiomeProviderSettings world) {
		super(BIOMES);
		getBiomesToSpawnIn().clear();
		getBiomesToSpawnIn().add(TFBiomes.twilightForest.get());
		getBiomesToSpawnIn().add(TFBiomes.denseTwilightForest.get());
		getBiomesToSpawnIn().add(TFBiomes.clearing.get());
		getBiomesToSpawnIn().add(TFBiomes.tfSwamp.get());
		getBiomesToSpawnIn().add(TFBiomes.mushrooms.get());

		genBiomes = makeLayers(world.getSeed());
		mapCache = new TFBiomeCache(this, 512, true);
	}

	private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> makeLayers(LongFunction<C> seed) {
		IAreaFactory<T> biomes = new GenLayerTFBiomes().apply(seed.apply(1L));
		biomes = GenLayerTFKeyBiomes.INSTANCE.apply(seed.apply(1000L), biomes);
		biomes = GenLayerTFCompanionBiomes.INSTANCE.apply(seed.apply(1000L), biomes);

		biomes = ZoomLayer.NORMAL.apply(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.apply(seed.apply(1001), biomes);

		biomes = GenLayerTFBiomeStabilize.INSTANCE.apply(seed.apply(700L), biomes);

		biomes = GenLayerTFThornBorder.INSTANCE.apply(seed.apply(500L), biomes);

		biomes = ZoomLayer.NORMAL.apply(seed.apply(1002), biomes);
		biomes = ZoomLayer.NORMAL.apply(seed.apply(1003), biomes);
		biomes = ZoomLayer.NORMAL.apply(seed.apply(1004), biomes);
		biomes = ZoomLayer.NORMAL.apply(seed.apply(1005), biomes);

		IAreaFactory<T> riverLayer = GenLayerTFStream.INSTANCE.apply(seed.apply(1L), biomes);
		riverLayer = SmoothLayer.INSTANCE.apply(seed.apply(7000L), riverLayer);
		biomes = GenLayerTFRiverMix.INSTANCE.apply(seed.apply(100L), biomes, riverLayer);

		return biomes;

		// do "voronoi" zoom
//		Layer genlayervoronoizoom = new VoroniZoomLayer(10L, biomes);

//		biomes.initWorldGenSeed(seed);
//		genlayervoronoizoom.initWorldGenSeed(seed);
//
//		genBiomes = biomes;
//		biomeIndexLayer = genlayervoronoizoom;

//		Layer biomes = new GenLayerTFBiomes(1L);
//		biomes = new GenLayerTFKeyBiomes(1000L, biomes);
//		biomes = new GenLayerTFCompanionBiomes(1000L, biomes);
//
//		biomes = new GenLayerZoom(1000L, biomes);
//		biomes = new GenLayerZoom(1001, biomes);
//
//		biomes = new GenLayerTFBiomeStabilize(700L, biomes);
//
//		biomes = new GenLayerTFThornBorder(500L, biomes);
//
//		biomes = new GenLayerZoom(1002, biomes);
//		biomes = new GenLayerZoom(1003, biomes);
//		biomes = new GenLayerZoom(1004, biomes);
//		biomes = new GenLayerZoom(1005, biomes);
//
//		Layer riverLayer = new GenLayerTFStream(1L, biomes);
//		riverLayer = new GenLayerSmooth(7000L, riverLayer);
//		biomes = new GenLayerTFRiverMix(100L, biomes, riverLayer);
//
//		// do "voronoi" zoom
//		Layer genlayervoronoizoom = new VoroniZoomLayer(10L, biomes);
//
//		biomes.initWorldGenSeed(seed);
//		genlayervoronoizoom.initWorldGenSeed(seed);
//
//		genBiomes = biomes;
//		biomeIndexLayer = genlayervoronoizoom;
	}

	public static Layer makeLayers(long seed) {
		IAreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaLayerContext(25, seed, context));
		return new Layer(areaFactory);
	}

	@Override
	public Biome getBiomeForNoiseGen(int x, int y, int z) {
		return this.genBiomes.func_215738_a(x, z);
	}

//	@Override
//	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
//		return getBiomesForGeneration(biomes, x, z, width, height, true);
//	}

//	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height, boolean useCache) {
//		// for grid-centred magic maps, get from map cache
//		if (useCache && mapCache.isGridAligned(x, z, width, height)) {
//			Biome[] cached = mapCache.getBiomes(x, z);
//			return Arrays.copyOf(cached, cached.length);
//		}
//		return super.getBiomesForGeneration(biomes, x, z, width, height);
//	}

//	@Override
//	public void cleanupCache() {
//		mapCache.cleanup();
//		super.cleanupCache();
//	}
}
