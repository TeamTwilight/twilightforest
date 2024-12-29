package twilightforest.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class CharmEvents {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CASKET_BROKEN_TAG = "CasketBroken";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	// Check for charm of life first to stop a player from dying
	public static void applyCharmOfLife(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (charmOfLife(player)) event.setCanceled(true); // Executes if the player had charms
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	// Then check if the player should keep any items through death
	public static void applyKeepingAndCasket(LivingDeathEvent event) {
		LivingEntity living = event.getEntity();

		//ensure our player is real and in survival before attempting anything
		if (event.isCanceled() || living.level().isClientSide() || !(living instanceof Player player) || living instanceof FakePlayer ||
				player.isCreative() || player.isSpectator()) return;

		if (!living.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			charmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			keepsakeCasket(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
		if (!event.isEndConquered()) {
			if (getPlayerData(serverPlayer).contains(CASKET_BROKEN_TAG)) {
				serverPlayer.sendSystemMessage(Component.translatable("block.twilightforest.casket.broken").withStyle(ChatFormatting.RED));
				getPlayerData(serverPlayer).remove(CASKET_BROKEN_TAG);
			}
			returnStoredItems(serverPlayer);
		}
	}

	private static boolean charmOfLife(Player player) {
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
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
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

	private static void charmOfKeeping(Player player) {
		//check our inventory for any charms of keeping. We also want to check curio slots (if the mod is installed)
		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_3.get(), getPlayerData(player), true) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_3.get(), player);
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_2.get(), getPlayerData(player), true) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_2.get(), player);
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_KEEPING_1.get(), getPlayerData(player), true) || hasCharmCurio(TFItems.CHARM_OF_KEEPING_1.get(), player);

		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(player);
		ListTag tagList = new ListTag();

		//if we have any charm of keeping, all armor and offhand items are kept, so add those
		if (tier1) {
			keepWholeList(keepInventory.armor, player.getInventory().armor);
			keepWholeList(keepInventory.offhand, player.getInventory().offhand);
		}

		if (tier3) {
			//tier 3 keeps our entire inventory
			keepWholeList(keepInventory.items, player.getInventory().items);
		} else if (tier2) {
			//tier 2 keeps our hotbar only
			for (int i = 0; i < 9; i++) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		} else if (tier1) {
			//tier 1 keeps our selected item only
			int i = player.getInventory().selected;
			if (Inventory.isHotbarSlot(i)) {
				keepInventory.items.set(i, player.getInventory().items.get(i).copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.is(ItemTagGenerator.KEPT_ON_DEATH)) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			ItemStack armor = player.getInventory().armor.get(i);
			if (armor.is(ItemTagGenerator.KEPT_ON_DEATH)) {
				keepInventory.armor.set(i, armor.copy());
				player.getInventory().armor.set(i, ItemStack.EMPTY);
			}
		}

		if (player.getInventory().offhand.get(0).is(ItemTagGenerator.KEPT_ON_DEATH)) {
			keepInventory.offhand.set(0, player.getInventory().offhand.get(0).copy());
			player.getInventory().offhand.set(0, ItemStack.EMPTY);
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			keepInventory.save(tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
		}
	}

	private static void keepsakeCasket(Player player) {
		boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET.get().asItem(), getPlayerData(player), false);

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

		int damage = getPlayerData(player).contains(CASKET_DAMAGE_TAG) ? getPlayerData(player).getInt(CASKET_DAMAGE_TAG) : 0;
		BlockState setState = TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState()
			.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()))
			.setValue(KeepsakeCasketBlock.BREAKAGE, damage)
			.setValue(KeepsakeCasketBlock.FACING, Direction.from2DDataValue(level.getRandom().nextInt(3)));

		if (level.getRandom().nextFloat() <= 1.0F) {
			if (damage >= 2) {
				setState = TFBlocks.SKULL_CHEST.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()));
				getPlayerData(player).putBoolean(CASKET_BROKEN_TAG, true);
				TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, alerting the player and placing Skull Chest instead", player.getName().getString());
			} else {
				damage = damage + 1;
				setState = TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState().setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType())).setValue(KeepsakeCasketBlock.BREAKAGE, damage);
				TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
			}
		}

		if (!level.setBlockAndUpdate(immutablePos, setState)) {
			TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at " + pos);
			return;
		}

		if (!(level.getBlockEntity(immutablePos) instanceof KeepsakeCasketBlockEntity casket)) {
			TwilightForestMod.LOGGER.error("Failed to set Keepsake Casket data at " + pos);
			return;
		}

		if (TFConfig.casketUUIDLocking) {
			//make it so only the player who died can open the chest if our config allows us
			casket.playeruuid = player.getGameProfile().getId();
		} else {
			casket.playeruuid = null;
		}

		casket.playerName = player.getName().getString();
		//some names are way too long for the casket so we'll cut them down
		String modifiedName = casket.playerName.substring(0, Math.min(12, player.getName().getString().length()));
		casket.casketname = modifiedName;
		casket.name = (Component.literal(modifiedName + "'s " + (level.getRandom().nextInt(1000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));

		int casketCapacity = casket.getContainerSize();
		List<ItemStack> list = new ArrayList<>(casketCapacity);
		NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

		// lets add our inventory exactly how it was on us
		list.addAll(TFItemStackUtils.sortArmorForCasket(player));
		player.getInventory().armor.clear();
		list.addAll(filler);
		list.addAll(player.getInventory().offhand);
		player.getInventory().offhand.clear();
		list.addAll(TFItemStackUtils.sortInvForCasket(player));
		player.getInventory().items.clear();

		casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
		getPlayerData(player).remove(CASKET_DAMAGE_TAG);
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TwilightForestMod.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level().isClientSide() && playerData.contains(CHARM_INV_TAG)) {
			ListTag tagList = playerData.getList(CHARM_INV_TAG, 10);
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			getPlayerData(player).getList(CHARM_INV_TAG, 10).clear();
			getPlayerData(player).remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (getPlayerData(player).contains(CONSUMED_CHARM_TAG)) {
			ItemStack stack = ItemStack.parseOptional(player.registryAccess(), (CompoundTag) getPlayerData(player).get(CONSUMED_CHARM_TAG));

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
		return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
	}

	//transfers a list of items to another
	private static void keepWholeList(NonNullList<ItemStack> transferTo, NonNullList<ItemStack> transferFrom) {
		for (int i = 0; i < transferFrom.size(); i++) {
			transferTo.set(i, transferFrom.get(i).copy());
		}
		transferFrom.clear();
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		if (ModList.get().isLoaded("curios")) {
			return CuriosCompat.findAndConsumeCurio(item, player);
		}

		return false;
	}
}
