package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
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

	public static boolean isModifierActive(Entity entity, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return isModifierActive(entity.registryAccess(), stack, modifierKey, entity.isSpectator());
	}

	public static boolean isModifierActive(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey, boolean spectator) {
		return lookupHolderOrThrow(registries, modifierKey).value().isActive(stack, modifierKey, spectator);
	}

	public static boolean isModifierActive(Entity entity, ResourceKey<TravellersModifier> modifierKey) {
		return entity instanceof LivingEntity livingEntity && isModifierActive(livingEntity, modifierKey);
	}

	public static boolean isModifierActive(LivingEntity livingEntity, ResourceKey<TravellersModifier> modifierKey) {
		TravellersModifier modifier = lookupHolderOrThrow(livingEntity.registryAccess(), modifierKey).value();
		ItemStack equippedStack = getStackForGroup(livingEntity, modifier.group());

		return !equippedStack.isEmpty()
			&& modifier.isActive(
			equippedStack,
			modifierKey,
			livingEntity.isSpectator()
		);
	}

	public static boolean hasTravellersModifier(ItemStack stack, Holder<TravellersModifier> modifierHolder) {
		return modifierHolder.value().hasModifier(stack);
	}

	public static boolean hasTravellersModifier(HolderLookup.Provider registries, ItemStack stack, ResourceKey<TravellersModifier> modifierKey) {
		return lookupHolderOrThrow(registries, modifierKey).value().hasModifier(stack);
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
		return TooltipStringInterpolator.render(getKeyOrThrow(modifier).identifier().toLanguageKey(TravellersModifier.getPrefix()));
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

	public static Holder.Reference<TravellersModifier> lookupHolderOrThrow(HolderLookup.Provider registries, ResourceKey<TravellersModifier> key) {
		return lookupHolder(registries, key).orElseThrow(() -> new IllegalStateException("TravellersModifier holder was not found"));
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
