package twilightforest.worldgen.biomes;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeAmbience.GrassColorModifier;
import twilightforest.TwilightForestMod;

import java.util.Random;

import static net.minecraft.world.biome.Biome.INFO_NOISE;

public class BiomeGrassColors {
	private static final Random COLOR_RNG = new Random();
	private static int getEnchantedColor(int x, int z) {
		// center of the biome is at % 256 - 8
		int cx = 256 * Math.round((x - 8) / 256F) + 8;
		int cz = 256 * Math.round((z - 8) / 256F) + 8;

		int dist = (int) MathHelper.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
		int color = dist * 64;
		color %= 512;

		if (color > 255) {
			color = 511 - color;
		}

		color = 255 - color;

		// FIXME Biome colors are cached on chunk model build since like 1.7, we should be doing this differently. Maybe perlin based
		int randomFlicker = COLOR_RNG.nextInt(32) - 16;

		if (0 < color + randomFlicker && color + randomFlicker > 255) {
			color += randomFlicker;
		}

		return color;
	}

	public static final GrassColorModifier ENCHANTED_FOREST = make("enchanted_forest", (x, z, color) -> {
		return (color & 0xFFFF00) + getEnchantedColor((int) x, (int) z); //TODO
	});

	// FIXME Flat color, resolve
	public static final GrassColorModifier SWAMP = make("swamp", (x, z, color) -> ((GrassColors.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2);
	// FIXME Flat color, resolve
	public static final GrassColorModifier DARK_FOREST = make("dark_forest", (x, z, color) -> ((GrassColors.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E) / 2);
	public static final GrassColorModifier DARK_FOREST_CENTER = make("dark_forest_center", (x, z, color) -> {
		double d0 = INFO_NOISE.noiseAt(x * 0.0225D, z * 0.0225D, false); //TODO: Check
		return d0 < -0.2D ? 0x667540 : 0x554114;
	});

	private static GrassColorModifier make(String name, GrassColorModifier.ColorModifier delegate) {
		name = TwilightForestMod.prefix(name).toString();

		return GrassColorModifier.create(name, name, delegate);
	}
}
