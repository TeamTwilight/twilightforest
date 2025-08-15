package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.*;

public class TravellersModifiersManager {

	// all
	public static final ResourceKey<TravellersModifier> AUTO_REPAIR_MODIFIER = makeKey("auto_repair");
	// goggles
	public static final ResourceKey<TravellersModifier> ZOOM_ABILITY = makeKey("zoom");
	public static final ResourceKey<TravellersModifier> AQUATIC_AGILITY_MODIFIER = makeKey("aquatic_agility");
	public static final ResourceKey<TravellersModifier> RED_THREAD_VISION_MODIFIER = makeKey("red_thread_vision");
	public static final ResourceKey<TravellersModifier> ALL_NIGHT_GOGGLES_MODIFIER = makeKey("all_night_goggles");
	public static final ResourceKey<TravellersModifier> ITEM_DISPLAY_MODIFIER = makeKey("item_display");
	// vest
	public static final ResourceKey<TravellersModifier> SWIFT_SWIM_ABILITY = makeKey("swift_swim");
	public static final ResourceKey<TravellersModifier> STEALTH_MODIFIER = makeKey("stealth");
	public static final ResourceKey<TravellersModifier> ARROW_MAGNETISM_MODIFIER = makeKey("arrow_magnetism");
	public static final ResourceKey<TravellersModifier> FOOD_EFFICIENCY_MODIFIER = makeKey("food_efficiency");
	public static final ResourceKey<TravellersModifier> PERFECT_DODGE_MODIFIER = makeKey("perfect_dodge");
	public static final ResourceKey<TravellersModifier> HASTE_MODIFIER = makeKey("haste");
	// belt
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_ABILITY = makeKey("swap_hotbar");
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_MODIFIER = makeKey("swap_hotbar_modifier");
	// wings
	public static final ResourceKey<TravellersModifier> HIGH_JUMP_ABILITY = makeKey("high_jump");
	public static final ResourceKey<TravellersModifier> CONTROLLED_FALL_MODIFIER = makeKey("controlled_fall");
	public static final ResourceKey<TravellersModifier> AGILE_RANGER_MODIFIER = makeKey("agile_ranger");
	public static final ResourceKey<TravellersModifier> DOUBLE_JUMP_MODIFIER = makeKey("double_jump");
	public static final ResourceKey<TravellersModifier> SIDESTEP_MODIFIER = makeKey("side_step");
	// boots
	public static final ResourceKey<TravellersModifier> HIGH_STEP_ABILITY = makeKey("high_step");
	public static final ResourceKey<TravellersModifier> STRAIGHT_AHEAD_MODIFIER = makeKey("straight_ahead");
	public static final ResourceKey<TravellersModifier> SLIMY_SOLES_MODIFIER = makeKey("slimy_soles");
	public static final ResourceKey<TravellersModifier> WATER_WALK_MODIFIER = makeKey("water_walk");

	public static final Set<ResourceKey<TravellersModifier>> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER);

	private static ResourceKey<TravellersModifier> makeKey(String name) {
		return ResourceKey.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<TravellersModifier> context) {
		context.register(AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(TFDataComponents.AUTO_REPAIR_PROBABILITY.get(), 0.001F));
		context.register(ZOOM_ABILITY, new BuiltinTravellersComponentModifier(TFDataComponents.ZOOM_ABILITY_MODIFIER.get()));
		context.register(AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
		), TFDataComponents.AQUATIC_AGILITY.asOptional()));
		context.register(RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(TFDataComponents.RED_THREAD_VISION.get(), Unit.INSTANCE));
		context.register(ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(TFDataComponents.ALL_NIGHT_GOGGLES.get(), Unit.INSTANCE));
		context.register(ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(TFDataComponents.ITEM_DISPLAY.get(), ItemDisplayContents.EMPTY));

		context.register(SWIFT_SWIM_ABILITY, new TravellersEntryModifier(List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST))));
		context.register(STEALTH_MODIFIER, new TravellersComponentModifier(TFDataComponents.STEALTH_CROUCHING.get(), Unit.INSTANCE));
		context.register(ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(TFDataComponents.ARROW_MAGNETISM.get(), Unit.INSTANCE));
		context.register(FOOD_EFFICIENCY_MODIFIER, new TravellersComponentModifier(TFDataComponents.EFFICIENT_EATER.get(), 2F));
		context.register(PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(TFDataComponents.PERFECT_DODGE_PROBABILITY.get(), 0.1F));
		context.register(HASTE_MODIFIER, new TravellersComponentModifier(TFDataComponents.HASTE_AMPLIFIER.get(), 1));

		context.register(SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(TFDataComponents.SWAP_HOTBAR_ABILITY.get()));
		context.register(SWAP_HOTBAR_MODIFIER, new TravellersComponentModifier(TFDataComponents.SWAP_HOTBAR_MODIFIER.get(), Unit.INSTANCE));

		context.register(HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(TFDataComponents.HIGH_JUMP_AMPLIFIER.get()));
		context.register(CONTROLLED_FALL_MODIFIER, new TravellersComponentModifier(TFDataComponents.CONTROLLED_FALLING_MULTIPLIER.get(), 1 - 1 / 6F));
		context.register(AGILE_RANGER_MODIFIER, new TravellersComponentModifier(TFDataComponents.AGILE_RANGER_MODIFIER.get(), 5F));
		context.register(DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(TFDataComponents.DOUBLE_JUMP.get(), Unit.INSTANCE));
		context.register(SIDESTEP_MODIFIER, new TravellersComponentModifier(TFDataComponents.SIDESTEP_COOLDOWN.get(), 3 * 20L));

		context.register(HIGH_STEP_ABILITY, new TravellersEntryModifier(List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET))));
		context.register(STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(TFDataComponents.FORWARD_BOOST_MULTIPLIER.get(), 1.4));
		context.register(SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(TFDataComponents.SLIMY_SOLES_COEFFICIENT.get(), 0.5F));
		context.register(WATER_WALK_MODIFIER, new TravellersComponentModifier(TFDataComponents.WATER_WALK.get(), Unit.INSTANCE));
	}

	protected static void register(BootstrapContext<TravellersModifier> context, ResourceKey<TravellersModifier> key, TravellersModifier travellersModifier) {
		context.register(key, travellersModifier);
	}

	public static boolean isModifierActive(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return registries.holder(modifierKey).map(ref -> ref.value().isActive(stack, modifierKey)).orElse(false);
	}

	public static boolean hasTravellersModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return registries.holder(modifierKey).map(ref -> ref.value().hasModifier(stack)).orElse(false);
	}

	public static boolean addModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		Optional<Holder.Reference<TravellersModifier>> modifier = registries.holder(modifierKey);
		if (modifier.isEmpty() || !(modifier.get().value() instanceof InsertableTravellersModifier insertableTravellersModifier))
			return false;
		return insertableTravellersModifier.addModifier(stack);
	}

	public static List<Holder.Reference<TravellersModifier>> findAllInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements().filter(travellersModifier -> travellersModifier.value() instanceof InsertableTravellersModifier && !travellersModifier.value().isAbility() && travellersModifier.value().hasModifier(stack)).toList();
	}

	public static List<Holder.Reference<TravellersModifier>> findAllAbilityModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements().filter(travellersModifier -> travellersModifier.value().isAbility() && travellersModifier.value().hasModifier(stack)).toList();
	}

	public static long countInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return findAllInsertableModifiers(registries, stack).size();
	}
}
