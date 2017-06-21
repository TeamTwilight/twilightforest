package twilightforest.world.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import twilightforest.biomes.TFBiomes;

public class GenLayerTF7x7Preset extends GenLayer {


	Biome preset[][] = new Biome[9][9];

	public GenLayerTF7x7Preset(long par1) {
		super(par1);
		initPresets();
	}

	@Override
	public int[] getInts(int x, int z, int width, int depth) {
		int dest[] = IntCache.getIntCache(width * depth);
		for (int dz = 0; dz < depth; dz++) {
			for (int dx = 0; dx < width; dx++) {
				int sx = x + dx + 4;
				int sz = z + dz + 4;

				if (sx >= 0 && sx < 8 && sz >= 0 && sz < 8) {
					dest[dx + dz * width] = Biome.getIdForBiome(preset[sx][sz]);
				} else {
					dest[dx + dz * width] = Biome.getIdForBiome(Biomes.OCEAN);
				}
			}

		}

		return dest;

	}

	private void initPresets() {
		char[][] map = {
				{'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
				{'P', 'H', 'H', 'H', 'H', 'D', 'D', 'D', 'P'},
				{'P', 'H', 'H', 'H', 'H', 'D', 'D', 'D', 'P'},
				{'P', 'F', 'f', 'F', 'm', 'M', 'D', 'D', 'P'},
				{'P', 'F', 'F', 'F', 'C', 'm', 'D', 'D', 'P'},
				{'P', 'F', 'f', 'f', 'F', 'E', 'O', 'O', 'P'},
				{'P', 'S', 'S', 'S', 'L', 'L', 'O', 'G', 'P'},
				{'P', 'Y', 'S', 'S', 'L', 'L', 'O', 'G', 'P'},
				{'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}};


		for (int x = 0; x < map.length; x++) {
			for (int z = 0; z < map[x].length; z++) {
				preset[x][z] = getBiomeFor(map[z][x]);
			}
		}

	}

	protected Biome getBiomeFor(char c) {
		switch (c) {
			default:
				return Biomes.OCEAN;
//		case 'P' :
//			return Biome.plains;
			case 'F':
				return TFBiomes.twilightForest;
			case 'f':
				return TFBiomes.denseTwilightForest;
			case 'E':
				return TFBiomes.enchantedForest;
			case 'm':
				return TFBiomes.mushrooms;
			case 'M':
				return TFBiomes.deepMushrooms;
			case 'C':
				return TFBiomes.clearing;
			case 'S':
				return TFBiomes.tfSwamp;
			case 'Y':
				return TFBiomes.fireSwamp;
			case 'D':
				return TFBiomes.darkForest;
			case 'L':
				return TFBiomes.tfLake;
			case 'O':
				return TFBiomes.snowy_forest;
			case 'G':
				return TFBiomes.glacier;
			case 'H':
				return TFBiomes.highlands;
		}
	}


}
