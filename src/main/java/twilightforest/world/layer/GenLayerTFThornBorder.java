package twilightforest.world.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import twilightforest.biomes.TFBiomes;


public class GenLayerTFThornBorder extends GenLayer {

    public GenLayerTFThornBorder(long l, GenLayer genlayer)
    {
        super(l);
        parent = genlayer;
    }

    /**
     * Turn specific biomes into other biomes along their borders
     */
	@Override
    public int[] getInts(int x, int z, int width, int depth)
    {
        int nx = x - 1;
        int nz = z - 1;
        int nwidth = width + 2;
        int ndepth = depth + 2;
        int input[] = parent.getInts(nx, nz, nwidth, ndepth);
        int output[] = IntCache.getIntCache(width * depth);
        for(int dz = 0; dz < depth; dz++)
        {
            for(int dx = 0; dx < width; dx++)
            {
                int right = input[dx + 0 + (dz + 1) * nwidth];
                int left = input[dx + 2 + (dz + 1) * nwidth];
                int up = input[dx + 1 + (dz + 0) * nwidth];
                int down = input[dx + 1 + (dz + 2) * nwidth];
                int center = input[dx + 1 + (dz + 1) * nwidth];
                // thorn border requires also checking diagonally
                int ur = input[dx + 0 + (dz + 0) * nwidth];
                int ul = input[dx + 2 + (dz + 0) * nwidth];
                int dr = input[dx + 0 + (dz + 2) * nwidth];
                int dl = input[dx + 2 + (dz + 2) * nwidth];
                if (onBorder(Biome.getIdForBiome(TFBiomes.highlandsCenter), center, right, left, up, down))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.thornlands);
                }
                else if (onBorder(Biome.getIdForBiome(TFBiomes.highlandsCenter), center, ur, ul, dr, dl))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.thornlands);
                }
                else
                {
                    output[dx + dz * width] = center;
                }
            }

        }

        return output;
    }
	
	/**
	 * Returns true if the center biome is the first specified biome and any of the surrounding biomes are the second bioms
	 */
	private boolean onBorder(int biomeID, int biomeID2, int center, int right, int left, int up, int down) {
		if (center != biomeID) {
			return false;
		}

		if (right == biomeID2) {
			return true;
		}
		if (left == biomeID2) {
			return true;
		}
		if (up == biomeID2) {
			return true;
		}
		if (down == biomeID2) {
			return true;
		}
		
		return false;
	}

	/**
	 * Returns true if the center biome is not the specified biome and any of the surrounding biomes are the specified biomes
	 */
	private boolean onBorder(int biomeID, int center, int right, int left,
			int up, int down) {
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
