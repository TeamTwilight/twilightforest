package twilightforest.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.TFBiomeProvider;

public enum GenLayerTFCompanionBiomes implements ICastleTransformer {
	INSTANCE;

	GenLayerTFCompanionBiomes() { }

	private Registry<Biome> registry;

	public GenLayerTFCompanionBiomes setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int apply(INoiseRandom noise, int up, int left, int down, int right, int center) {
		int fireSwamp        = TFBiomeProvider.getBiomeId(TFBiomes.fireSwamp, registry);
		int swamp            = TFBiomeProvider.getBiomeId(TFBiomes.tfSwamp, registry);
		int glacier          = TFBiomeProvider.getBiomeId(TFBiomes.glacier, registry);
		int snowyForest      = TFBiomeProvider.getBiomeId(TFBiomes.snowy_forest, registry);
		int darkForestCenter = TFBiomeProvider.getBiomeId(TFBiomes.darkForestCenter, registry);
		int darkForest       = TFBiomeProvider.getBiomeId(TFBiomes.darkForest, registry);
		int highlandsCenter  = TFBiomeProvider.getBiomeId(TFBiomes.finalPlateau, registry);
		int highlands        = TFBiomeProvider.getBiomeId(TFBiomes.highlands, registry);

		if (isKey(fireSwamp, center, right, left, up, down)) {
			return swamp;
		} else if (isKey(glacier, center, right, left, up, down)) {
			return snowyForest;
		} else if (isKey(darkForestCenter, center, right, left, up, down)) {
			return darkForest;
		} else if (isKey(highlandsCenter, center, right, left, up, down)) {
			return highlands;
		} else {
			return center;
		}
	}

	/**
	 * Returns true if any of the surrounding biomes is the specified biome
	 */
	boolean isKey(int biome, int center, int right, int left, int up, int down) {
		return center != biome && (right == biome || left == biome || up == biome || down == biome);
	}
}
