package twilightforest.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.TFBiomeProvider;

public enum GenLayerTFThornBorder implements IThornsTransformer {
	INSTANCE;

	private Registry<Biome> registry;

	GenLayerTFThornBorder() { }

	public GenLayerTFThornBorder setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int apply(INoiseRandom noise, int up, int left, int down, int right, int center, int nw, int sw, int se, int ne) {
		int highlandsCenter = TFBiomeProvider.getBiomeId(TFBiomes.finalPlateau, registry);
		int thornlands      = TFBiomeProvider.getBiomeId(TFBiomes.thornlands, registry);

		if (onBorder(highlandsCenter, center, right, left, up, down)) {
			return thornlands;
		} else if (onBorder(highlandsCenter, center, ne, nw, se, sw)) {
			return thornlands;
		} else {
			return center;
		}
	}

	/**
	 * Returns true if the center biome is not the specified biome and any of the surrounding biomes are the specified biomes
	 */
	private boolean onBorder(int biomeID, int center, int right, int left, int up, int down) {
		if (center == biomeID) {
			return false;
		} else if (right == biomeID) {
			return true;
		} else if (left == biomeID) {
			return true;
		} else if (up == biomeID) {
			return true;
		} else if (down == biomeID) {
			return true;
		} else {
			return false;
		}
	}
}
