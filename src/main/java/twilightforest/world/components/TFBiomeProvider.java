package twilightforest.world.components;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.SmoothLayer;
import net.minecraft.world.level.newbiome.layer.ZoomLayer;
import twilightforest.world.registration.TFDimensions;
import twilightforest.world.registration.biomes.BiomeKeys;
import twilightforest.world.components.layer.GenLayerTFBiomeStabilize;
import twilightforest.world.components.layer.GenLayerTFBiomes;
import twilightforest.world.components.layer.GenLayerTFCompanionBiomes;
import twilightforest.world.components.layer.GenLayerTFKeyBiomes;
import twilightforest.world.components.layer.GenLayerTFRiverMix;
import twilightforest.world.components.layer.GenLayerTFStream;
import twilightforest.world.components.layer.GenLayerTFThornBorder;

import java.util.List;
import java.util.Optional;
import java.util.function.LongFunction;

public class TFBiomeProvider extends BiomeSource {
	public static final Codec<TFBiomeProvider> TF_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.LONG.fieldOf("seed").stable().orElseGet(() -> TFDimensions.seed).forGetter((obj) -> obj.seed),
			RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.registry)
	).apply(instance, instance.stable(TFBiomeProvider::new)));

	private static final List<ResourceKey<Biome>> BIOMES = ImmutableList.of( //TODO: Can we do this more efficiently?
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

	private final Registry<Biome> registry;
	private final Layer genBiomes;
	private final long seed;

	public TFBiomeProvider(long seed, Registry<Biome> registryIn) {
		super(BIOMES
				.stream()
				.map(ResourceKey::location)
				.map(registryIn::getOptional)
				.filter(Optional::isPresent)
				.map(opt -> opt::get)
		);

		this.seed = seed;
		//getBiomesToSpawnIn().clear();
		//getBiomesToSpawnIn().add(TFBiomes.twilightForest.get());
		//getBiomesToSpawnIn().add(TFBiomes.denseTwilightForest.get());
		//getBiomesToSpawnIn().add(TFBiomes.clearing.get());
		//getBiomesToSpawnIn().add(TFBiomes.tfSwamp.get());
		//getBiomesToSpawnIn().add(TFBiomes.mushrooms.get());

		registry = registryIn;
		genBiomes = makeLayers(seed, registryIn);
	}

	public static int getBiomeId(ResourceKey<Biome> biome, Registry<Biome> registry) {
		return registry.getId(registry.get(biome));
	}

	private static <T extends Area, C extends BigContext<T>> AreaFactory<T> makeLayers(LongFunction<C> seed, Registry<Biome> registry, long rawSeed) {
		AreaFactory<T> biomes = GenLayerTFBiomes.INSTANCE.setup(registry).run(seed.apply(1L));
		biomes = GenLayerTFKeyBiomes.INSTANCE.setup(registry, rawSeed).run(seed.apply(1000L), biomes);
		biomes = GenLayerTFCompanionBiomes.INSTANCE.setup(registry).run(seed.apply(1000L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1000L), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1001L), biomes);

		biomes = GenLayerTFBiomeStabilize.INSTANCE.run(seed.apply(700L), biomes);

		biomes = GenLayerTFThornBorder.INSTANCE.setup(registry).run(seed.apply(500L), biomes);

		biomes = ZoomLayer.NORMAL.run(seed.apply(1002), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1003), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1004), biomes);
		biomes = ZoomLayer.NORMAL.run(seed.apply(1005), biomes);

		AreaFactory<T> riverLayer = GenLayerTFStream.INSTANCE.setup(registry).run(seed.apply(1L), biomes);
		riverLayer = SmoothLayer.INSTANCE.run(seed.apply(7000L), riverLayer);
		biomes = GenLayerTFRiverMix.INSTANCE.setup(registry).run(seed.apply(100L), biomes, riverLayer);

		return biomes;
	}
	
	public static Layer makeLayers(long seed, Registry<Biome> registry) {
		AreaFactory<LazyArea> areaFactory = makeLayers((context) -> new LazyAreaContext(25, seed, context), registry, seed);
		// Debug code to render an image of the biome layout within the ide
		/*final Map<Integer, Integer> remapColors = new HashMap<>();
		remapColors.put(getBiomeId(TFBiomes.tfLake, registry), 0x0000FF);
		remapColors.put(getBiomeId(TFBiomes.twilightForest, registry), 0x00FF00);
		remapColors.put(getBiomeId(TFBiomes.denseTwilightForest, registry), 0x00AA00);
		remapColors.put(getBiomeId(TFBiomes.highlands, registry), 0xCC6900);
		remapColors.put(getBiomeId(TFBiomes.mushrooms, registry), 0xcc008b);
		remapColors.put(getBiomeId(TFBiomes.tfSwamp, registry), 0x00ccbb);
		remapColors.put(getBiomeId(TFBiomes.stream, registry), 0x0000FF);
		remapColors.put(getBiomeId(TFBiomes.snowy_forest, registry), 0xFFFFFF);
		remapColors.put(getBiomeId(TFBiomes.glacier, registry), 0x82bff5);
		remapColors.put(getBiomeId(TFBiomes.clearing, registry), 0x84f582);
		remapColors.put(getBiomeId(TFBiomes.oakSavanna, registry), 0xeff582);
		remapColors.put(getBiomeId(TFBiomes.fireflyForest, registry), 0x58fc66);
		remapColors.put(getBiomeId(TFBiomes.deepMushrooms, registry), 0xb830b8);
		remapColors.put(getBiomeId(TFBiomes.darkForest, registry), 0x193d0d);
		remapColors.put(getBiomeId(TFBiomes.enchantedForest, registry), 0x00FFFF);
		remapColors.put(getBiomeId(TFBiomes.fireSwamp, registry), 0xFF0000);
		remapColors.put(getBiomeId(TFBiomes.darkForestCenter, registry), 0xFFFF00);
		remapColors.put(getBiomeId(TFBiomes.finalPlateau, registry), 0x000000);
		remapColors.put(getBiomeId(TFBiomes.thornlands, registry), 0x3d250d);
		remapColors.put(getBiomeId(TFBiomes.spookyForest, registry), 0x7700FF);
		BufferedImage image = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
		Graphics2D display = image.createGraphics();
		LazyArea area = areaFactory.make();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int z = 0; z < image.getHeight(); z++) {
				int c = area.getValue(x, z);
				display.setColor(new Color(remapColors.getOrDefault(c, c)));
				display.drawRect(x, z, 1, 1);
			}
		}
 		System.out.println("breakpoint");*/
		return new Layer(areaFactory) {
			@Override
			public Biome get(Registry<Biome> p_242936_1_, int p_242936_2_, int p_242936_3_) {
				int i = this.area.get(p_242936_2_, p_242936_3_);
				Biome biome = registry.byId(i);
				if (biome == null)
					throw new IllegalStateException("Unknown biome id emitted by layers: " + i);
				return biome;
			}
		};
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return TF_CODEC;
	}

	@Override
	public BiomeSource withSeed(long l) {
		return new TFBiomeProvider(l, registry);
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return genBiomes.get(registry, x, z);
	}
}
