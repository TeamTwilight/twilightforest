package twilightforest.events;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
// TODO: Port curios compat module
//import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFItemTags;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;

@tamaized.beanification.Component
public class CharmEvents {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::applyCharmOfLife);
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, this::applyKeepingAndCasket);
		NeoForge.EVENT_BUS.addListener(this::returnItemsOnRespawn);
	}

	// Check for charm of life first to stop a player from dying
	private void applyCharmOfLife(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (handleCharmOfLife(player)) event.setCanceled(true); // Executes if the player had charms
	}

	// Then check if the player should keep any items through death
	private void applyKeepingAndCasket(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (!((net.minecraft.server.level.ServerLevel) living.level()).getGameRules().get(GameRules.KEEP_INVENTORY)) {
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
		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(player, new EntityEquipment());
		ListTag tagList = new ListTag();

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, player.getInventory().getNonEquipmentItems())) {
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, player.getInventory().getNonEquipmentItems().subList(0, 9))) {
				int i = player.getInventory().getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, NonNullList.of(player.getInventory().getNonEquipmentItems().get(i)));
				}
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int slot = 0; slot < player.getInventory().getNonEquipmentItems().size(); slot++) {
			ItemStack stack = player.getInventory().getNonEquipmentItems().get(slot);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.getNonEquipmentItems().set(slot, stack.copy());
				player.getInventory().getNonEquipmentItems().set(slot, ItemStack.EMPTY);
			}
		}

		for (EquipmentSlot equipmentSlot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			ItemStack armor = player.getItemBySlot(equipmentSlot);
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				player.setItemSlot(equipmentSlot, ItemStack.EMPTY);
			}
		}

		if (player.getItemBySlot(EquipmentSlot.OFFHAND).is(TFItemTags.KEPT_ON_DEATH)) {
			player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			saveInventoryToListTag(player.registryAccess(), keepInventory, tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
		}
	}

	private static boolean applyCharm(DeferredItem<Item> charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);
		//merge armor and offhand into check slots since theyll always be kept by a charm
		for (EquipmentSlot slot : List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)) {
			mergedCheck.add(player.getItemBySlot(slot));
		}
		mergedCheck.add(player.getItemBySlot(EquipmentSlot.OFFHAND));
		//first, check all affected slots to make sure they arent empty.
		//filter out the charm so it doesnt count towards keeping items if its the only thing we are holding
		if (mergedCheck.stream().filter(stack -> !stack.is(charm)).allMatch(ItemStack::isEmpty)) return false;

		//do we even have a charm? No? Then stop operation
		if (!TFItemStackUtils.consumeInventoryItem(player, charm, getPlayerData(player), true) && !hasCharmCurio(charm.value(), player)) return false;

		boolean keptACasket = keepWholeListAndCheckCasket(keptInventory.getNonEquipmentItems(), inventorySlots, charm == TFItems.CHARM_OF_KEEPING_3);
		keptACasket = keepArmorAndCheckCasket(keptInventory, player, keptACasket);
		keepOffhandAndCheckCasket(keptInventory, player, keptACasket);

		return true;
	}

	private static boolean keepArmorAndCheckCasket(Inventory keptInventory, Player player, boolean skipCasketCheck) {
		List<ItemStack> armorItems = List.of(
			player.getItemBySlot(EquipmentSlot.FEET),
			player.getItemBySlot(EquipmentSlot.LEGS),
			player.getItemBySlot(EquipmentSlot.CHEST),
			player.getItemBySlot(EquipmentSlot.HEAD)
		);
		boolean keptCasket = true;
		for (int i = 0; i < armorItems.size(); i++) {
			var item = armorItems.get(i).copy();
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				EquipmentSlot slot = switch (i) {
					case 0 -> EquipmentSlot.FEET;
					case 1 -> EquipmentSlot.LEGS;
					case 2 -> EquipmentSlot.CHEST;
					case 3 -> EquipmentSlot.HEAD;
					default -> null;
				};
				if (slot != null) {
					player.setItemSlot(slot, ItemStack.EMPTY);
				}
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					player.setItemSlot(EquipmentSlot.FEET, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	private static boolean keepOffhandAndCheckCasket(Inventory keptInventory, Player player, boolean skipCasketCheck) {
		ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
		if (!skipCasketCheck && offhand.is(TFItems.KEEPSAKE_CASKET)) {
			if (offhand.getCount() > 1) {
				offhand.shrink(1);
				player.setItemSlot(EquipmentSlot.OFFHAND, offhand.copyWithCount(1));
			}
		} else {
			player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
		}
		return true;
	}

	private static void stockKeepsakeCasket(Player player) {
		//make sure we are still actually holding onto items before trying to place a casket
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

			int damage = getPlayerData(player).contains(CASKET_DAMAGE_TAG) ? getPlayerData(player).getInt(CASKET_DAMAGE_TAG).orElse(0) : 0;
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
				//make it so only the player who died can open the chest if our config allows us
				casket.owner = ResolvableProfile.createResolved(player.getGameProfile());
			} else {
				casket.owner = null;
			}

			//some names are way too long for the casket so we'll cut them down
			String modifiedName = player.getName().getString().substring(0, Math.min(12, player.getName().getString().length()));
			casket.name = (Component.literal(modifiedName + "'s " + (level.getRandom().nextInt(1000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));

			int casketCapacity = casket.getContainerSize();
			List<ItemStack> list = new ArrayList<>(casketCapacity);
			NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

			// lets add our inventory exactly how it was on us
			list.addAll(TFItemStackUtils.sortArmorForCasket(player));
			list.addAll(filler);
			list.add(player.getItemBySlot(EquipmentSlot.OFFHAND));
			player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
			list.addAll(TFItemStackUtils.sortInvForCasket(player));
			player.getInventory().getNonEquipmentItems().clear();

			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			//inventory is empty minus the casket: put the casket into the kept inventory
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET)) {
					Inventory tmp = new Inventory(player, new EntityEquipment());
					ListTag charmInvTag = getPlayerData(player).getListOrEmpty(CHARM_INV_TAG);
					loadListTagToInventory(player.registryAccess(), charmInvTag, tmp);
					tmp.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);
					ListTag savedTag = new ListTag();
					saveInventoryToListTag(player.registryAccess(), tmp, savedTag);
					getPlayerData(player).put(CHARM_INV_TAG, savedTag);
				}
			}
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level().isClientSide() && playerData.contains(CHARM_INV_TAG)) {
			ListTag tagList = playerData.getList(CHARM_INV_TAG).orElse(new ListTag());
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			getPlayerData(player).getList(CHARM_INV_TAG).orElse(new ListTag()).clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (getPlayerData(player).contains(CONSUMED_CHARM_TAG)) {
			net.minecraft.nbt.Tag charmData = getPlayerData(player).get(CONSUMED_CHARM_TAG);
			CompoundTag compound = charmData instanceof CompoundTag ct ? ct : new CompoundTag();
			ItemStack stack = ItemStack.OPTIONAL_CODEC.decode(player.registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), compound).result().map(Pair::getFirst).orElse(ItemStack.EMPTY);

			if (player instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(stack, TFSounds.CHARM_KEEP.getKey()));
				serverPlayer.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED.get());
			}
			getPlayerData(player).remove(CONSUMED_CHARM_TAG);
		}
	}

	public static CompoundTag getPlayerData(Player player) {
		if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}
		return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).orElse(new CompoundTag());
	}

	//transfers a list of items to another
	private static boolean keepWholeListAndCheckCasket(NonNullList<ItemStack> transferTo, List<ItemStack> transferFrom, boolean skipCasketCheck) {
		boolean keptCasket = false;
		for (int i = 0; i < transferFrom.size(); i++) {
			var item = transferFrom.get(i).copy();
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				transferTo.set(i, item);
				transferFrom.set(i, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					transferTo.set(i, item);
					transferFrom.set(i, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	// Helper to save inventory items to a ListTag (replaces old Inventory.save(ListTag))
	private static void saveInventoryToListTag(RegistryAccess registryAccess, Inventory inventory, ListTag tagList) {
		NonNullList<ItemStack> items = inventory.getNonEquipmentItems();
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (!stack.isEmpty()) {
				CompoundTag tag = new CompoundTag();
				tag.putByte("Slot", (byte) i);
				tag.merge((CompoundTag) ItemStack.CODEC.encodeStart(registryAccess.createSerializationContext(NbtOps.INSTANCE), stack).getOrThrow());
				tagList.add(tag);
			}
		}
	}

	// Helper to load items from a ListTag into an Inventory
	private static void loadListTagToInventory(RegistryAccess registryAccess, ListTag tagList, Inventory inventory) {
		for (int i = 0; i < tagList.size(); i++) {
			CompoundTag tag = tagList.getCompound(i).orElse(new CompoundTag());
			int slot = tag.getByte("Slot").orElse((byte)0) & 0xFF;
			ItemStack stack = ItemStack.OPTIONAL_CODEC.decode(registryAccess.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), tag).result().map(Pair::getFirst).orElse(ItemStack.EMPTY);
			if (!stack.isEmpty() && slot >= 0 && slot < inventory.getNonEquipmentItems().size()) {
				inventory.getNonEquipmentItems().set(slot, stack);
			}
		}
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if (ModList.get().isLoaded("curios")) {
			// TODO: Port curios compat module
			return false;
		}

		return false;
	}
}
