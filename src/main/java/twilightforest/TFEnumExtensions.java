package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import tamaized.beanification.Autowired;
import twilightforest.init.TFSounds;
import twilightforest.util.ModidPrefixUtil;
import twilightforest.world.components.BiomeColorAlgorithms;

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
}
