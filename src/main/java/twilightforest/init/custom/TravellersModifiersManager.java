package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	public static final ResourceKey<TravellersModifier> EFFICIENT_EATER_MODIFIER = makeKey("efficient_eater");
	public static final ResourceKey<TravellersModifier> PERFECT_DODGE_MODIFIER = makeKey("perfect_dodge");
	public static final ResourceKey<TravellersModifier> HASTE_MODIFIER = makeKey("haste");
	// belt
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_ABILITY = makeKey("swap_hotbar_ability");
	public static final ResourceKey<TravellersModifier> SWAP_HOTBAR_MODIFIER = makeKey("swap_hotbar");
	// wings
	public static final ResourceKey<TravellersModifier> HIGH_JUMP_ABILITY = makeKey("high_jump");
	public static final ResourceKey<TravellersModifier> GRADUAL_GLIDE_MODIFIER = makeKey("gradual_glide");
	public static final ResourceKey<TravellersModifier> AGILE_RANGER_MODIFIER = makeKey("agile_ranger");
	public static final ResourceKey<TravellersModifier> DOUBLE_JUMP_MODIFIER = makeKey("double_jump");
	public static final ResourceKey<TravellersModifier> SIDESTEP_MODIFIER = makeKey("side_step");
	// boots
	public static final ResourceKey<TravellersModifier> STEP_UP_ABILITY = makeKey("step_up");
	public static final ResourceKey<TravellersModifier> STRAIGHT_AHEAD_MODIFIER = makeKey("straight_ahead");
	public static final ResourceKey<TravellersModifier> SLIMY_SOLES_MODIFIER = makeKey("slimy_soles");
	public static final ResourceKey<TravellersModifier> UNRESTRAINED_MODIFIER = makeKey("unrestrained");
	public static final ResourceKey<TravellersModifier> WATER_WALK_MODIFIER = makeKey("water_walk");

	public static final Set<ResourceKey<TravellersModifier>> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER);

	private static ResourceKey<TravellersModifier> makeKey(String name) {
		return ResourceKey.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TwilightForestMod.prefix(name));
	}

	public static void bootstrap(BootstrapContext<TravellersModifier> context) {
		context.register(AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.ARMOR, TFDataComponents.AUTO_REPAIR_PROBABILITY.get(), 0.001F, componentText(AUTO_REPAIR_MODIFIER)));
		context.register(ZOOM_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ZOOM_ABILITY_MODIFIER.get()));
		context.register(AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(EquipmentSlotGroup.HEAD, List.of(
			new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN, EquipmentSlotGroup.HEAD),
			new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING, EquipmentSlotGroup.HEAD)
		), TFDataComponents.AQUATIC_AGILITY, componentText(AQUATIC_AGILITY_MODIFIER), false));
		context.register(RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.RED_THREAD_VISION.get(), Unit.INSTANCE, componentText(RED_THREAD_VISION_MODIFIER)));
		context.register(ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ALL_NIGHT_GOGGLES.get(), Unit.INSTANCE, componentText(ALL_NIGHT_GOGGLES_MODIFIER)));
		context.register(ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.HEAD, TFDataComponents.ITEM_DISPLAY.get(), ItemDisplayContents.EMPTY, componentText(ITEM_DISPLAY_MODIFIER)));

		context.register(SWIFT_SWIM_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.CHEST, List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM, EquipmentSlotGroup.CHEST)), TFDataComponents.SWIFT_SWIM, true));
		context.register(STEALTH_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.STEALTH_CROUCHING.get(), Unit.INSTANCE, componentText(STEALTH_MODIFIER)));
		context.register(ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.ARROW_MAGNETISM.get(), Unit.INSTANCE, componentText(ARROW_MAGNETISM_MODIFIER)));
		context.register(EFFICIENT_EATER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.EFFICIENT_EATER.get(), 2F, componentText(EFFICIENT_EATER_MODIFIER)));
		context.register(PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.PERFECT_DODGE_PROBABILITY.get(), 0.3F, componentText(PERFECT_DODGE_MODIFIER)));
		context.register(HASTE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.CHEST, TFDataComponents.HASTE_AMPLIFIER.get(), 1, componentText(HASTE_MODIFIER)));

		context.register(SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_ABILITY.get()));
		context.register(SWAP_HOTBAR_MODIFIER, new TransferableComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SWAP_HOTBAR_MODIFIER.get(), DataComponents.CONTAINER, TravellersArmorBeltItem.DEFAULT_EMPTY_BELT_CONTAINER, componentText(SWAP_HOTBAR_MODIFIER)));

		context.register(HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.HIGH_JUMP_AMPLIFIER.get()));
		context.register(GRADUAL_GLIDE_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.GRADUALLY_GLIDING_MULTIPLIER.get(), 1 - 1 / 6F, componentText(GRADUAL_GLIDE_MODIFIER)));
		context.register(AGILE_RANGER_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.AGILE_RANGER_MODIFIER.get(), 5F, componentText(AGILE_RANGER_MODIFIER)));
		context.register(DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.DOUBLE_JUMP.get(), Unit.INSTANCE, componentText(DOUBLE_JUMP_MODIFIER)));
		context.register(SIDESTEP_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.LEGS, TFDataComponents.SIDESTEP_COOLDOWN.get(), 2 * 20L, componentText(SIDESTEP_MODIFIER, Component.keybind("key.left"), Component.keybind("key.right"))));

		context.register(STEP_UP_ABILITY, new TravellersEntryModifier(EquipmentSlotGroup.FEET, List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP, EquipmentSlotGroup.FEET)), TFDataComponents.HIGH_STEP, true));
		context.register(STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER.get(), 1.4, componentText(STRAIGHT_AHEAD_MODIFIER)));
		context.register(SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.SLIMY_SOLES_COEFFICIENT.get(), 0.5F, componentText(SLIMY_SOLES_MODIFIER)));
		context.register(UNRESTRAINED_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.UNRESTRAINED.get(), Unit.INSTANCE, componentText(UNRESTRAINED_MODIFIER)));
		context.register(WATER_WALK_MODIFIER, new TravellersComponentModifier(EquipmentSlotGroup.FEET, TFDataComponents.WATER_WALK.get(), Unit.INSTANCE, componentText(WATER_WALK_MODIFIER)));
	}

	private static List<Component> componentText(ResourceKey<TravellersModifier> modifier, Object... args) {
		return List.of(Component.translatable(modifier.identifier().toLanguageKey("travellers_gear.modifier", "description"), args));
	}

	public static boolean isModifierActive(ItemStack stack, Holder<TravellersModifier> modifierHolder, boolean spectator) {
		return modifierHolder.value().isActive(stack, modifierHolder, spectator);
	}

	public static boolean isModifierActive(Entity entity, ItemStack stack, Holder<TravellersModifier> modifierHolder) {
		return isModifierActive(stack, modifierHolder, entity.isSpectator());
	}

	public static boolean isModifierActive(Entity entity, Holder<TravellersModifier> modifierHolder) {
		return entity instanceof LivingEntity livingEntity && isModifierActive(livingEntity, modifierHolder);
	}

	public static boolean isModifierActive(LivingEntity livingEntity, Holder<TravellersModifier> modifierHolder) {
		TravellersModifier modifier = modifierHolder.value();
		ItemStack equippedStack = getStackForGroup(livingEntity, modifier.group());

		return !equippedStack.isEmpty()
			&& modifier.isActive(
			equippedStack,
			modifierHolder,
			livingEntity.isSpectator()
		);
	}

	public static boolean hasTravellersModifier(ItemStack stack, Holder<TravellersModifier> modifierHolder) {
		return modifierHolder.value().hasModifier(stack);
	}

	public static boolean addModifier(ItemStack stack, Holder<TravellersModifier> modifierHolder) {
		if (!(modifierHolder.value() instanceof InsertableTravellersModifier insertableTravellersModifier))
			return false;
		return insertableTravellersModifier.addModifier(stack);
	}

	public static boolean transferModifier(ItemStack stack, CraftingInput input, Holder<TravellersModifier> modifierHolder) {
		if (!(modifierHolder.value() instanceof TransferableTravellersModifier transferableTravellersModifier))
			return false;
		return transferableTravellersModifier.transfer(stack, input);
	}

	public static int getModifierDataComponentProviders(CraftingInput input, Holder<TravellersModifier> modifierHolder) {
		if (!(modifierHolder.value() instanceof TransferableComponentModifier transferableComponentModifier))
			return 0;
		return transferableComponentModifier.findDataComponentProviders(input).size();
	}

	public static MutableComponent getModifierTooltipComponent(Holder<TravellersModifier> modifier) {
		return TooltipStringInterpolator.render(getKeyOrThrow(modifier).identifier().toLanguageKey(modifier.value().getPrefix()));
	}

	public static List<Holder.Reference<TravellersModifier>> findAllInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements().filter(travellersModifier -> travellersModifier.value() instanceof InsertableTravellersModifier && !travellersModifier.value().isAbility() && travellersModifier.value().hasModifier(stack)).toList();
	}

	public static List<Holder.Reference<TravellersModifier>> findAllInsertableModifiers(Entity entity, ItemStack stack) {
		return findAllInsertableModifiers(entity.registryAccess(), stack);
	}

	public static List<Holder.Reference<TravellersModifier>> findAllInsertableModifiers(Level level, ItemStack stack) {
		return findAllInsertableModifiers(level.registryAccess(), stack);
	}

	public static List<Holder.Reference<TravellersModifier>> findAllAbilityModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return registries.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements().filter(travellersModifier -> travellersModifier.value().isAbility() && travellersModifier.value().hasModifier(stack)).toList();
	}

	public static long countInsertableModifiers(HolderLookup.Provider registries, ItemStack stack) {
		return findAllInsertableModifiers(registries, stack).size();
	}

	public static Optional<Holder.Reference<TravellersModifier>> lookupHolder(HolderLookup.Provider registries, ResourceKey<TravellersModifier> key) {
		Optional<Holder.Reference<TravellersModifier>> holder = registries.holder(key);

		if (holder.isEmpty()) {
			TwilightForestMod.LOGGER.warn("Travellers modifier {} is not present in the registry", key.identifier());
		}

		return holder;
	}

	public static ResourceKey<TravellersModifier> getKeyOrThrow(Holder<TravellersModifier> holder) {
		return holder.unwrapKey().orElseThrow(() -> {
			TwilightForestMod.LOGGER.error(
				"Expected a registry-backed TravellersModifier holder but received {}",
				holder
			);
			return new IllegalStateException("TravellersModifier holder is not registry-backed");
		});
	}

	private static ItemStack getStackForGroup(LivingEntity livingEntity, EquipmentSlotGroup group) {
		EquipmentSlot matchedSlot = null;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!slot.isArmor() || !group.test(slot))
				continue;
			if (matchedSlot != null)
				return ItemStack.EMPTY;
			matchedSlot = slot;
		}
		return matchedSlot == null ? ItemStack.EMPTY : livingEntity.getItemBySlot(matchedSlot);
	}
}
