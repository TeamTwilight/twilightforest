package twilightforest.world.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import twilightforest.biomes.TFBiomes;


public class GenLayerTFCompanionBiomes extends GenLayer {

    public GenLayerTFCompanionBiomes(long l, GenLayer genlayer)
    {
        super(l);
        parent = genlayer;
    }

    /**
     * If we are next to one of the 4 "key" biomes, we randomly turn into a companion biome for that center biome
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
                if (isKey(Biome.getIdForBiome(TFBiomes.fireSwamp), center, right, left, up, down))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.tfSwamp);
                }
                else if (isKey(Biome.getIdForBiome(TFBiomes.glacier), center, right, left, up, down))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.snowy_forest);
                }
                else if (isKey(Biome.getIdForBiome(TFBiomes.darkForestCenter), center, right, left, up, down))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.darkForest);
                }
                else if (isKey(Biome.getIdForBiome(TFBiomes.highlandsCenter), center, right, left, up, down))
                {
                    output[dx + dz * width] = Biome.getIdForBiome(TFBiomes.highlands);
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
	 * Returns true if any of the surrounding biomes is the specified biome
	 */
	boolean isKey(int biome, int center, int right, int left, int up, int down) {
		
		return center != biome && (right == biome || left == biome || up == biome || down == biome);
	}

}
