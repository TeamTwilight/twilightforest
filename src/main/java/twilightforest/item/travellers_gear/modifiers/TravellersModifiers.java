package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.init.TFDataComponents;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public abstract class TravellersModifiers {
	public static final  Set<TravellersModifier> MODIFIERS = new HashSet<>();

	// goggles
	public static final TravellersGearComponentModifier RED_THREAD_VISION_MODIFIER = registerComponentModifier(TFDataComponents.RED_THREAD_VISION_ENABLE, true, "red_thread_vision");

	// vest
	public static final TravellersGearComponentModifier PERFECT_DODGE_MODIFIER = registerComponentModifier(TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.1F, "dodge");
	public static final TravellersGearComponentModifier STEALTH_MODIFIER = registerComponentModifier(TFDataComponents.STEALTH_CROUCHING_ENABLE, true, "stealth");
	public static final TravellersGearComponentModifier HASTE_MODIFIER = registerComponentModifier(TFDataComponents.HASTE_AMPLIFIER, 1, "haste");
	public static final TravellersGearComponentModifier ARROW_MAGNETISM_MODIFIER = registerComponentModifier(TFDataComponents.ARROW_MAGNETISM, true, "arrow_magnetism");
	public static final TravellersGearComponentModifier FOOD_EFFICIENCY_MODIFIER = registerComponentModifier(TFDataComponents.EFFICIENT_EATER, 1F, "food_efficiency");

	// wings
	public static final TravellersGearComponentModifier CONTROLLED_FALL_MODIFIER = registerComponentModifier(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F, "controlled_fall");
	public static final TravellersGearComponentModifier DOUBLE_JUMP_MODIFIER = registerComponentModifier(TFDataComponents.HAS_DOUBLE_JUMP, true, "double_jump");
	public static final TravellersGearComponentModifier AGILE_RANGER_MODIFIER = registerComponentModifier(TFDataComponents.AGILE_RANGER_MODIFIER, 5F, "agile_ranger");
	public static final TravellersGearComponentModifier SIDESTEP_MODIFIER = registerComponentModifier(TFDataComponents.SIDESTEP_COOLDOWN, 3 * 20L, "sidestep_cooldown");

	// boots
	public static final TravellersGearComponentModifier WATER_WALK_MODIFIER = registerComponentModifier(TFDataComponents.WATER_WALK_ENABLE, true, "water_walk");
	public static final TravellersGearComponentModifier SLIMY_SOLES_MODIFIER = registerComponentModifier(TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, "slimy_soles");
	public static final TravellersGearComponentModifier FORWARD_BOOST_MODIFIER = registerComponentModifier(TFDataComponents.FORWARD_BOOST_MULTIPLIER, 1.4, "forward_boost");

	// all
	public static final TravellersGearComponentModifier AUTO_REPAIR_MODIFIER = registerComponentModifier(TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, "auto_repair");

	public static <T> TravellersGearComponentModifier registerComponentModifier(DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value, String tooltip) {
		return register(new TravellersGearComponentModifier(dataComponent, value, tooltip));
	}

	public static <T extends TravellersModifier> T register(T travellersModifier) {
		MODIFIERS.add(travellersModifier);
		return travellersModifier;
	}

	public static Stream<TravellersModifier> findAllModifiers(ItemStack stack) {
		return MODIFIERS.stream().filter(travellersModifier -> travellersModifier.hasModifier(stack));
	}

	public static long countModifiers(ItemStack stack) {
		return findAllModifiers(stack).count();
	}
}
