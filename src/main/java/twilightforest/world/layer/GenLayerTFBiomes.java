package twilightforest.world.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.TFBiomeProvider;

import java.util.List;
import java.util.function.Supplier;

/**
 * Applies the twilight forest biomes to the map
 *
 * @author Ben
 */
public enum GenLayerTFBiomes implements IAreaTransformer0 {
	INSTANCE;
	private static final int RARE_BIOME_CHANCE = 15;

	protected static final List<RegistryKey<Biome>> commonBiomes = ImmutableList.of(
			TFBiomes.twilightForest,
			TFBiomes.denseTwilightForest,
			TFBiomes.mushrooms,
			TFBiomes.oakSavanna,
			TFBiomes.fireflyForest
	);
	protected static final List<RegistryKey<Biome>> rareBiomes = ImmutableList.of(
			TFBiomes.tfLake,
			TFBiomes.deepMushrooms,
			TFBiomes.enchantedForest,
			TFBiomes.clearing,
			TFBiomes.spookyForest
	);

	private Registry<Biome> registry;

	public GenLayerTFBiomes setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}

	GenLayerTFBiomes() {

	}

	@Override
	public int apply(INoiseRandom iNoiseRandom, int x, int y) {
		//return 0; //getRandomBiome(iNoiseRandom, commonBiomes));

		if (iNoiseRandom.random(RARE_BIOME_CHANCE) == 0) {
			// make rare biome
			return getRandomBiome(iNoiseRandom, rareBiomes);
		} else {
			// make common biome
			return getRandomBiome(iNoiseRandom, commonBiomes);
		}
	}

	private int getRandomBiome(INoiseRandom random, List<RegistryKey<Biome>> biomes) {
		return TFBiomeProvider.getBiomeId(biomes.get(random.random(biomes.size())), registry);
	}
}
