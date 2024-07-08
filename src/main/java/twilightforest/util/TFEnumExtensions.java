package twilightforest.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFSounds;
import twilightforest.world.components.BiomeGrassColors;

import java.util.function.UnaryOperator;

@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {
	/**
	 * {@link twilightforest.TwilightForestMod#PINCH}
	 */
	public static Object pinchDamage(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("pinch");
			case 1 -> TFSounds.PINCH_BEETLE_ATTACK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link TwilightForestMod#getRarity()}
	 */
	public static Object twilightRarity(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> prefix("twilight");
			case 2 -> (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.DARK_GREEN);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link BiomeGrassColors#ENCHANTED_FOREST}
	 */
	public static Object enchantedForestBiomeGrassColor(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("enchanted_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> {
				return (color & 0xFFFF00) + BiomeGrassColors.getEnchantedColor((int) x, (int) z); //TODO
			};
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link BiomeGrassColors#SWAMP}
	 */
	// FIXME Flat color, resolve
	public static Object swampBiomeGrassColor(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("swamp");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> ((GrassColor.get(0.8F, 0.9F) & 0xFEFEFE) + 0x4E0E4E) / 2;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link BiomeGrassColors#DARK_FOREST}
	 */
	// FIXME Flat color, resolve
	public static Object darkForestBiomeGrassColor(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("dark_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> ((GrassColor.get(0.7F, 0.8F) & 0xFEFEFE) + 0x1E0E4E) / 2;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link BiomeGrassColors#DARK_FOREST_CENTER}
	 */
	public static Object darkForestCenterBiomeGrassColor(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("dark_forest_center");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> {
				double d0 = Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false); //TODO: Check
				return d0 < -0.2D ? 0x667540 : 0x554114;
			};
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link BiomeGrassColors#SPOOKY_FOREST}
	 */
	public static Object spookyBiomeGrassColor(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("spooky_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> {
				double noise = (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
				return BiomeGrassColors.blendColors(0xc43323, 0x5BC423, noise > 0.6D ? noise * 0.1D : noise);
			};
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	private static String prefix(String id) {
		return TwilightForestMod.ID + ":" + id;
	}
}
