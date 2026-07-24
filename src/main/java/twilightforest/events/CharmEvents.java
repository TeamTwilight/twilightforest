package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
import twilightforest.config.TFConfig;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.tags.TFItemTags;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;

@tamaized.beanification.Component
public class CharmEvents {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";
	public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

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

		if (living.level() instanceof ServerLevel serverLevel) {
			if (serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) {
				// Did the player recover? No? Let's give them their stuff based on the keeping charms
				handleCharmOfKeeping(player);

				// Then let's store the rest of their stuff in the casket
				stockKeepsakeCasket(player);
			}
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
		Inventory keepInventory = new Inventory(player, new PlayerEquipment(player));

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, player.getInventory().getNonEquipmentItems())) {
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, player.getInventory().getNonEquipmentItems().subList(0, 9))) {
				int i = player.getInventory().getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, NonNullList.of(player.getInventory().getNonEquipmentItems().get(i)));
				}
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int i = 0; i < player.getInventory().getNonEquipmentItems().size(); i++) {
			ItemStack stack = player.getInventory().getNonEquipmentItems().get(i);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.getNonEquipmentItems().set(i, stack.copy());
				player.getInventory().getNonEquipmentItems().set(i, ItemStack.EMPTY);
			}
		}

		for (int i = 0; i < CharmEvents.ARMOR_SLOTS.length; i++) {
			int slotIndex = i + Inventory.INVENTORY_SIZE;
			ItemStack armor = player.getInventory().getItem(slotIndex);
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.setItem(slotIndex, armor.copy());
				player.getInventory().setItem(slotIndex, ItemStack.EMPTY);
			}
		}

		if (player.getInventory().getItem(Inventory.SLOT_OFFHAND).is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.setItem(Inventory.SLOT_OFFHAND, player.getInventory().getItem(Inventory.SLOT_OFFHAND));
			player.getInventory().setItem(Inventory.SLOT_OFFHAND, ItemStack.EMPTY);
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			ProblemReporter.Collector reporter = new ProblemReporter.Collector();
			TagValueOutput valueOutput = TagValueOutput.createWithContext(reporter, player.level().registryAccess());
			ValueOutput.TypedOutputList<ItemStackWithSlot> typedList = valueOutput.list("items", ItemStackWithSlot.CODEC);
			keepInventory.save(typedList);
			CompoundTag resultTag = valueOutput.buildResult();
			ListTag tagList = resultTag.getListOrEmpty("items");
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
		}
	}

	private static boolean applyCharm(DeferredItem<Item> charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);
		//merge armor and offhand into check slots since theyll always be kept by a charm
		for (int i = 0; i < ARMOR_SLOTS.length; i++) {
			mergedCheck.add(player.getInventory().getItem(Inventory.INVENTORY_SIZE + i));
		}
		mergedCheck.add(player.getInventory().getItem(Inventory.SLOT_OFFHAND));
		//first, check all affected slots to make sure they arent empty.
		//filter out the charm so it doesnt count towards keeping items if its the only thing we are holding
		if (mergedCheck.stream().filter(stack -> !stack.is(charm)).allMatch(ItemStack::isEmpty)) return false;

		//do we even have a charm? No? Then stop operation
		if (!TFItemStackUtils.consumeInventoryItem(player, charm, getPlayerData(player), true) && !hasCharmCurio(charm.value(), player)) return false;

		boolean keptACasket = keepWholeListAndCheckCasket(keptInventory.getNonEquipmentItems(), inventorySlots, charm == TFItems.CHARM_OF_KEEPING_3);
		keptACasket = keepWholeListAndCheckCasket(keptInventory, player.getInventory(), 0, Inventory.INVENTORY_SIZE, keptACasket);
		keepWholeListAndCheckCasket(keptInventory, player.getInventory(), Inventory.SLOT_OFFHAND, Inventory.SLOT_OFFHAND + 1, keptACasket);

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

			int damage = getPlayerData(player).getIntOr(CASKET_DAMAGE_TAG, 0);
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
			list.add(player.getInventory().getItem(Inventory.SLOT_OFFHAND));
			list.addAll(TFItemStackUtils.sortInvForCasket(player));
			player.getInventory().clearContent();

			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			//inventory is empty minus the casket: put the casket into the kept inventory
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET.get())) {
					Inventory tmp = new Inventory(player, new PlayerEquipment(player));
					ListTag savedListTag = getPlayerData(player).getListOrEmpty(CHARM_INV_TAG);
					CompoundTag rootLoadTag = new CompoundTag();
					rootLoadTag.put("items", savedListTag);
					ProblemReporter.Collector loadReporter = new ProblemReporter.Collector();
					ValueInput valueInput = TagValueInput.create(loadReporter, player.level().registryAccess(), rootLoadTag);
					ValueInput.TypedInputList<ItemStackWithSlot> typedInputList = valueInput.listOrEmpty("items", ItemStackWithSlot.CODEC);
					tmp.load(typedInputList);
					tmp.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);
					ProblemReporter.Collector saveReporter = new ProblemReporter.Collector();
					TagValueOutput valueOutput = TagValueOutput.createWithContext(saveReporter, player.level().registryAccess());
					ValueOutput.TypedOutputList<ItemStackWithSlot> typedOutputList = valueOutput.list("items", ItemStackWithSlot.CODEC);
					tmp.save(typedOutputList);
					CompoundTag resultTag = valueOutput.buildResult();
					ListTag finalTagList = resultTag.getListOrEmpty("items");
					getPlayerData(player).put(CHARM_INV_TAG, finalTagList);
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
			ListTag tagList = playerData.getListOrEmpty(CHARM_INV_TAG);
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			getPlayerData(player).getListOrEmpty(CHARM_INV_TAG).clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (getPlayerData(player).contains(CONSUMED_CHARM_TAG)) {
			CompoundTag tag = (CompoundTag) getPlayerData(player).get(CONSUMED_CHARM_TAG);
			HolderLookup.Provider lookupProvider = player.registryAccess();
			ItemStack parsedStack = ItemStack.CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(ItemStack.EMPTY);
			ItemStack stack = tag.isEmpty() ? ItemStack.EMPTY : parsedStack;

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
		return player.getPersistentData().getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
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

	//transfers content from some slots to another slots
	private static boolean keepWholeListAndCheckCasket(Inventory transferTo, Inventory transferFrom, int fromSlot, int toSlot, boolean skipCasketCheck) {
		boolean keptCasket = false;
		for (int i = fromSlot; i < toSlot; i++) {
			var item = transferFrom.getItem(i).copy();
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				transferTo.setItem(i, item);
				transferFrom.setItem(i, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					transferTo.setItem(i, item);
					transferFrom.setItem(i, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.findAndConsumeCurio(item, player);
		}

		return false;
	}
}
