package twilightforest.events;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.GrindstoneEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.TFRegistries;
import twilightforest.beans.Component;
import twilightforest.beans.PostConstruct;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.InsertableTravellersModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersEntryModifier;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;

import java.util.List;
import java.util.stream.Stream;

@Component
public class TravellersGearEvents {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::playerTickPre);
		NeoForge.EVENT_BUS.addListener(this::levelTickPost);
		NeoForge.EVENT_BUS.addListener(this::activateAndDeactivateTravellersModifiers);
		NeoForge.EVENT_BUS.addListener(this::cancelCombiningTravellersGear);
		NeoForge.EVENT_BUS.addListener(this::cancelPhantomSpawns);
		NeoForge.EVENT_BUS.addListener(this::fireCraftingModifierTrigger);
		NeoForge.EVENT_BUS.addListener(this::removeModifiersFromTravellersGear);
		NeoForge.EVENT_BUS.addListener(this::stopDamagingTravellersGear);
	}

	private void playerTickPre(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Boolean hasDoubleJump = null;
		if (!TravellersModifiersManager.isModifierActive(player.registryAccess(), player.getItemBySlot(EquipmentSlot.LEGS), TravellersModifiersManager.DOUBLE_JUMP_MODIFIER))
			hasDoubleJump = false;
		else if (player.onGround())
			hasDoubleJump = true;

		if (hasDoubleJump != null && hasDoubleJump != player.getData(TFDataAttachments.HAS_DOUBLE_JUMP)) {
			player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			player.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
		}

		//reset double jump wing anim if on the ground
		if (event.getEntity().level().isClientSide()) {
			if (player.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump && player.onGround()) {
				player.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJump = false;
				player.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM).doubleJumpTime = 0;
			}
		}

		TravellersArmorItem.travellersWingsSidestepCooldownSound(player);
	}

	private void levelTickPost(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.getEntities().getAll().forEach(entity -> {
				if (!(entity instanceof LivingEntity livingEntity))
					return;
				TravellersArmorItem.travellersWingsControlledFall(livingEntity);
				TravellersArmorItem.travellersVestHaste(livingEntity);
				TravellersArmorItem.travellersWingsHighJump(livingEntity);
				TravellersArmorItem.travellersGearAutoRepair(livingEntity);
				TravellersArmorItem.travellersBootsForwardBoost(livingEntity);
			});
		}
	}

	private void activateAndDeactivateTravellersModifiers(ItemAttributeModifierEvent event) {
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
			ItemStack armor = event.getItemStack();
			if (!armor.has(TFDataComponents.IS_TRAVELLERS_GEAR))
				return;

			List<Holder.Reference<TravellersModifier>> entryMods = access.lookupOrThrow(TFRegistries.Keys.TRAVELLERS_MODIFIERS).listElements().filter(travellersModifier -> travellersModifier.value() instanceof TravellersEntryModifier).toList();
			if (armor.getMaxDamage() - 1 <= armor.getDamageValue()) {
				entryMods.forEach(modifier -> ((TravellersEntryModifier)modifier.value()).deactivate(event));
			} else {
				entryMods.forEach(modifier -> ((TravellersEntryModifier)modifier.value()).activate(event));
			}
		}
	}

	private void stopDamagingTravellersGear(ArmorHurtEvent event) {
		if (!event.isCanceled()) {
			event.getArmorMap().forEach((slot, entry) -> {
				ItemStack damagedStack = event.getArmorItemStack(slot);
				if (damagedStack.has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
					if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage()) {
						event.setNewDamage(slot, 0);
					} else if (damagedStack.getDamageValue() + event.getNewDamage(slot) >= damagedStack.getMaxDamage() - 1 && event.getEntity() instanceof ServerPlayer player) {
						player.playNotifySound(SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, player.getVoicePitch());
					}
				}
			});
		}
	}

	private void cancelCombiningTravellersGear(AnvilUpdateEvent event) {
		if (event.getLeft().has(TFDataComponents.IS_TRAVELLERS_GEAR) && event.getRight().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			event.setCanceled(true);
		}
	}

	private void removeModifiersFromTravellersGear(GrindstoneEvent.OnPlaceItem event) {
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			RegistryAccess access = ServerLifecycleHooks.getCurrentServer().registryAccess();
			List<ItemStack> travellersItemStacks = Stream.of(event.getTopItem(), event.getBottomItem())
				.filter(stack -> stack.has(TFDataComponents.IS_TRAVELLERS_GEAR))
				.toList();

			if (travellersItemStacks.isEmpty())
				return; // Delegate to vanilla logic
			if (travellersItemStacks.size() > 1) {
				event.setCanceled(true);
				return;
			}
			ItemStack inputStack = travellersItemStacks.getFirst();
			List<Holder.Reference<TravellersModifier>> modifiers = TravellersModifiersManager.findAllInsertableModifiers(access, inputStack);
			if (modifiers.isEmpty()) {
				event.setCanceled(true);
				return;
			}

			ItemStack unmodifiedStack = inputStack.copy();
			modifiers.forEach(modifier -> ((InsertableTravellersModifier) modifier.value()).removeModifier(unmodifiedStack));
			ItemStack outputStack = unmodifiedStack.copy();
			if (outputStack.is(TFItems.TRAVELLERS_WINGS_BELT)) {
				outputStack = new ItemStack(TFItems.TRAVELLERS_WINGS, outputStack.getCount(), outputStack.getComponentsPatch());
				outputStack.remove(DataComponents.CONTAINER);
			}
			event.setOutput(outputStack);
		}
	}

	private void cancelPhantomSpawns(PlayerSpawnPhantomsEvent event) {
		if (TravellersModifiersManager.isModifierActive(event.getEntity().registryAccess(), event.getEntity().getItemBySlot(EquipmentSlot.HEAD), TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER)) {
			event.setResult(PlayerSpawnPhantomsEvent.Result.DENY);
		}
	}

	private void fireCraftingModifierTrigger(PlayerEvent.ItemCraftedEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && event.getCrafting().has(TFDataComponents.IS_TRAVELLERS_GEAR)) {
			ItemStack compareStack = ItemStack.EMPTY;
			for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
				if (event.getInventory().getItem(i).is(event.getCrafting().getItem())) compareStack = event.getInventory().getItem(i);
			}

			if (!compareStack.isEmpty()) {
				var oldMods = TravellersModifiersManager.findAllInsertableModifiers(player.registryAccess(), compareStack);
				TravellersModifiersManager.findAllInsertableModifiers(player.registryAccess(), event.getCrafting()).stream()
					.filter(modifier -> !oldMods.contains(modifier)).toList()
						.forEach(modifier -> TFAdvancements.ADD_MODIFIER.get().trigger(player, modifier.key().location()));
			}
		}
	}
}
