package twilightforest.world.layer;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

public enum GenLayerTFBiomeStabilize implements IAreaTransformer1 {

	INSTANCE;

//	public GenLayerTFBiomeStabilize(long l, GenLayer genlayer) {
//		super(l);
//		parent = genlayer;
//	}

	GenLayerTFBiomeStabilize() { }

	@Override
	public int getOffsetX(int x) {
		return x & 3;
	}

	@Override
	public int getOffsetZ(int z) {
		return z & 3;
	}

	/**
	 * When we are near the center of each biome, make nearby areas that biome too
	 */
//	@Override
//	public int[] getInts(int x, int z, int width, int depth) {
//		int nx = x - 1;
//		int nz = z - 1;
//		int nwidth = width + 2;
//		int ndepth = depth + 2;
//		int input[] = parent.getInts(nx, nz, nwidth, ndepth);
//		int output[] = IntCache.getIntCache(width * depth);
//
//		int offX = x & 3;
//		int offZ = z & 3;
//
//		for (int dz = 0; dz < depth; dz++) {
//			for (int dx = 0; dx < width; dx++) {
//				int centerX = ((dx + offX + 1) & 0xFFFFFFFC) - offX;
//				int centerZ = ((dz + offZ + 1) & 0xFFFFFFFC) - offZ;
//
////            	if (dx == centerX && dz == centerZ)
////            	{
////            		output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
//////            		output[dx + dz * width] = BiomeLibrary.glacier.biomeID;
////            	}
////            	else
//				if (dx <= centerX + 1 && dx >= centerX - 1 && dz <= centerZ + 1 && dz >= centerZ - 1) {
//					output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
////            		output[dx + dz * width] = Biome.desert.biomeID;
////            		output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
//				} else {
//					output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
//				}
//			}
//		}
//
//		return output;
//	}

	@Override
	public int apply(IExtendedNoiseRandom<?> iExtendedNoiseRandom, IArea iArea, int x, int z) {
		int offX = getOffsetX(x << 4);
		int offZ = getOffsetZ(z << 4);
		int centerX = ((x + offX + 1) & -4) - offX;
		int centerZ = ((z + offZ + 1) & -4) - offZ;

//            	if (dx == centerX && dz == centerZ)
//            	{
//            		output[dx + dz * width] = input[centerX + 1 + (centerZ + 1) * nwidth];
////            		output[dx + dz * width] = BiomeLibrary.glacier.biomeID;
//            	}
//            	else
		if (x <= centerX + 1 && x >= centerX - 1 && z <= centerZ + 1 && z >= centerZ - 1) {
			return iArea.getValue(centerX, centerZ);
//            		output[dx + dz * width] = Biome.desert.biomeID;
//            		output[dx + dz * width] = input[dx + 1 + (dz + 1) * nwidth];
		} else {
			return iArea.getValue(x, z);
		}
	}
}
