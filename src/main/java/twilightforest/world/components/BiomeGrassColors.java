package twilightforest.world.components;

import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import twilightforest.util.TFEnumExtensions;

public class BiomeGrassColors {

	public static void init() {
		// I think this is not needed anymore? idk
	}

	/**
	 * {@link TFEnumExtensions#enchantedForestBiomeGrassColor(int, Class)}
	 */
	public static final BiomeSpecialEffects.GrassColorModifier ENCHANTED_FOREST = BiomeSpecialEffects.GrassColorModifier.valueOf("TWILIGHTFOREST_ENCHANTED_FOREST");

	/**
	 * {@link TFEnumExtensions#swampBiomeGrassColor(int, Class)}
	 */
	public static final GrassColorModifier SWAMP = BiomeSpecialEffects.GrassColorModifier.valueOf("TWILIGHTFOREST_SWAMP");

	/**
	 * {@link TFEnumExtensions#darkForestBiomeGrassColor(int, Class)}
	 */
	public static final GrassColorModifier DARK_FOREST = BiomeSpecialEffects.GrassColorModifier.valueOf("TWILIGHTFOREST_DARK_FOREST");

	/**
	 * {@link TFEnumExtensions#darkForestCenterBiomeGrassColor(int, Class)}
	 */
	public static final GrassColorModifier DARK_FOREST_CENTER = BiomeSpecialEffects.GrassColorModifier.valueOf("TWILIGHTFOREST_DARK_FOREST_CENTER");

	/**
	 * {@link TFEnumExtensions#spookyBiomeGrassColor(int, Class)}
	 */
	public static final GrassColorModifier SPOOKY_FOREST = BiomeSpecialEffects.GrassColorModifier.valueOf("TWILIGHTFOREST_SPOOKY_FOREST");

	public static int blendColors(int a, int b, double ratio) {
		int mask1 = 0x00FF00FF;
		int mask2 = 0xFF00FF00;

		int f2 = (int) (256 * ratio);
		int f1 = 256 - f2;

		return (((((a & mask1) * f1) + ((b & mask1) * f2)) >> 8) & mask1)
			| (((((a & mask2) * f1) + ((b & mask2) * f2)) >> 8) & mask2);
	}

	public static int getEnchantedColor(int x, int z) {
		// center of the biome is at % 256 - 8
		int cx = 256 * Math.round((x - 8) / 256F) + 8;
		int cz = 256 * Math.round((z - 8) / 256F) - 8;

		int dist = (int) Mth.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
		int color = dist * 64;
		color %= 512;

		if (color > 255) {
			color = 511 - color;
		}

		color = 255 - color;

		return color;
	}
}
