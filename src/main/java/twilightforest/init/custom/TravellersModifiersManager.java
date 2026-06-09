package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

	private static final Map<ResourceKey<TravellersModifier>, TravellersModifier> CACHED_MODIFIERS = new ConcurrentHashMap<>();
	private static final Set<ResourceKey<TravellersModifier>> MISSING_MODIFIERS = ConcurrentHashMap.newKeySet();

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

	public static boolean isModifierActive(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey, boolean spectator) {
		return getCachedModifier(registries, modifierKey).map(modifier -> modifier.isActive(stack, modifierKey, spectator)).orElse(false);
	}

	public static boolean isModifierActive(Entity entity, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return isModifierActive(entity.registryAccess(), stack, modifierKey, entity.isSpectator());
	}

	public static boolean isModifierActive(Entity entity, ResourceKey<TravellersModifier> modifierKey) {
		return entity instanceof LivingEntity livingEntity && isModifierActive(livingEntity, modifierKey);
	}

	public static boolean isModifierActive(LivingEntity livingEntity, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(livingEntity.registryAccess(), modifierKey);
		if (modifier.isEmpty())
			return false;
		ItemStack equippedStack = getStackForGroup(livingEntity, modifier.get().group());
		return !equippedStack.isEmpty() && modifier.get().isActive(equippedStack, modifierKey, livingEntity.isSpectator());
	}

	public static boolean hasTravellersModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return getCachedModifier(registries, modifierKey).map(modifier -> modifier.hasModifier(stack)).orElse(false);
	}

	public static boolean addModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof InsertableTravellersModifier insertableTravellersModifier))
			return false;
		return insertableTravellersModifier.addModifier(stack);
	}

	public static boolean transferModifier(HolderLookup.Provider registries, ItemStack stack, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof TransferableTravellersModifier transferableTravellersModifier))
			return false;
		return transferableTravellersModifier.transfer(stack, ingredients);
	}

	public static int getModifierDataComponentProviders(HolderLookup.Provider registries, List<Ingredient> ingredients, ResourceKey<TravellersModifier> modifierKey) {
		Optional<TravellersModifier> modifier = getCachedModifier(registries, modifierKey);
		if (modifier.isEmpty() || !(modifier.get() instanceof TransferableComponentModifier transferableComponentModifier))
			return 0;
		return transferableComponentModifier.findDataComponentProviders(ingredients).size();
	}

	public static MutableComponent getModifierTooltipComponent(Holder.Reference<TravellersModifier> modifier) {
		return TooltipStringInterpolator.render(modifier.getKey().identifier().toLanguageKey(modifier.value().getPrefix()));
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

	public static boolean isModifierEnabled(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifierKey) {
		return getCachedModifier(registries, modifierKey).isPresent();
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

	public static void clearCache() {
		CACHED_MODIFIERS.clear();
		MISSING_MODIFIERS.clear();
	}

	private static Optional<TravellersModifier> getCachedModifier(HolderLookup.Provider registries, ResourceKey<TravellersModifier> modifierKey) {
		TravellersModifier cached = CACHED_MODIFIERS.get(modifierKey);
		if (cached != null)
			return Optional.of(cached);
		if (MISSING_MODIFIERS.contains(modifierKey))
			return Optional.empty();

		Optional<Holder.Reference<TravellersModifier>> modifier = registries.holder(modifierKey);
		if (modifier.isPresent()) {
			CACHED_MODIFIERS.put(modifierKey, modifier.get().value());
			return Optional.of(modifier.get().value());
		}

		TwilightForestMod.LOGGER.warn("Travellers modifier {} is not present in the registry", modifierKey.identifier());
		MISSING_MODIFIERS.add(modifierKey);
		return Optional.empty();
	}

	public static final class CacheInvalidationReloadListener extends SimplePreparableReloadListener<Unit> {
		public static final CacheInvalidationReloadListener INSTANCE = new CacheInvalidationReloadListener();

		private CacheInvalidationReloadListener() {
		}

		@Override
		protected Unit prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
			return Unit.INSTANCE;
		}

		@Override
		protected void apply(Unit object, ResourceManager resourceManager, ProfilerFiller profiler) {
			clearCache();
		}
	}
}
