package twilightforest.world.components;

import net.minecraft.util.Mth;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import twilightforest.TwilightForestMod;

public class BiomeGrassColors {

	public static void init() {
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

	public static final GrassColorModifier ENCHANTED_FOREST = make("enchanted_forest", (x, z, color) -> {
		return (color & 0xFFFF00) + getEnchantedColor((int) x, (int) z); //TODO
	});

	// FIXME Flat color, resolve
	public static final GrassColorModifier SWAMP = make("swamp", (x, z, color) -> ((GrassColor.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2);
	// FIXME Flat color, resolve
	public static final GrassColorModifier DARK_FOREST = make("dark_forest", (x, z, color) -> ((GrassColor.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E) / 2);
	public static final GrassColorModifier DARK_FOREST_CENTER = make("dark_forest_center", (x, z, color) -> {
		double d0 = Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false); //TODO: Check
		return d0 < -0.2D ? 0x667540 : 0x554114;
	});
	public static final GrassColorModifier SPOOKY_FOREST = make("spooky_forest", (x, z, color) -> {
		double noise = (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
		return blendColors(0xc43323, 0x5BC423, noise > 0.6D ? noise * 0.1D : noise);
	});

	public static int blendColors(int a, int b, double ratio) {
		int mask1 = 0x00FF00FF;
		int mask2 = 0xFF00FF00;

		int f2 = (int) (256 * ratio);
		int f1 = 256 - f2;

		return (((((a & mask1) * f1) + ((b & mask1) * f2)) >> 8) & mask1)
			| (((((a & mask2) * f1) + ((b & mask2) * f2)) >> 8) & mask2);
	}

	private static GrassColorModifier make(String name, GrassColorModifier.ColorModifier delegate) {
		name = TwilightForestMod.prefix(name).toString();

		return GrassColorModifier.create(name, name, delegate);
	}
}
