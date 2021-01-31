package twilightforest.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.TFBiomeProvider;
import twilightforest.world.TFDimensions;

import java.util.Random;

/**
 * Puts key biomes in the proper positions
 *
 * @author Ben
 */
public enum GenLayerTFKeyBiomes implements IAreaTransformer1 {
	INSTANCE;

	private Registry<Biome> registry;
	private static final Random RANDOM = new Random();

	GenLayerTFKeyBiomes() { }

	public GenLayerTFKeyBiomes setup(Registry<Biome> registry) {
		this.registry = registry;
		return this;
	}

	@Override
	public int getOffsetX(int x) {
		return x | 3;
	}

	@Override
	public int getOffsetZ(int z) {
		return z | 3;
	}

	@Override
	public int apply(IExtendedNoiseRandom<?> random, IArea iArea, int x, int z) {
		RANDOM.setSeed(TFDimensions.seed + (x & -4) * 25117 + (z & -4) * 151121);
		int ox = RANDOM.nextInt(2) + 1;
		int oz = RANDOM.nextInt(2) + 1;
		RANDOM.setSeed(TFDimensions.seed + (x / 8) * 25117 + (z / 8) * 151121);
		int offset = RANDOM.nextInt(3);
		if ((x & 3) == ox && (z & 3) == oz) {
			// determine which of the 4
			if ((x & 4) == 0) {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset);
				} else {
					return getKeyBiomeFor(offset + 1);
				}
			} else {
				if ((z & 4) == 0) {
					return getKeyBiomeFor(offset + 2);
				} else {
					return getKeyBiomeFor(offset + 3);
				}
			}

		} else {
			return iArea.getValue(x, z);
		}
	}

	/**
	 * Determine which map "region" the specified points are in.  Assign the 0-3 of the index to the key biomes based on that region.
	 */
	private int getKeyBiomeFor(int index) {
		// do we need to shuffle this better?
		// the current version just "rotates" the 4 key biomes
		switch ((index) & 0b11) {
			case 0:
			default:
				return TFBiomeProvider.getBiomeId(TFBiomes.glacier, registry);
			case 1:
				return TFBiomeProvider.getBiomeId(TFBiomes.fireSwamp, registry);
			case 2:
				return TFBiomeProvider.getBiomeId(TFBiomes.darkForestCenter, registry);
			case 3:
				return TFBiomeProvider.getBiomeId(TFBiomes.finalPlateau, registry);
		}
	}
}
