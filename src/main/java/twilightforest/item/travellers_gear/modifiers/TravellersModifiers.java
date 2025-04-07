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
	public static final TravellersGearComponentModifier RED_THREAD_VISION_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.RED_THREAD_VISION_ENABLE, true, "red_thread_vision");

	// vest
	public static final TravellersGearComponentModifier DODGE_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.1F, "dodge");
	public static final TravellersGearComponentModifier STEALTH_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.STEALTH_CROUCHING_ENABLE, true, "stealth");
	public static final TravellersGearComponentModifier HASTE_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.HASTE_AMPLIFIER, 1, "haste");
	public static final TravellersGearComponentModifier ARROW_MAGNETISM_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.ARROW_MAGNETISM, true, "arrow_magnetism");
	public static final TravellersGearComponentModifier FOOD_EFFICIENCY_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.EFFICIENT_EATER, 1F, "food_efficiency");

	// wings
	public static final TravellersGearComponentModifier CONTROLLED_FALL_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F, "controlled_fall");
	public static final TravellersGearComponentModifier DOUBLE_JUMP_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.HAS_DOUBLE_JUMP, true, "double_jump");
	public static final TravellersGearComponentModifier AGILE_RANGER_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.AGILE_RANGER_MODIFIER, 5F, "agile_ranger");
	public static final TravellersGearComponentModifier SIDESTEP_COOLDOWN_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.SIDESTEP_COOLDOWN, 3 * 20L, "sidestep_cooldown");

	// boots
	public static final TravellersGearComponentModifier WATER_WALK_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.WATER_WALK_ENABLE, true, "water_walk");
	public static final TravellersGearComponentModifier SLIMY_SOLES_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, "slimy_soles");
	public static final TravellersGearComponentModifier FORWARD_BOOST_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.FORWARD_BOOST_MULTIPLIER, 1.4, "forward_boost");

	// all
	public static final TravellersGearComponentModifier AUTO_REPAIR_MODIFIER = new TravellersGearComponentModifier(TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, "auto_repair");

	public static <T> TravellersGearComponentModifier registerDataComponents(DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value, String tooltip) {
		return new TravellersGearComponentModifier(dataComponent, value, tooltip);
	}

	public static TravellersModifier register(TravellersModifier travellersModifier) {
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
