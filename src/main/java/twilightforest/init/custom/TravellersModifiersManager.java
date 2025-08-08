package twilightforest.init.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataComponents;
import twilightforest.item.travellers_gear.modifiers.*;

import java.util.*;

public class TravellersModifiersManager {
	public static class ManagedTravellersModifier {
		public final ResourceKey<TravellersModifier> key;
		private ModifiersLoadedState loadedState;

		@Nullable
		private TravellersModifier modifier;
		protected ManagedTravellersModifier(ResourceKey<TravellersModifier> key, ModifiersLoadedState loadedState, @Nullable TravellersModifier modifier) {
			this.key = key;
			this.loadedState = loadedState;
			this.modifier = modifier;
		}

		protected List<TravellersModifier> modifiers = new ArrayList<>();
		protected ManagedTravellersModifier(ResourceKey<TravellersModifier> key) {
			this(key, ModifiersLoadedState.PENDING, null);
		}

		@Nullable
		public TravellersModifier get() {
			if (loadedState == ModifiersLoadedState.PENDING)
				tryToLoad();
			if (loadedState == ModifiersLoadedState.LOADED)
				return modifier;
			return null;
		}

		private void tryToLoad() {
			modifier = TravellersModifiersManager.getTravellersModifier(key);
			loadedState = modifier == null ? ModifiersLoadedState.ERROR : ModifiersLoadedState.LOADED;
		}
	}

	public static final HashMap<ResourceKey<TravellersModifier>, ManagedTravellersModifier> MANAGED_TRAVELLERS_MODIFIER_HASH_MAP = new HashMap<>();
	public static final Lazy<List<TravellersModifier>> TRAVELLERS_MODIFIERS = Lazy.of(TravellersModifiersManager::getAllTravellersModifiers);
	public static final Lazy<List<InsertableTravellersModifier>> INSERTABLE_MODIFIERS = Lazy.of(TravellersModifiersManager::getAllInsertableTravellersModifiers);
	public static final Lazy<List<BuiltinTravellersComponentModifier>> BUILTIN_MODIFIERS = Lazy.of(TravellersModifiersManager::getAllBuiltInComponentModifiers);
	public static final Lazy<List<TravellersEntryModifier>> ENTRY_MODIFIERS = Lazy.of(TravellersModifiersManager::getAllEntryModifiers);


	/**
	 * Modifiers are ordered by the length of the name in English, longest to shortest.
	 */

	// all
	public static final ManagedTravellersModifier AUTO_REPAIR_MODIFIER = makeManagedModifier("auto_repair");
	// goggles
	public static final ManagedTravellersModifier ZOOM_ABILITY = makeManagedModifier("zoom");
	public static final ManagedTravellersModifier AQUATIC_AGILITY_MODIFIER = makeManagedModifier("aquatic_agility");
	public static final ManagedTravellersModifier RED_THREAD_VISION_MODIFIER = makeManagedModifier("red_thread_vision");
	public static final ManagedTravellersModifier ALL_NIGHT_GOGGLES_MODIFIER = makeManagedModifier("all_night_goggles");
	public static final ManagedTravellersModifier ITEM_DISPLAY_MODIFIER = makeManagedModifier("item_display");
	// vest
	public static final ManagedTravellersModifier SWIFT_SWIM_ABILITY = makeManagedModifier("swift_swim");
	public static final ManagedTravellersModifier STEALTH_MODIFIER = makeManagedModifier("stealth");
	public static final ManagedTravellersModifier ARROW_MAGNETISM_MODIFIER = makeManagedModifier("arrow_magnetism");
	public static final ManagedTravellersModifier FOOD_EFFICIENCY_MODIFIER = makeManagedModifier("food_efficiency");
	public static final ManagedTravellersModifier PERFECT_DODGE_MODIFIER = makeManagedModifier("perfect_dodge");
	public static final ManagedTravellersModifier HASTE_MODIFIER = makeManagedModifier("haste");
	// belt
	public static final ManagedTravellersModifier SWAP_HOTBAR_ABILITY = makeManagedModifier("swap_hotbar");
	public static final ManagedTravellersModifier SWAP_HOTBAR_MODIFIER = makeManagedModifier("swap_hotbar_modifier");
	// wings
	public static final ManagedTravellersModifier HIGH_JUMP_ABILITY = makeManagedModifier("high_jump");
	public static final ManagedTravellersModifier CONTROLLED_FALL_MODIFIER = makeManagedModifier("controlled_fall");
	public static final ManagedTravellersModifier AGILE_RANGER_MODIFIER = makeManagedModifier("agile_ranger");
	public static final ManagedTravellersModifier DOUBLE_JUMP_MODIFIER = makeManagedModifier("double_jump");
	public static final ManagedTravellersModifier SIDESTEP_MODIFIER = makeManagedModifier("side_step");
	// boots
	public static final ManagedTravellersModifier HIGH_STEP_ABILITY = makeManagedModifier("high_step");
	public static final ManagedTravellersModifier STRAIGHT_AHEAD_MODIFIER = makeManagedModifier("straight_ahead");
	public static final ManagedTravellersModifier SLIMY_SOLES_MODIFIER = makeManagedModifier("slimy_soles");
	public static final ManagedTravellersModifier WATER_WALK_MODIFIER = makeManagedModifier("water_walk");

