package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredItem;
import tamaized.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.components.entity.CharmAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.*;
import twilightforest.tags.TFItemTags;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;

@tamaized.beanification.Component
public class CharmEvents {
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	private static final List<Integer> CHARM_EQUIPMENT_SLOTS = List.of(
		EquipmentSlot.FEET.getIndex(Inventory.INVENTORY_SIZE),
		EquipmentSlot.LEGS.getIndex(Inventory.INVENTORY_SIZE),
		EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE),
		EquipmentSlot.HEAD.getIndex(Inventory.INVENTORY_SIZE),
		Inventory.SLOT_OFFHAND
	);

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::applyCharmOfLife);
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, this::applyKeepingAndCasket);
		NeoForge.EVENT_BUS.addListener(this::returnItemsOnRespawn);
	}

	// Check for charm of life first to stop a player from dying
	private void applyCharmOfLife(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		// Ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (handleCharmOfLife(player)) event.setCanceled(true); // Executes if the player had charms
	}

	// Then check if the player should keep any items through death
	private void applyKeepingAndCasket(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		// Ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (living.level() instanceof ServerLevel server && !server.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			handleCharmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			stockKeepsakeCasket(player);
		}
	}

	private void returnItemsOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
		if (!event.isEndConquered()) {
			returnStoredItems(serverPlayer);
		}
	}

	private static boolean handleCharmOfLife(Player player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2.get(), getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_2.get(), player);
		boolean charm1 = !charm2 && (TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1.get(), getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_1.get(), player));

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth(player.getMaxHealth());

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(new ItemStack(charm1 ? TFItems.CHARM_OF_LIFE_1.get() : TFItems.CHARM_OF_LIFE_2.get()), TFSounds.CHARM_LIFE.getKey()));
				serverPlayer.awardStat(TFStats.LIFE_CHARMS_ACTIVATED.get());
			}

			return true;
		}

		return false;
	}

	private static void handleCharmOfKeeping(Player player) {
		// Create an additional inventory to hold our stored items
		Inventory inventory = player.getInventory();
		Inventory keepInventory = new Inventory(player, new EntityEquipment());

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, inventory.getNonEquipmentItems())) {
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, inventory.getNonEquipmentItems().subList(0, 9))) {
				int i = inventory.getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, inventory.getNonEquipmentItems().subList(i, i + 1));
				}
			}
		}

		// Keep all items in the KEPT_ON_DEATH tag
		for (int i = 0; i < inventory.getNonEquipmentItems().size(); i++) {
			keepTaggedItem(inventory, keepInventory, i);
		}

		// Handle equipment as well
		for (int slot : Inventory.EQUIPMENT_SLOT_MAPPING.keySet()) {
			keepTaggedItem(inventory, keepInventory, slot);
		}

		// Save our data to the attachment
		CharmAttachment attachment = CharmAttachment.fromInventory(keepInventory);
		if (attachment.isEmpty()) {
			player.removeData(TFDataAttachments.CHARM_INVENTORY);
		} else {
			player.setData(TFDataAttachments.CHARM_INVENTORY, attachment);
		}
	}

	private static void keepTaggedItem(Inventory inventory, Inventory keepInventory, int slot) {
		ItemStack stack = inventory.getItem(slot);
		if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.setItem(slot, stack.copy());
			inventory.setItem(slot, ItemStack.EMPTY);
		}
	}

	private static boolean applyCharm(DeferredItem<Item> charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);

		// Armor and offhand are always kept by a Charm of Keeping
		mergedCheck.addAll(List.of(
			player.getInventory().getItem(EquipmentSlot.FEET.getIndex(36)),
			player.getInventory().getItem(EquipmentSlot.LEGS.getIndex(36)),
			player.getInventory().getItem(EquipmentSlot.CHEST.getIndex(36)),
			player.getInventory().getItem(EquipmentSlot.HEAD.getIndex(36)),
			player.getInventory().getItem(Inventory.SLOT_OFFHAND)
		));

		// Filter out the charm so it does not count towards keeping items if it is the only thing being held
		if (mergedCheck.stream().filter(stack -> !stack.is(charm)).allMatch(ItemStack::isEmpty)) {
			return false;
		}

		// Stop operation if there is no charm present
		if (!TFItemStackUtils.consumeInventoryItem(player, charm, getPlayerData(player), true) && !hasCharmCurio(charm.value(), player)) {
			return false;
		}

		boolean skipCasketCheck = charm == TFItems.CHARM_OF_KEEPING_3;
		boolean keptCasket = false;

		for (int i = 0; i < inventorySlots.size(); i++) {
			ItemStack item = inventorySlots.get(i).copy();
			if (skipCasketCheck || !item.is(TFItems.KEEPSAKE_CASKET) || keptCasket) {
				keptInventory.getNonEquipmentItems().set(i, item);
				inventorySlots.set(i, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					keptInventory.getNonEquipmentItems().set(i, item);
					inventorySlots.set(i, item.copyWithCount(1));
				}
			}
		}

		for (int slot : CHARM_EQUIPMENT_SLOTS) {
			ItemStack item = player.getInventory().getItem(slot).copy();
			if (skipCasketCheck || !item.is(TFItems.KEEPSAKE_CASKET) || keptCasket) {
				keptInventory.setItem(slot, item);
				player.getInventory().setItem(slot, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					keptInventory.setItem(slot, item);
					player.getInventory().setItem(slot, item.copyWithCount(1));
				}
			}
		}

		return true;
	}

	private static void stockKeepsakeCasket(Player player) {
		// Make sure we are still actually holding onto items before trying to place a casket
		if (player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && !stack.is(TFItems.KEEPSAKE_CASKET))) {
			boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET, getPlayerData(player), false);

			if (!casketConsumed)
				return;

			Level level = player.level();
			BlockPos.MutableBlockPos pos = player.blockPosition().mutable();

			if (pos.getY() < level.dimensionType().minY() + 2) {
				pos.setY(level.dimensionType().minY() + 2);
			} else {
				int logicalHeight = player.level().dimensionType().logicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			pos.move(0, -1, 0);

			do {
				pos.move(0, 1, 0);
			} while (!level.getBlockState(pos).canBeReplaced());

			BlockPos immutablePos = pos.immutable();
			FluidState fluidState = level.getFluidState(immutablePos);

			int damage = getPlayerData(player).getInt(CASKET_DAMAGE_TAG).orElse(0);
			BlockState setState = TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState()
				.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()))
				.setValue(KeepsakeCasketBlock.BREAKAGE, damage)
				.setValue(KeepsakeCasketBlock.FACING, Direction.from2DDataValue(level.getRandom().nextInt(3)));

			if (player.getRandom().nextFloat() <= 0.15F) {
				if (damage >= 2) {
					setState = TFBlocks.SKULL_CHEST.get().withPropertiesOf(setState);
					TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, placing Skull Chest instead", player.getName().getString());
				} else {
					damage = damage + 1;
					setState = TFBlocks.KEEPSAKE_CASKET.get().withPropertiesOf(setState).setValue(KeepsakeCasketBlock.BREAKAGE, damage);
					TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
				}
			}

			if (!level.setBlockAndUpdate(immutablePos, setState)) {
				TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at {}", pos);
				return;
			}

			if (!(level.getBlockEntity(immutablePos) instanceof SkullChestBlockEntity casket)) {
				TwilightForestMod.LOGGER.error("Failed to set Keepsake Casket data at {}", pos);
				return;
			}

			if (TFConfig.casketUUIDLocking) {
				// Make it so only the player who died can open the chest if our config allows us
				casket.owner = ResolvableProfile.createResolved(player.getGameProfile());
			} else {
				casket.owner = null;
			}

			// Some names are way too long for the casket so we'll cut them down
			String modifiedName = player.getName().getString().substring(0, Math.min(12, player.getName().getString().length()));
			casket.name = (Component.literal(modifiedName + "'s " + (level.getRandom().nextInt(1000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));

			int casketCapacity = casket.getContainerSize();
			List<ItemStack> list = new ArrayList<>(casketCapacity);
			NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

			// Let's add our inventory exactly how it was on us.
			list.addAll(TFItemStackUtils.sortArmorForCasket(player));
			list.addAll(filler);
			list.addAll(TFItemStackUtils.sortInvForCasket(player));

			for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
				player.getInventory().setItem(slot, ItemStack.EMPTY);
			}

			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			// Inventory is empty minus the casket: put the casket into the kept inventory
			CharmAttachment attachment = player.getData(TFDataAttachments.CHARM_INVENTORY);
			Inventory keptInventory = attachment.toInventory(player);
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET)) {
					keptInventory.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);
				}
			}
			player.setData(TFDataAttachments.CHARM_INVENTORY, CharmAttachment.fromInventory(keptInventory));
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {
		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		if (!player.level().isClientSide() && player.hasData(TFDataAttachments.CHARM_INVENTORY)) {
			CharmAttachment attachment = player.getData(TFDataAttachments.CHARM_INVENTORY);
			player.removeData(TFDataAttachments.CHARM_INVENTORY);
			if (!attachment.isEmpty()) {
				attachment.applyTo(player.getInventory());
			}
		}

		CompoundTag playerData = getPlayerData(player);
		if (playerData.contains(CONSUMED_CHARM_TAG)) {
			if (player instanceof ServerPlayer serverPlayer) {
				ItemStack charm = ItemStack.CODEC
					.parse(player.registryAccess().createSerializationContext(NbtOps.INSTANCE), playerData.get(CONSUMED_CHARM_TAG))
					.resultOrPartial(TwilightForestMod.LOGGER::error)
					.orElse(ItemStack.EMPTY);
				if (!charm.isEmpty()) {
					PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(charm, TFSounds.CHARM_KEEP.getKey()));
				}
				serverPlayer.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED.get());
			}
			getPlayerData(player).remove(CONSUMED_CHARM_TAG);
		}
	}

	public static CompoundTag getPlayerData(Player player) {
		if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}
		return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).get();
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.findAndConsumeCurio(item, player);
		}

		return false;
	}
}
