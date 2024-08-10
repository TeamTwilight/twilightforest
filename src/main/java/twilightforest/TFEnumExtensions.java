package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.renderer.block.JarRenderer;
import twilightforest.beans.Autowired;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {

	@Autowired
	private static BiomeColorAlgorithms biomeColorAlgorithms;

	/**
	 * {@link net.minecraft.world.damagesource.DamageEffects}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFDamageEffectsEnumExtension#PINCH}
	 */
	public static Object DamageEffects_PINCH(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("pinch");
			case 1 -> TFSounds.PINCH_BEETLE_ATTACK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.item.Rarity}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFRarityEnumExtension#TWILIGHT}
	 */
	public static Object Rarity_TWILIGHT(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> -1;
			case 1 -> prefix("twilight");
			case 2 -> (UnaryOperator<Style>) style -> style.withColor(ChatFormatting.DARK_GREEN);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFGrassColorModifierEnumExtension#ENCHANTED_FOREST}
	 */
	public static Object GrassColorModifier_ENCHANTED_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("enchanted_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> biomeColorAlgorithms.enchanted(color, (int) x, (int) z);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFGrassColorModifierEnumExtension#SWAMP}
	 */
	public static Object GrassColorModifier_SWAMP(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("swamp");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> biomeColorAlgorithms.swamp(BiomeColorAlgorithms.Type.Grass);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFGrassColorModifierEnumExtension#DARK_FOREST}
	 */
	public static Object GrassColorModifier_DARK_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("dark_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> biomeColorAlgorithms.darkForest(BiomeColorAlgorithms.Type.Grass);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFGrassColorModifierEnumExtension#DARK_FOREST_CENTER}
	 */
	public static Object GrassColorModifier_DARK_FOREST_CENTER(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("dark_forest_center");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> biomeColorAlgorithms.darkForestCenterGrass(x, z);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFGrassColorModifierEnumExtension#SPOOKY_FOREST}
	 */
	public static Object GrassColorModifier_SPOOKY_FOREST(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> prefix("spooky_forest");
			case 1 -> (BiomeSpecialEffects.GrassColorModifier.ColorModifier) (x, z, color) -> biomeColorAlgorithms.spookyGrass(x, z);
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.item.ItemDisplayContext}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFItemDisplayContextEnumExtension#JARRED}
	 */
	public static Object ItemDisplayContext_JARRED(int idx, Class<?> type) {
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
