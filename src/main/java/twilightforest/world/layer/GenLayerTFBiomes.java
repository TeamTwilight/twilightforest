package twilightforest.world.layer;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import twilightforest.worldgen.biomes.BiomeKeys;
import twilightforest.world.TFBiomeProvider;

import java.util.List;

/**
 * Applies the twilight forest biomes to the map
 *
 * @author Ben
 */
public enum GenLayerTFBiomes implements IAreaTransformer0 {
	INSTANCE;
	private static final int RARE_BIOME_CHANCE = 15;

	protected static final List<RegistryKey<Biome>> commonBiomes = ImmutableList.of(
			BiomeKeys.FOREST,
			BiomeKeys.DENSE_FOREST,
			BiomeKeys.MUSHROOM_FOREST,
			BiomeKeys.OAK_SAVANNAH,
			BiomeKeys.FIREFLY_FOREST
	);
	protected static final List<RegistryKey<Biome>> rareBiomes = ImmutableList.of(
			BiomeKeys.LAKE,
			BiomeKeys.DENSE_MUSHROOM_FOREST,
			BiomeKeys.ENCHANTED_FOREST,
			BiomeKeys.CLEARING,
			BiomeKeys.SPOOKY_FOREST
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
