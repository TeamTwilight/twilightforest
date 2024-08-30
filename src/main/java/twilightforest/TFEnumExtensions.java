package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.beans.Autowired;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.ModidPrefixUtil;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {

	@Autowired
	private static BiomeColorAlgorithms biomeColorAlgorithms;

	private static final ModidPrefixUtil modidPrefixUtil = new ModidPrefixUtil(); // Enum extensions run before the bean context loads

	/**
	 * {@link net.minecraft.world.damagesource.DamageEffects}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFDamageEffectsEnumExtension#PINCH}
	 */
	public static Object DamageEffects_PINCH(int idx, Class<?> type) {
		return type.cast(switch (idx) {
			case 0 -> modidPrefixUtil.stringPrefix("pinch");
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
			case 1 -> modidPrefixUtil.stringPrefix("twilight");
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
			case 0 -> modidPrefixUtil.stringPrefix("enchanted_forest");
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
			case 0 -> modidPrefixUtil.stringPrefix("swamp");
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
			case 0 -> modidPrefixUtil.stringPrefix("dark_forest");
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
			case 0 -> modidPrefixUtil.stringPrefix("dark_forest_center");
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
			case 0 -> modidPrefixUtil.stringPrefix("spooky_forest");
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
			case 1 -> modidPrefixUtil.stringPrefix("jarred");
			case 2 -> "FIXED";
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#TWILIGHT_OAK}
	 */
	public static Object Boat$Type_TWILIGHT_OAK(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.TWILIGHT_OAK_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("twilight_oak");
			case 2 -> TFItems.TWILIGHT_OAK_BOAT;
			case 3 -> TFItems.TWILIGHT_OAK_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#CANOPY}
	 */
	public static Object Boat$Type_CANOPY(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.CANOPY_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("canopy");
			case 2 -> TFItems.CANOPY_BOAT;
			case 3 -> TFItems.CANOPY_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#MANGROVE}
	 */
	public static Object Boat$Type_MANGROVE(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.MANGROVE_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("mangrove");
			case 2 -> TFItems.MANGROVE_BOAT;
			case 3 -> TFItems.MANGROVE_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#DARK}
	 */
	public static Object Boat$Type_DARK(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.DARK_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("dark");
			case 2 -> TFItems.DARK_BOAT;
			case 3 -> TFItems.DARK_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#TIME}
	 */
	public static Object Boat$Type_TIME(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.TIME_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("time");
			case 2 -> TFItems.TIME_BOAT;
			case 3 -> TFItems.TIME_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#TRANSFORMATION}
	 */
	public static Object Boat$Type_TRANSFORMATION(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.TRANSFORMATION_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("transformation");
			case 2 -> TFItems.TRANSFORMATION_BOAT;
			case 3 -> TFItems.TRANSFORMATION_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#MINING}
	 */
	public static Object Boat$Type_MINING(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.MINING_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("mining");
			case 2 -> TFItems.MINING_BOAT;
			case 3 -> TFItems.MINING_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

	/**
	 * {@link net.minecraft.world.entity.vehicle.Boat.Type}<p/>
	 *
	 * {@link twilightforest.enums.extensions.TFBoatTypeEnumExtension#SORTING}
	 */
	public static Object Boat$Type_SORTING(int idx, Class<?> type) {
		if (idx == 5) // Lazy away around boxing the boolean
			return false;
		return type.cast(switch (idx) {
			case 0 -> TFBlocks.SORTING_PLANKS;
			case 1 -> modidPrefixUtil.stringPrefix("sorting");
			case 2 -> TFItems.SORTING_BOAT;
			case 3 -> TFItems.SORTING_CHEST_BOAT;
			case 4 -> (Supplier<Item>) () -> Items.STICK;
			default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
		});
	}

}
