package twilightforest.item.travellers_gear.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;

import java.util.*;

public abstract class TravellersModifiers {
	public static final List<InsertableTravellersModifier> INSERTABLE_MODIFIERS = new ArrayList<>();
	public static final List<BuiltinTravellersComponentModifier> BUILTIN_MODIFIERS = new ArrayList<>();
	public static final List<TravellersEntryModifier> ENTRY_MODIFIERS = new ArrayList<>();

	/**
	 * Modifiers are ordered by the length of the name in English, longest to shortest.
	 */

	// all
	public static final TravellersComponentModifier AUTO_REPAIR_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("auto_repair"), TFDataComponents.AUTO_REPAIR_PROBABILITY, 0.001F);

	// goggles
	public static final BuiltinTravellersComponentModifier ZOOM_MODIFIER = registerBuiltinComponentModifier(TwilightForestMod.prefix("zoom"), TFDataComponents.ZOOM_ABILITY_MODIFIER);
	public static final TravellersEntryModifier AQUATIC_AGILITY = registerEntryModifiers(TwilightForestMod.prefix("aquatic_agility"),
		List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN_ACTIVE, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING_ACTIVE, EquipmentSlotGroup.HEAD)),
		List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN_DEACTIVATED, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING_DEACTIVATED, EquipmentSlotGroup.HEAD)));
	public static final TravellersComponentModifier RED_THREAD_VISION_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("red_thread_vision"), TFDataComponents.RED_THREAD_VISION, Unit.INSTANCE);
	public static final TravellersComponentModifier ALL_NIGHT_GOGGLES_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("all_night_goggles"), TFDataComponents.ALL_NIGHT_GOGGLES, Unit.INSTANCE);
	public static final TravellersComponentModifier ITEM_DISPLAY_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("item_display"), TFDataComponents.ITEM_DISPLAY, ItemDisplayContents.EMPTY);

	// vest
	public static final TravellersEntryModifier SWIFT_SWIM_MODIFIER = registerEntryModifier(TwilightForestMod.prefix("swift_swim"), Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM_ACTIVATE, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM_DEACTIVATED, EquipmentSlotGroup.CHEST);
	public static final TravellersComponentModifier STEALTH_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("stealth"), TFDataComponents.STEALTH_CROUCHING, Unit.INSTANCE);
	public static final TravellersComponentModifier ARROW_MAGNETISM_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("arrow_magnetism"), TFDataComponents.ARROW_MAGNETISM, Unit.INSTANCE);
	public static final TravellersComponentModifier FOOD_EFFICIENCY_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("food_efficiency"), TFDataComponents.EFFICIENT_EATER, 2F);
	public static final TravellersComponentModifier PERFECT_DODGE_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("perfect_dodge"), TFDataComponents.PERFECT_DODGE_PROBABILITY, 0.1F);
	public static final TravellersComponentModifier HASTE_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("haste"), TFDataComponents.HASTE_AMPLIFIER, 1);

	// belt
	public static final BuiltinTravellersComponentModifier SWAP_HOTBAR_ABILITY = registerBuiltinComponentModifier(TwilightForestMod.prefix("swap_hotbar"), TFDataComponents.SWAP_HOTBAR_ABILITY);
	public static final TravellersComponentModifier SWAP_HOTBAR_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("swap_hotbar"), TFDataComponents.SWAP_HOTBAR_MODIFIER, Unit.INSTANCE);
	// wings
	public static final BuiltinTravellersComponentModifier HIGH_JUMP_MODIFIER = registerBuiltinComponentModifier(TwilightForestMod.prefix("high_jump"), TFDataComponents.HIGH_JUMP_AMPLIFIER);
	public static final TravellersComponentModifier CONTROLLED_FALL_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("controlled_fall"), TFDataComponents.CONTROLLED_FALLING_MULTIPLIER, 1 - 1 / 6F);
	public static final TravellersComponentModifier AGILE_RANGER_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("agile_ranger"), TFDataComponents.AGILE_RANGER_MODIFIER, 5F);
	public static final TravellersComponentModifier DOUBLE_JUMP_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("double_jump"), TFDataComponents.DOUBLE_JUMP, Unit.INSTANCE);
	public static final TravellersComponentModifier SIDESTEP_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("side_step"), TFDataComponents.SIDESTEP_COOLDOWN, 3 * 20L);

	// boots
	public static final TravellersEntryModifier HIGH_STEP_MODIFIER = registerEntryModifier(TwilightForestMod.prefix("high_step"), Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP_ACTIVE, TFAttributeModifiers.TRAVELLERS_HIGH_STEP_DEACTIVATED, EquipmentSlotGroup.FEET);
	public static final TravellersComponentModifier STRAIGHT_AHEAD_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("straight_ahead"), TFDataComponents.FORWARD_BOOST_MULTIPLIER, 1.4);
	public static final TravellersComponentModifier SLIMY_SOLES_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("slimy_soles"), TFDataComponents.SLIMY_SOLES_COEFFICIENT, 0.5F);
	public static final TravellersComponentModifier WATER_WALK_MODIFIER = registerComponentModifier(TwilightForestMod.prefix("water_walk"), TFDataComponents.WATER_WALK, Unit.INSTANCE);

	public static final Set<TravellersModifier> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER);

	public static <T> TravellersComponentModifier registerComponentModifier(ResourceLocation name, DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent, T value) {
		return register(new TravellersComponentModifier(name, dataComponent.get(), value));
	}

	public static <T> TravellersComponentModifier registerComponentModifier(ResourceLocation name, DataComponentType<T> dataComponent, T value) {
		return register(new TravellersComponentModifier(name, dataComponent, value));
	}

	public static <T> BuiltinTravellersComponentModifier registerBuiltinComponentModifier(ResourceLocation name, DeferredHolder<DataComponentType<?>, DataComponentType<T>> dataComponent) {
		return register(new BuiltinTravellersComponentModifier(name, dataComponent));
	}

	public static TravellersEntryModifier registerEntryModifier(ResourceLocation name, Holder<Attribute> attribute, AttributeModifier activeModifier, AttributeModifier deactivatedModifier, EquipmentSlotGroup slot) {
		return registerEntryModifier(name,
			new ItemAttributeModifiers.Entry(attribute, activeModifier, slot),
			new ItemAttributeModifiers.Entry(attribute, deactivatedModifier, slot)
		);
	}

	public static TravellersEntryModifier registerEntryModifier(ResourceLocation name, ItemAttributeModifiers.Entry activeEntry, ItemAttributeModifiers.Entry deactivatedEntry) {
		return register(new TravellersEntryModifier(name, List.of(activeEntry), List.of(deactivatedEntry)));
	}

	public static TravellersEntryModifier registerEntryModifiers(ResourceLocation name, List<ItemAttributeModifiers.Entry> activeEntries, List<ItemAttributeModifiers.Entry> deactivatedEntries) {
		return register(new TravellersEntryModifier(name, activeEntries, deactivatedEntries));
	}

	public static <T extends InsertableTravellersModifier> T register(T modifier) {
		INSERTABLE_MODIFIERS.add(modifier);
		return modifier;
	}

	public static <T extends BuiltinTravellersComponentModifier> T register(T modifier) {
		BUILTIN_MODIFIERS.add(modifier);
		return modifier;
	}

	public static <T extends TravellersEntryModifier> T register(T modifier) {
		ENTRY_MODIFIERS.add(modifier);
		return modifier;
	}


	public static List<InsertableTravellersModifier> findAllInsertableModifiers(ItemStack stack) {
		return INSERTABLE_MODIFIERS.stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static List<BuiltinTravellersComponentModifier> findAllBuiltinModifiers(ItemStack stack) {
		return BUILTIN_MODIFIERS.stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static List<TravellersEntryModifier> findAllEntryModifiers(ItemStack stack) {
		return ENTRY_MODIFIERS.stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static long countInsertableModifiers(ItemStack stack) {
		return findAllInsertableModifiers(stack).size();
	}
}
