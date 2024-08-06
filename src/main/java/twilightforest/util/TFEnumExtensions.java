package twilightforest.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.tileentity.JarRenderer;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.world.components.BiomeGrassColors;

import java.util.function.Supplier;
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

	/**
	 * {@link JarRenderer.MasonJarRenderer#JARRED}
	 */
	@Nullable
	public static Object jarred(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> prefix("jarred");
			case 2 -> "FIXED";
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	public static Object twilightOakBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.TWILIGHT_OAK_PLANKS);
			case 1 -> type.cast("twilightforest:twilight_oak");
			case 2 -> type.cast(TFItems.TWILIGHT_OAK_BOAT);
			case 3 -> type.cast(TFItems.TWILIGHT_OAK_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object canopyBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.CANOPY_PLANKS);
			case 1 -> type.cast("twilightforest:canopy");
			case 2 -> type.cast(TFItems.CANOPY_BOAT);
			case 3 -> type.cast(TFItems.CANOPY_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object mangroveBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.MANGROVE_PLANKS);
			case 1 -> type.cast("twilightforest:mangrove");
			case 2 -> type.cast(TFItems.MANGROVE_BOAT);
			case 3 -> type.cast(TFItems.MANGROVE_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object darkBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.DARK_PLANKS);
			case 1 -> type.cast("twilightforest:dark");
			case 2 -> type.cast(TFItems.DARK_BOAT);
			case 3 -> type.cast(TFItems.DARK_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object timeBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.TIME_PLANKS);
			case 1 -> type.cast("twilightforest:time");
			case 2 -> type.cast(TFItems.TIME_BOAT);
			case 3 -> type.cast(TFItems.TIME_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object transformationBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.TRANSFORMATION_PLANKS);
			case 1 -> type.cast("twilightforest:transformation");
			case 2 -> type.cast(TFItems.TRANSFORMATION_BOAT);
			case 3 -> type.cast(TFItems.TRANSFORMATION_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object miningBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.MINING_PLANKS);
			case 1 -> type.cast("twilightforest:mining");
			case 2 -> type.cast(TFItems.MINING_BOAT);
			case 3 -> type.cast(TFItems.MINING_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	public static Object sortingBoat(int idx, Class<?> type) {
		return switch (idx) {
			case 0 -> type.cast(TFBlocks.SORTING_PLANKS);
			case 1 -> type.cast("twilightforest:sorting");
			case 2 -> type.cast(TFItems.SORTING_BOAT);
			case 3 -> type.cast(TFItems.SORTING_CHEST_BOAT);
			case 4 -> type.cast((Supplier<Item>) () -> Items.STICK);
			case 5 -> false;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		};
	}

	private static String prefix(String id) {
		return TwilightForestMod.ID + ":" + id;
	}
}
