package twilightforest;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.potion.Effects;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.BlockTFCritter;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.compat.TFCompat;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ITFProjectile;
import twilightforest.item.ItemTFPhantomArmor;
import twilightforest.item.TFItems;
import twilightforest.network.PacketAreaProtection;
import twilightforest.network.PacketEnforceProgressionStatus;
import twilightforest.network.PacketSetSkylightEnabled;
import twilightforest.network.PacketUpdateShield;
import twilightforest.network.TFPacketHandler;
import twilightforest.potions.TFPotions;
import twilightforest.util.TFItemStackUtils;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;
import twilightforest.world.WorldProviderTwilightForest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * So much of the mod logic in this one class
 */
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFEventListener {

	private static final ImmutableSet<String> SHIELD_DAMAGE_BLACKLIST = ImmutableSet.of(
			"inWall", "cramming", "drown", "starve", "fall", "flyIntoWall", "outOfWorld", "fallingBlock"
	);

	private static final Map<UUID, PlayerInventory> playerKeepsMap = new HashMap<>();
	private static final Map<UUID, NonNullList<ItemStack>> playerKeepsMapBaubles = new HashMap<>();

	private static boolean isBreakingWithGiantPick = false;
	private static boolean shouldMakeGiantCobble = false;
	private static int amountOfCobbleToReplace = 0;

	@SubscribeEvent
	public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		ItemStack itemStack = event.getCrafting();
		PlayerEntity player = event.getPlayer();

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
        //TODO: Can this be an Ingredient?
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.OAK_PLANKS) && itemStack.getCount() == 64 && doesCraftMatrixHaveGiantLog(event.getInventory())) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Blocks.OAK_PLANKS, 64));
		}
	}

	private static boolean doesCraftMatrixHaveGiantLog(IInventory inv) {
		Item giantLogItem = Item.getItemFromBlock(TFBlocks.giant_log);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).getItem() == giantLogItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Also check if we need to transform 64 cobbles into a giant cobble
	 */
	@SubscribeEvent
	public static void harvestDrops(HarvestDropsEvent event) {
		// this flag is set in reaction to the breakBlock event, but we need to remove the drops in this event
		if (shouldMakeGiantCobble && event.getDrops().size() > 0) {
			// turn the next 64 cobblestone drops into one giant cobble
			if (event.getDrops().get(0).getItem() == Item.getItemFromBlock(Blocks.COBBLESTONE)) {
				event.getDrops().remove(0);
				if (amountOfCobbleToReplace == 64) {
					event.getDrops().add(new ItemStack(TFBlocks.giant_cobblestone));
				}

				amountOfCobbleToReplace--;

				if (amountOfCobbleToReplace <= 0) {
					shouldMakeGiantCobble = false;
				}
			}
		}
	}

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {

		LivingEntity living = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		String damageType = damageSource.getDamageType();
		Entity trueSource = damageSource.getTrueSource();

		// fire aura
		if (living instanceof PlayerEntity && damageType.equals("mob") && trueSource != null) {
			PlayerEntity player = (PlayerEntity) living;
			int fireLevel = TFEnchantment.getFieryAuraLevel(player.inventory, damageSource);

			if (fireLevel > 0 && player.getRNG().nextInt(25) < fireLevel) {
				trueSource.setFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof PlayerEntity && damageType.equals("mob") && trueSource instanceof LivingEntity) {
			PlayerEntity player = (PlayerEntity) living;
			int chillLevel = TFEnchantment.getChillAuraLevel(player.inventory, damageSource);

			if (chillLevel > 0) {
				((LivingEntity) trueSource).addPotionEffect(new EffectInstance(TFPotions.frosty, chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;

			if (player.getHeldItemMainhand().getItem() == TFItems.triple_bow || player.getHeldItemOffhand().getItem() == TFItems.triple_bow) {
				living.hurtResistantTime = 0;
			}
		}

		// enderbow teleports
		if (damageType.equals("arrow") && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;

			if (player.getHeldItemMainhand().getItem() == TFItems.ender_bow || player.getHeldItemOffhand().getItem() == TFItems.ender_bow) {

				double sourceX = player.posX, sourceY = player.posY, sourceZ = player.posZ;
				float sourceYaw = player.rotationYaw, sourcePitch = player.rotationPitch;

				// this is the only method that will move the player properly
				player.rotationYaw = living.rotationYaw;
				player.rotationPitch = living.rotationPitch;
				player.setPositionAndUpdate(living.posX, living.posY, living.posZ);
				player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

				// monsters are easy to move
				living.setPositionAndRotation(sourceX, sourceY, sourceZ, sourceYaw, sourcePitch);
				living.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
		}

		// Smashing!
		ItemStack stack = living.getItemStackFromSlot(EquipmentSlotType.HEAD);
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block instanceof BlockTFCritter) {
			BlockTFCritter poorBug = (BlockTFCritter) block;
			living.setItemStackToSlot(EquipmentSlotType.HEAD, poorBug.getSquishResult());
			living.world.playSound(null, living.posX, living.posY, living.posZ, poorBug.getSoundType().getBreakSound(), living.getSoundCategory(), 1, 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void charmOfLife(LivingDeathEvent event) {

		LivingEntity living = event.getEntityLiving();
		if (living.world.isRemote) return;

		boolean charm1 = false;
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_2, 1);
		if (!charm2) {
			charm1 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_1, 1);
		}

		if (charm2 || charm1) {

			if (charm1) {
				living.setHealth(8);
				living.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, 0));
			}

			if (charm2) {
				living.setHealth((float) living.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());

				living.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 3));
				living.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 600, 0));
				living.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 600, 0));
			}

			// spawn effect thingers
			EntityTFCharmEffect effect = new EntityTFCharmEffect(living.world, living, charm1 ? TFItems.charm_of_life_1 : TFItems.charm_of_life_2);
			living.world.addEntity(effect);

			EntityTFCharmEffect effect2 = new EntityTFCharmEffect(living.world, living, charm1 ? TFItems.charm_of_life_1 : TFItems.charm_of_life_2);
			effect2.offset = (float) Math.PI;
			living.world.addEntity(effect2);

			living.world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ITEM_TOTEM_USE, living.getSoundCategory(), 1, 1);

			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void charmOfKeeping(LivingDeathEvent event) {

		LivingEntity living = event.getEntityLiving();
		if (living.world.isRemote) return;

		if (living instanceof PlayerEntity && !living.world.getGameRules().getBoolean("keepInventory")) {
			keepItems((PlayerEntity) living);
		}
	}

	private static void keepItems(PlayerEntity player) {

		// drop any existing held items, just in case
		dropStoredItems(player);

		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 =          TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_3, 1);
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_2, 1);
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_1, 1);

		PlayerInventory keepInventory = new PlayerInventory(null);

		UUID playerUUID = player.getUniqueID();

		if (tier1) {
			keepAllArmor(player, keepInventory);
			keepOffHand(player, keepInventory);
		}

		if (tier3) {
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_3));

		} else if (tier2) {
			for (int i = 0; i < 9; i++) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_2));

		} else if (tier1) {
			int i = player.inventory.currentItem;
			if (PlayerInventory.isHotbar(i)) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_1));
		}

		// always keep tower keys
		for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ItemStack stack = player.inventory.mainInventory.get(i);
			if (stack.getItem() == TFItems.tower_key) {
				keepInventory.mainInventory.set(i, stack.copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
		}

		//TODO: Baubles is dead
		/*if (tier1 && TFCompat.BAUBLES.isActivated()) {
			playerKeepsMapBaubles.put(playerUUID, Baubles.keepBaubles(player));
		}*/

		for (int i = 0; i < player.inventory.armorInventory.size(); i++) { // TODO also consider Phantom tools, when those get added
			ItemStack armor = player.inventory.armorInventory.get(i);
			if (armor.getItem() instanceof ItemTFPhantomArmor) {
				keepInventory.armorInventory.set(i, armor.copy());
				player.inventory.armorInventory.set(i, ItemStack.EMPTY);
			}
		}

		playerKeepsMap.put(playerUUID, keepInventory);
	}

	/**
	 * Move the full armor inventory to the keep pile
	 */
	private static void keepAllArmor(PlayerEntity player, PlayerInventory keepInventory) {
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			keepInventory.armorInventory.set(i, player.inventory.armorInventory.get(i).copy());
			player.inventory.armorInventory.set(i, ItemStack.EMPTY);
		}
	}

	private static void keepOffHand(PlayerEntity player, PlayerInventory keepInventory) {
		for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
			keepInventory.offHandInventory.set(i, player.inventory.offHandInventory.get(i).copy());
			player.inventory.offHandInventory.set(i, ItemStack.EMPTY);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.isEndConquered()) {
			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
		} else {
			returnStoredItems(event.getPlayer());
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(PlayerEntity player) {
		PlayerInventory keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.debug("Player {} respawned and received items held in storage", player.getName());

			NonNullList<ItemStack> displaced = NonNullList.create();

			for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
				ItemStack kept = keepInventory.armorInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.armorInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.inventory.offHandInventory.size(); i++) {
				ItemStack kept = keepInventory.offHandInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.offHandInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}
			for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
				ItemStack kept = keepInventory.mainInventory.get(i);
				if (!kept.isEmpty()) {
					ItemStack existing = player.inventory.mainInventory.set(i, kept);
					if (!existing.isEmpty()) {
						displaced.add(existing);
					}
				}
			}

			// try to give player any displaced items
			for (ItemStack extra : displaced) {
				ItemHandlerHelper.giveItemToPlayer(player, extra);
			}

			// spawn effect thingers
			if (!keepInventory.getItemStack().isEmpty()) {
				EntityTFCharmEffect effect = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				player.world.addEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(player.world, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.world.addEntity(effect2);

				player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, player.getSoundCategory(), 1.5F, 1.0F);
			}
		}

		//TODO: Baubles is dead
		/*if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.debug("Player {} respawned and received baubles held in storage", player.getName());
				Baubles.returnBaubles(player, baubles);
			}
		}*/
	}

	/**
	 * Dump stored items if player logs out
	 */
	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		dropStoredItems(event.getPlayer());
	}

	private static void dropStoredItems(PlayerEntity player) {
		PlayerInventory keepInventory = playerKeepsMap.remove(player.getUniqueID());
		if (keepInventory != null) {
			TwilightForestMod.LOGGER.warn("Dropping inventory items previously held in reserve for player {}", player.getName());
			keepInventory.player = player;
			keepInventory.dropAllItems();
		}
		if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.warn("Dropping baubles previously held in reserve for player {}", player.getName());
				for (ItemStack itemStack : baubles) {
					if (!itemStack.isEmpty()) {
						player.dropItem(itemStack, true, false);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity living = event.getEntityLiving();

		IShieldCapability cap = living.getCapability(CapabilityList.SHIELDS, null);
		if (cap != null) cap.update();

		// Stop the player from sneaking while riding an unfriendly creature
		if (living instanceof PlayerEntity && living.isSneaking() && isRidingUnfriendly(living)) {
			living.setSneaking(false);
		}
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getRidingEntity() instanceof IHostileMount;
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * Also check for breaking blocks with the giant's pickaxe and maybe break nearby blocks
	 */
	@SubscribeEvent
	public static void breakBlock(BreakEvent event) {

		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (world.isRemote) return;

		if (isBlockProtectedFromBreaking(world, pos) && isAreaProtected(world, player, pos)) {
			event.setCanceled(true);

		} else if (!isBreakingWithGiantPick && canHarvestWithGiantPick(player, state)) {

			isBreakingWithGiantPick = true;

			// check nearby blocks for same block or same drop

			// pre-check for cobble!
			Item cobbleItem = Item.getItemFromBlock(Blocks.COBBLESTONE);
			boolean allCobble = state.getBlock().getItemDropped(state, world.rand, 0) == cobbleItem;

			if (allCobble) {
				for (BlockPos dPos : BlockTFGiantBlock.getVolume(pos)) {
					if (dPos.equals(pos)) continue;
					BlockState stateThere = world.getBlockState(dPos);
					if (stateThere.getBlock().getItemDropped(stateThere, world.rand, 0) != cobbleItem) {
						allCobble = false;
						break;
					}
				}
			}

			if (allCobble && !player.abilities.isCreativeMode) {
				shouldMakeGiantCobble = true;
				amountOfCobbleToReplace = 64;
			} else {
				shouldMakeGiantCobble = false;
				amountOfCobbleToReplace = 0;
			}

			// break all nearby blocks
			if (player instanceof ServerPlayerEntity) {
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				for (BlockPos dPos : BlockTFGiantBlock.getVolume(pos)) {
					if (!dPos.equals(pos) && state == world.getBlockState(dPos)) {
						// try to break that block too!
						playerMP.interactionManager.tryHarvestBlock(dPos);
					}
				}
			}

			isBreakingWithGiantPick = false;
		}
	}

	private static boolean canHarvestWithGiantPick(PlayerEntity player, BlockState state) {
		ItemStack heldStack = player.getHeldItemMainhand();
		Item heldItem = heldStack.getItem();
		return heldItem == TFItems.giant_pickaxe && heldItem.canHarvestBlock(heldStack, state);
	}

	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {

		PlayerEntity player = event.getPlayer();
		World world = player.world;

		if (!world.isRemote && isBlockProtectedFromInteraction(world, event.getPos()) && isAreaProtected(world, player, event.getPos())) {
			event.setUseBlock(Event.Result.DENY);
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		// TODO: improve this?
		return block == TFBlocks.tower_device || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST
				|| block == Blocks.STONE_BUTTON || block == Blocks.WOODEN_BUTTON || block == Blocks.LEVER;
	}

	private static boolean isBlockProtectedFromBreaking(World world, BlockPos pos) {
		// todo improve
		return !world.getBlockState(pos).getBlock().getRegistryName().getPath().contains("grave");
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(World world, PlayerEntity player, BlockPos pos) {

		if (player.abilities.isCreativeMode || !TFWorld.isProgressionEnforced(world)) {
			return false;
		}

		ChunkGeneratorTFBase chunkGenerator = TFWorld.getChunkGenerator(world);

		if (chunkGenerator != null && chunkGenerator.isBlockInStructureBB(pos)) {
			// what feature is nearby?  is it one the player has not unlocked?
			TFFeature nearbyFeature = TFFeature.getFeatureAt(pos.getX(), pos.getZ(), world);

			if (!nearbyFeature.doesPlayerHaveRequiredAdvancements(player) && chunkGenerator.isBlockProtected(pos)) {

				// send protection packet
				sendAreaProtectionPacket(world, pos, chunkGenerator.getSBBAt(pos));

				// send a hint monster?
				nearbyFeature.trySpawnHintMonster(world, player, pos);

				return true;
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(World world, BlockPos pos, StructureBoundingBox sbb) {
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
		TFPacketHandler.CHANNEL.sendToAllAround(new PacketAreaProtection(sbb, pos), targetPoint);
	}

	@SubscribeEvent
	public static void livingAttack(LivingAttackEvent event) {
		LivingEntity living = event.getEntityLiving();
		// cancel attacks in protected areas
		if (!living.world.isRemote && living instanceof IMob && event.getSource().getTrueSource() instanceof PlayerEntity
				&& isAreaProtected(living.world, (PlayerEntity) event.getSource().getTrueSource(), new BlockPos(living))) {

			event.setCanceled(true);
			return;
		}
		// shields
		if (!living.world.isRemote && !SHIELD_DAMAGE_BLACKLIST.contains(event.getSource().damageType)) {
			IShieldCapability cap = living.getCapability(CapabilityList.SHIELDS, null);
			if (cap != null && cap.shieldsLeft() > 0) {
				cap.breakShield();
				event.setCanceled(true);
			}
		}
	}

	/**
	 * When player logs in, report conflict status, set enforced_progression rule
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote && event.getPlayer() instanceof ServerPlayerEntity) {
			sendEnforcedProgressionStatus((ServerPlayerEntity) event.getPlayer(), TFWorld.isProgressionEnforced(event.getPlayer().world));
			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
			banishNewbieToTwilightZone(event.getPlayer());
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().world.isRemote && event.getPlayer() instanceof ServerPlayerEntity) {
			if (event.toDim == TFConfig.dimension.dimensionID) {
				sendEnforcedProgressionStatus((ServerPlayerEntity) event.getPlayer(), TFWorld.isProgressionEnforced(event.getPlayer().world));
			}
			updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayerEntity) event.getPlayer(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayerEntity player, Entity entity) {
		IShieldCapability cap = entity.getCapability(CapabilityList.SHIELDS, null);
		if (cap != null && cap.shieldsLeft() > 0) {
			TFPacketHandler.CHANNEL.sendTo(new PacketUpdateShield(entity, cap), player);
		}
	}

	private static void sendEnforcedProgressionStatus(ServerPlayerEntity player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.sendTo(new PacketEnforceProgressionStatus(isEnforced), player);
	}

	private static void sendSkylightEnabled(ServerPlayerEntity player, boolean skylightEnabled) {
		TFPacketHandler.CHANNEL.sendTo(new PacketSetSkylightEnabled(skylightEnabled), player);
	}

	@SubscribeEvent
	public static void onClientConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
		INetHandlerPlayServer handler = event.getHandler();
		if (handler instanceof NetHandlerPlayServer) {
			ServerPlayerEntity player = ((NetHandlerPlayServer) handler).player;
			sendSkylightEnabled(player, WorldProviderTwilightForest.isSkylightEnabled(TFWorld.getDimensionData(player.world)));
		}
	}

	@SubscribeEvent
	public static void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		WorldProviderTwilightForest.syncFromConfig();
	}

	/**
	 * When world is loaded, check if the game rule is defined
	 */
	@SubscribeEvent
	public static void worldLoaded(WorldEvent.Load event) {
		if (!event.getWorld().isRemote() && !event.getWorld().getGameRules().hasRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			TwilightForestMod.LOGGER.info("Loaded a world with the {} game rule not defined. Defining it.", TwilightForestMod.ENFORCED_PROGRESSION_RULE);
			event.getWorld().getGameRules().addGameRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE, String.valueOf(TFConfig.progressionRuleDefault), GameRules.ValueType.BOOLEAN_VALUE);
		}
	}

	/**
	 * Check if someone's changing the progression game rule
	 */
	@SubscribeEvent
	public static void gameRuleChanged(GameRuleChangeEvent event) {
		if (event.getRuleName().equals(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			boolean isEnforced = event.getRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
			TFPacketHandler.CHANNEL.sendToAll(new PacketEnforceProgressionStatus(isEnforced));
		}
	}

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	private static void banishNewbieToTwilightZone(PlayerEntity player) {
		CompoundNBT tagCompound = player.getEntityData();
		CompoundNBT playerData = tagCompound.getCompoundTag(PlayerEntity.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.dimension.newPlayersSpawnInTF && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(PlayerEntity.PERSISTED_NBT_TAG, playerData); // commit

		if (shouldBanishPlayer) BlockTFPortal.attemptSendPlayer(player, true); // See ya hate to be ya
	}

	// Advancement Trigger
	@SubscribeEvent
	public static void onAdvancementGet(AdvancementEvent event) {
		PlayerEntity player = event.getPlayer();
		if (player instanceof ServerPlayerEntity) {
			TFAdvancements.ADVANCEMENT_UNLOCKED.trigger((ServerPlayerEntity) player, event.getAdvancement());
		}
	}

	@SubscribeEvent
	public static void armorChanged(LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntityLiving();
		if (!living.world.isRemote && living instanceof ServerPlayerEntity) {
			TFAdvancements.ARMOR_CHANGED.trigger((ServerPlayerEntity) living, event.getFrom(), event.getTo());
		}
	}

	// Parrying

 	private static boolean globalParry = !Loader.isModLoaded("parry");

	@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent.Arrow event) {
		final EntityArrow projectile = event.getArrow();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof ITFProjectile)) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof LivingEntity) {
				LivingEntity entityBlocking = (LivingEntity) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksArrow) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

					projectile.shootingEntity = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent.Fireball event) {
		final EntityFireball projectile = event.getFireball();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof ITFProjectile)) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof LivingEntity) {
				LivingEntity entityBlocking = (LivingEntity) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksFireball) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.motionX = playerVec3.x;
					projectile.motionY = playerVec3.y;
					projectile.motionZ = playerVec3.z;
					projectile.accelerationX = projectile.motionX * 0.1D;
					projectile.accelerationY = projectile.motionY * 0.1D;
					projectile.accelerationZ = projectile.motionZ * 0.1D;

					projectile.shootingEntity = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent.Throwable event) {
		final EntityThrowable projectile = event.getThrowable();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.shieldInteractions.parryNonTwilightAttacks
				|| projectile instanceof ITFProjectile)) {

			Entity entity = event.getRayTraceResult().entityHit;

			if (event.getEntity() != null && entity instanceof LivingEntity) {
				LivingEntity entityBlocking = (LivingEntity) entity;

				if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
					@Override
					public Vec3d getDamageLocation() { return projectile.getPositionVector(); }
				}) && (entityBlocking.getActiveItemStack().getItem().getMaxItemUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.shieldInteractions.shieldParryTicksThrowable) {
					Vec3d playerVec3 = entityBlocking.getLookVec();

					projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

					projectile.thrower = entityBlocking;

					event.setCanceled(true);
				}
			}
		}
	}
}
