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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MutableBoundingBox;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.BlockTFCritter;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.enchantment.TFEnchantment;
import twilightforest.entity.EntityTFCharmEffect;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.ITFProjectile;
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
import twilightforest.world.TFDimensions;
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
	//private static final Map<UUID, NonNullList<ItemStack>> playerKeepsMapBaubles = new HashMap<>();

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
		Item giantLogItem = Item.getItemFromBlock(TFBlocks.giant_log.get());
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
					event.getDrops().add(new ItemStack(TFBlocks.giant_cobblestone.get()));
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
				((LivingEntity) trueSource).addPotionEffect(new EffectInstance(TFPotions.frosty.get(), chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;

			if (player.getHeldItemMainhand().getItem() == TFItems.triple_bow.get() || player.getHeldItemOffhand().getItem() == TFItems.triple_bow.get()) {
				living.hurtResistantTime = 0;
			}
		}

		// enderbow teleports
		if (damageType.equals("arrow") && trueSource instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) trueSource;

			if (player.getHeldItemMainhand().getItem() == TFItems.ender_bow.get() || player.getHeldItemOffhand().getItem() == TFItems.ender_bow.get()) {

				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.rotationYaw, sourcePitch = player.rotationPitch;

				// this is the only method that will move the player properly
				player.rotationYaw = living.rotationYaw;
				player.rotationPitch = living.rotationPitch;
				player.setPositionAndUpdate(living.getX(), living.getY(), living.getZ());
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
			living.world.playSound(null, living.getX(), living.getY(), living.getZ(), poorBug.getSoundType(poorBug.getDefaultState()).getBreakSound(), living.getSoundCategory(), 1, 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void charmOfLife(LivingDeathEvent event) {

		LivingEntity living = event.getEntityLiving();
		if (living.world.isRemote) return;

		boolean charm1 = false;
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_2.get(), 1);
		if (!charm2) {
			charm1 = TFItemStackUtils.consumeInventoryItem(living, s -> s.getItem() == TFItems.charm_of_life_1.get(), 1);
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
			EntityTFCharmEffect effect = new EntityTFCharmEffect(TFEntities.charm_effect.get(), living.world, living, charm1 ? TFItems.charm_of_life_1.get() : TFItems.charm_of_life_2.get());
			living.world.addEntity(effect);

			EntityTFCharmEffect effect2 = new EntityTFCharmEffect(TFEntities.charm_effect.get(), living.world, living, charm1 ? TFItems.charm_of_life_1.get() : TFItems.charm_of_life_2.get());
			effect2.offset = (float) Math.PI;
			living.world.addEntity(effect2);

			living.world.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ITEM_TOTEM_USE, living.getSoundCategory(), 1, 1);

			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void charmOfKeeping(LivingDeathEvent event) {

		LivingEntity living = event.getEntityLiving();
		if (living.world.isRemote) return;

		if (living instanceof PlayerEntity && !living.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			keepItems((PlayerEntity) living);
		}
	}

	private static void keepItems(PlayerEntity player) {

		// drop any existing held items, just in case
		dropStoredItems(player);

		// TODO also consider situations where the actual slots may be empty, and charm gets consumed anyway. Usually won't happen.
		boolean tier3 = TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_3.get(), 1);
		boolean tier2 = tier3 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_2.get(), 1);
		boolean tier1 = tier2 || TFItemStackUtils.consumeInventoryItem(player, s -> s.getItem() == TFItems.charm_of_keeping_1.get(), 1);

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
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_3.get()));

		} else if (tier2) {
			for (int i = 0; i < 9; i++) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_2.get()));

		} else if (tier1) {
			int i = player.inventory.currentItem;
			if (PlayerInventory.isHotbar(i)) {
				keepInventory.mainInventory.set(i, player.inventory.mainInventory.get(i).copy());
				player.inventory.mainInventory.set(i, ItemStack.EMPTY);
			}
			keepInventory.setItemStack(new ItemStack(TFItems.charm_of_keeping_1.get()));
		}

		// always keep tower keys
		for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
			ItemStack stack = player.inventory.mainInventory.get(i);
			if (stack.getItem() == TFItems.tower_key.get()) {
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
				EntityTFCharmEffect effect = new EntityTFCharmEffect(TFEntities.charm_effect.get(), player.world, player, keepInventory.getItemStack().getItem());
				player.world.addEntity(effect);

				EntityTFCharmEffect effect2 = new EntityTFCharmEffect(TFEntities.charm_effect.get(), player.world, player, keepInventory.getItemStack().getItem());
				effect2.offset = (float) Math.PI;
				player.world.addEntity(effect2);

				player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, player.getSoundCategory(), 1.5F, 1.0F);
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
		//TODO: Baubles is dead
		/*if (TFCompat.BAUBLES.isActivated()) {
			NonNullList<ItemStack> baubles = playerKeepsMapBaubles.remove(player.getUniqueID());
			if (baubles != null) {
				TwilightForestMod.LOGGER.warn("Dropping baubles previously held in reserve for player {}", player.getName());
				for (ItemStack itemStack : baubles) {
					if (!itemStack.isEmpty()) {
						player.dropItem(itemStack, true, false);
					}
				}
			}
		}*/
	}

	@SubscribeEvent
	public static void livingUpdate(LivingUpdateEvent event) {
		LivingEntity living = event.getEntityLiving();

		living.getCapability(CapabilityList.SHIELDS).ifPresent(IShieldCapability::update);

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

		World world = event.getWorld().getWorld();
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
		return heldItem == TFItems.giant_pickaxe.get() && heldItem.canHarvestBlock(heldStack, state);
	}

	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {

		PlayerEntity player = event.getPlayer();
		World world = player.world;

		if (!world.isRemote && isBlockProtectedFromInteraction(world, event.getPos()) && isAreaProtected(world, player, event.getPos())) {
			event.setUseBlock(Event.Result.DENY);
		}
	}

	public static final Tag<Block> PROTECTED_INTERACTION = new BlockTags.Wrapper(TwilightForestMod.prefix("protected_interaction"));

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private static boolean isBlockProtectedFromInteraction(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block.isIn(PROTECTED_INTERACTION);
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

	private static void sendAreaProtectionPacket(World world, BlockPos pos, MutableBoundingBox sbb) {
		PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 64, world.getDimension().getType());
		TFPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), new PacketAreaProtection(sbb, pos));
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
			living.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
				if (cap.shieldsLeft() > 0) {
					cap.breakShield();
					event.setCanceled(true);
				}
			});
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
			if (event.getTo() == TFDimensions.tf_dimType) {
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
		entity.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdateShield(entity, cap));
			}
		});
	}

	private static void sendEnforcedProgressionStatus(ServerPlayerEntity player, boolean isEnforced) {
		TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketEnforceProgressionStatus(isEnforced));
	}

	private static void sendSkylightEnabled(ServerPlayerEntity player, boolean skylightEnabled) {
		TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketSetSkylightEnabled(skylightEnabled));
	}

	// TODO: This previously used the network-connection connected events
	// TODO: which no longer seem to be present.
	@SubscribeEvent
	public static void onClientConnect(PlayerEvent.PlayerLoggedInEvent event) {
		// This event is only ever fired on the server side
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		sendSkylightEnabled(player, WorldProviderTwilightForest.isSkylightEnabled(TFWorld.getDimensionData(player.world)));
	}

	@SubscribeEvent
	public static void onServerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
		WorldProviderTwilightForest.syncFromConfig();
	}

	/**
	 * When world is loaded, check if the game rule is defined
	 */
	@SubscribeEvent
	public static void worldLoaded(WorldEvent.Load event) {
		World world = event.getWorld().getWorld();

		if (!world.isRemote() && world.getGameRules().get(TwilightForestMod.ENFORCED_PROGRESSION_RULE).get()) {
			TwilightForestMod.LOGGER.info("Loaded a world with the {} game rule not defined. Defining it.", TwilightForestMod.ENFORCED_PROGRESSION_RULE);
			//world.getGameRules().addGameRule(TwilightForestMod.ENFORCED_PROGRESSION_RULE, String.valueOf(TFConfig.COMMON_CONFIG.progressionRuleDefault), GameRules.ValueType.BOOLEAN_VALUE);
		}
	}

	/**
	 * Check if someone's changing the progression game rule
	 */
	//TODO gamerule register was changed, so doesn't need this
	/*@SubscribeEvent
	public static void gameRuleChanged(GameRuleChangeEvent event) {
		if (event.getRuleName().equals(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			boolean isEnforced = event.getRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
			TFPacketHandler.CHANNEL.sendToAll(new PacketEnforceProgressionStatus(isEnforced));
		}
	}*/

	// Teleport first-time players to Twilight Forest

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";

	private static void banishNewbieToTwilightZone(PlayerEntity player) {
		CompoundNBT tagCompound = player.getPersistentData();
		CompoundNBT playerData = tagCompound.getCompound(PlayerEntity.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

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

	private static boolean globalParry = !ModList.get().isLoaded("parry");

	@SubscribeEvent
	public static void arrowParry(ProjectileImpactEvent.Arrow event) {
		final AbstractArrowEntity projectile = event.getArrow();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vec3d getDamageLocation() {
							return projectile.getPositionVector();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksArrow.get()) {
						Vec3d playerVec3 = entityBlocking.getLookVec();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.shootingEntity = entityBlocking.getUniqueID();

						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void fireballParry(ProjectileImpactEvent.Fireball event) {
		final DamagingProjectileEntity projectile = event.getFireball();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vec3d getDamageLocation() {
							return projectile.getPositionVector();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksFireball.get()) {
						Vec3d playerVec3 = entityBlocking.getLookVec();

//					projectile.motionX = playerVec3.x;
//					projectile.motionY = playerVec3.y;
//					projectile.motionZ = playerVec3.z;
						projectile.setMotion(new Vec3d(playerVec3.x, playerVec3.y, playerVec3.z));
						projectile.accelerationX = projectile.getMotion().getX() * 0.1D;
						projectile.accelerationY = projectile.getMotion().getY() * 0.1D;
						projectile.accelerationZ = projectile.getMotion().getZ() * 0.1D;

						projectile.shootingEntity = entityBlocking;

						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent.Throwable event) {
		final ThrowableEntity projectile = event.getThrowable();

		if (!projectile.getEntityWorld().isRemote && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
				Entity entity = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();


				if (event.getEntity() != null && entity instanceof LivingEntity) {
					LivingEntity entityBlocking = (LivingEntity) entity;

					if (entityBlocking.canBlockDamageSource(new DamageSource("parry_this") {
						@Override
						public Vec3d getDamageLocation() {
							return projectile.getPositionVector();
						}
					}) && (entityBlocking.getActiveItemStack().getItem().getUseDuration(entityBlocking.getActiveItemStack()) - entityBlocking.getItemInUseCount()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vec3d playerVec3 = entityBlocking.getLookVec();

						projectile.shoot(playerVec3.x, playerVec3.y, playerVec3.z, 1.1F, 0.1F);  // reflect faster and more accurately

						projectile.owner = entityBlocking;

						event.setCanceled(true);
					}
				}
			}
		}
	}
}
