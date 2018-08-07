package twilightforest.world.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import twilightforest.biomes.TFBiomes;

public class GenLayerTFStream extends GenLayer {

	public GenLayerTFStream(long l, GenLayer genlayer) {
		super(l);
		super.parent = genlayer;
	}

	@Override
	public int[] getInts(int x, int z, int width, int depth) {

		int nx = x - 1;
		int nz = z - 1;
		int nwidth = width + 2;
		int ndepth = depth + 2;
		int input[] = parent.getInts(nx, nz, nwidth, ndepth);
		int output[] = IntCache.getIntCache(width * depth);

		int stream = Biome.getIdForBiome(TFBiomes.stream);

		for (int dz = 0; dz < depth; dz++) {
			for (int dx = 0; dx < width; dx++) {

				int left  = input[dx + 0 + (dz + 1) * nwidth];
				int right = input[dx + 2 + (dz + 1) * nwidth];
				int down  = input[dx + 1 + (dz + 0) * nwidth];
				int up    = input[dx + 1 + (dz + 2) * nwidth];
				int mid   = input[dx + 1 + (dz + 1) * nwidth];
//                if(mid == 0 || left == 0 || right == 0 || down == 0 || up == 0)
//                {
//                    output[dx + dz * width] = Biome.getIdForBiome(BiomeLibrary.stream);
//                    continue;
//                }
				if (shouldStream(mid, left, down, right, up)) {
					output[dx + dz * width] = stream;
				} else {
					output[dx + dz * width] = -1;
				}
			}
		}

		return output;
	}

	boolean shouldStream(int mid, int left, int down, int right, int up) {
		if (shouldStream(mid, left)) {
			return true;
		} else if (shouldStream(mid, right)) {
			return true;
		} else if (shouldStream(mid, down)) {
			return true;
		} else if (shouldStream(mid, up)) {
			return true;
		} else {
			return false;
		}
	}

	boolean shouldStream(int id1, int id2) {

		if (id1 == id2) {
			return false;
		}
		if (id1 == -id2) {
			return false;
		}

		Biome biome1 = Biome.getBiomeForId(id1);
		Biome biome2 = Biome.getBiomeForId(id2);

		// glacier and snow have no border
		if (biome1 == TFBiomes.glacier && biome2 == TFBiomes.snowy_forest) {
			return false;
		}
		if (biome1 == TFBiomes.snowy_forest && biome2 == TFBiomes.glacier) {
			return false;
		}
		// mushrooms
		if (biome1 == TFBiomes.deepMushrooms && biome2 == TFBiomes.mushrooms) {
			return false;
		}
		if (biome1 == TFBiomes.mushrooms && biome2 == TFBiomes.deepMushrooms) {
			return false;
		}
		// fire swamp
		if (biome1 == TFBiomes.tfSwamp && biome2 == TFBiomes.fireSwamp) {
			return false;
		}
		if (biome1 == TFBiomes.fireSwamp && biome2 == TFBiomes.tfSwamp) {
			return false;
		}
		// highlands
		if (biome1 == TFBiomes.highlands && biome2 == TFBiomes.highlandsCenter) {
			return false;
		}
		if (biome1 == TFBiomes.highlandsCenter && biome2 == TFBiomes.highlands) {
			return false;
		}
		// dark forest
		if (biome1 == TFBiomes.darkForest && biome2 == TFBiomes.darkForestCenter) {
			return false;
		}
		if (biome1 == TFBiomes.darkForestCenter && biome2 == TFBiomes.darkForest) {
			return false;
		}
		// no lake border
		if (biome1 == TFBiomes.tfLake || biome2 == TFBiomes.tfLake) {
			return false;
		}
		// clearing
		if (biome1 == TFBiomes.clearing || biome2 == TFBiomes.oakSavanna) {
			return false;
		}
		if (biome1 == TFBiomes.oakSavanna || biome2 == TFBiomes.clearing) {
			return false;
		}
		// thorns need no stream
		if (biome1 == TFBiomes.thornlands || biome2 == TFBiomes.thornlands) {
			return false;
		}

		return true;
	}
}
