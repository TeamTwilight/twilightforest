package twilightforest.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import twilightforest.biomes.TFBiomes;

/**
 * Puts key biomes in the proper positions
 *
 * @author Ben
 */
public enum GenLayerTFKeyBiomes implements IAreaTransformer1 {

	INSTANCE;

	GenLayerTFKeyBiomes() { }

//	public GenLayerTFKeyBiomes(long l, Layer genlayer) {
//		super(l);
//		parent = genlayer;
//	}
//
//	public GenLayerTFKeyBiomes(long l) {
//		super(l);
//	}

	@Override
	public int getOffsetX(int x) {
		return x | 3;
	}

	@Override
	public int getOffsetZ(int z) {
		return z | 3;
	}

	//TODO: Find out how to get an X, Z, Width, and Depth of a Layer. Generally, you don't see it.
//	@Override
//	public int[] getInts(int x, int z, int width, int depth) {
//		int src[] = this.parent.getInts(x, z, width, depth);
//		int dest[] = IntCache.getIntCache(width * depth);
//		for (int dz = 0; dz < depth; dz++) {
//			for (int dx = 0; dx < width; dx++) {
//				// get offsets
//				initChunkSeed(((dx + x) | 3), ((dz + z) | 3));
//
//				int ox = this.nextInt(3) + 1;
//				int oz = this.nextInt(3) + 1;
//
//				if (((dx + x) & 3) == ox && ((dz + z) & 3) == oz) {
//					// determine which of the 4
//					if (((dx + x) & 4) == 0) {
//						if (((dz + z) & 4) == 0) {
//							dest[dx + dz * width] = getKeyBiomeFor(dx + x, dz + z, 0);
//						} else {
//							dest[dx + dz * width] = getKeyBiomeFor(dx + x, dz + z, 1);
//						}
//					} else {
//						if (((dz + z) & 4) == 0) {
//							dest[dx + dz * width] = getKeyBiomeFor(dx + x, dz + z, 2);
//						} else {
//							dest[dx + dz * width] = getKeyBiomeFor(dx + x, dz + z, 3);
//						}
//					}
//
//				} else {
//					dest[dx + dz * width] = src[dx + dz * width];
//				}
//			}
//		}
//
//		return dest;
//	}

	//TODO: This logic is butchered to hell and back
	@Override
	public int apply(IExtendedNoiseRandom<?> random, IArea iArea, int x, int z) {
		int dx = getOffsetX(x);
		int dz = getOffsetZ(z);
		// get offsets
		//initChunkSeed(((dx + x) | 3), ((dz + z) | 3));

		int ox = random.random(3) + 1;
		int oz = random.random(3) + 1;

		if (((dx + x) & 3) == ox && ((dz + z) & 3) == oz) {
			// determine which of the 4
			if (((dx + x) & 4) == 0) {
				if (((dz + z) & 4) == 0) {
					return getKeyBiomeFor(random, dx + x, dz + z, 0);
				} else {
					return getKeyBiomeFor(random, dx + x, dz + z, 1);
				}
			} else {
				if (((dz + z) & 4) == 0) {
					return getKeyBiomeFor(random, dx + x, dz + z, 2);
				} else {
					return getKeyBiomeFor(random, dx + x, dz + z, 3);
				}
			}

		} else {
			return dx + dz * z;
		}
	}

	/**
	 * Determine which map "region" the specified points are in.  Assign the 0-3 of the index to the key biomes based on that region.
	 */
	private int getKeyBiomeFor(IExtendedNoiseRandom<?> random, int mapX, int mapZ, int index) {

		int regionX = (mapX + 4) >> 3;
		int regionZ = (mapZ + 4) >> 3;

		//this.initChunkSeed(regionX, regionZ);
		int offset = random.random(4);

		// do we need to shuffle this better?
		// the current version just "rotates" the 4 key biomes
		switch ((index + offset) % 4) {
			case 0:
			default:
				return Registry.BIOME.getId(TFBiomes.glacier.get());
			case 1:
				return Registry.BIOME.getId(TFBiomes.fireSwamp.get());
			case 2:
				return Registry.BIOME.getId(TFBiomes.darkForestCenter.get());
			case 3:
				return Registry.BIOME.getId(TFBiomes.highlandsCenter.get());
		}
	}
}