	public static final Set<ResourceLocation> ALWAYS_ACTIVE = Set.of(AUTO_REPAIR_MODIFIER.key.location());

	private static ManagedTravellersModifier makeManagedModifier(String name) {
		ManagedTravellersModifier modifier = new ManagedTravellersModifier(ResourceKey.create(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TwilightForestMod.prefix(name)));
		MANAGED_TRAVELLERS_MODIFIER_HASH_MAP.put(modifier.key, modifier);
		return modifier;
	}

	public static void bootstrap(BootstrapContext<TravellersModifier> context) {
		register(context, AUTO_REPAIR_MODIFIER, new TravellersComponentModifier(AUTO_REPAIR_MODIFIER.key.location(), TFDataComponents.AUTO_REPAIR_PROBABILITY.get(), 0.001F));
		register(context, ZOOM_ABILITY, new BuiltinTravellersComponentModifier(ZOOM_ABILITY.key.location(), TFDataComponents.ZOOM_ABILITY_MODIFIER.get()));
		register(context, AQUATIC_AGILITY_MODIFIER, new TravellersEntryModifier(
			AQUATIC_AGILITY_MODIFIER.key.location(),
			List.of(
				new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN_ACTIVE, EquipmentSlotGroup.HEAD),
				new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING_ACTIVE, EquipmentSlotGroup.HEAD)
			),
			List.of(
				new ItemAttributeModifiers.Entry(Attributes.OXYGEN_BONUS, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_OXYGEN_DEACTIVATED, EquipmentSlotGroup.HEAD),
				new ItemAttributeModifiers.Entry(Attributes.SUBMERGED_MINING_SPEED, TFAttributeModifiers.TRAVELLERS_AQUATIC_AGILITY_MINING_DEACTIVATED, EquipmentSlotGroup.HEAD)
			)
		));
		register(context, RED_THREAD_VISION_MODIFIER, new TravellersComponentModifier(RED_THREAD_VISION_MODIFIER.key.location(), TFDataComponents.RED_THREAD_VISION.get(), Unit.INSTANCE));
		register(context, ALL_NIGHT_GOGGLES_MODIFIER, new TravellersComponentModifier(ALL_NIGHT_GOGGLES_MODIFIER.key.location(), TFDataComponents.ALL_NIGHT_GOGGLES.get(), Unit.INSTANCE));
		register(context, ITEM_DISPLAY_MODIFIER, new TravellersComponentModifier(ITEM_DISPLAY_MODIFIER.key.location(), TFDataComponents.ITEM_DISPLAY.get(), ItemDisplayContents.EMPTY));

		register(context, SWIFT_SWIM_ABILITY, new TravellersEntryModifier(
			SWIFT_SWIM_ABILITY.key.location(),
			List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM_ACTIVATE, EquipmentSlotGroup.CHEST)),
			List.of(new ItemAttributeModifiers.Entry(Attributes.WATER_MOVEMENT_EFFICIENCY, TFAttributeModifiers.TRAVELLERS_SWIFT_SWIM_DEACTIVATED, EquipmentSlotGroup.CHEST))
		));
		register(context, STEALTH_MODIFIER, new TravellersComponentModifier(STEALTH_MODIFIER.key.location(), TFDataComponents.STEALTH_CROUCHING.get(), Unit.INSTANCE));
		register(context, ARROW_MAGNETISM_MODIFIER, new TravellersComponentModifier(ARROW_MAGNETISM_MODIFIER.key.location(), TFDataComponents.ARROW_MAGNETISM.get(), Unit.INSTANCE));
		register(context, FOOD_EFFICIENCY_MODIFIER, new TravellersComponentModifier(FOOD_EFFICIENCY_MODIFIER.key.location(), TFDataComponents.EFFICIENT_EATER.get(), 2F));
		register(context, PERFECT_DODGE_MODIFIER, new TravellersComponentModifier(PERFECT_DODGE_MODIFIER.key.location(), TFDataComponents.PERFECT_DODGE_PROBABILITY.get(), 0.1F));
		register(context, HASTE_MODIFIER, new TravellersComponentModifier(HASTE_MODIFIER.key.location(), TFDataComponents.HASTE_AMPLIFIER.get(), 1));

		register(context, SWAP_HOTBAR_ABILITY, new BuiltinTravellersComponentModifier(SWAP_HOTBAR_ABILITY.key.location(), TFDataComponents.SWAP_HOTBAR_ABILITY.get()));
		register(context, SWAP_HOTBAR_MODIFIER, new TravellersComponentModifier(SWAP_HOTBAR_MODIFIER.key.location(), TFDataComponents.SWAP_HOTBAR_MODIFIER.get(), Unit.INSTANCE));

		register(context, HIGH_JUMP_ABILITY, new BuiltinTravellersComponentModifier(HIGH_JUMP_ABILITY.key.location(), TFDataComponents.HIGH_JUMP_AMPLIFIER.get()));
		register(context, CONTROLLED_FALL_MODIFIER, new TravellersComponentModifier(CONTROLLED_FALL_MODIFIER.key.location(), TFDataComponents.CONTROLLED_FALLING_MULTIPLIER.get(), 1 - 1 / 6F));
		register(context, AGILE_RANGER_MODIFIER, new TravellersComponentModifier(AGILE_RANGER_MODIFIER.key.location(), TFDataComponents.AGILE_RANGER_MODIFIER.get(), 5F));
		register(context, DOUBLE_JUMP_MODIFIER, new TravellersComponentModifier(DOUBLE_JUMP_MODIFIER.key.location(), TFDataComponents.DOUBLE_JUMP.get(), Unit.INSTANCE));
		register(context, SIDESTEP_MODIFIER, new TravellersComponentModifier(SIDESTEP_MODIFIER.key.location(), TFDataComponents.SIDESTEP_COOLDOWN.get(), 3 * 20L));

		register(context, HIGH_STEP_ABILITY, new TravellersEntryModifier(
			HIGH_STEP_ABILITY.key.location(),
			List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP_ACTIVE, EquipmentSlotGroup.FEET)),
			List.of(new ItemAttributeModifiers.Entry(Attributes.STEP_HEIGHT, TFAttributeModifiers.TRAVELLERS_HIGH_STEP_DEACTIVATED, EquipmentSlotGroup.FEET))
		));
		register(context, STRAIGHT_AHEAD_MODIFIER, new TravellersComponentModifier(STRAIGHT_AHEAD_MODIFIER.key.location(), TFDataComponents.FORWARD_BOOST_MULTIPLIER.get(), 1.4));
		register(context, SLIMY_SOLES_MODIFIER, new TravellersComponentModifier(SLIMY_SOLES_MODIFIER.key.location(), TFDataComponents.SLIMY_SOLES_COEFFICIENT.get(), 0.5F));
		register(context, WATER_WALK_MODIFIER, new TravellersComponentModifier(WATER_WALK_MODIFIER.key.location(), TFDataComponents.WATER_WALK.get(), Unit.INSTANCE));
	}

	protected static void register(BootstrapContext<TravellersModifier> context, ManagedTravellersModifier managedTravellersModifier, TravellersModifier travellersModifier) {
		context.register(managedTravellersModifier.key, travellersModifier);
	}

	public static List<InsertableTravellersModifier> findAllInsertableModifiers(ItemStack stack) {
		return INSERTABLE_MODIFIERS.get().stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static List<BuiltinTravellersComponentModifier> findAllBuiltinModifiers(ItemStack stack) {
		return BUILTIN_MODIFIERS.get().stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static List<TravellersEntryModifier> findAllEntryModifiers(ItemStack stack) {
		return ENTRY_MODIFIERS.get().stream().filter(travellersModifier -> travellersModifier.hasModifier(stack)).toList();
	}

	public static long countInsertableModifiers(ItemStack stack) {
		return findAllInsertableModifiers(stack).size();
	}

	public static boolean isModifierActive(ItemStack stack, ManagedTravellersModifier managedTravellersModifier) {
		TravellersModifier modifier = managedTravellersModifier.get();
		if (modifier == null)
			return false;
		return modifier.isActive(stack);
	}

	public static boolean hasTravellersModifier(ItemStack stack, ManagedTravellersModifier managedTravellersModifier) {
		TravellersModifier modifier = managedTravellersModifier.get();
		if (modifier == null)
			return false;
		return modifier.hasModifier(stack);
	}

	public static boolean addModifier(ItemStack stack, ManagedTravellersModifier managedTravellersModifier) {
		TravellersModifier modifier = managedTravellersModifier.get();
		if (!(modifier instanceof InsertableTravellersModifier insertableTravellersModifier))
			return false;
		insertableTravellersModifier.addModifier(stack);
		return true;
	}

	protected static List<TravellersModifier> getAllTravellersModifiers() {
		HolderLookup.RegistryLookup<TravellersModifier> registryLookup = getRegistryLookup();
		if (registryLookup == null)
			throw new RuntimeException("Unable to find registry lookup");

		return registryLookup.listElements().map(Holder.Reference::value).toList();
	}

	protected static List<InsertableTravellersModifier> getAllInsertableTravellersModifiers() {
		return getAllTravellersModifiersOFType(InsertableTravellersModifier.class);
	}

	protected static List<BuiltinTravellersComponentModifier> getAllBuiltInComponentModifiers() {
		return getAllTravellersModifiersOFType(BuiltinTravellersComponentModifier.class);
	}

	protected static List<TravellersEntryModifier> getAllEntryModifiers() {
		return getAllTravellersModifiersOFType(TravellersEntryModifier.class);
	}

	protected static @Nullable HolderLookup.RegistryLookup<TravellersModifier> getRegistryLookup() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			TwilightForestMod.LOGGER.error("Current server is null");
			return null;
		}

		Optional<HolderLookup.RegistryLookup<TravellersModifier>> registryLookup =
			server.registryAccess().lookup(TFRegistries.Keys.TRAVELLERS_MODIFIERS);
		if (registryLookup.isEmpty()) {
			TwilightForestMod.LOGGER.error("Unable to find registry lookup for {}", TFRegistries.Keys.TRAVELLERS_MODIFIERS);
			return null;
		}
		return registryLookup.get();
	}

	public static @Nullable TravellersModifier getTravellersModifier(ResourceKey<TravellersModifier> key) {
		HolderLookup.RegistryLookup<TravellersModifier> registryLookup = getRegistryLookup();
		if (registryLookup == null)
			return null;
		Optional<Holder.Reference<TravellersModifier>> travellersModifier = registryLookup.get(key);
		if (travellersModifier.isEmpty()) {
			TwilightForestMod.LOGGER.error("Unable to find modifier for {}", key);
			return null;
		}

		return travellersModifier.get().value();
	}

	protected static <T extends TravellersModifier> List<T> getAllTravellersModifiersOFType(Class<T> type) {
		return TRAVELLERS_MODIFIERS.get().stream()
			.filter(type::isInstance)
			.map(type::cast)
			.toList();
	}

	protected enum ModifiersLoadedState {
		LOADED,
		ERROR,
		PENDING
	}
}
