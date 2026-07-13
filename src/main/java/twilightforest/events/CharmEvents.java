package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEquipment;
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
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
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
			if (!serverLevel.getGameRules().get(GameRules.KEEP_INVENTORY)) {
				handleCharmOfKeeping(player);
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
		Inventory keepInventory = new Inventory(player, new EntityEquipment());

		List<ItemStack> allItems = new ArrayList<>();
		for (int i = 0; i < 36; i++) {
			allItems.add(player.getInventory().getItem(i));
		}

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, allItems)) {
			List<ItemStack> hotbarItems = allItems.subList(0, 9);
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, hotbarItems)) {
				int i = player.getInventory().getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, NonNullList.of(player.getInventory().getItem(i)));
				}
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			ItemStack armor = player.getInventory().armor.get(i);
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.armor.set(i, armor.copy());
				player.getInventory().armor.set(i, ItemStack.EMPTY);
			}
		}

		if (player.getInventory().offhand.getFirst().is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.offhand.set(0, player.getInventory().offhand.getFirst().copy());
			player.getInventory().offhand.set(0, ItemStack.EMPTY);
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			ProblemReporter.Collector reporter = new ProblemReporter.Collector();
			TagValueOutput valueOutput = TagValueOutput.createWithContext(reporter, player.level().registryAccess());
			var typedList = valueOutput.list("items", ItemStackWithSlot.CODEC);
			keepInventory.save(typedList);
			CompoundTag resultTag = valueOutput.buildResult();
			ListTag finalTagList = resultTag.getList("items").get();
			getPlayerData(player).put(CHARM_INV_TAG, finalTagList);
		}

	}

	private static boolean applyCharm(DeferredItem<Item> charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> playerArmor = new java.util.AbstractList<>() {
			@Override public ItemStack get(int index) { return player.getInventory().getItem(36 + index); }
			@Override public int size() { return 4; }
			@Override public ItemStack set(int index, ItemStack element) {
				ItemStack old = player.getInventory().getItem(36 + index);
				player.getInventory().setItem(36 + index, element);
				return old;
			}
		};

		List<ItemStack> playerOffhand = new java.util.AbstractList<>() {
			@Override public ItemStack get(int index) { return player.getInventory().getItem(40); }
			@Override public int size() { return 1; }
			@Override public ItemStack set(int index, ItemStack element) {
				ItemStack old = player.getInventory().getItem(40);
				player.getInventory().setItem(40, element);
				return old;
			}
		};

		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);
		mergedCheck.addAll(playerArmor);
		mergedCheck.addAll(playerOffhand);

		if (mergedCheck.stream().filter(stack -> !stack.is(charm)).allMatch(ItemStack::isEmpty)) return false;
		if (!TFItemStackUtils.consumeInventoryItem(player, charm, getPlayerData(player), true) && !hasCharmCurio(charm.value(), player)) return false;

		NonNullList<ItemStack> keptItemsList = NonNullList.create();
		for (int i = 0; i < inventorySlots.size(); i++) {
			keptItemsList.add(keptInventory.getItem(i));
		}

		boolean keptACasket = keepWholeListAndCheckCasket(keptItemsList, inventorySlots, charm == TFItems.CHARM_OF_KEEPING_3);
		for (int i = 0; i < inventorySlots.size(); i++) {
			keptInventory.setItem(i, keptItemsList.get(i));
		}

		NonNullList<ItemStack> keptArmorList = NonNullList.create();
		for (int i = 0; i < 4; i++) {
			keptArmorList.add(keptInventory.getItem(i + 36));
		}

		keptACasket = keepWholeListAndCheckCasket(keptArmorList, playerArmor, keptACasket);
		for (int i = 0; i < 4; i++) {
			keptInventory.setItem(i + 36, keptArmorList.get(i));
		}

		NonNullList<ItemStack> keptOffhandList = NonNullList.create();
		keptOffhandList.add(keptInventory.getItem(40));
		keepWholeListAndCheckCasket(keptOffhandList, playerOffhand, keptACasket);
		keptInventory.setItem(40, keptOffhandList.get(0));

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

			int damage = getPlayerData(player).contains(CASKET_DAMAGE_TAG) ? getPlayerData(player).getInt(CASKET_DAMAGE_TAG).get() : 0;
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
			for (int slot = 36; slot <= 39; slot++) {
				player.getInventory().setItem(slot, net.minecraft.world.item.ItemStack.EMPTY);
			}
			list.addAll(filler);
			list.add(player.getInventory().getItem(40));
			player.getInventory().setItem(40, net.minecraft.world.item.ItemStack.EMPTY);
			list.addAll(TFItemStackUtils.sortInvForCasket(player));
			for (int slot = 0; slot < 36; slot++) {
				player.getInventory().setItem(slot, net.minecraft.world.item.ItemStack.EMPTY);
			}


			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			//inventory is empty minus the casket: put the casket into the kept inventory
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET.get())) {
					Inventory tmp = new Inventory(player, new EntityEquipment());
					ListTag savedListTag = getPlayerData(player).getList(CHARM_INV_TAG).get();
					CompoundTag rootLoadTag = new CompoundTag();

					rootLoadTag.put("items", savedListTag);

					ProblemReporter.Collector loadReporter = new ProblemReporter.Collector();
					var valueInput = TagValueInput.create(loadReporter, player.level().registryAccess(), rootLoadTag);
					var typedInputList = valueInput.list("items", ItemStackWithSlot.CODEC);

					tmp.load(typedInputList.get());
					tmp.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);

					ProblemReporter.Collector saveReporter = new ProblemReporter.Collector();
					TagValueOutput valueOutput = TagValueOutput.createWithContext(saveReporter, player.level().registryAccess());

					var typedOutputList = valueOutput.list("items", net.minecraft.world.ItemStackWithSlot.CODEC);
					tmp.save(typedOutputList);

					CompoundTag resultTag = valueOutput.buildResult();
					ListTag finalTagList = resultTag.getList("items").get();

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
			ListTag tagList = playerData.getList(CHARM_INV_TAG).get();
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			getPlayerData(player).getList(CHARM_INV_TAG).get().clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (getPlayerData(player).contains(CONSUMED_CHARM_TAG)) {
			Tag charmTag = getPlayerData(player).get(CONSUMED_CHARM_TAG);
			ItemStack stack = charmTag == null ? ItemStack.EMPTY : ItemStack.OPTIONAL_CODEC.parse(
				RegistryOps.create(NbtOps.INSTANCE, player.registryAccess()),
				charmTag
			).result().orElse(ItemStack.EMPTY);

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
		return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).get();
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

	private static boolean hasCharmCurio(Item item, Player player) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.findAndConsumeCurio(item, player);
		}

		return false;
	}
}
