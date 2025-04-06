package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.world.entity.EquipmentSlotGroup;
import twilightforest.init.TFDataComponents;

import java.util.Map;
import java.util.Set;

public abstract class Modifiers {
	Map<EquipmentSlotGroup, Set<Modifier>> modifiers;

	// goggles
	public static final Modifier RED_THREAD_VISION_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.RED_THREAD_VISION_ENABLE, true, "red_thread_vision");

	// vest
	public static final Modifier DODGE_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.1F, "dodge");
	public static final Modifier STEALTH_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.STEALTH_CROUCHING_ENABLE, true, "stealth");
	public static final Modifier HASTE_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.HASTE_AMPLIFIER, 1, "haste");
	public static final Modifier ARROW_MAGNETISM_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.ARROW_MAGNETISM, true, "arrow_magnetism");
	public static final Modifier FOOD_EFFICIENCY_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.EFFICIENT_EATER, 1F, "food_efficiency");

	// wings
	public static final Modifier CONTROLLED_FALL_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F, "controlled_fall");
	public static final Modifier DOUBLE_JUMP_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.HAS_DOUBLE_JUMP, true, "double_jump");
	public static final Modifier AGILE_RANGER_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.AGILE_RANGER_MODIFIER, 5F, "agile_ranger");
	public static final Modifier SIDESTEP_COOLDOWN_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.SIDESTEP_COOLDOWN, 3 * 20L, "sidestep_cooldown");

	// boots
	public static final Modifier WATER_WALK_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.WATER_WALK_ENABLE, true, "water_walk");
	public static final Modifier SLIMY_SOLES_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F, "slimy_soles");
	public static final Modifier FORWARD_BOOST_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.FORWARD_BOOST_MULTIPLIER, 1.4, "forward_boost");

	// all
	public static final Modifier AUTO_REPAIR_MODIFIER = new TravellersGearComponentModifier<>(TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F, "auto_repair");
}
